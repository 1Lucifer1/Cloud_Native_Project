  
FROM openjdk:8-jre-alpine

RUN ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime
RUN echo 'Asia/Shanghai' >/etc/timezone

ENV JAVA_OPTS ''

WORKDIR /app
ADD target/cloud-native-project-0.0.1-SNAPSHOT.jar .


ENTRYPOINT ["sh", "-c", "set -e && java -XX:+PrintFlagsFinal \
                                           -XX:+HeapDumpOnOutOfMemoryError \
                                           -XX:HeapDumpPath=/heapdump/heapdump.hprof \
                                           -XX:+UnlockExperimentalVMOptions \
                                           -XX:+UseCGroupMemoryLimitForHeap \
                                           $JAVA_OPTS -jar cloud-native-project-0.0.1-SNAPSHOT.jar"]