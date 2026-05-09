package com.lcy;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.Test;

import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtTest {
    // // 将Map声明为成员变量
    Map<String, Object> claims = new HashMap<>();

    // 创建SecretKey
    String secret = "lcyaidywdywwoainiyibeizi12345678910";
    byte[] secretBytes = Base64.getEncoder().encode(secret.getBytes());
    SecretKeySpec secretKey = new SecretKeySpec(secretBytes, SignatureAlgorithm.HS256.getJcaName());
    /**
     * 生成JWT令牌--Jwts.builder()
     */
    @Test
    public void testGenJwt() {

        //旧版本jjwt0.9.1的写法
       /* String jwt = Jwts.builder().signWith("lcy123", io.jsonwebtoken.SignatureAlgorithm.HS256)
                .addClaims(claims)
                .setExpiration(new Date(System.currentTimeMillis() + 12 * 3600 * 1000))
                .compact();*/
        //新版本jjwt的写法

        claims.put("id", 1);
        claims.put("username", "lcy");

        String jwt = Jwts.builder()
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .addClaims(claims)//添加自定义信息
                .setExpiration(new Date(System.currentTimeMillis() + 12 * 3600 * 1000))//设置过期时间
                .compact();//构建jwt

        System.out.println(jwt);

    }
    /**
     * 解析JWT令牌--Jwts.parser()
     */
    @Test
    public void testParseJwt(){
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJpZCI6MTAsInVzZXJuYW1lIjoibGN5IiwiZXhwIjoxNzYyOTEyNzYyfQ.KsnrTkHep_I3pqvbDsRF9HdlfD2H3I88PzvdkKS-HyA";

        Claims claims = Jwts
                .parser()//创建JwtParser对象
                .setSigningKey(secretKey)//设置签名密钥
                .parseClaimsJws(token)//解析JWT令牌
                .getBody();//获取Claims对象
        System.out.println(claims);
    }
    /**
     * 验证JWT令牌
     */


}



