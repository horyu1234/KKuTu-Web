/*
 * KKuTu-Web (https://github.com/horyu1234/KKuTu-Web)
 * Copyright (C) 2021. horyu1234(admin@horyu.me)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package me.horyu.kkutuweb

import me.horyu.kkutuweb.extension.toHexString
import me.horyu.kkutuweb.setting.KKuTuSetting
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

private const val IV_LENGTH = 16

@Component
class AES256(
    @Autowired private val kKuTuSetting: KKuTuSetting
) {
    private val secretKeySpec: SecretKeySpec? = try {
        SecretKeySpec(kKuTuSetting.getCryptoKey().toByteArray(), "AES")
    } catch (e: Exception) {
        println("Error while generating key: $e")
        null
    }

    fun encrypt(data: String): String {
        val iv = ByteArray(IV_LENGTH)
        SecureRandom.getInstanceStrong().nextBytes(iv)

        val ivParameterSpec = IvParameterSpec(iv)

        val cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING")
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec)
        val encrypted = cipher.doFinal(data.toByteArray())

        return iv.toHexString() + ":" + encrypted.toHexString()
    }
}