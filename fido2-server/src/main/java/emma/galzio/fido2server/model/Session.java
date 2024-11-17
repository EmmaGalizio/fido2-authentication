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

package emma.galzio.fido2server.model;

import lombok.ToString;
import org.springframework.data.annotation.Id;

import emma.galzio.fido2server.common.server.AuthOptionResponse;
import emma.galzio.fido2server.common.server.RegOptionResponse;

import lombok.Data;

@Data
@ToString
public class Session {
    @Id
    private String id;
    private String hmacKey;
    private RegOptionResponse regOptionResponse;
    private AuthOptionResponse authOptionResponse;
    private boolean served;
    private User user;

    //TODO ver como:
    // - Representar un usuario en la DB
    // - Conectar el usuario a la sesion
    // - Si es posible, usar la sesion solo para el proceso inicial, después solo JWT
}
