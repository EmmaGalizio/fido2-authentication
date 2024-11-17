FROM amazoncorretto:21-alpine3.19-jdk as builder
LABEL authors="emmagalzio"

WORKDIR /code

RUN apk update && apk add tar gzip curl

# Directorio de instalación de Maven
ENV MAVEN_HOME /usr/local/apache-maven-3.9.5

RUN adduser --disabled-password --gecos '' user_no_root

# Descarga y descomprime Apache Maven
RUN mkdir -p $MAVEN_HOME \
    && curl -L https://dlcdn.apache.org/maven/maven-3/3.9.5/binaries/apache-maven-3.9.5-bin.tar.gz -o /tmp/apache-maven.tar.gz \
    && tar xzvf /tmp/apache-maven.tar.gz -C /usr/local/ \
    && rm /tmp/apache-maven.tar.gz

# Agrega Maven al PATH
ENV PATH $MAVEN_HOME/bin:$PATH
RUN mvn --version

COPY ./fido2-server/ .

RUN mvn clean package -DskipTests


FROM amazoncorretto:21-alpine3.19

WORKDIR /app
COPY --from=builder /code/target/fido2-server-0.0.1-SNAPSHOT.jar .

RUN adduser --disabled-password --gecos '' user_no_root

#USER user_no_root

CMD ["java", "-jar", "-Dspring.profiles.active=docker", "fido2-server-0.0.1-SNAPSHOT.jar"]
