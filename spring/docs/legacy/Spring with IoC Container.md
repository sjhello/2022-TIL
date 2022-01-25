# 서블릿 애플리케이션에서 Spring 사용하기

- 서블릿 애플리케이션에 스프링을 사용한다는 것은 2가지 의미가 존재한다
  - 작성한 서블릿에서 IoC 컨테이너를 사용하겠다
  - 스프링이 제공해주는 서블릿 구현체인 DispatcherServlet을 사용하겠다
    - Spring MVC를 사용하겠다는 말

```xml
의존성 주입
```

## ContextLoaderListener Class

- Spring IoC 컨테이너(ApplicationContext)를 서블릿 애플리케이션의 생명주기에 맞춰서 서블릿에서 사용 할 수 있게끔 도와주는 클래스
  - 애플리케이션 컨텍스트를 서블릿 컨텍스트에서 쓸수있게함
- ContextLoaderListener은 애플리케이션 컨텍스트를 만들어야 하는데 Spring 설정이 필요하다

<br>

# Servlet 필터

- 서블릿 애플리케이션에 어떠한 요청을 줄때 다음과 같은 절차를 따르게 되는데
  - 최초 요청이라면, init() -> service() -> HttpMethod에 따른 do\* 메서드 -> 응답
- 서블릿 필터는 do\* 메서드로 요청을 처리하기 전이나 요청을 처리 한 후 응답을 보내기 전에 전처리가 필요한 경우에 사용 할 수 있다
- 여러개의 서블릿에 적용 할 수도있고 특정한 URL 패턴에도 적용 가능하다
- 서블릿 필터는 동시에 처리되는 것이 아니라 어떠한 필터가 처리되면 다음 필터가 처리되는 구조이다
  - 필터 체인
  - 하나의 필터가 끝나면 다음 필터로 연결을 해줘야 한다(적용 순서는 web.xml의 filter-mapping 이 적용된 순서이다)

```xml
<!DOCTYPE web-app PUBLIC
 "-//Sun Microsystems, Inc.//DTD Web Application 2.3//EN"
 "http://java.sun.com/dtd/web-app_2_3.dtd" >

<web-app>
  <display-name>Archetype Created Web Application</display-name>

  <listener>
    <listener-class>com.sjhello.listener.MyListener</listener-class>
  </listener>

  <filter>
    <filter-name>sampleFilter</filter-name>
    <filter-class>com.sjhello.filter.SampleFilter</filter-class>
  </filter>

  <filter>
    <filter-name>myFilter</filter-name>
    <filter-class>com.sjhello.filter.MyFilter</filter-class>
  </filter>

  <filter-mapping>
    <filter-name>myFilter</filter-name>
    <servlet-name>hello</servlet-name>
  </filter-mapping>

  <filter-mapping>
    <filter-name>sampleFilter</filter-name>
    <servlet-name>hello</servlet-name>
  </filter-mapping>

  <servlet>
    <servlet-name>hello</servlet-name>
    <servlet-class>com.sjhello.servlet.HelloServlet</servlet-class>
  </servlet>

  <servlet>
    <servlet-name>sample</servlet-name>
    <servlet-class>com.sjhello.servlet.SampleServlet</servlet-class>
  </servlet>

  <servlet-mapping>
    <servlet-name>sample</servlet-name>
    <url-pattern>/sample</url-pattern>
  </servlet-mapping>

  <servlet-mapping>
    <servlet-name>hello</servlet-name>
    <url-pattern>/hello</url-pattern>
  </servlet-mapping>
</web-app>

```

myFilter 의 doFilter 처리 후 sampleFilter로 연결된다

<br>

# 리스너, 필터, 서블릿 요청 처리 순서

<br>

## 서블릿 애플리케이션 구동 시 및 서블릿 최초 요청 시

1. ServletContextListener init()
2. web.xml에 Filter가 등록되어있다면 Filter 클래스를 구현한 클래스들의 init() 메소드 호출 - Filter 등록
3. (최초 요청이라면, 최초 요청이 아니라면 생략) Servlet init()
4. web.xml의 filter 설정중 서블릿명 혹은 url 패턴이 설정되어있다면 해당 서블릿의 service()가 호출되기 전에 Filter의 doFilter() 호출
5. 다음 Filter로 전달 -> FilterChain doFilter(request, response) 다음 필터가 없으면 서블릿에게 전달
6. 서블릿의 service() 호출
7. HTTP Method에 따라 do{method명}() 메서드 호출
8. response

<br>

## 서블릿 애플리케이션 종료 시

1. 서블릿 destroy()
2. Filter destroy()
3. ServletContext destroy()
