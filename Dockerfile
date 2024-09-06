FROM openjdk:17-alpine

WORKDIR /spring_ecommerce
COPY . .

RUN apk add --no-cache git