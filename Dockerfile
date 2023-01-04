from openjdk:11
VOLUME /tmp
COPY target/*.jar app.jar
RUN wget -O dd-java-agent.jar 'https://dtdg.co/latest-java-tracer'
ENTRYPOINT ["java","-javaagent:dd-java-agent.jar","-Ddd.logs.injection=true","-Ddd.service=parking-toll","-Ddd.env=prod","-jar","/app.jar"]