FROM anapsix/alpine-java

VOLUME /tmp

ADD build/libs/assets.jar /app.jar
ADD <PATH_TO_WALLET_KEYSTORE> /tmp/<WALLET_KEYSTORE>
EXPOSE 7001

ENTRYPOINT ["java","-Xmx512m", "-jar","/app.jar"]