package me.horyu.kkutuweb

import me.horyu.kkutuweb.extension.toHexString
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

private const val IV_LENGTH = 16

@Component
class AES256(
        @Value("\${kkutu.crypto.secret-key}") private val secretKey: String
) {
    private val secretKeySpec: SecretKeySpec?

    init {
        secretKeySpec = try {
            SecretKeySpec(secretKey.toByteArray(), "AES")
        } catch (e: Exception) {
            println("Error while generating key: $e")
            null
        }
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