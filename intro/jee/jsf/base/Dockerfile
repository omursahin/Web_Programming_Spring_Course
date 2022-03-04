# Hangi OS kullanılacak belirtilir.
# Bizim durumumuzda OS olarak WildFly kullanacağız
FROM jboss/wildfly:18.0.1.Final


# "target" içerisinde oluşturulan "WAR" WildFly'ın kurulan WAR dosyalarını beklemiş olduğu Docker imajı klasörüne atılır,
COPY target/base.war /opt/jboss/wildfly/standalone/deployments/


# Burada CMD ihtiyacı yok WildFly servis olarak otomatik başlatılır
