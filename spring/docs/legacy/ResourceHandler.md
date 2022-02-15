# 리소스 핸들러

이미지, 자바스크립트, html파일 등 정적 리소스를 처리하는 핸들러

## 서블릿 컨테이너의 default servlet

- 톰캣과 같은 서블릿 컨테이너는 기본적으로 제공하는 서블릿에 정적 리소스를 처리하는 리소스 핸들러가 포함되어 있다
  - 참고: [Interface HandlerInterceptor](https://tomcat.apache.org/tomcat-9.0-doc/default-servlet.html)
  - 디폴트 서블릿의 선언위치($CATALINA_BASE/conf/web.xml), 디폴트 서블릿에서 바꿀수있는 값들이 나와있다

## 스프링 MVC에서의 리소스 핸들러 맵핑의 등록

- 모든 요청에서 리소스 핸들러 맵핑을 먼저 거쳐서 핸들러를 처리한다면 비효율이 발생함
- 때문에 / 이하 요청은 다른 리소스 핸들러 맵핑이 먼저 처리하도록 하고 마지막에 리소스 핸들러 맵핑이 처리하도록 한다
  - 다시 말해서 / 이하 모든 요청은 리소스 핸들러 맵핑을 제외한 RequestMappingHandlerMapping 라던가.. 다른 핸들러 맵핑이 먼저 처리 하고 마지막에 리소스 핸들러 맵핑이 처리한다
- DefaultServletHandlerConfigurer에 의해서 서블릿 컨테이너의 DefaultServlet에 요청을 전달해서 정적 리소스를 처리하기 위한 요청 핸들러를 구성된다
- 참고: [DefaultServletHandlerConfigurer](https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/web/servlet/config/annotation/DefaultServletHandlerConfigurer.html)

## 리소스 핸들러 설정

![image](https://user-images.githubusercontent.com/23889744/154095329-77a24703-a5ae-4c0b-9337-3b8cd4f0699d.png)

- MVC 설정에서 설정할수있다(@Configuration + Implements WebMvcConfigurer)
  - 어떤 요청 패턴에 리소스 핸들러를 적용할지?
  - 어떤 리소스에 리소스 핸들러를 적용할지?
    - 요청에 해당하는 리소스를 어떻게 찾을지?(리소스를 찾는 전략)
      - 캐싱, WebJars 등
        ![image](https://user-images.githubusercontent.com/23889744/154096249-7ea5ac0b-2c9f-4725-b95f-88a68725ff91.png)
  - 응답으로 보낼 리소스를 수정하는 전략
    - 캐싱, CSS 링크 등
      ![image](https://user-images.githubusercontent.com/23889744/154096461-c94c1309-857c-4c00-8d77-50028e51a976.png)

---

디폴트 서블릿, ResourceResolver, ResourceTransFormer 추가 공부
