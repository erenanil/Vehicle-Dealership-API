package com.anileren.jwt;

import java.io.IOException;

import javax.swing.Spring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.anileren.exception.BaseException;
import com.anileren.exception.ErrorMessage;
import com.anileren.exception.MessageType;
import com.anileren.utils.JwtConstant;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter{

    
    @Autowired
    JwtService jwtService;

    @Autowired
    UserDetailsService userDetailsService;
    
    //Spring Security, her HTTP isteği geldiğinde bu metodu çalıştırır.
    //JWT’nin doğrulanmasını ve kullanıcının kimlik bilgilerinin alınmasını sağlar.
    //Eğer token geçerliyse, kullanıcının kimliği SecurityContextHolder içine yerleştirilir.
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String header;
        String token;
        String username;

        header = request.getHeader(JwtConstant.AUTHORIZATION_HEADER_STRING);

        if (header == null || !header.startsWith(JwtConstant.TOKEN_BEARER_PREFIX)) {
            filterChain.doFilter(request, response);
            return;
        }

        token = header.substring(7);

        try {
            username = jwtService.getUsernameByToken(token);

            if(username != null || SecurityContextHolder.getContext().getAuthentication() == null){
                //username'e göre veritabanında kayıt olup olmamasını kontrol eder, varsa userDeails değişkenine setler.
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                //user varsa ve token'ı geçerliliğini kaybetmediyse 
                if (userDetails != null && !jwtService.isTokenExpired(token)) {

                    //Kullanıcıyı kimlik doğrulama sistemine tanıtmak ve yetkilerini (authorities) belirlemek için kullanılıyoruz.
                    UsernamePasswordAuthenticationToken authenticationToken = 
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                
                    
                    //buildDetails(request) ile ilgili HTTP isteği hakkında detaylı bilgiler eklenir (örneğin, istemcinin IP adresi ve oturum bilgileri).
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                
                    //Spring Security'nin, kimlik doğrulama bilgisini sakladığı merkezi bir bileşendir.
                    //SecurityContextHolder içindeki SecurityContext, her HTTP isteği için kullanıcı kimlik doğrulama bilgisini tutar.
                    //setAuthentication(authenticationToken) → Kimlik doğrulanmış kullanıcının bilgilerini Spring Security’ye bildirir.
                    //Bu sayede, uygulamanın herhangi bir yerinde SecurityContextHolder.getContext().getAuthentication() ile giriş yapmış kullanıcıya erişebiliriz.
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
                

            }
        }catch(ExpiredJwtException ex){
            throw new BaseException(new ErrorMessage(MessageType.TOKEN_IS_EXPIRED, ex.getMessage()));
        }
         catch (Exception e) {
            throw new BaseException(new ErrorMessage(MessageType.TOKEN_IS_EXPIRED, e.getMessage()));

        }
        filterChain.doFilter(request, response);
    }
    
}
