
FROM openjdk:17-alpine


WORKDIR /spring_ecommerce


COPY . .


RUN apk add --no-cache git

RUN addgroup --system compasser && adduser --system --ingroup compasser dev


USER dev


EXPOSE 8080


CMD ["java", "-jar", "target/spring_ecommerce-0.0.1-SNAPSHOT.jar"]
