FROM openjdk:8-jdk-alpine
COPY . /usr/src/app
WORKDIR /usr/src/app
RUN chmod +x run.sh
#CMD ["./run.sh"]
RUN javac -cp "libs/*" src/Test.java 
ENTRYPOINT ["java"] 
CMD ["-cp", ".:libs/*", "src.Test"]