# Dispatcher Servlet

## Special Bean Types

[Special Bean Types - 참고 사이트](https://olivejua-develop.tistory.com/59)

<br>

![image](https://user-images.githubusercontent.com/23889744/151183516-ad705d86-34ca-401d-a56d-c8c744dde6e4.png)
_<DispatcherServlet에서 제공하는 특수한 Bean들 >_

- 디스패처 서블릿에서 제공하는 빈들을 **Special Bean Types** 이라고 한다
  - HandlerMapping
  - HandlerAdapter
  - HandlerExceptionResolver
  - ViewResolver
  - LocaleResolver, LocalContextResolver
  - ThemeResolver
  - MultipartResolver
  - FlashMapManager
- Special Bean Types의 빈들은 override 할 수 있고 기본 전략을 제공하기도 한다
  - 기본 전략은 DispatcherServlet.properties 에서 확인 할 수 있다

<br>

### MultipartResolver

- 파일 업로드 요청 처리에 필요한 빈
- 서버에 파일을 업로드 하면 request 요청 Header에 content-type: multipart/form-data가 되고 message Body에 인코딩 하여 보내게 되는데 서버에서는 이를 파트별로 분리하여 정보를 받게 된다
  - 이렇게 분리되어있는 바이너리 데이터를 처리하는 로직이 필요한데 이를 쉽게 해주는 것이 MultipartResolver이다
  - 결국 MultipartResolver가 들어온 request를 MultipartHttpServletRequest로 변환해서 안에 담겨있는 파일을 쉽게 얻을 수 있게 해준다
- MultipartResolver는 기본 전략에 등록되어 있지 않은 빈이다(스프링 부트는 기본 전략으로 사용되고 있다 StandardMultipartHttpServletRequest )
  - MultipartResolver를 사용하려면 설정이 필요하다

### LocalResolver

- 요청이 디스패처 서블릿으로 들어왔을때 들어온 요청을 분석하는 단계에서 사용이 된다
  - 요청을 보낸 클라이언트의 Locale 정보를 확인한다
    - 기본 전략에서 사용하는 구현체(AcceptHeaderLocaleResolver)는 request의 accept-language를 확인한다
  - Locale 정보를 활용하여 Local에 맞는 적절한 messageSource를 제공 한다(Spring의 기능)

### ThemeResolver

- 클라이언트의 요청 자체를 통해서 애플리케이션의 테마를 변경할 수 있게 해준다
- 기본 전략은 FixedThemeResolver

### HandlerMapping

- 요청을 처리하는 핸들러를 찾아주는 인터페이스
- 기본 전략은
  - **RequestMappingHandlerMapping**
    - @RequestMapping 정보를 기반으로 핸들러를 찾아주는 구현체
  - BeanNameUrlHandlerMapping

### HandlerAdapter

- HandlerMapping 이 찾아낸 **핸들러**를 처리하는 인터페이스
- **RequestMappingHandlerAdapter**, SimpleControllerHandlerAdapter, HttpRequestHandlerAdapter

### HandlerExceptionResolvers

- 요청 처리 중에 발생한 에러를 처리하는 인터페이스

### RequestToViewNameTranslator

- 요청을 처리하는 핸들러에서 뷰 이름을 지정하지 않아도 요청을 통해 뷰 이름을 판단하는 인터페이스

### ViewResolver

- 해당하는 뷰 이름을 찾아주는 인터페이스

### FlashMapManager

- Post 요청 -> redirect -> Get 요청의 경우 Post 요청을 한 다음에 데이터를 디비에 저장하고 redirect를 하게되는데..
  - redirect를 하는 이유는 화면에서 새로고침을 하게 되면 같은 Post 요청이 반복적으로 서버로 오게 된다 이것을 방지하기 위함이다
  - 그래서 Post 요청을 한 후 데이터를 저장하고, Get 요청으로 redirect 하게 되면 새로고침을 하여도 Post 요청을 날리지 못하게 된다(이미 Get으로 redirect가 되었기 때문에)
  - 결국 중복 Post 요청을 방지하기 위함이다
- Post 요청 처리 후 redirect를 하는 과정에 데이터를 전달해줄 필요가 있다
  - 이때 redirect하게 될때에는 url에다가 데이터를 담아서 전달하게 된다
    - ex> redirect:/events/id=200, redirect:/events?id=200 ....
    - 이렇게 Path에 데이터를 주거나, 쿼리 파라미터에 데이터를 주지 않아도 전송하게 해주는 것이 FlashMap 이다
    - FlashMap은 FlashMapManager가 관리한다
  - 결국 redirect를 할때 데이터 전송을 편하게 해주는 것이 FlashMapManager의 역할이다
