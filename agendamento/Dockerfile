FROM maven:3.9-eclipse-temurin-25 AS compilacao
WORKDIR /aplicacao
COPY pom.xml .
COPY src ./src
RUN mvn -B package

FROM eclipse-temurin:25-jre
WORKDIR /aplicacao
RUN useradd --system --uid 10001 aplicacao
COPY --from=compilacao /aplicacao/target/*.war aplicacao.war
USER aplicacao
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "aplicacao.war"]
