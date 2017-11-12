package com.example.demo.config;
import com.example.demo.util.JwtTokenUtil;
import io.jsonwebtoken.JwtException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

/**
 *  jwt token验证类,验证成功后设置进去SecurityContext中
 */
public class VerifyTokenFilter extends OncePerRequestFilter {

  private JwtTokenUtil jwtTokenUtil;

  public VerifyTokenFilter(JwtTokenUtil jwtTokenUtil) {
    this.jwtTokenUtil = jwtTokenUtil;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
    try {
      if (request.getSession().getAttribute("token") == null){
        filterChain.doFilter(request,response);
      }
      String token = request.getSession().getAttribute("token").toString();
      MutableHttpServletRequest mutableRequest = new MutableHttpServletRequest(request);
      mutableRequest.putHeader("x-authorization",token);
      Optional<Authentication> authentication = jwtTokenUtil.verifyToken(mutableRequest);
      SecurityContextHolder.getContext().setAuthentication(authentication.orElse(null));
      filterChain.doFilter(mutableRequest,response);
    } catch (JwtException e) {
      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      //可以在这里指定重定向还是返回错误接口示例
    }
  }
}
