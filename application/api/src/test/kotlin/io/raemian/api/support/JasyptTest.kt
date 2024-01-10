package io.raemian.api.support

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor
import org.junit.jupiter.api.Test

class JasyptTest {
    @Test
    fun jasypt() {
        val input = ""
        val encrypted = jasyptEncrypt(input)

        // Assertions.assertThat(input).isEqualTo(jasyptDecrypt(encrypted))
    }

    private fun jasyptEncrypt(input: String): String {
        val encryptor = StandardPBEStringEncryptor()
        encryptor.setAlgorithm("PBEWithMD5AndDES")
        encryptor.setPassword("")
        return encryptor.encrypt(input)
    }

    private fun jasyptDecrypt(input: String): String {
        val encryptor = StandardPBEStringEncryptor()
        encryptor.setAlgorithm("PBEWithMD5AndDES")
        encryptor.setPassword("")
        return encryptor.decrypt(input)
    }
}
