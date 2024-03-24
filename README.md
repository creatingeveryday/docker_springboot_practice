# 도커 가이드

도커 학습 정리 및 실습

## 포스팅 환경
- Java 17
- SpringBoot 3.1.7
- Gradle - Groovy 8.5
- Spring Web
- H2
- Spring Data Jpa
- Docker 25.0.2

## 도커 가이드 목차
### [1. 도커 컨테이너](docs/1_docker_container/dockerContainer.md)
### [2. 도커파일로 이미지 생성](docs/2_docker_file/Dockerfile.md)
### [3. 도커 아미지](docs/3_docker_image/dockerImage.md)
### [4. 도커 데이터 관리: volumes, bind mounts, tmpfs mounts](docs/4_docker_volume/dockerVolume.md)
### [5. 도커 컨테이너 네트워킹](docs/5_docker_network/dockerNetwork.md)

### docker 설치
- 실습을 위해 윈도우 환경이라면 docker desktop 설치가 필요하다
- 윈도우 환경에서는 PowerShell 을 사용해서 진행한다.
- [설치 참고 링크](https://docs.docker.com/get-docker/)

```shell
# 버전 확인
docker version

# 도커의 모든 컨테이너의 목록을 표시(실행중이지 않은 컨테이너도 포함)
docker ps -a
# 결과 출력
# CONTAINER ID   IMAGE     COMMAND   CREATED   STATUS    PORTS     NAMES
# 컨테이너의 ID, 이미지 이름, 컨테이너 이름, 생성 시간, 상태 등을 표시한다.
# 현재 실행한 컨테이너가 없기 때문에 아무런 내용이 표시되지 않는다.

# 현재 가지고 있는 이미지 리스트 표시
docker image ls

# 컨테이너 백그라운드 실행시 로그 확인
# 컨테이너에서 출력된 표준출력과 표준에러 스트림의 모든 내용을 보여줌
docker logs [컨테이너 ID]

# 컨테이너 로그 실시간 확인: 10줄 단위로 출력
docker logs -n 10 -f [컨테이너 ID]
```