FROM openjdk:17-alpine
WORKDIR /app

COPY build/libs/MAS-0.1.jar build/

WORKDIR /app/build
ENTRYPOINT java -jar MAS-0.1.jar