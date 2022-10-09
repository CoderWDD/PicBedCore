FROM openjdk:17
MAINTAINER CoderWdd

COPY /target/*.jar /PicBedCore.jar

EXPOSE 7788

ENTRYPOINT ["java","-jar","/PicBedCore.jar"]