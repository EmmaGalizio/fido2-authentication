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

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import com.fasterxml.jackson.dataformat.cbor.CBORFactory;
import com.fasterxml.jackson.dataformat.cbor.CBORGenerator;

import emma.galzio.fido2server.common.server.COSEAlgorithm;
import emma.galzio.fido2server.cose.COSEEllipticCurve;
import emma.galzio.fido2server.cose.COSEKeyType;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OctetKey extends CredentialPublicKey {
    private COSEAlgorithm algorithm;
    private COSEEllipticCurve curve;
    private byte[] x;

    @Override
    public byte[] encode() throws IOException {

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        CBORFactory factory = new CBORFactory();
        CBORGenerator gen = factory.createGenerator(outputStream);

        // start map
        gen.writeStartObject();

        gen.writeFieldId(1);    // kty label
        gen.writeNumber(COSEKeyType.OKP.getValue()); // EC2 kty

        gen.writeFieldId(3);    // alg label
        gen.writeNumber(algorithm.getValue());  // alg value

        gen.writeFieldId(-1);   // crv label
        gen.writeNumber(curve.getValue());  // crv value

        gen.writeFieldId(-2);   // x label
        gen.writeBinary(x); // x value

        // end map
        gen.writeEndObject();

        gen.close();

        return outputStream.toByteArray();
    }
}
