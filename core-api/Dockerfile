# API 서버 모듈: 도커 파일로 이미지 생성

FROM openjdk:17

# 작업 디렉토리 설정
WORKDIR /app

# 볼륨 설정
VOLUME /app/logs

EXPOSE 8097

ARG JAR_FILE=build/libs/core-api-0.0.1-SNAPSHOT.jar

COPY ${JAR_FILE} core-api.jar

# 스프링 프로필을 설정하기 위한 환경 변수
ENV SPRING_PROFILES_ACTIVE="production"

ENTRYPOINT ["java","-jar","core-api.jar"]