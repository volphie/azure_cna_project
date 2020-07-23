
-- Mysql Docker Image 다운로드
docker pull mysql:5.7

-- Docker에서 Mysql 실행
docker run -d -p 3306:3306 -e MYSQL_ROOT_PASSWORD=*** --name docker-mysql mysql:5.7

-- Mysql Option과 함께 실행(서버 character set 설정)
docker run -d -p 3306:3306 -e MYSQL_ROOT_PASSWORD=#12jojeong04 --name docker-mysql-utf8 mysql:5.7 --character-set-server=utf8 --collation-server=utf8_unicode_ci

docker run -d -p 3306:3306 --name docker-mysql-utf8 mysql:5.7 --character-set-server=utf8 --collation-server=utf8_unicode_ci

-- Docker에서 Mysql Container의 Shell 접속
docker exec -i -t docker-mysql bash

-- Database 및 계정 생성
mysql> create database db_example; -- Create the new database
mysql> create user 'springuser'@'%' identified by 'ThePassword'; -- Creates the user
mysql> grant all on db_example.* to 'springuser'@'%'; -- Gives all the privileges to the new user on the newly created database

-- application.properties 파일 수정
spring.jpa.hibernate.ddl-auto=create
spring.datasource.url=jdbc:mysql://docker-mysql-utf8:3306/db_example
spring.datasource.username=springuser
spring.datasource.password=ThePassword

-- pom.xml 수정
		<!-- Use MySQL Connector-J -->
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
        </dependency>

-- 윈도우에서 curl 실행하여 데이터 insert(Quatation 처리 주의)
curl -X POST localhost:9090/users -H "Content-type:application/json" -d "{\"id\": 1, \"name\": \"Samwise Gamgee\", \"email\": \"gamgee@gmail.com\"}"
curl -X GET localhost:9090/users 

-- hellospring 배포 및 실행용 Docker Image 생성을 위한 Dockerfile 작성
FROM openjdk:8-jdk-alpine
ADD target/sbtemplate-0.0.1-SNAPSHOT.jar app.jar
ENV JAVA_OPTS=""
ENTRYPOINT ["java","-jar","/app.jar"]

-- Docker Image Build
docker build --tag sbtemplate:0.0.1 .

-- Run spring boot as a docker container
docker run -d -p 9090:9090 --link docker-mysql-utf8 sbtemplate:0.0.1
==> Container 내부의 Application에서 외부의 mysql 서버 접속되지 않음
--link 옵션으로 docker-mysql container에 대한 접근 획득
docker-mysql 컨테이너의 IP와 hostname이 sbtemplate 컨테이너 내부의 hosts 파일에 자동으로 등록됨
특별히 설정하지 않은 상황이라면 컨테이너 내부에서 localhost는 컨테이너 자체를 가리킴. 즉, 컨테이너 바깥의 프로세스나 데몬에 접근하려면 직접 docker가 설치되어 있는 host의 IP를 명시적으로 지정해야 함

