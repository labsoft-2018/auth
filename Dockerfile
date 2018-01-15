FROM java:8
MAINTAINER labsoft-2018

ADD target/auth-0.0.1-SNAPSHOT-standalone.jar /srv/auth.jar

EXPOSE 8080

CMD ["java", "-jar", "/srv/auth.jar"]
