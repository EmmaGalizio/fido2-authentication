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

package emma.galzio.fido2rp.model.transport;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import emma.galzio.fido2rp.common.UserVerificationRequirement;
import emma.galzio.fido2rp.common.extension.AuthenticationExtensionsClientInputs;
import emma.galzio.fido2rp.common.server.ServerPublicKeyCredentialDescriptor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServerPublicKeyCredentialGetOptionsResponse extends AdapterServerResponse {
    private String challenge;
    private long timeout;
    private String rpId;
    @JsonInclude(Include.NON_NULL)
    private List<ServerPublicKeyCredentialDescriptor> allowCredentials;
    @JsonInclude(Include.NON_NULL)
    private UserVerificationRequirement userVerification;
    //extensions
    private AuthenticationExtensionsClientInputs extensions;
}
