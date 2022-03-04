FROM jboss/wildfly:18.0.1.Final


COPY target/quizgame.war /opt/jboss/wildfly/standalone/deployments/


# Bu örneği çalıştırmak için öncelikle
# quizgame.war'ı oluşturmak gerekir
#
# mvn package
#
# daha sonra, Docker imajı oluşturmalıyız:
#
# docker build . -t quizgame
#
# ardından, bu imajı şu komutla çalıştırabiliriz
#
# docker run -p 8080:8080 quizgame
#
# sonuç olarak uygulamaya bu adresten erişebiliriz:
#
# localhost:8080/quizgame
