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

package emma.galzio.fido2server.common;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@AllArgsConstructor
public enum PublicKeyCredentialType {
    PUBLIC_KEY("public-key");

    @JsonValue @Getter private final String value;

    @JsonCreator(mode=JsonCreator.Mode.DELEGATING)
    public static PublicKeyCredentialType fromValue(String value) {
        return Arrays.stream(PublicKeyCredentialType.values())
                     .filter(e -> e.value.equals(value))
                     .findFirst()
                     .get();
    }
}
