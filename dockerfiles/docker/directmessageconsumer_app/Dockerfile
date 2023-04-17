FROM amazoncorretto:17
COPY ./DirectMessageConsumer-0.0.1-SNAPSHOT.jar /app/
RUN echo $(ls -a /app)
CMD ["java","-jar","/app/DirectMessageConsumer-0.0.1-SNAPSHOT.jar"]

