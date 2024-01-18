package io.raemian.api.config

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@EnableEncryptableProperties
class JasyptConfig(
    @Value("\${JASYPT_ENCRYPTION_PASSWORD: ''}") val password: String,
) {

    @Bean
    fun jasyptStringEncryptor(): StandardPBEStringEncryptor {
        val encryptor = StandardPBEStringEncryptor()
        encryptor.setAlgorithm("PBEWithMD5AndDES")
        encryptor.setPassword(password)
        return encryptor
    }
}
