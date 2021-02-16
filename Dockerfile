FROM openjdk:11
ADD target/SuperHeroes.jar SuperHeroes.jar
EXPOSE 8181
ENTRYPOINT ["java", "-jar", "SuperHeroes.jar"]