package io.raemian.api.auth.converter

import com.nimbusds.jose.util.IOUtils
import com.nimbusds.jose.util.StandardCharset
import io.jsonwebtoken.Jwts
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo
import org.bouncycastle.openssl.PEMParser
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter
import org.springframework.core.convert.converter.Converter
import org.springframework.core.io.ClassPathResource
import org.springframework.http.RequestEntity
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequestEntityConverter
import org.springframework.stereotype.Component
import org.springframework.util.LinkedMultiValueMap
import java.io.File
import java.io.StringReader
import java.security.PrivateKey
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.Date

@Component
class TokenRequestEntityConverter(
    private val appleLoginProps: AppleLoginProps,
) : Converter<OAuth2AuthorizationCodeGrantRequest, RequestEntity<*>> {

    private val defaultConverter: OAuth2AuthorizationCodeGrantRequestEntityConverter =
        OAuth2AuthorizationCodeGrantRequestEntityConverter()

    override fun convert(req: OAuth2AuthorizationCodeGrantRequest): RequestEntity<*> {
        val entity = defaultConverter.convert(req)!!
        val registrationId = req.clientRegistration.registrationId
        val params = entity.body as LinkedMultiValueMap<String, String>

        if (registrationId.contains("apple")) {
            params.set("client_secret", createClientSecret())
        }

        return RequestEntity<Any>(params, entity.headers, entity.method, entity.url)
    }

    fun getPrivateKey(): PrivateKey {
        val resource = File(appleLoginProps.keyPath)
        val converter = JcaPEMKeyConverter()

        val input = resource.inputStream()
        val pemParser = PEMParser(StringReader(IOUtils.readInputStreamToString(input, StandardCharset.UTF_8)))
        val key = pemParser.readObject() as PrivateKeyInfo

        return converter.getPrivateKey(key)
    }

    fun createClientSecret(): String {
        val expirationDate: Date =
            Date.from(LocalDateTime.now().plusDays(30).atZone(ZoneId.systemDefault()).toInstant())
        val jwtHeader = mapOf("kid" to appleLoginProps.keyId, "alg" to "ES256")

        return Jwts.builder()
            .setHeaderParams(jwtHeader)
            .setIssuer(appleLoginProps.teamId)
            .setIssuedAt(Date(System.currentTimeMillis()))
            .setExpiration(expirationDate)
            .setAudience(appleLoginProps.url)
            .setSubject(appleLoginProps.clientId)
            .signWith(getPrivateKey())
            .compact()
    }
}
