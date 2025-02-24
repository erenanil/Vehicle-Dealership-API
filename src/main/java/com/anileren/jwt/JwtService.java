package com.anileren.jwt;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;


@Service
//JwtService sınıfı, JSON Web Token (JWT) oluşturma, doğrulama ve çözümleme işlemlerini yöneten bir servistir. JWT'nin güvenli bir şekilde üretilmesi, kullanıcı bilgilerini içermesi ve doğrulanması için çeşitli metotlar sağlar.
public class JwtService {

    //JWT’nin imzalanması (signing) için kullanılan gizli anahtardır.
    //HMAC-SHA256 (HS256) algoritmasıyla imzalama yapılacağı için güçlü bir anahtar gereklidir.
    //Anahtar, BASE64 kodlanmış bir dizgedir.
    private final String SECRET_KEY ="Fe1RMyoWzkz/bdxnzcxX5JiM2RI8zkp8b5mn5+OfmuU=";
    
    //Token üretimi
    public String generateToken(UserDetails userDetails){
        var token = Jwts.builder()
        .setSubject(userDetails.getUsername())//Token’ın Subject (sub) alanına kullanıcı adını ekler.

        //.claim(String key, Object value): JWT'ye özel bir claim (hak) eklemeye yarayan bir metottur.
        //"roles": Claim'in adı, yani JWT içinde "roles" adlı bir alan olacak.
        //...: Buraya bir değer atanacak ve "roles" alanı bir liste içerecek.
        .claim("roles", userDetails.getAuthorities()
        .stream()
        .map(GrantedAuthority::getAuthority)
        .toList())//Kullanıcının rollerini (yetkilerini) token içine ekler.

        .setIssuedAt(new Date())//Token’ın oluşturulma zamanını kaydeder.
        .setExpiration(new Date(System.currentTimeMillis()+ 1000*60*60*2))//Token’ın geçerlilik süresini belirler (2 saat).
        .signWith(getKey(),SignatureAlgorithm.HS256)//Token’ı SECRET_KEY ile HMAC-SHA256 algoritmasını kullanarak imzalar.
        .compact();//JWT’yi base64 formatında sıkıştırılmış hale getirir.

        return token;
    }
    
    //Token’ın İçeriğini Çözme
    //Token’ın geçerliliğini kontrol eder. içeriği değiştirilmişse hata fırlatır.
    public Claims getClaims(String token){
        Claims claims = 
        Jwts.parserBuilder()
        .setSigningKey(getKey())//Token'ın doğruluğunu kontrol etmek için imza anahtarını ekler
        .build()
        .parseClaimsJws(token)// Token'ı parse eder
        .getBody();// Token içindeki bilgileri (Claims) alır
        return claims;
    }

    //Genel Amaçlı Token Veri Çıkarma
    //Bu metod, verilen bir Claims fonksiyonunu (lambda) kullanarak token içindeki belirli bir değeri çıkarır.
    //Genel amaçlıdır, çünkü herhangi bir Claims alanını almak için kullanılabilir.
    public <T> T exportToken (String token , Function<Claims,T> claimsFunc){
        Claims claims = getClaims(token);
        return claimsFunc.apply(claims);
    }

    //Token’ın Süresinin Dolup Dolmadığını Kontrol Etme
    public boolean isTokenExpired(String token){
        Date expiredToken = exportToken(token, Claims::getExpiration);
        return new Date().after(expiredToken);
    }

    //Token içinden kullanıcı adını alma
    public String getUsernameByToken(String token){
        return exportToken(token, Claims::getSubject);
    }

    //Token için kullanılan gizli anahtarı almak
    public Key getKey(){
        byte[] decode = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(decode);
        
    }
}
