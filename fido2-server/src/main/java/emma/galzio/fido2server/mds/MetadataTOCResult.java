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

package emma.galzio.fido2server.mds;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MetadataTOCResult {
    private boolean result;
    private int totalCount;
    private int updatedCount;
    private int u2fEntryCount;
    private int uafEntryCount;
    private int fido2EntryCount;
    private String reason;
}
