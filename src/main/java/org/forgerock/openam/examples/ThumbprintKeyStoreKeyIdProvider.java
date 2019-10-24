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


/**
 * KeyStoreKeyIdProvider implementation for ForgeRock AM.
 *
 * Provides rfc7638 compliant key ids built from SHA1 hash of JWK thumbprint
 */

public class ThumbprintKeyStoreKeyIdProvider implements KeyStoreKeyIdProvider {
    @Override
    public String getKeyId(KeyUse keyUse, String alias, PublicKey publicKey, Optional<Certificate> certificate) {

        return ThumbprintUtils.getThumbprintFromKey(publicKey);
    }
}


