package io.raemian.api.config

import org.jasypt.encryption.StringEncryptor
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class EncryptorConfig {

    @Bean("stringEncryptor")
    fun stringEncryptor(
        @Value(value = "\${jasypt.secret-key}") encryptorPassword: String?,
    ): StringEncryptor {
        val pooledPBEStringEncryptor = PooledPBEStringEncryptor()

        val simpleStringPBEConfig = SimpleStringPBEConfig()
        simpleStringPBEConfig.setPassword(encryptorPassword)
        simpleStringPBEConfig.poolSize = 1

        pooledPBEStringEncryptor.setConfig(simpleStringPBEConfig)
        return pooledPBEStringEncryptor
    }
}
