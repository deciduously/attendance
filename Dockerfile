FROM java:8-alpine
MAINTAINER Ben Lovy <ben@deciduously.com>

ADD target/attendance-v0.2.0.jar /attendance/app.jar

EXPOSE 3000

CMD ["java", "-jar", "/attendance/app.jar"]