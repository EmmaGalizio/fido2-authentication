/*
 * Copyright 2021 LINE Corporation
 *
 * LINE Corporation licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package emma.galzio.fido2server.service;

import emma.galzio.fido2server.common.COSEAlgorithmIdentifier;
import emma.galzio.fido2server.common.PublicKeyCredentialParameters;
import emma.galzio.fido2server.common.PublicKeyCredentialType;
import emma.galzio.fido2server.common.extension.AuthenticationExtensionsClientInputs;
import emma.galzio.fido2server.common.server.*;
import emma.galzio.fido2server.ServerConstant;
import emma.galzio.fido2server.entity.UserEntity;
import emma.galzio.fido2server.error.InternalErrorCode;
import emma.galzio.fido2server.exception.FIDO2ServerRuntimeException;
import emma.galzio.fido2server.model.Session;
import emma.galzio.fido2server.model.User;
import emma.galzio.fido2server.model.UserKey;
import emma.galzio.fido2server.repository.IUserRepository;
import emma.galzio.fido2server.repository.SessionRepository;
import emma.galzio.fido2server.util.ChallengeGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Primary
@Service
public class ChallengeServiceImpl implements ChallengeService {
    private final RpService rpService;
    private final UserKeyService userKeyService;
    private final SessionService sessionService;
    private final IUserRepository userRepository;
    private final SessionRepository sessionRepository;

    @Value("${fido.fido2.session-ttl-millis}")
    private long sessionTtlMillis;

    @Autowired
    public ChallengeServiceImpl(RpService rpService,
                                UserKeyService userKeyService,
                                SessionService sessionService, IUserRepository userRepository, SessionRepository sessionRepository) {
        this.rpService = rpService;
        this.userKeyService = userKeyService;
        this.sessionService = sessionService;
        this.userRepository = userRepository;
        this.sessionRepository = sessionRepository;
    }

    /**
     * Get options for reg operation
     * @param regOptionRequest
     * @return
     */
    @Override
    public RegOptionResponse getRegChallenge(RegOptionRequest regOptionRequest) {
        String rpId = regOptionRequest.getRp().getId();
        String userId = regOptionRequest.getUser().getId();
        log.debug("ChallengeService::getRegChallenge: rpId={}, userId={}", rpId, userId);

        RegOptionResponse.RegOptionResponseBuilder builder = RegOptionResponse.builder();

        // check rp id
        if (!rpService.contains(rpId)) {
            throw new FIDO2ServerRuntimeException(InternalErrorCode.RPID_NOT_FOUND, "RP ID not found: " + rpId);
        }

        // set rp info
        builder.rp(rpService.get(rpId));

        // set user info
        builder.user(regOptionRequest.getUser());

        // get user key
        List<UserKey> userKeys = userKeyService.getWithUserId(rpId, userId);

        // check registered credentials and set them to exclude list in order to prevent re-registration of the credential
        builder.excludeCredentials(getExcludeAndIncludeCredentials(userKeys));

        // set public key params with all available algorithms
        List<PublicKeyCredentialParameters> publicKeyCredentialParameters = new ArrayList<>();
        for (COSEAlgorithmIdentifier identifier : COSEAlgorithmIdentifier.values()) {
            PublicKeyCredentialParameters parameters = new PublicKeyCredentialParameters();
            parameters.setType(PublicKeyCredentialType.PUBLIC_KEY);
            parameters.setAlg(identifier);
            publicKeyCredentialParameters.add(parameters);
        }
        builder.pubKeyCredParams(publicKeyCredentialParameters);

        // create challenge
        builder.challenge(ChallengeGenerator.generate(ServerConstant.SERVER_CHALLENGE_LENGTH));

        // set timeout
        builder.timeout(sessionTtlMillis);

        // set authenticator selection
        builder.authenticatorSelection(regOptionRequest.getAuthenticatorSelection());

        // set attestation conveyance preference
        builder.attestation(regOptionRequest.getAttestation());

        // create and set session
        Session session = sessionService.createSessionData();
        //TODO revisar seteo temporal de user a sesion
        User user = new User();
        user.setId(userId);
        user.setEmail(regOptionRequest.getUser().getEmail());
        user.setFirstName(regOptionRequest.getUser().getFirstName());
        user.setLastName(regOptionRequest.getUser().getLastName());
        user.setUsername(regOptionRequest.getUser().getName());
        session.setUser(user);

        builder.sessionId(session.getId());

        // set extension
        AuthenticationExtensionsClientInputs extensions = new AuthenticationExtensionsClientInputs();

        // 1. credProps extension
        extensions.setCredProps(true);
        // 2. credProtect extension
        if (regOptionRequest.getCredProtect() != null) {
            extensions
                    .setCredentialProtectionPolicy(regOptionRequest.getCredProtect().getCredentialProtectionPolicy());
            extensions
                    .setEnforceCredentialProtectionPolicy(regOptionRequest.getCredProtect().getEnforceCredentialProtectionPolicy());
        }

        builder.extensions(extensions);

        // set server response
        RegOptionResponse regOptionResponse =
                builder.serverResponse(ServerResponse
                                               .builder()
                                               .internalErrorCode(InternalErrorCode.SUCCESS.getCode())
                                               .internalError(InternalErrorCode.SUCCESS.name())
                                               .build())
                       .build();

        session.setRegOptionResponse(regOptionResponse);

        // write session
        sessionService.createSession(session);

        log.debug("regOptionResponse: {}", regOptionResponse);

        return regOptionResponse;
    }

    /**
     * Get options for auth operation
     * @param authOptionRequest
     * @return
     */
    @Override
    public AuthOptionResponse getAuthChallenge(AuthOptionRequest authOptionRequest) {
        String rpId = authOptionRequest.getRpId();
        String userId = authOptionRequest.getUserId();
        log.debug("ChallengeService::getAuthChallenge: rpId={}, userId={}", rpId, userId);

        AuthOptionResponse.AuthOptionResponseBuilder builder = AuthOptionResponse.builder();

        // check rp id
        if (!rpService.contains(rpId)) {
            throw new FIDO2ServerRuntimeException(InternalErrorCode.RPID_NOT_FOUND, "RP ID not found: " + rpId);
        }

        // create challenge
        builder.challenge(ChallengeGenerator.generate(ServerConstant.SERVER_CHALLENGE_LENGTH));

        // set timeout -- FIDO2 requires milliseconds timeout
        builder.timeout(sessionTtlMillis);

        // set rp id
        builder.rpId(rpId);

        // get user key
        List<UserKey> userKeys = userKeyService.getWithUserId(rpId, userId);

        // set allowCredentials by searching with rp id and user id
        List<ServerPublicKeyCredentialDescriptor> allowCredentials = getExcludeAndIncludeCredentials(userKeys);
        if (!StringUtils.isEmpty(userId)) {
            // if there is no credentials for dedicated to the userId, throw an error
            if (allowCredentials.isEmpty()) {
                throw new FIDO2ServerRuntimeException(InternalErrorCode.CREDENTIAL_NOT_FOUND, "User Id: " + userId);
            }
        }

        builder.allowCredentials(allowCredentials);

        // set user verification
        builder.userVerification(authOptionRequest.getUserVerification());

        // set extension
        AuthenticationExtensionsClientInputs extensions = new AuthenticationExtensionsClientInputs();
        builder.extensions(extensions);

        // create and set session
        Session session = sessionService.createSessionData();
        builder.sessionId(session.getId());

        // set server response
        AuthOptionResponse authOptionResponse =
                builder.serverResponse(ServerResponse
                        .builder()
                        .internalErrorCode(InternalErrorCode.SUCCESS.getCode())
                        .internalError(InternalErrorCode.SUCCESS.name())
                        .build())
                        .build();

        session.setAuthOptionResponse(authOptionResponse);
        UserEntity userEntity = userRepository.findById(userId).orElse(null);

        User user = new User();
        if(userEntity != null) {
            user.setId(userId);
            user.setEmail(userEntity.getEmail());
            user.setUsername(userEntity.getUsername());
            user.setFirstName(userEntity.getFirstName());
            user.setLastName(userEntity.getLastName());
        }

        session.setUser(user);

        // write session
        sessionService.createSession(session);

        //sessionRepository.save(session);

        log.debug("authOptionResponse: {}", authOptionResponse);

        return authOptionResponse;
    }

    private List<ServerPublicKeyCredentialDescriptor> getExcludeAndIncludeCredentials(List<UserKey> userKeys) {
        List<ServerPublicKeyCredentialDescriptor> publicKeyCredentialDescriptors = new ArrayList<>();
        if (userKeys != null &&
                !userKeys.isEmpty()) {
            for (UserKey userKey : userKeys) {
                ServerPublicKeyCredentialDescriptor serverPublicKeyCredentialDescriptor = new ServerPublicKeyCredentialDescriptor();
                serverPublicKeyCredentialDescriptor.setId(userKey.getCredentialId());
                serverPublicKeyCredentialDescriptor.setType(PublicKeyCredentialType.PUBLIC_KEY);
                serverPublicKeyCredentialDescriptor.setTransports(userKey.getTransports());
                publicKeyCredentialDescriptors.add(serverPublicKeyCredentialDescriptor);
            }
        }

        return publicKeyCredentialDescriptors;
    }
}
