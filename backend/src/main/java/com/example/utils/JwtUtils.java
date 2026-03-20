package com.example.utils;

import io.jsonwebtoken.*;

import java.util.Date;
import java.util.Map;

public class JwtUtils {

    private static String signKey = "itheima";
    private static Long expire = 43200000L; // 24小时候过期

    /**
     * 生成JWT令牌
     * @param claims JWT第二部分负载 payload 中存储的内容
     * @return
     */
    public static String generateJwt(Map<String, Object> claims){
        String jwt = Jwts.builder()
                .addClaims(claims)
                .signWith(SignatureAlgorithm.HS256, signKey)
                .setExpiration(new Date(System.currentTimeMillis() + expire))
                .compact();
        return jwt;
    }

    /**
     * 解析JWT令牌
     * @param jwt JWT令牌
     * @return JWT第二部分负载 payload 中存储的内容
     */
    public static Claims parseJWT(String jwt){
        Claims claims = Jwts.parser()
                .setSigningKey(signKey)
                .parseClaimsJws(jwt)
                .getBody();
        return claims;
    }

    public static Claims validateJwt(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(signKey)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (SignatureException e) {
            // 签名不正确
            System.out.println("Invalid signature.");
        } catch (MalformedJwtException e) {
            // JWT格式错误
            System.out.println("Invalid JWT token.");
        } catch (ExpiredJwtException e) {
            // JWT过期
            System.out.println("Expired JWT token.");
        } catch (UnsupportedJwtException e) {
            // 不支持的JWT
            System.out.println("Unsupported JWT token.");
        } catch (IllegalArgumentException e) {
            // JWT为空或格式错误
            System.out.println("JWT token compact of handler are invalid.");
        }
        return null;
    }
}