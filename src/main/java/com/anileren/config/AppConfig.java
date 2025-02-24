package com.anileren.config;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.anileren.model.User;
import com.anileren.repository.UserRepository;

@Configuration
public class AppConfig {
    //Security yapılandırmalarını içeren bir konfigürasyon sınıfı
    //authentication ve authorization işlemlerini yönetmek için gerekli olan bean'leri tanımlıyoruz.

    @Autowired
    UserRepository userRepository;

//Spring Security, kullanıcı bilgilerini doğrulamak için bir UserDetailsService bileşenine ihtiyaç duyar. Biz de bunu sağlıyoruz.    
//Spring Security, kimlik doğrulama işlemi sırasında kullanıcı bilgilerini bu metod üzerinden alır. Bu metod, veritabanından kullanıcıyı arar ve bulamazsa hata fırlatır.
    @Bean
public UserDetailsService userDetailsService() {
    return new UserDetailsService() {
        @Override
        public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));
                    
            //Spring Security, kullanıcı rollerini GrantedAuthority nesneleri olarak almak ister.
            return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                user.getRoles().stream()//user.getRoles().stream() ile kullanıcının rollerini alıyoruz.
                    .map(role -> new SimpleGrantedAuthority("ROLE_" + role.name())) // Spring Security rollerle çalışırken genellikle "ROLE_" öneki beklediği için her role "ROLE_" ekleniyor.
                    .collect(Collectors.toList())
            );
        }
    };
}
    
    //AuthenticationProvider, kimlik doğrulama işleminin gerçekleştirildiği yerdir. Yani, kullanıcının kimlik bilgilerini alır, bu bilgileri doğrular ve sonuç olarak bir Authentication nesnesi döndürür.
    //Kimlik doğrulama işleminin nasıl yapılacağını belirler. Örn, kullanıcı bilgileri nereden alınacak (UserDetailsService), şifreler nasıl karşılaştırılacak (PasswordEncoder) gibi detaylar burada tanımlanır.
    @Bean
    AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider dao = new DaoAuthenticationProvider();
        dao.setUserDetailsService(userDetailsService());
        dao.setPasswordEncoder(passwordEncoder());

        return dao;
    }

    //***AuthenticationManager, kimlik doğrulama işleminin yönetildiği yerdir. Yani, kimlik doğrulama isteğini alır ve bu isteği uygun AuthenticationProvider'a yönlendirir.
    //Örn, bir kullanıcı giriş yapmaya çalıştığında, AuthenticationManager bu isteği alır ve uygun AuthenticationProvider'a iletir.
    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception{
        return configuration.getAuthenticationManager();
    }

    @Bean
    BCryptPasswordEncoder passwordEncoder(){
      return new BCryptPasswordEncoder();
    }





}
