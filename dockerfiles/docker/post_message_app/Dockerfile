FROM amazoncorretto:17
COPY ./PostMessageApp.jar /app/
RUN echo $(ls -a /app)
CMD ["java","-jar","/app/PostMessageApp.jar"]

