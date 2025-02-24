package com.anileren.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.anileren.handler.AuthEntryPoint;
import com.anileren.jwt.JwtAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecuirtyConfig {

    //HTTP güvenlik yapılandırılması için 

    public static final String REGISER = "/register";
    public static final String AUTHENTICATE = "/authenticate";
    public static final String REFRESH_TOKEN = "/refreshToken";


    @Autowired
    AuthenticationProvider authenticationProvider;

    @Autowired
    JwtAuthenticationFilter authenticationFilter;

    @Autowired
    AuthEntryPoint authEntryPoint;
    
        //Yetkilendirme kurallarını ve güvenlik yapılandırmasını belirtiyor.
        //SecurityFilterChain, Spring Security’nin HTTP isteklerini nasıl işleyeceğini belirler.
        //HttpSecurity nesnesi, HTTP isteklerine uygulanacak güvenlik yapılandırmasını sağlar.

     @Bean
     public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
        //CSRF (Cross-Site Request Forgery) korumasını kapatıyor.
        //Eğer API’niz sadece token tabanlı kimlik doğrulama (JWT gibi) kullanıyorsa, CSRF’ye ihtiyacınız olmaz.
        .csrf(csrf -> csrf.disable())
        //Yetkilendirme kuralları; hangi isteklerin serbest olduğunu, hangilerinin belirli roller gerektirdiğini tanımlar.
        .authorizeHttpRequests(auth -> auth
        .requestMatchers(REGISER, AUTHENTICATE,REFRESH_TOKEN)
        .permitAll()//yukardaki endpointlere herkes erişebilir.

        //GET istekleri, yalnızca "ROLE_USER" veya "ROLE_ADMIN" yetkisine sahip kullanıcılar tarafından erişilebilir.
        .requestMatchers(HttpMethod.GET, "/rest/api/**").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")
        
        //POST, PUT, DELETE işlemleri SADECE ADMIN tarafından yapılabilir
        .requestMatchers(HttpMethod.POST, "/rest/api/**").hasAuthority("ROLE_ADMIN")
            .requestMatchers(HttpMethod.PUT, "/rest/api/**").hasAuthority("ROLE_ADMIN")
            .requestMatchers(HttpMethod.DELETE, "/rest/api/**").hasAuthority("ROLE_ADMIN")

            .anyRequest().authenticated()
            //authenticated() ile burada belirtilmeyen tüm endpointlere erişebilmek için kullanıcıların kimlik doğrulaması yapması zorunlu.
            //Yetkilendirme şartı konulmamış istekler bile "giriş yapmış bir kullanıcı" olmayı gerektirir.
        )
        
        //SecurityContext’i manuel olarak kaydetmeye gerek kalmaz.
        //Spring Security, kullanıcı oturumunu kendisi yönetir.
        .securityContext(securityContext -> securityContext.requireExplicitSave(false)) 
        
        //  Exception Handling ve Authentication Management
        .exceptionHandling(exception -> exception.authenticationEntryPoint(new AuthEntryPoint()))// kimlik doğrulaması başarısız olduğunda istediğimiz tipte bir hata kodu almak için.

        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        //SessionCreationPolicy.STATELESS -> Spring Security’nin oturum (session) kullanmamasını sağlar.
        //JWT gibi token tabanlı kimlik doğrulamada, sunucu oturum saklamaz.
        //Her istek bağımsızdır ve kullanıcı kimliği her seferinde token ile doğrulanır.
        
        .authenticationProvider(authenticationProvider)
        //authenticationProvider, kullanıcıları nasıl doğrulayacağını belirleyen bileşendir.
        //Spring Security’nin varsayılan DaoAuthenticationProvider yerine özel bir kimlik doğrulama sağlayıcısı kullanmasını sağlar.
        //Genellikle bu JWT token veya özel şifreleme metodları içerir.
        
        .addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class);
        //authenticationFilter, özel bir Filter sınıfıdır.
        //Bu filtre, UsernamePasswordAuthenticationFilter’dan önce çalıştırılır.

        return http.build();
    }
    
    

}
