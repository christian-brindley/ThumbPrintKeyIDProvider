/*
 * The contents of this file are subject to the terms of the Common Development and
 * Distribution License (the License). You may not use this file except in compliance with the
 * License.
 *
 * You can obtain a copy of the License at legal/CDDLv1.0.txt. See the License for the
 * specific language governing permission and limitations under the License.
 *
 * When distributing Covered Software, include this CDDL Header Notice in each file and include
 * the License file at legal/CDDLv1.0.txt. If applicable, add the following below the CDDL
 * Header, with the fields enclosed by brackets [] replaced by your own identifying
 * information: "Portions copyright [year] [name of copyright owner]".
 *
 * Copyright 2014-2019 ForgeRock AS. All Rights Reserved
 */
package org.forgerock.openam.examples;

import java.security.PublicKey;
import java.security.cert.Certificate;
import java.util.Optional;
import org.forgerock.openam.secrets.KeyStoreKeyIdProvider;
import org.forgerock.json.jose.jwk.KeyUse;
import org.forgerock.json.jose.jws.SupportedEllipticCurve;
import java.security.interfaces.ECPublicKey;
import java.security.interfaces.RSAPublicKey;
import com.nimbusds.jose.jwk.ECKey;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.Curve;
import com.nimbusds.jose.util.Base64URL;

/**
 * KeyStoreKeyIdProvider implementation which provides key ids built from SHA1 hash of JWK thumbprint
 * Uses nimbus library to provide hash of rfc7638 thumbprint
 */

public class ThumbprintKeyStoreKeyIdProvider implements KeyStoreKeyIdProvider {

    @Override
    public String getKeyId(KeyUse keyUse, String alias, PublicKey publicKey, Optional<Certificate> certificate) {
        if (publicKey instanceof ECPublicKey) {
            try {
                ECPublicKey ecPublicKey = (ECPublicKey) publicKey;
                Curve curve = getCurve(ecPublicKey);
                ECKey ecKey = new ECKey.Builder( curve, ecPublicKey).build();
                Base64URL thumbprint = ecKey.computeThumbprint("SHA-1");
                return thumbprint.toString();
            }
            catch (Exception e) {
                throw new IllegalArgumentException("Can't decipher EC key " + e);
            }
        } else if (publicKey instanceof RSAPublicKey) {
            try {
                RSAKey rsaKey = new RSAKey.Builder((RSAPublicKey) publicKey).build();
                Base64URL thumbprint = rsaKey.computeThumbprint("SHA-1");
                return thumbprint.toString();
            }
            catch (Exception e) {
                throw new IllegalArgumentException("Can't decipher RSA key");
            }

        } else {
            throw new IllegalArgumentException("Public key type '" + publicKey + "' not supported.");
        }
    }

    private Curve getCurve(ECPublicKey ecPublicKey) {
        Curve c = null;

        switch (SupportedEllipticCurve.forKey(ecPublicKey))
        {
            case P256:
                c = Curve.P_256;
                break;

            case P384:
                c = Curve.P_384;
                break;

            case P521:
                c = Curve.P_521;
                break;
        }

        return c;
    }

}


