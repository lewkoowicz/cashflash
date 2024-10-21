FROM ubuntu:latest

COPY target/cashflash-api /app/cashflash-api

EXPOSE 8080

ENTRYPOINT ["/app/cashflash-api", "--spring.profiles.active=qa"]
