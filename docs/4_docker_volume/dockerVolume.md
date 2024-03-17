# 도커 가이드 4편: 데이터 관리하기 - volumes, bind mounts, tmpfs mounts

```text
지난 가이드에서 도커 컨테이너 각각은 격리된 파일 시스템을 가지고 있다는 것을 알아보았습니다.
컨테이너 내부에서 생성된 모든 파일은 컨테이너 내부의 writable container layer 에 저장됩니다.
그러므로 컨테이너를 삭제하면 내부에 저장된 데이터도 같이 삭제됩니다.

컨테이너 종료시 애플리케이션이 생성한 데이터가 삭제된다면 제대로 서비스를 운영할 수 있을까요?
애플리케이션은 로그 파일을 생성합니다.
유저는 게시글을 작성하면서 이미지 파일을 첨부합니다.
이러한 중요한 파일은 컨테이너가 종료되어도 유지되어야할 데이터입니다.
반드시 컨테이너 외부에 따로 저장하고 관리해야합니다.

docker 에서는 컨테이너가 호스트의 파일시스템에 파일을 저장하는 2가지 방법을 제공합니다.
volume 과 bind mount 입니다.
이 방법을 사용하면 컨테이너가 중지된 후에도 파일이 유지되도록 할 수 있습니다.

추가로 tmpfs mount 로 메모리에 파일을 임시로 저장하는 방법도 제공합니다.
```

![docker_types_of_mounts.webp](images/docker_types_of_mounts.webp)

## 어떤 방법을 사용해야할까?

- volume
    - 도커 엔진에게 관리를 맡긴다.
    - 폴더의 위치를 알 수 없고, 알 필요도 없을때 사용하면 좋다.
    - 호스트 파일시스템 내부의 docker 가 관리하는 임의의 공간에 파일을 저장한다.
    - docker 외의 프로세스는 수정할 수 없다.
    - 데이터를 저장해야하지만 편집할 필요할 필요가 없는 경우에 사용하면 좋다.
- bind mount
    - 개발자가 직접 관리한다.
    - 폴더의 위치를 알 수 있다.
    - 명시적으로 호스트의 폴더를 지정하여 파일을 저장한다.
    - 도커 호스트와 도커 컨테이너 내부의 docker 외의 프로세스가 모두 수정할 수 있다.
    - 데이터를 저장해야하고 동시에 편집도 가능해야하는 경우에 사용하면 좋다.
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
- 다른 볼륨과 함께 사용될 때 컨테이너에 이미 존재하는 종속성 폴더 같은 데이터를 유지하는데 활용된다.(덮어쓰기 방지)

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
- 컨테이너를 삭제해도 명시적으로 볼륨을 삭제하지 않는 한 유지된다.
- --mount 또는 -v(--volume) 플래그를 사용한다.
- 여러 컨테이너간 데이터를 공유하는데 사용할 수 있다.
- 구성 파일이나 영구적인 데이터를 저장하는데 주로 사용된다.

***named volume 과 함께 컨테이너를 생성***

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

---

## 바인드 마운트(Bind Mounts) 란?

- 바인드 마운트란 호스트 머신의 파일이나 디렉토리를 도커 컨테이너 내부의 경로에 직접 마운트하는 방법이다.
- 바인드 마운트는 호스트 머신의 특정 경로를 컨테이너 내부의 특정 경로에 직접 연결한다.
    - 그렇기 때문에 컨테이너 내에서 호스트 머신의 파일이나 디렉터리를 참조할 수 있다.
- 호스트와 컨테이너 간에 바인드 마운트된 파일은 실시간으로 동기화된다.
    - 호스트에서 파일을 수정하면 컨테이너에서도 해당 수정 사항이 즉시 반영된다.
- 컨테이너를 생성할 때만 적용할 수 있다.(도커파일에서는 설정 불가능)
- 주의 사항
    - 바인드 마운트로 공유 중인 폴더에 도커가 엑세스할 수 있는지 확인해야한다.

***호스트의 /host/path 경로가 컨테이너 내의 /container/path 경로에 바인드 마운트한 상태로 컨테이너 실행***

```text
docker run -d -v /host/path:/container/path my_image
```

## 볼륨 결합

- 여러 볼륨을 하나의 마운트 지점에 동시에 마운트하는 경우에 병합, 충돌이 발생한다.
- 도커 컨테이너에서 여러 볼륨을 동시에 사용하여 데이터를 공유하고 관리하는 용도로 사용할 수 있기 때문이다.
- 병합 규칙
    - 병합 발생시 더 긴 경로를 우선한다. 그 다음으로는 마지막으로 마운트된 볼륨이 우선한다.

***volume1과 volume2가 모두 /path/to/mount에 마운트된다. 이때 volume2가 나중에 마운트 됬기 때문에 volume1을 덮어쓰게 된다.***

```text
docker run -v volume1:/path/to/mount -v volume2:/path/to/mount my_image
```

***병합 발생시 더 긴 경로를 우선한다. /path/to/mount/long 경로는 볼륨1 이 적용된다.***

```text
docker run -v volume1:/path/to/mount/long -v volume2:/path/to/mount my_image
```

# 읽기 전용 볼륨
- 볼륨 또는 바인드 마운트 사용시 컨테이너와 연결된 폴더를 읽기만 할 경우 명시적으로 읽기 전용 옵션을 사용해주는게 좋다.

***읽기전용 옵션(ro) 적용***

```text
docker run -v my_named_volume:/path/to/mount:ro my_image

docker run -d -v /host/path:/container/path:ro my_image
```

## 사용 형태 간략 정리

```text
# Anonymous Volumes
docker run -v /app

# Named Volumes
docker run -v app:/app

# Bind Mounts
docker run -v /path/app:/app
```

# tmpfs mounts
- 볼륨과 바인드 마운트 방법과 달리 tmpfs mount 는 일시적으로 컨테이너 유지시에만 호스트 메모리에만 유지된다.
- 컨테이너가 제거되면 호스트 메모리에 기록된 파일을 유지되지 않는다.
- 중요한 파일을 임시 저장하는 용도로만 사용한다.
- tmpfs 마운트는 컨테이너간 공유가 불가능하다.

```text
docker run -d \
  -it \
  --name tmptest \
  --mount type=tmpfs,destination=/app \
  nginx:latest
  
docker run -d \
  -it \
  --name tmptest \
  --tmpfs /app \
  nginx:latest
```

참고

- [Udemy: Docker & Kubernetes: The Practical Guide](https://www.udemy.com/course/docker-kubernetes-the-practical-guide/)
- [docker - storage](https://docs.docker.com/storage/)
- [docker - volumes](https://docs.docker.com/storage/volumes/)