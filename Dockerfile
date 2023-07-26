FROM adoptopenjdk/openjdk11:debian

USER root
ARG JAR_FILE=build/libs/anytimedelivery-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar
COPY script/wait.sh wait.sh
ENTRYPOINT ["./wait.sh", "mysql-slave:3306", "--", \
"java", \
"-jar", \
"-Dspring.profiles.active=prod", \
"-Dspring.timezone=Asia/Seoul", \
"app.jar"]