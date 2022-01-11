/*
 * Copyright (c) 2019-2022 Tomasz Babiuk
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package eu.automateeverything.mobileaccessplugin

import saltchannel.util.Hex
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.security.KeyStore
import java.security.KeyStore.PasswordProtection
import java.security.KeyStore.ProtectionParameter
import javax.crypto.SecretKey
import javax.crypto.spec.SecretKeySpec


class SecretStorage {
    companion object {
        const val KEYSTORE_PATH = "mobilecredentials.jks"
        const val KEYSTORE_PASS = "Using @l!as p@ssw0rdz"
    }

    @Throws(Exception::class)
    private fun loadKeyStore(): KeyStore {
        val file = File(KEYSTORE_PATH)
        val keyStore = KeyStore.getInstance("JCEKS")
        if (file.exists()) {
            keyStore.load(FileInputStream(file), KEYSTORE_PASS.toCharArray())
        } else {
            keyStore.load(null, null)
        }
        return keyStore
    }

    fun storeSecret(aliasPassword: String, alias: String, secret: ByteArray) {
        val keyStore = loadKeyStore()
        val protectionParam: ProtectionParameter = PasswordProtection(aliasPassword.toCharArray())

        val mySecretKey: SecretKey = SecretKeySpec(secret, "DSA")

        val secretKeyEntry = KeyStore.SecretKeyEntry(mySecretKey)
        keyStore.setEntry(alias, secretKeyEntry, protectionParam)

        saveKeyStore(keyStore)
    }

    fun loadSecret(aliasPassword: String, pubKeyAsAlias: String): saltchannel.util.KeyPair? {
        val keyStore = loadKeyStore()
        if (keyStore.containsAlias(pubKeyAsAlias)) {
            val protectionParam: ProtectionParameter = PasswordProtection(aliasPassword.toCharArray())
            val secretEntry = keyStore.getEntry(pubKeyAsAlias, protectionParam) as KeyStore.SecretKeyEntry
            return secretEntryToKeyPair(secretEntry, pubKeyAsAlias)
        }

        return null
    }

    private fun saveKeyStore(keyStore: KeyStore, ) {
        val file = File(KEYSTORE_PATH)
        keyStore.store(FileOutputStream(file), KEYSTORE_PASS.toCharArray())
    }

    private fun secretEntryToKeyPair(secretEntry: KeyStore.SecretKeyEntry, pubKey: String): saltchannel.util.KeyPair {
        val secretKeySpec = secretEntry.secretKey as SecretKeySpec
        val secretKey = secretKeySpec.encoded
        return saltchannel.util.KeyPair(secretKey, Hex.toBytes(pubKey))
    }
}