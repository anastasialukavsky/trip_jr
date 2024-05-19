package com.trip_jr.tripJr.service.utils.jwt
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.stereotype.Component
import java.util.*
import javax.crypto.Cipher.SECRET_KEY

@Component
class JwtUtils {
    companion object {
        private const val EXPIRATION_TIME = 86400000 // 1 day
    }

    fun generateToken(email: String): String {
        val key = Keys.secretKeyFor(io.jsonwebtoken.SignatureAlgorithm.HS256)
        return Jwts.builder()
            .setSubject(email)
            .setIssuedAt(Date())
            .setExpiration(Date(System.currentTimeMillis() + EXPIRATION_TIME))
            .signWith(key)
            .compact()
    }
}