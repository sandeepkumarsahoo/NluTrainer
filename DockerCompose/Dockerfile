FROM openjdk:8u171-jre-alpine3.8
EXPOSE 8084
COPY /tmp/test.json /app/test.json
COPY /home/sandeep/Desktop/CCHAT_SCHEDULE/target/CCHAT_SCHEDULER-0.0.1-SNAPSHOT.jar /usr/src/myapp/app.jar
WORKDIR /usr/src/myapp
CMD ["java", "-jar", "app.jar"]
