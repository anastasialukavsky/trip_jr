package com.trip_jr.tripJr.service.utils.jwt

import com.trip_jr.tripJr.service.utils.user.CustomUserDetails
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.stereotype.Component
import java.util.*

@Component
class JwtUtils {
    companion object {
        private const val EXPIRATION_TIME = 86400000 // 1 day
        private val secret_key = "secret_key"
    }

    fun generateToken(userDetails: CustomUserDetails): String {

        val claims = HashMap<String, Any>()
        claims["userId"] = userDetails.getUserId()
        claims["email"] = userDetails.getUsername()
        claims["passwordHash"] = userDetails.getPassword()
        claims["role"] = userDetails.getUserRole()

        return createToken(claims, userDetails.userDetails.email)
    }

    fun createToken(claims: Map<String, Any>, subject: String): String {
        val currentDate = Date(System.currentTimeMillis())
        val key = Keys.secretKeyFor(io.jsonwebtoken.SignatureAlgorithm.HS256)

        return Jwts.builder()
            .setClaims(claims)
            .setSubject(subject)
            .setIssuedAt(currentDate)
            .setExpiration(Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
            .signWith(key)
            .compact()
    }

    fun extractAllClaims(token: String): Claims {
        return Jwts.parserBuilder().setSigningKey(secret_key.toByteArray()).build().parseClaimsJws(token).body
    }

    fun <T> extractClaim(token: String, claimResolver: (Claims) -> T): T {
        val claims = extractAllClaims(token)
        return claimResolver.invoke(claims)
    }


    fun extractUserId(token: String): String {
        val userId = extractClaim(token) { claims ->
            claims["userId"] as String
        }

        return userId
    }

    fun extractUserEmail(token: String): String {
        val email = extractClaim(token) { claims ->
            claims["email"] as String
        }

        return email
    }

    fun validateToken(token: String, userDetails: CustomUserDetails): Boolean {
        val email = extractUserEmail(token)
        return email == userDetails.userDetails.email && !isTokenExpired(token)
    }

    fun extractClaimsAndExpiration(token: String): Pair<Claims, Date> {
        val jwtParser = Jwts.parserBuilder().setSigningKey(secret_key.toByteArray()).build()
        val jwt = jwtParser.parseClaimsJws(token)
        val claims = jwt.body
        val expiration = claims.expiration
        return Pair(claims, expiration)
    }

    fun isTokenExpired(token: String): Boolean {
        val (claims, expiration) = extractClaimsAndExpiration(token)
        return expiration != null && expiration.before(Date())
    }







}