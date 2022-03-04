# Hangi OS çalışacak belirtilir.
# Bizim durumumuzda OS imajı olarak WildFly kullanacağız
FROM jboss/wildfly:18.0.1.Final

# Oluşturulan target içindeki WAR dosyasını WildFly'ın kurulan WAR dosyalarını
# beklediği docker imajı yoluna kopyalayacağız
COPY target/examples.war /opt/jboss/wildfly/standalone/deployments/

# Burada bir CMD'ye ihtiyacımız yok, WildFly otomatik bir servis olarak başlatacaktır


# Bu örneği çalıştırmak için öncelikle
# examples.war'ı oluşturmak gerekir
#
# mvn package
#
# daha sonra, Docker imajı oluşturmalıyız:
#
# docker build . -t examples
#
# ardından, bu imajı şu komutla çalıştırabiliriz
#
# docker run -p 8080:8080 examples
#
# sonuç olarak uygulamaya bu adresten erişebiliriz:
#
# localhost:8080/examples




