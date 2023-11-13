package io.raemian.springboot.core.auth.support

import io.jsonwebtoken.Claims
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.MalformedJwtException
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.UnsupportedJwtException
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import io.jsonwebtoken.security.SecurityException
import io.raemian.springboot.core.auth.domain.CurrentUser
import io.raemian.springboot.core.auth.domain.TokenDTO
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.stereotype.Component
import java.security.Key
import java.util.Date


@Component
class TokenProvider() {
    private val key: Key

    private val secretKey: String =
        "c3ByaW5nLWJvb3Qtc2VjdXJpdHktand0LXR1dG9yaWFsLWppd29vbi1zcHJpbmctYm9vdC1zZWN1cml0eS1qd3QtdHV0b3JpYWwK"
    private val AUTHORITIES_KEY = "auth"
    private val EMAIL_KEY = "email"
    private val ID_KEY = "id"
    private val BEARER_TYPE = "Bearer"
    private val ACCESS_TOKEN_EXPIRE_TIME = (1000 * 60 * 300)  // 300분
    private val REFRESH_TOKEN_EXPIRE_TIME = (1000 * 60 * 60 * 24 * 70) // 70일

    init {
        val keyBytes = Decoders.BASE64.decode(secretKey)
        key = Keys.hmacShaKeyFor(keyBytes)
    }

    fun generateTokenDto(authentication: Authentication): TokenDTO {
        val currentUser = authentication.principal as CurrentUser
        // 권한들 가져오기
        val authorities: String = authentication.authorities
            .map { obj: GrantedAuthority -> obj.authority }
            .joinToString(",")
        val now: Long = Date().time

        // Access Token 생성
        val accessTokenExpiresIn = Date(now + ACCESS_TOKEN_EXPIRE_TIME)
        val accessToken: String = Jwts.builder()
            .setSubject(authentication.name) // payload "sub": "name"
            .claim(AUTHORITIES_KEY, authorities) // payload "auth": "ROLE_USER"
            .claim(EMAIL_KEY, authentication.name)
            .claim(ID_KEY, currentUser.id)
            .setExpiration(accessTokenExpiresIn) // payload "exp": 151621022 (ex)
            .signWith(key, SignatureAlgorithm.HS512) // header "alg": "HS512"
            .compact()

        // Refresh Token 생성
        val refreshToken: String = Jwts.builder()
            .setExpiration(Date(now + REFRESH_TOKEN_EXPIRE_TIME))
            .signWith(key, SignatureAlgorithm.HS512)
            .compact()
        return TokenDTO(
            grantType = BEARER_TYPE,
            accessToken = accessToken,
            refreshToken = refreshToken,
            accessTokenExpiresIn = accessTokenExpiresIn.time,
        )
    }

    fun getAuthentication(accessToken: String): Authentication {
        // 토큰 복호화
        val claims = parseClaims(accessToken)
        if (claims[AUTHORITIES_KEY] == null) {
            throw RuntimeException("권한 정보가 없는 토큰입니다.")
        }

        claims[AUTHORITIES_KEY].toString().split(",".toRegex())
        // 클레임에서 권한 정보 가져오기
        val authorities = claims[AUTHORITIES_KEY]
            .toString()
            .split(",".toRegex())
            .dropLastWhile { it.isEmpty() }

        val id = claims[ID_KEY]
            .toString()
            .toLong()

        // UserDetails 객체를 만들어서 Authentication 리턴
        val principal = CurrentUser(id = id, claims.subject, "", authorities)
        return UsernamePasswordAuthenticationToken(principal, "", principal.authorities)
    }

    fun validateToken(token: String?): Boolean {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token)
            return true
        } catch (e: SecurityException) {
            // log.info("잘못된 JWT 서명입니다.")
            println("securty exception" + e)
        } catch (e: MalformedJwtException) {
            // log.info("잘못된 JWT 서명입니다.")
            println("malformed " + e)
        } catch (e: ExpiredJwtException) {
            // log.info("만료된 JWT 토큰입니다.")
            println("expired token " + e)
        } catch (e: UnsupportedJwtException) {
            // log.info("지원되지 않는 JWT 토큰입니다.")
            println("not supoorted " + e)
        } catch (e: IllegalArgumentException) {
            // log.info("JWT 토큰이 잘못되었습니다.")
            println("illegal " + e)
        }
        return false
    }

    private fun parseClaims(accessToken: String): Claims {
        return try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody()
        } catch (e: ExpiredJwtException) {
            e.claims
        }
    }
}