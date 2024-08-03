package com.shopping.portal.util;

import java.nio.charset.MalformedInputException;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.SecureRandom;
import java.time.Instant;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletRequest;

@Component
public class JWTUtil {

	private static final Logger logger = LoggerFactory.getLogger(JWTUtil.class);
	private String jwtSecrete = "D75451ABAEF6820A42AF5D6C33722A9DB9288B61863CF22B2B4F5BBD3677DD847B97F3AEBFE2BAAC111ECBF103325639EE21E48025DD640109118257EAAD78B5";
    private final SecretKey encryptionKey = generateKey(); // 256-bit key for AES
    private final byte[] iv = new byte[16]; // 128-bit IV for AES

    public JWTUtil() {
        // Initialize the IV with random bytes
        new SecureRandom().nextBytes(iv);
    }

	    // Generate a 256-bit AES key
	    private SecretKey generateKey() {
	        try {
	            KeyGenerator keyGen = KeyGenerator.getInstance("AES");
	            keyGen.init(256, new SecureRandom());
	            return keyGen.generateKey();
	        } catch (Exception e) {
	            logger.error("Error generating encryption key: {}", e.getMessage());
	            throw new RuntimeException(e);
	        }
	    }
	    
	public String getJWTfromHeader(HttpServletRequest request) {
		String bearerToken = request.getHeader("Authorization"); 
		logger.debug("Authorization Header :: {} ", bearerToken);
		if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
			return bearerToken.substring(7);
		}
		return null;
	}

	private Key key() {
		try {
			return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecrete));
		} catch (IllegalArgumentException e) {
			logger.error("Error generating key: {}", e.getMessage());
			throw e;
		}
	}

	public String generateTokenfromUsername(UserDetails userDetails) {
		try {
			Map<String, Object> claims = new HashMap<>();
			claims.put("role", userDetails.getAuthorities());
			claims.put("name", userDetails.getUsername());
			claims.put("password", userDetails.getPassword());
			String jwtToken =  Jwts.builder().claims(claims).subject(userDetails.getUsername()).issuedAt(new Date())
					.expiration(Date.from(Instant.now().plusMillis(TimeUnit.MINUTES.toMillis(30)))).signWith(key()).compact();
            return encrypt(jwtToken);
		} catch (Exception e) {
			logger.error("Error generating token from username: {}", e.getMessage());
			return null;
		}
	}

	public String getUsernameFromToken(String jwtToken) {
		try {
            String decryptedToken = decrypt(jwtToken);
			return Jwts.parser().verifyWith((SecretKey) key()).build().parseSignedClaims(decryptedToken).getPayload()
					.getSubject();
		} catch (Exception e) {
			logger.error("Error getting username from token: {}", e.getMessage());
			return null;
		}
	}

	public Claims getClaims(String jwtToken) {
		try {
            String decryptedToken = decrypt(jwtToken);
			return Jwts.parser().verifyWith((SecretKey) key()).build().parseSignedClaims(decryptedToken).getPayload();
		} catch (Exception e) {
			logger.error("Error getting claims from token: {}", e.getMessage());
			return null;
		}
	}

	public boolean isTokenvalid(String jwtToken) throws Exception {
        String decryptedToken = decrypt(jwtToken);
		Claims claims = getClaims(decryptedToken);
		return claims.getExpiration().after(Date.from(Instant.now()));
	}

	public boolean validateJwtToken(String jwtToken) {
		try {
	        String decryptedToken = decrypt(jwtToken);
			Jwts.parser().verifyWith((SecretKey) key()).build().parseSignedClaims(decryptedToken);
			return true;
		} catch (MalformedJwtException e) {
			logger.error("Invalid JWT Token : {}", e.getMessage());
		} catch (ExpiredJwtException e) {
			logger.error("JWT token Expired : {}", e.getMessage());
		} catch (UnsupportedJwtException e) {
			logger.error("JWT token Unsupported : {}", e.getMessage());
		} catch (IllegalArgumentException e) {
			logger.error("JWT claims string is empty : {}", e.getMessage());
		} catch (Exception e) {
			logger.error("Some Exception Occured in validateJwtToken  : {}", e.getMessage());
		}
		return false;
	}

    private String encrypt(String data) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        SecretKeySpec keySpec = new SecretKeySpec(encryptionKey.getEncoded(), "AES");
        IvParameterSpec ivSpec = new IvParameterSpec(iv);
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);
        byte[] encryptedData = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(encryptedData);
    }

    private String decrypt(String encryptedData) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        SecretKeySpec keySpec = new SecretKeySpec(encryptionKey.getEncoded(), "AES");
        IvParameterSpec ivSpec = new IvParameterSpec(iv);
        cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
        byte[] decodedData = Base64.getDecoder().decode(encryptedData);
        byte[] decryptedData = cipher.doFinal(decodedData);
        return new String(decryptedData, StandardCharsets.UTF_8);
    }
    
    public String decodeEncryptedkey(String encryptedJwtToken) {
    	try {
			return decrypt(encryptedJwtToken);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return encryptedJwtToken;
    }
}
