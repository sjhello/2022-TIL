# HTTP 요청 메소드

참고: [RESTful web API 디자인](https://docs.microsoft.com/ko-kr/azure/architecture/best-practices/api-design#define-api-operations-in-terms-of-http-methods)

## GET

- 서버에게 리소스를 얻고자 할때 사용한다 서버는 get 요청을 받으면 응답의 본문에 리소스의 세부정보를 포함하여 응답을 보낸다
  - /orders 모든 주문 정보 요청
  - /orders/1 주문의 고유한 값이 id라면 id가 1번은 주문정보를 요청
- GET 요청의 응답은 캐싱 할 수 있다
  - 서버는 GET 요청에 대한 응답을 보낼때 캐시와 관련된 헤더를 응답에 실어 보낼 수 있다.
    - 그렇게 되면 나중에 동일한 요청을 보낼때 어떠한 조건(조건부 헤더)에 따라서 200 ok를 보내느냐(브라우저에 저장된 캐시된 값이 바뀐 경우) 304 not modified(저장된 캐시된 값이 바뀌지 않은 경우)를 보내느냐가 달라진다
    - 조건부 헤더: [HTTP 조건부 요청](https://developer.mozilla.org/ko/docs/Web/HTTP/Conditional_requests)
    - 캐시된 값은 브라우저가 관리하게 된다.
  - 유저의 비밀번호와 같이 민감한 정보는 GET 요청을 사용하지 않는다
    - get 요청은 message body가 없는 구조이며 Request Header에 정보가 담겨져서 보내어진다
    - Request Header안에 담겨져있는 정보는 URL에 노출된다
    - 브라우저에 따라 Request Header에 담을수있는 데이터의 길이가 달라진다
  - 멱등성(Idemponent)을 보장한다
    - 멱등성은 동일한 연산은 항상 동일한 결과값을 가진다는 것이다
    - 즉 동일한 get 요청은 항상 동일한 응답을 보낸다는 말이다

## POST

- 서버에 리소스를 새로 만들거나 기존 리소스에 **처리할** 데이터를 보내는데에 사용한다(수정)
- 멱등성을 보장하지 않는다
- 캐시되지 않는다
  - 캐시되지 않기 때문에 브라우저에 캐시된 값이 남지도 않는다
- GET과는 다르게 Message body를 사용하여 정보를 보낸다 이곳에 담는 데이터의 길이는 제한이 없다
- Get 요청에 비해서 민감한 정보를 다룰때 비교적 안전하다
  - 그렇다고 무조건 안전하지도 않다(http의 사용)

## PUT

- 데이터에 **해당하는** 리소스를 새로 만들거나 기존 리소스를 수정한다
  - 서버에서는 PUT 요청을 통해서 리소스를 수정하게만 할 수도 있고 리소스 생성을 막을수도 있다
  - PUT 요청을 통해서 리소스를 생성하는 경우에는 서버는 반드시 클라이언트에게 **201 Created** 응답을 보내어 리소스가 생성되었다는 것을 알려야 한다
  - PUT 요청을 통해서 리소스가 존재한다는 것을 확인했을때에는 **200 OK** 또는 **204 No Content** 응답을 보내어 PUT 요청이 성공했다는 것을 알려야한다
- 멱등성을 보장한다
  - PUT이 멱등성을 보장하는 이유는 같은 값을 사용하여 같은 리소스를 수정하기 때문이다
  - PUT의 요청 URI는 보내는 데이터에 해당하는 리소스를 지칭하기 때문이다

### POST와 PUT의 차이

- 멱등성의 차이(URI의 의미가 다르다)
  - POST는 멱등성을 보장하지 않는다
    - 요청으로 보내는 데이터를 **처리할 리소스를 지칭**하기 때문에 POST 요청을 날릴때마다 리소스가 생성되기 때문에 멱등성을 보장하지 않는다
  - PUT은 멱등성을 보장한다
    - 요청으로 보내는 데이터에 **해당하는 리소스를 지칭**하기 때문에 결과가 항상 같다

## PATCH

- PUT과 비슷하게 리소스를 수정한다 기존 리소스와의 차이점만을 보내어 리소스를 변경한다
- 멱등성을 보장(반드시 보장하는건 아니다)
  - [RESTful web API 디자인](https://docs.microsoft.com/ko-kr/azure/architecture/best-practices/api-design#define-api-operations-in-terms-of-http-methods)에 따르면 PATCH 요청은 null 리소스에 대한 업데이트를 지정하여서 새 리소스를 만들수도 있다고 한다

## DELETE

- URI에 해당하는 리소스를 삭제한다
- 멱등성을 보장한다

## HEAD

- HEAD 요청은 GET 요청과 비슷한데 다른점은 서버에서 HEAD 요청에 대한 응답을 보내줄때 body 부분은 빼고 보내는게 GET과 다르다

## OPTIONS

- 요청하는 URI에서 사용 가능한 HTTP METHOD 정보를 응답 헤더에 실어 보낸다
  - HEADERS의 Allow라는 값안에 사용가능한 HTTP METHOD 정보가 있다
- HEAD와 마찬가지로 서버에서 body 부분은 빼고 응답을 보냄

<br>

---

# 스프링 웹 MVC에서 HTTP method 맵핑하기

- 클래스에 지정
  - 클래스에 작성된 메서드 전부에게 적용
- method에 지정
  - @RequestMapping("/sample")
    - 모든 http 요청 처리한다
  - @RequestMapping(value = "/sample", method = RequestMethod.GET)
    - GET 요청으로 처리
  - @RequestMapping(value = "/sample", method = {RequestMethod.GET, RequestMethod.POST})
    - GET, POST 요청으로 처리

<br>

# 커스텀 애노테이션

## 어노테이션에 대한 간단한 설명

- composed 어노테이션: 다른 애노테이션들과의 조합으로 만들어진 어노테이션
  - @GetMapping은 @RequestMapping을 사용하고 있다
- 메타 어노테이션: 어노테이션에 대한 어노테이션
  - @Retention
    - 어노테이션을 만들때 그 어노테이션을 언제까지 유지 할 것인지를 명시하는 @Rention 어노테이션을 반드시 써줘야 한다
    - @Retention의 RetentionPolicy 값에 따라서 해당 어노테이션이 Source 에서만 유지될지, Class 레벨 까지 유지될지, Runtime시에도 어노테이션 정보가 유지될지가 결정된다
    - RetentionPolicy.SOURCE
      - 작성한 소스코드 까지에서만 어노테이션 정보를 유지(.class 파일에선 어노테이션이 사라진다)
      - 어노테이션이 주석의 역할을 하는 경우
    - RetentionPolicy.CLASS
      - @Retention을 사용할때 지정되는 기본값이다
      - .class 까지만 어노테이션 정보를 유지하겠다(런타임 시에는 어노테이션 정보를 모름)
    - RetentionPolicy.RUNTIME
      - 런타임시에도 어노테이션 정보를 유지하겠다는 의미이다
      - 스프링 MVC에서 사용하는 대부분의 어노테이션이 이 값을 사용하고 있다
        - @Controller, @Service, @Repositry, @RequestMapping ....
      - 디스패처 서블릿의 경우 들어온 요청에 대해서 어떤 핸들러로 처리를 해야될지 찾아야 하고(HandlerMapping), 처리할 핸들러를 찾았으면 그 핸들러를 실행(HandlerAdapter)까지 해야하기 때문에 런타임시에 어노테이션에 대한 정보를 알고 있어야 한다
  - @Target
    - @Target은 해당 어노테이션을 어디에서 사용 할 수 있는지에 대한 정보
      - **ElementType.TYPE**
        - class, interface(include annotation), enum
      - ElementType.FIELD
      - **ElementType.METHOD**
      - ElementType.PARAMETER
      - ElementType.CONSTRUCTOR
      - ElementType.LOCAL_VARIABLE
      - ElementType.ANNOTATION_TYPE
      - ElementType.PACKAGE
      - ElementType.TYPE_PARAMETER
      - ElementType.TYPE_USE
      - ElementType.MODULE
  - @Documented
    - javadoc을 만들때 해당 어노테이션을 대한 정보를 문서에 남기게 해주는 어노테이션
      - [@GetMapping에 대한 문서](https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/web/bind/annotation/GetMapping.html) 를 보면 @AliasFor를 확인 할 수 있는데 @AliasFor 에도 @Documented가 있기 때문에 @RequestMapping 문서에서 @AliasFor가 확인이 되는 것이다

---

# 참고

- [MDN Web DOCS](https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers#conditionals)
- [HTTP 조건부 요청](https://developer.mozilla.org/ko/docs/Web/HTTP/Conditional_requests)
- [HTTP 상태코드](https://developer.mozilla.org/ko/docs/Web/HTTP/Status)
- [HTTP 요청 메서드](https://developer.mozilla.org/ko/docs/Web/HTTP/Methods)
- [RESTful web API 디자인](https://docs.microsoft.com/ko-kr/azure/architecture/best-practices/api-design#conform-to-http-semantics)
- [인텔리제이에서 원하는 메서드가 alt + enter로 나오지 않을때](https://junho85.pe.kr/1598)
- [HTTP 1.1 Method Definitions](https://www.w3.org/Protocols/rfc2616/rfc2616-sec9.html)
- [OPTIONS 테스트 할때 순서에 상관없이 http 메서드를 확인하는 코드](https://github.com/spring-projects/spring-framework/blob/main/spring-test/src/test/java/org/springframework/test/web/servlet/samples/standalone/resultmatchers/HeaderAssertionTests.java)
