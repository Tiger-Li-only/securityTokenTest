package com.example.demo.util;
import com.example.demo.domain.SysRole;
import com.example.demo.domain.SysUser;
import com.example.demo.domain.TokenUserAuthentication;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * jwt token
 *
 */
public  class JwtTokenUtil {
  /**
   * token过期时间
   */
  private static final long VALIDITY_TIME_MS = 24 * 60 * 60 * 1000; // 24 hours  validity
  /**
   * header中标识
   */
  private static final String AUTH_HEADER_NAME = "x-authorization";

  /**
   * 签名密钥
   */
  @Value("${jwt.token.secret}")
  private String secret;

  /**
   * 验签
   */
  public Optional<Authentication> verifyToken(HttpServletRequest request) {
    final String token = request.getHeader(AUTH_HEADER_NAME);
    if (token != null && !token.isEmpty()){
      final SysUser user = parse(token.trim());
      if (user != null) {
        return Optional.of(new TokenUserAuthentication(user, true));
      }
    }
    return Optional.empty();
  }


  /**
   * 从用户中创建一个jwt Token
   *
   * @param sysUser 用户
   * @return token
   */
  public  String create(SysUser sysUser) {
    return Jwts.builder()
        .setExpiration(new Date(System.currentTimeMillis() + VALIDITY_TIME_MS))
        .setSubject(sysUser.getUsername())
        .claim("id", sysUser.getId())
        .claim("roles", sysUser.getRoles())
        .signWith(SignatureAlgorithm.HS256, secret)
        .compact();
  }

  /**
   * 从token中取出用户
   */
  public SysUser parse(String token) {
    Claims claims = Jwts.parser()
        .setSigningKey(secret)
        .parseClaimsJws(token)
        .getBody();
    SysUser sysUser = new SysUser();
    sysUser.setId(NumberUtils.toLong(claims.getId()));
    sysUser.setUsername(claims.get("username",String.class));
    sysUser.setRoles((List<SysRole>) claims.get("roles"));
    return sysUser;
  }

}
