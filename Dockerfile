FROM adoptopenjdk/openjdk11:jre-11.0.10_9-alpine

ADD /target/casestudy-*.jar /usr/local/

WORKDIR /usr/local/

EXPOSE 8080

CMD java -jar casestudy-*.jar
