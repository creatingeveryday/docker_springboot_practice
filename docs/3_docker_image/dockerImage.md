# 도커 가이드 3편: 도커 이미지

```text
이번 시간에는 도커 이미지와 이미지를 공유하는 방법에 대해서 알아보겠습니다.
```

![docker_image_layer.png](images/docker_image_layer.png)
(이미지 출처: https://docs.docker.com/build/guide/layers/)

## 도커 이미지란?
- 이미지는 읽기 전용이다.
- 동일한 이미지를 기반으로 격리된 컨테이너를 생성할 수 있다.
- Dockerfile 에 작성한 각각의 명령은 Docker Image 의 레이어를 의미한다.
  - 이미지 빌드시 이미지 파일시스템에 변화를 주는 명령어는 새로운 이미지 레이어를 생성한다.
    - 생성된 이미지 레이어는 캐시된다.
  - 이미지 빌드시 이미지 파일시스템에 변화를 주지 않는 명령어는 새로운 이미지 레이어를 생성하지 않고 캐시된 레이어를 재사용한다.
  - 이미지 레이어가 변경된 경우 그 이후 레이어의 캐시는 무효화되고 모두 다시 빌드된다.

## 이미지 관리

```text
1편에서 컨테이너를 관리하는 방법을 확인했습니다.
이번 편에서는 이미지를 관리하는 방법과 공유하는 방법을 확인해보겠습니다.
```

### 이미지 태그
- name:tag 형식으로 이미지를 고유하게 식별할 수 있다.
- name : 이미지 그룹 이름
  - 소문자만 가능하다.
- tag : 이미지 그룹내에서 구분하기 위한 값. 
  - tag 값은 옵션으로 추가해줄 수 있다.
  - 버전 등으로 설정: 예) node:14

```text
docker build -t nodeserver .
docker build -t api:1.0 .
docker build -t api:1.0 .
```

기존 이미지 이름 및 태그 변경
- 기존 이미지를 복사한 새로운 이미지가 생성된다.

```text
docker tag <old_name:tag> <new_name:tag>
```

### 이미지 목록 조회

```text
docker images
```

### 이미지 검사 분석
- 이미지를 검사해서 이미지 ID, 생성된 날짜 등 다양한 정보를 조회할 수 있다. 

```text
docker image inspect <imageId>
```

### 이미지 제거
- 컨테이너에서 사용중인 상태가 아닌 이미지만 삭제할 수 있다.
- 이미지를 사용하는 컨테이너를 모두 제거한 경우에만 이미지를 삭제할 수 있다.
- 다수의 imageId 를 공백으로 구분하여 삭제할 수도 있다.

```text
docker rmi <imageId>
```

***사용중이지 않은 이미지를 모두 삭제한다.(태그가 없는 이미지만 삭제)***
```text
docker image prune
```

***사용중이지 않은 태그된 이미지를 모두 삭제한다.***
```text
docker image prune -a
```

### 이미지 공유하기

이미지를 공유하는 방법은 크게 2가지가 있다.
1. Dockerfile 과 소스코드를 직접 공유한다. 도커파일을 통해 자체 이미지를 직접 생성해서 컨테이너를 실행한다.
2. 일반적으로는 빌드된 도커이미지 자체를 공유한다.
- Docker Hub 나 Private Registry 저장소를 활용한다.
- docker push <imageName> : 업로드
- docker pull <imageName> : 다운로드


### 참고
[도커 공식문서 - Layers](https://docs.docker.com/build/guide/layers/)