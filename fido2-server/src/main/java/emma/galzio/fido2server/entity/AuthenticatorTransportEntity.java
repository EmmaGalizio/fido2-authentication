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

package emma.galzio.fido2server.entity;


import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.antlr.v4.runtime.misc.NotNull;

@NoArgsConstructor
@Data
@Entity
@Table(name = "AUTHENTICATOR_TRANSPORT")
public class AuthenticatorTransportEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;    // internal

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name= "user_key_id", nullable = false)
    private UserKeyEntity userKeyEntity;

    @Column
    @NotNull
    private String transport;

    public AuthenticatorTransportEntity(@NotNull String transport) {
        this.transport = transport;
    }
}
