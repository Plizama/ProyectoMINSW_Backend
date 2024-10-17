FROM openjdk:17
ARG JAR_File=target/Proyecto_MISW.jar
COPY ${JAR_File} Proyecto_MISW.jar
ENTRYPOINT ["java","-jar","/Proyecto_MISW.jar"]