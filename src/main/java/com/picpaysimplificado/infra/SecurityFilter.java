package com.picpaysimplificado.infra;

import java.io.IOException;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.picpaysimplificado.domain.user.User;
import com.picpaysimplificado.repositories.UserRepository;
import com.picpaysimplificado.services.implementation.TokenServiceImplementation;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class SecurityFilter extends OncePerRequestFilter{

  @Autowired
  private TokenServiceImplementation tokenService;

  @Autowired
  private UserRepository userRepository;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    var token = this.recoverToken(request);
    var login = this.tokenService.validateToken(token);

    if(login != null) {
      User user = this.userRepository.findByEmail(login).orElseThrow(() -> new RuntimeException("User not found"));
      var authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
      var authentication = new UsernamePasswordAuthenticationToken(user, null, authorities);
      SecurityContextHolder.getContext().setAuthentication(authentication);
    }
    filterChain.doFilter(request, response);
  }
  
  private String recoverToken(HttpServletRequest request) {
    var authHeader = request.getHeader("Authorization");
    if(authHeader == null) return null;
    return authHeader.replace("Bearer ", "");
  }
}
