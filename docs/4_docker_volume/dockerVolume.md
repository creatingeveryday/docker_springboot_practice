# 도커 가이드 4편: 데이터 관리하기 - volumes, bind mounts, tmpfs mounts

```text
지난 가이드에서 도커 컨테이너 각각은 격리된 파일 시스템을 가지고 있다는 것을 알아보았습니다.
컨테이너 내부에서 생성된 모든 파일은 컨테이너 내부의 writable container layer 에 저장됩니다.
그러므로 컨테이너를 삭제하면 내부에 저장된 데이터도 같이 삭제된다는 의미입니다.

컨테이너를 종료시 애플리케이션이 생성한 데이터가 삭제된다면 제대로 서비스를 운영할 수 있을까요?
애플리케이션은 로그 파일을 생성합니다.
유저는 게시글을 작성하면서 이미지 파일을 첨부합니다.
이러한 중요한 파일은 컨테이너 종료되도 관리되어야할 데이터입니다.
반드시 컨테이너 외부에 따로 저장해야될 겁니다.

docker 에서는 컨테이너가 호스트의 파일시스템에 파일을 저장하는 2가지 방법을 제공합니다.
volume 과 bind mount 입니다.
이 방법을 사용하면 컨테이너가 중지된 후에도 파일이 유지되도록 할 수 있습니다.

추가로 tmpfs mount 로 메모리에 파일을 저장하는 방법도 제공합니다.
```

![docker_types_of_mounts.webp](images/docker_types_of_mounts.webp)

## 어떤 방법을 사용해야할까?

- volume
    - 호스트 파일시스템 내부의 docker 가 관리하는 임의의 공간에 파일을 저장한다.
    - docker 외의 프로세스는 수정할 수 없다.
- bind mount
    - 명시적으로 호스트의 폴더를 지정하여 파일을 저장한다.
    - 도커 호스트와 도커 컨테이너 내부의 docker 외의 프로세스가 모두 수정할 수 있다.
- tmpfs mount
    - 호스트 시스템의 메모리에만 저장한다.

## 볼륨(Volumes) 이란?

- 뷸륨은 컨테이너 내부의 폴더와 매핑된 호스트의 폴더를 의미한다.
    - 호스트의 폴더에 파일을 추가하면 컨테이너 내부에서도 사용할 수 있다.
    - 반대로 컨테이너 내부에 파일을 추가하면 호스트에서도 사용할 수 있다.
- 도커 엔진이 전적으로 관리한다.
- 특정 볼륨을 다수의 컨테이너에서 동시에 마운트해서 사용할 수 있다.

***볼륨 생성***

- 이름으로 지정하여 볼륨 생성

```text
docker volume create <VOLUME_NAME>
```

***볼륨 목록 조회***

```text
docker volume ls
```

***볼륨 상세 정보 조회***

```text
docker volume inspect <VOLUME_NAME>
```

***볼륨 삭제***

```text
docker volume rm <VOLUME_NAME>

# 볼륨 강제 삭제
docker volume rm -f <VOLUME_NAME>

# 볼륨과 관련된 컨테이너까지 모두 삭제
docker volume rm -v <VOLUME_NAME>

# 사용하지 않는 볼륨 모두 삭제
docker volume prune
```

## 볼륨의 종류

### 1. Anonymous Volume(익명 볼륨)

- 명시적으로 이름을 지정하지 않고 생성되는 볼륨이다.
- 볼륨 이름은 임의의 문자열로 자동으로 생성된다.
- 컨테이너를 생성할 때 ***--rm*** 플래그를 같이 사용한 경우 컨테이너가 종료될때 같이 삭제된다.
- 특정 컨테이너와 밀접하게 연관된 볼륨으로 다른 컨테이너와 익명 볼륨을 공유하는 것은 권장되지 않는다.
- 컨테이너의 임시 로그 파일이나 일시적인 데이터를 저장하는데 주로 사용된다.

***익명 볼륨 생성: 컨테이너 생성시***

```text
# -v 또는 --volume 옵션을 사용

docker run -d -v /data <IMAGE_NAME>
```

***익명 볼륨 생성: 도커 파일***

- VOLUME <컨테이너 내부에서 볼륨으로 지정된 경로>
- 도커파일에서 컨테이너의 내부 경로만 설정하고 호스트 머신의 경로를 설정하지 않는다.
- 해당 도커 파일 이용시 컨테이너를 실행할 때 익명 볼륨이 자동으로 마운트된다.

```text
FROM ubuntu
VOLUME /data
```

### 2. Named Volumes

- 명시적으로 이름을 지정하여 생성한 볼륨으로 관리하기 편하다.
- 도커파일 내부에서 named 볼륨을 설정할수는 없다.
- 컨테이너를 생성할 때 ***--rm*** 플래그를 같이 사용한 경우에도 컨테이너 종료와 상관없이 볼륨은 유지된다.
- --mount 또는 -v(--volume) 플래그를 사용한다.
- 구성 파일이나 영구적인 데이터를 저장하는데 주로 사용된다.

***named volume 을 마운트해서 컨테이너를 생성***

```text
# -v [VOLUME_NAME]:[컨테이너 내부 경로] 

docker run -d \
  --name devtest \
  -v myvol2:/app \
  nginx:latest
```

```text
# --mount source=[VOLUME_NAME],target=[컨테이너 내부 경로]

docker run -d \
  --name devtest \
  --mount source=myvol2,target=/app \
  nginx:latest
```

bind mounts, tmpfs mounts 에 대해서는 추후 업데이트 예정입니다.

참고

- [Udemy: Docker & Kubernetes: The Practical Guide](https://www.udemy.com/course/docker-kubernetes-the-practical-guide/)
- [docker - storage](https://docs.docker.com/storage/)
- [docker - volumes](https://docs.docker.com/storage/volumes/)