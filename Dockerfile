FROM java:8
MAINTAINER Your Name <you@example.com>

ADD target/auth-0.0.1-SNAPSHOT-standalone.jar /srv/auth.jar

EXPOSE 8080

CMD ["java", "-jar", "/srv/auth.jar"]
