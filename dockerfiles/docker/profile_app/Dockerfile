FROM amazoncorretto:17
RUN echo $(ls -a /)
COPY ./ProfileApp.jar /app/
RUN echo $(ls -a /app)
CMD ["java","-jar","/app/ProfileApp.jar"]

