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

package emma.galzio.fido2rp.common.server;

import emma.galzio.fido2rp.common.PublicKeyCredentialEntity;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;


@Data
@ToString
public class ServerPublicKeyCredentialUserEntity extends PublicKeyCredentialEntity {
    @NotNull
    @Length(min = 1, max = 64)
    private String id;  //base64url encoded
    private String displayName;
    private String firstName;
    private String lastName;
    private String email;
}