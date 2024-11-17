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

package emma.galzio.fido2server.attestation.none;

import emma.galzio.fido2server.common.server.AttestationType;
import org.springframework.stereotype.Component;

import emma.galzio.fido2server.attestation.AttestationVerificationResult;
import emma.galzio.fido2server.attestation.AttestationVerifier;
import emma.galzio.fido2server.model.AttestationStatement;
import emma.galzio.fido2server.model.AttestationStatementFormatIdentifier;
import emma.galzio.fido2server.model.AuthenticatorData;

import java.util.ArrayList;

@Component
public class NoneAttestationVerifier implements AttestationVerifier {
    @Override
    public AttestationStatementFormatIdentifier getIdentifier() {
        return AttestationStatementFormatIdentifier.NONE;
    }

    @Override
    public AttestationVerificationResult verify(AttestationStatement attestationStatement, AuthenticatorData authenticatorData,
                                                byte[] clientDataHash) {
        return AttestationVerificationResult
                .builder()
                .success(true)
                .type(AttestationType.NONE)
                .trustPath(new ArrayList<>())
                .format(AttestationStatementFormatIdentifier.NONE)
                .build();

    }
}