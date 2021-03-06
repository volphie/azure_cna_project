# Template
* Local PC 환경에서 Docker를 이용해 Application Container와 DB Container를 띄워 RESTful HTTP 요청을 처리하는 예제 프로젝트
* Application : Spring Boot, Spring JPA
* DB : Mysql

## 사전 조건
* Windows 10
* Docker Desktop 설치
* JDK 설치
* Maven 설치
* Docker Client 설치

## Getting Started
### 0. Git 다운로드
```
$ git clone https://github.com/volphie/azure_cna_project.git
```

### 1. 작업 디렉토리로 이동
```
$ cd azure_cna_project/template
```

### 2. Packaging
```
$ mvn package
```

### 3. DB 생성 및 실행(서버 character set 설정)
```
$ docker run -d -p 3306:3306 -e MYSQL_ROOT_PASSWORD=비밀번호 --name docker-mysql-utf8 mysql:5.7 --character-set-server=utf8 --collation-server=utf8_unicode_ci
```
* 비밀번호 위치에 mysql root 계정의 비밀번호로 사용할 값을 입력

### 4. Database 및 계정 생성
```
$ docker exec -i -t docker-mysql-utf8 bash
# mysql -u root -p
mysql> create database db_example; -- Create the new database
mysql> create user 'springuser'@'%' identified by 'ThePassword'; -- Creates the user
mysql> grant all on db_example.* to 'springuser'@'%'; -- Gives all the privileges to the new user on the newly created database
```


### 5. Application 설정 확인(application.properties 파일 수정)
* 파일 위치 : src/main/resources/
```
spring.jpa.hibernate.ddl-auto=create
spring.datasource.url=jdbc:mysql://docker-mysql-utf8:3306/db_example
spring.datasource.username=springuser
spring.datasource.password=ThePassword
```

### 6. Dockerfile 내용 확인
```
FROM openjdk:8-jdk-alpine
ADD target/sbtemplate-0.0.1-SNAPSHOT.jar app.jar
ENV JAVA_OPTS=""
ENTRYPOINT ["java","-jar","/app.jar"]
```

### 7. Application Container Image 생성
```
$ docker build --tag sbtemplate:0.0.1 .
```

### 8. Run spring boot as a docker container
```
$ docker run -d -p 9090:9090 --link docker-mysql-utf8 --name sbtemplate sbtemplate:0.0.1
```
* sbtemplate container 내부에서 mysql 서버 접속되도록 설정 필요
* --link 옵션으로 docker-mysql-utf8 container에 대한 접근 획득
* docker-mysql-utf8 container의 IP와 hostname이 sbtemplate container 내부의 hosts 파일에 자동 등록
* 특별히 설정하지 않은 상황이라면 container 내부에서 localhost는 컨테이너 자체를 가리킴. 즉, 컨테이너 바깥의 프로세스나 데몬에 접근하려면 직접 docker가 설치되어 있는 host의 IP를 명시적으로 지정해야 함

### 9. curl 실행하여 데이터 insert(Quatation 처리 주의)
```
$ curl -X POST localhost:9090/users -H "Content-type:application/json" -d "{\"id\": 1, \"name\": \"Samwise Gamgee\", \"email\": \"gamgee@gmail.com\"}"
```

### 10. insert 데이터 확인
```
$ curl -X GET localhost:9090/users
```


