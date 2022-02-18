# 핸들러 인터셉터

- 핸들러(Controller)를 실행하기전, 핸들러를 실행한 후 마지막으로 랜더링 되고 나서 진행되어야할 작업들을 설정할 수 있다
- 핸들러를 이용하여 중복되는 로직들을 핸들러에 정의함으로써 중복코드를 줄일 수 있다.
  - ex> 로깅, validation, Locale 변경 등..

## 핸들러 인터셉터 메소드 설명

참고: [Interface HandlerInterceptor](https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/web/servlet/HandlerInterceptor.html)

![image](https://user-images.githubusercontent.com/23889744/153884822-c5741efa-9203-44fe-812d-da4ae74bb0f3.png)

### boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)

- 핸들러를 실행하기 전에 호출된다
- 인자값으로 handler에 대한 정보를 알수있기 때문에 서블릿 필터보다 구체적인 로직을 구현할수있다
- 리턴값이 boolean인데 true값이면 다음 인터셉터 혹은 인터셉터가 없다면 핸들러로 요청을 보내고 false이면 인터셉터에서 응답을 처리하겠다는 의미이다

### void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView)

- 화면이 랜더링 되기전에 핸들러 요청이 끝난 시점에 로직을 처리하는 곳이다
- 인자값으로 ModelAndView를 받기 때문에 뷰에 전달할 데이터를 이곳에서 조작할수있고, handler에 대한 정보도 알수있다.
- 비동기적인 요청처리에는 이 메서드가 호출되지 않는다

### void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)

- 핸들러의 요청처리가 완전히 끝난 뒤에 호출된다(랜더링 끝난 뒤)
- prehandle()에서 true를 리턴하는 경우에 호출되며 마지막 prehandle()의 리턴값이 true가 되는 경우에 핸들러 요청이 끝나고 afterCompletion()이 호출된다
- 비동기적인 요청처리에는 이 메서드가 호출되지 않는다

<br>

### 여러개의 인터셉터가 존재할때 호출순서

A 핸들러에 요청을 보내기전에 처리되어야 할 인터셉터 B,C가 존재할때 인터셉터의 메서드 호출순서는 이렇다

- B의 preHandle()
- C의 preHandle()
- C의 postHandle()
- B의 postHandle()
- C의 afterCompletion()
- B의 afterCompletion()

<br>

## 서블릿 필터와 인터셉터 사이에서 무엇을 써야할까?

일단 인터셉터는 서블릿 필터보단 더 많은 정보를 알고있다(ex> 핸들러 정보, ModelAndView) 때문에 웹과 관련된 처리 + 스프링에 특화된 정보가 필요하고 보다 구체적인 로직을 구현해야 할때에는 서블릿 필터보다는 인터셉터를 사용하는 것이 나아보인다
<br><br>
하지만 스프링에 특화된 정보가 아닌 예를들어 들어오는 요청에 대한 인코딩 처리라던가, XSS 공격에 대응하는 즉 MVC에 특화되어있는 정보가 필요한게 아니라면, 보다 일반적인 처리가 필요하다면, 서블릿 필터를 등록하여 처리하는 것이 좋다
