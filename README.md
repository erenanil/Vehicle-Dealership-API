Vehicle Dealership API (Spring Boot)

Bu proje, Spring Boot tabanlı bir araç alım-satım API'sidir ve JWT tabanlı kimlik doğrulama ile güvenliği sağlamaktadır. Kullanıcılar, rollerine (, ADMIN, USER) göre belirli işlemleri gerçekleştirebilirler.

Ayrıca sistem, Türkiye Cumhuriyet Merkez Bankası (TCMB) API'ını entegre ederek güncel döviz kurlarını çeker. Eğer satın alınmak istenen araç dolar bazlı fiyatlandırılmışsa, kullanıcının TL cinsinden bütçesi otomatik olarak döviz kuruna göre dönüştürülerek işlem yapılır. Satın alma sonrası kalan bütçe tekrar TL'ye çevrilerek güncellenir.

🚀 Özellikler
✅ JWT Authentication & Role-Based Access Control: Kullanıcı yetkilendirme ve erişim yönetimi.
✅ Araç Alım-Satım İşlemleri: Kullanıcıların belirlenen bütçelerle araç satın almasını sağlama.
✅ TCMB API Entegrasyonu: Güncel döviz kurlarını çekerek fiyat dönüşümü yapma.
✅ Bütçe Yönetimi: Araç alımı sonrası kalan parayı güncelleyerek TL’ye çevirme.
✅ Spring Boot Rest API: Modern, ölçeklenebilir ve güvenli API yapısı.

🛠️ Teknolojiler
Spring Boot 3+
Spring Security & JWT
JPA (Hibernate) & PostgreSQL
RESTful API
Feign Client (veya RestTemplate) ile TCMB API Entegrasyonu
Lombok, MapStruct, Validation

📌 Kurulum ve Çalıştırma
1️⃣ Gerekli Bağımlılıkları Yükleme
Öncelikle, projenin çalışması için Java 17+, Maven, ve PostgreSQL kurulu olmalıdır.


# TCMB API URL
currency.api.url=https://evds2.tcmb.gov.tr/service/ 

3️⃣ Uygulamayı Çalıştırma
sh
Copy
Edit
mvn spring-boot:run
veya

sh
Copy
Edit
java -jar target/vehicle-dealership-api.jar
API http://localhost:8080 adresinde çalışacaktır.


📌 API Örnekleri

🔑 Kimlik Doğrulama
Kayıt Ol (Register): POST /rest/api/register
Giriş Yap (Authenticate): POST /rest/api/authenticate

💱 Döviz Kuru Entegrasyonu
Güncel döviz kuru çekme: GET /rest/api/currency-rates

------------------------------------

Vehicle Dealership API (Spring Boot)

This project is a Spring Boot-based vehicle trading API with JWT-based authentication for security. Users can perform actions based on their roles (ADMIN, USER).

Additionally, the system integrates with the Central Bank of Turkey (TCMB) API to fetch real-time exchange rates. If a vehicle is priced in USD, the user's TL budget is automatically converted using the current exchange rate before the transaction. After purchasing, the remaining budget is converted back to TL and updated accordingly.

🚀 Features
✅ JWT Authentication & Role-Based Access Control: Secure user authentication and access management.
✅ Vehicle Purchase Transactions: Users can purchase vehicles within their allocated budgets.
✅ TCMB API Integration: Fetch real-time exchange rates for price conversion.
✅ Budget Management: Convert remaining budget back to TL after purchasing.
✅ Spring Boot Rest API: A modern, scalable, and secure API structure.

🛠️ Technologies

Spring Boot 3+
Spring Security & JWT
JPA (Hibernate) & PostgreSQL
RESTful API
Feign Client (or RestTemplate) for TCMB API Integration
Lombok, MapStruct, Validation
📌 Installation & Running

1️⃣ Install Dependencies
Before running the project, ensure that Java 17+, Maven, and PostgreSQL are installed.

properties
Copy
Edit
# TCMB API URL
currency.api.url=https://evds2.tcmb.gov.tr/service/ 
3️⃣ Run the Application
sh
Copy
Edit
mvn spring-boot:run
or

sh
Copy
Edit
java -jar target/vehicle-dealership-api.jar
The API will be available at http://localhost:8080.

📌 API Examples

🔑 Authentication

Register: POST /rest/api/register
Authenticate: POST /rest/api/authenticate
💱 Currency Exchange Integration

Get real-time exchange rates: GET /rest/api/currency-rates
