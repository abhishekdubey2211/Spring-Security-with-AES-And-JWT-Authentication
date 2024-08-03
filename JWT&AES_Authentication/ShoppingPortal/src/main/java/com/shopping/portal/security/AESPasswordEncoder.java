package com.shopping.portal.security;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.shopping.portal.util.AESUtil;
import com.shopping.portal.util.JWTUtil;

public class AESPasswordEncoder implements PasswordEncoder {

    private final AESUtil aesUtil;

    public AESPasswordEncoder() {
		this.aesUtil = new AESUtil();
    }

    @Override
    public String encode(CharSequence rawPassword) {
        try {
            return aesUtil.encrypt(rawPassword.toString());
        } catch (Exception e) {
            throw new RuntimeException("Encryption error", e);
        }
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        try {
            String decryptedPassword = aesUtil.decodeEncryptedkey(encodedPassword);
            return rawPassword.toString().equals(decryptedPassword);
        } catch (Exception e) {
            throw new RuntimeException("Decryption error", e);
        }
    }
}
