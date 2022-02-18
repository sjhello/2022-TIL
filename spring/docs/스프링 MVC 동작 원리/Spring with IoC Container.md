# 서블릿 애플리케이션에서 Spring 사용하기

- 서블릿 애플리케이션에 스프링을 사용한다는 것은 2가지 의미가 존재한다
  - **작성한 서블릿에서 IoC 컨테이너를 사용하겠다**
  - **스프링이 제공해주는 서블릿 구현체인 DispatcherServlet을 사용하겠다**
    - Spring MVC를 사용하겠다는 말

```xml
// pom.xml에 spring-webmvc 의존성 추가
<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-webmvc</artifactId>
    <version>5.1.3.RELEASE</version>
</dependency>
```

![Untitled](https://user-images.githubusercontent.com/23889744/150978708-bb588e7f-8145-4e89-a0e6-8500bc61da0f.png)

_<spring-webmvc 의존성 주입 확인>_

<br>

## ContextLoaderListener Class

- Spring IoC 컨테이너(ApplicationContext)를 서블릿 애플리케이션의 생명주기에 맞춰서 서블릿에서 사용 할 수 있게끔 도와주는 클래스
  - 애플리케이션 컨텍스트를 서블릿 컨텍스트에서 쓸수있게함
  - ContextLoaderListener 는 ContextLoader 를 상속하고 있는데 서블릿 애플리케이션이 구동되면 ContextLoaderListener 의 contextInitialized()에서 ApplicationContext(WebApplicationContext)를 ServletContext의 애트리뷰트로 등록하는 것을 확인 할 수 있다

```java
// ContextLoader의 initWebApplicationContext()의 일부 내용
public WebApplicationContext initWebApplicationContext(ServletContext servletContext) {
if (servletContext.getAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE) != null) {
			throw new IllegalStateException(
					"Cannot initialize context because there is already a root application context present - " +
					"check whether you have multiple ContextLoader* definitions in your web.xml!");
		}

		servletContext.log("Initializing Spring root WebApplicationContext");
		Log logger = LogFactory.getLog(ContextLoader.class);
		if (logger.isInfoEnabled()) {
			logger.info("Root WebApplicationContext: initialization started");
		}
		long startTime = System.currentTimeMillis();

		try {
			// Store context in local instance variable, to guarantee that
			// it is available on ServletContext shutdown.
			if (this.context == null) {
				this.context = createWebApplicationContext(servletContext);
			}
			if (this.context instanceof ConfigurableWebApplicationContext) {
				ConfigurableWebApplicationContext cwac = (ConfigurableWebApplicationContext) this.context;
				if (!cwac.isActive()) {
					// The context has not yet been refreshed -> provide services such as
					// setting the parent context, setting the application context id, etc
					if (cwac.getParent() == null) {
						// The context instance was injected without an explicit parent ->
						// determine parent for root web application context, if any.
						ApplicationContext parent = loadParentContext(servletContext);
						cwac.setParent(parent);
					}
					configureAndRefreshWebApplicationContext(cwac, servletContext);
				}
			}
			servletContext.setAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE, this.context);
......
}
```

- 서블릿 애플리케이션(톰캣)을 구동하면 WebApplicationContext 가 등록되었다는 로그도 확인 할 수 있다

```
...
25-Jan-2022 21:38:22.945 INFO [RMI TCP Connection(5)-127.0.0.1] org.springframework.web.context.ContextLoader.initWebApplicationContext Root WebApplicationContext initialized in 415 ms
....
```

- ContextLoaderListener은 애플리케이션 컨텍스트를 만들어야 하는데 Spring 설정이 필요하다
  - 기본적으로 Spring 설정은 applicationContext.xml을 사용하는데 이는 XmlWebApplicationContext에서 확인 할 수 있다
    ![Untitled](https://user-images.githubusercontent.com/23889744/150982103-220c3c2c-95b5-4b25-855b-f65f18b30d65.png)

<br>

---

<br>

# Servlet으로 애플리케이션 만들때의 단점

- 요청당 서블릿을 작성해야 한다
  - URL 하나당 서블릿을 작성해야한다(특정 URL 하위 패턴으로 작성이 가능하지만 어쨌든 URL을 서블릿에다가 맵핑 해줘야함)
  - 많은 서블릿이 작성 될 수록 web.xml은 지저분해지고 길어진다
  - 새로운 요청이 생길수록 servlet + 요청 url이 많아져서 서버입장에서 공통 로직을 처리하기가 부담스럽다
- 특정 URL 패턴이나 특정한 패턴의 서블릿에서 공통적으로 처리하고 싶은 로직이 있을건데 Filter로 처리가 가능하겠지만 마찬가지로 web.xml의 설정이 복잡해진다

<br>

## Front Controller 패턴

- 참고: [frontcontroller 패턴](https://www.oracle.com/java/technologies/front-controller.html)
- 어떠한 url 요청을 서블릿으로 보내기 전에 frontcontroller를 앞에 두어 들어온 요청을 처리할 다른 컨트롤러(handler)에 위임하는 하는 것을 말한다

<br>

## Dispatcher Servlet

![Untitled](https://user-images.githubusercontent.com/23889744/150990478-f2f4541d-caa4-47af-831d-7e7ed2d2c7c2.png)

- Spring MVC의 DispatcherServlet은 대표적으로 Front Controller를 구현한 Serlvet 이다
- DispatcherServlet은 서블릿 애플리케이션 컨텍스트에 만약 ApplicationContext(WebApplicationContext)가 존재한다면 이를 부모로 하여 또 하나의 ApplicationContext를 만든다
  - 디스패처 서블릿이 상속구조로 ApplicationContext를 만드는 이유는 다른 서블릿들이 ContextLoaderListener에 의해 만들어진 WebApplicationContext를 공유 할 수 있기 때문이다
  - 즉 또 다른 디스패처 서블릿에서 WebApplicationContext에 접근이 가능하다
  - 하지만 디스패처 서블릿이 만든 ApplicationContext는 또 다른 디스패처 서블릿에서는 접근 할 수 없다
  - 그래서 주로 ContextLoaderListener에 의해 만들어진 WebApplicationContext 에서는 모든 서블릿들이 공통적으로 사용하는 bean들이 들어가게 되고(웹과 관련된 빈들은 등록 되지 않는다 ex> @Controller, ViewResolver, HandlerMapping)
  - 디스패처 서블릿에서 만든 ApplicationContext에는 주로 웹과 관련된 빈들만 등록이 된다(@Service, @Repository 관련 빈은 등록 안됨)
  - 하나의 디스패처 서블릿 안에 모든 빈들을 등록 할 수도 있다

```xml
// 디스패처 서블릿이 WebApplicationContext를 상속하는 구조
// AppConfig 에는 웹 관련 Bean들은 존재하지 않는다
// @ComponentScan(excludeFilters = @ComponentScan.Filter(Controller.class))

<!DOCTYPE web-app PUBLIC
 "-//Sun Microsystems, Inc.//DTD Web Application 2.3//EN"
 "http://java.sun.com/dtd/web-app_2_3.dtd" >

<web-app>
  <display-name>Archetype Created Web Application</display-name>

// 디스패처 모든 빈을 관리한다면 ContextLoaderListener은 필요없어진다
  <context-param>
    <param-name>contextClass</param-name>
    <param-value>org.springframework.web.context.support.AnnotationConfigWebApplicationContext</param-value>
  </context-param>

  <context-param>
    <param-name>contextConfigLocation</param-name>
    <param-value>com.sjhello.AppConfig</param-value>
  </context-param>

  <listener>
    <listener-class>org.springframework.web.context.ContextLoaderListener</listener-class>
  </listener>
// 여기까지


// 디스패처 서블릿에서 만든 ApplicationContext에 모든 빈을 관리한다
// WebConfig 에서 모든 빈을 ComponentScan 해야한다
// @ComponentScan
  <servlet>
    <servlet-name>app</servlet-name>
    <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
    <init-param>
      <param-name>contextClass</param-name>
      <param-value>org.springframework.web.context.support.AnnotationConfigWebApplicationContext</param-value>
    </init-param>
    <init-param>
      <param-name>contextConfigLocation</param-name>
      <param-value>com.sjhello.WebConfig</param-value>
    </init-param>
  </servlet>
  <servlet-mapping>
    <servlet-name>app</servlet-name>
    <url-pattern>/app/*</url-pattern>
  </servlet-mapping>
</web-app>

```
