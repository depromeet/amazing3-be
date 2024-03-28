package io.raemian.api.support

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor
import org.junit.jupiter.api.Test

class JasyptTest {
    @Test
    fun jasypt() {
        val input = "http://localhost:3000/oauth2/token"
        val encrypted = jasyptEncrypt(input)
        println(encrypted)

        // Assertions.assertThat(input).isEqualTo(jasyptDecrypt(encrypted))
    }

    private fun jasyptEncrypt(input: String): String {
        val encryptor = StandardPBEStringEncryptor()
        encryptor.setAlgorithm("PBEWithMD5AndDES")
        encryptor.setPassword("0ne-bailey")
        return encryptor.encrypt(input)
    }

    private fun jasyptDecrypt(input: String): String {
        val encryptor = StandardPBEStringEncryptor()
        encryptor.setAlgorithm("PBEWithMD5AndDES")
        encryptor.setPassword("0ne-bailey")
        return encryptor.decrypt(input)
    }
}
