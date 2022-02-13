# 스프링 부트에서 JSP 사용하기

## spring-boot 문서에 따르면 JSP 사용을 권장하지 않는다

- 부트 프로젝트에서 JSP를 사용하면 몇가지 제약사항이 들기 때문이다
  - JSP를 사용하면 반드시 WAR 프로젝트로 만들어야 한다
    - WAR로 프로젝트를 배포할시 webapp라는 디렉토리를 알고있어야한다
  - WAR 프로젝트여도 실행은 가능하다(java -jar 명령어를 사용) 하지만 WAR파일을 독립적인 JAR 파일처럼 실행할수는 없다(jar 파일만을 이용하여 실행 java 명령어 사용X)
  - 언더토우(톰캣과 같은 서블릿 컨테이너)에서는 JSP를 지원하지 않는다
    - 서블릿 컨테이너를 톰캣만 사용할거라면 이 제약사항은 생각하지 않아도 될거같다
  - whitelabel 에러페이지를 error.jsp로 오버라이딩 할수없다
  - 참고: [제약 사항이 나와있는 공식 문서](https://docs.spring.io/spring-boot/docs/current/reference/html/web.html#web.servlet.embedded-container.jsp-limitations)

## java -jar 명령어를 사용하여 패키징된 war파일을 실행해보기

인텔리제이에서 명령창을 열어 다음 명령어를 실행한다

```shell
./mvnw package
```

Spring Initializer를 이용하여 maven spring-boot 프로젝트를 만들면

![image](https://user-images.githubusercontent.com/23889744/153747944-c51bbecb-dc4f-45d0-adc6-478fd18e1533.png)

mvnw 파일이 자동으로 생성된다 mvnw 파일은 컴퓨터에 maven을 설치하지 않아도 maven 명령어를 실행할수 있게끔 해주는 실행파일이다
<br>
./mvnw package 명령을 실행하면 프로젝트 pom.xml의 내용을 바탕으로 war 파일이 생성이 되는데 이 파일을

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.6.3</version>
        <relativePath/> <!-- lookup parent from repository -->
    </parent>
    <groupId>com.example</groupId>
    <artifactId>boot-notuse-jsp</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <packaging>war</packaging>
    <name>boot-notuse-jsp</name>
    <description>boot-notuse-jsp</description>
    <properties>
        <java.version>11</java.version>
    </properties>
		... 디펜던시 부분 생략
</project>
```

```shell
java -jar boot-notuse-jsp-0.0.1-SNAPSHOT.war
```

명령어를 실행하면 war 파일을 실행 할 수 있다

<br>

## WAR 배포해보기

```java
@SpringBootApplication
public class BootNotuseJspApplication {

	public static void main(String[] args) {
		SpringApplication.run(BootNotuseJspApplication.class, args);
	}

}
```

인텔리제이에서 Spring Initializer로 프로젝트를 만들경우 이와 같은 클래스가 생기는데 이 클래스를 이용하여 프로젝트를 실행시킨다. 예제로 만든 프로젝트는 Packaging을 war로 설정하였다

```java
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

public class ServletInitializer extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(BootNotuseJspApplication.class);
	}

}
```

이렇게 war로 패키징을 하는 프로젝트 즉 임베디드 톰캣을 사용하지 않고 외장 톰캣을 사용하는 프로젝트 SpringApplicationBuilder 클래스를 이용하여 어플리케이션을 실행 하게된다

## java 명령어를 사용해서 어플리케이션을 실행하는 것과 외장 톰캣을 사용하여 어플리케이션을 실행시키는 것의 차이점

![image](https://user-images.githubusercontent.com/23889744/153747746-f89f5c9e-95cc-4b2f-9f88-9d86e6b108e1.png)
<br>
_<외장 톰캣을 사용하는 경우>_

톰캣(서블릿 컨테이너)에 Spring IoC 컨테이너가 등록됨

<br>

![image](https://user-images.githubusercontent.com/23889744/153748129-d64f811d-d9ca-4c7b-ae32-8d8f283cf6f3.png)
<br>
_<java 명령어를 사용하는 경우>_

Spring Application 안에다가 서블릿이 등록됨
