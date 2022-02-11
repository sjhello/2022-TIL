# Spring MVC 설정

## @Bean 사용하여 직접 설정

```java
// impot 생략

@Configuration
@ComponentScan(useDefaultFilters = false, includeFilters = @ComponentScan.Filter(Controller.class))
public class WebConfig {

	@Bean
	public ViewResolver viewResolver() {
		InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
		viewResolver.setPrefix("/WEB-INF/view/");
		viewResolver.setSuffix(".jsp");
		return viewResolver;
	}

	// 기본전략을 사용하는 것과 같은 효과
	@Bean
	public HandlerAdapter handlerAdapter() {
		RequestMappingHandlerAdapter handlerAdapter = new RequestMappingHandlerAdapter();
		return handlerAdapter;
	}

	// 기본전략을 사용하는 것과 같은 효과
	@Bean
	public HandlerMapping handlerMapping() {
		RequestMappingHandlerMapping handlerMapping = new RequestMappingHandlerMapping();
		return handlerMapping;
	}
}
```

- 가장 low한 수준의 mvc 설정
- @EnableWebMvc 인터페이스를 사용하여 좀 더 편리한 설정이 가능하다

## @EnableWebMvc 사용하여 설정

```java
// import 생략

@Configuration
@EnableWebMvc
@ComponentScan(useDefaultFilters = false, includeFilters = @ComponentScan.Filter(Controller.class))
public class WebConfig {

	@Bean
	public ViewResolver viewResolver() {
		InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
		viewResolver.setPrefix("/WEB-INF/view/");
		viewResolver.setSuffix(".jsp");
		return viewResolver;
	}
}
```

```java
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import(DelegatingWebMvcConfiguration.class)
public @interface EnableWebMvc {
}
```

```java
// DelegatingWebMvcConfiguration
@Configuration
public class DelegatingWebMvcConfiguration extends WebMvcConfigurationSupport {

	private final WebMvcConfigurerComposite configurers = new WebMvcConfigurerComposite();


	@Autowired(required = false)
	public void setConfigurers(List<WebMvcConfigurer> configurers) {
		if (!CollectionUtils.isEmpty(configurers)) {
			this.configurers.addWebMvcConfigurers(configurers);
		}
	}

  // ...
}


// WebMvcConfigurationSupport
public class WebMvcConfigurationSupport implements ApplicationContextAware, ServletContextAware {

	private static final boolean romePresent;

	private static final boolean jaxb2Present;

	private static final boolean jackson2Present;

	private static final boolean jackson2XmlPresent;

	private static final boolean jackson2SmilePresent;

	private static final boolean jackson2CborPresent;

	private static final boolean gsonPresent;

	private static final boolean jsonbPresent;

	static {
		ClassLoader classLoader = WebMvcConfigurationSupport.class.getClassLoader();
		romePresent = ClassUtils.isPresent("com.rometools.rome.feed.WireFeed", classLoader);
		jaxb2Present = ClassUtils.isPresent("javax.xml.bind.Binder", classLoader);
		jackson2Present = ClassUtils.isPresent("com.fasterxml.jackson.databind.ObjectMapper", classLoader) &&
						ClassUtils.isPresent("com.fasterxml.jackson.core.JsonGenerator", classLoader);
		jackson2XmlPresent = ClassUtils.isPresent("com.fasterxml.jackson.dataformat.xml.XmlMapper", classLoader);
		jackson2SmilePresent = ClassUtils.isPresent("com.fasterxml.jackson.dataformat.smile.SmileFactory", classLoader);
		jackson2CborPresent = ClassUtils.isPresent("com.fasterxml.jackson.dataformat.cbor.CBORFactory", classLoader);
		gsonPresent = ClassUtils.isPresent("com.google.gson.Gson", classLoader);
		jsonbPresent = ClassUtils.isPresent("javax.json.bind.Jsonb", classLoader);
	}

  // ...
}
```

- @EnableWebMvc안에는 DelegatingWebMvcConfiguration을 @Import 하게 되는데 DelegatingWebMvcConfiguration은 WebMvcConfigurationSupport을 상속한다
- WebMvcConfigurationSupport에 의해서 기본적인 mvc 설정이 되어진다
- @Bean 사용한 설정과 다른점을 얕게 알아보자면...
  - HandlerMapping이 핸들러를 찾을때 사용하는 전략인 RequestMappingHandlerMapping 클래스의 우선순위가 높아졌고 HandlerAdapter 또한 RequestMappingHandlerAdapter를 제일 먼저 사용하게 바뀌었다
    ![image](https://user-images.githubusercontent.com/23889744/152672677-7417b632-a985-4e72-95ea-dbc32d6d5636.png)
  - RequestMappingHandlerMapping에는 ConversionServiceExposingInterceptor와 ResourceUrlProviderExposingInterceptor가 기본 인터셉터로 등록된다
    ![image](https://user-images.githubusercontent.com/23889744/152673029-df3accb3-df7f-4181-8fea-556ac6a39dad.png)
  - HandlerAdapter의 RequestMappingHandlerAdapter의 MessageConverter가 4개에서 6개로 바뀐다
    ![image](https://user-images.githubusercontent.com/23889744/152673008-b4d6d221-37ed-4b03-ae53-19e509e56a0f.png)
  - 더 자세한 설정은 WebMvcConfigurationSupport에서 확인 할 수 있다

## @EnableWebMvc에 WebMvcConfigurer 구현하여 설정

- @EnableWebMvc은 DelegatingWebMvcConfiguration을 사용하여 다른 클래스에서 설정을 읽어오는 방식으로 mvc 설정이 되어진다
  - WebMvcConfigurer는 WebMvcConfigurationSupport에서 설정해주는 HandlerMapping, HandlerAdapter 에다가 추가해주는 interceptor나 messageConverter 뿐만아니라 새로운 interceptor나 messageConverter 등을 쉽게 설정 할 수 있게 도와준다

<br>

# spring boot에서의 MVC 설정

![image](https://user-images.githubusercontent.com/23889744/153240864-b680e11d-f9e5-4a01-adc7-fd73e0f26008.png)

- spring-boot-autoconfigurer 모듈의 META-INF/spring.factories가 있는데 이 파일은 boot가 자동으로 설정해주는(AutoConfiguration) 설정정보들을 기술해놓았다
  - DispatcherServlet 설정은 org.springframework.boot.autoconfigure.web.servlet.DispatcherServletAutoConfiguration에서 확인 가능하다

## spring boot DispatcherServlet에서 설정하는 HandlerMapping, HandlerAdapter, ViewResolver

![image](https://user-images.githubusercontent.com/23889744/153235950-e3fac378-35ba-4a80-a686-24ca70f94922.png)

<br>

- boot를 쓰지않은 Spring MVC에서의 HandlerMapping, HandlerAdapter에서 2가지 전략을 추가하였다
  - RouterFunctionMapping
  - SimpleUrlHandlerMapping
- 우선적으로 RequestMapping 관련 HandlerMapping과 HandlerAdapter를 사용한다

![image](https://user-images.githubusercontent.com/23889744/153238985-e7373ec7-86ed-4310-8fe5-ef7ac99346d8.png)

- viewResolver에서는 ContentNegotiatingViewResolver을 주로 사용할텐데 이것은 요청정보에서 해당 요청이 어떠한 정보를 원하는 것인지 분석을 하여 다른 viewResolver들에게 위임한다
  - BeanNameViewResolver
  - ThymleafViewResolver
  - ViewResolverComposite
  - InternalResourceViewResolver
- 사진에도 나와있듯이 ContentNegotiatingViewResolver 클래스에서 나머지 ViewResolver들을 참조하고 있다

## application.properties로 설정

![image](https://user-images.githubusercontent.com/23889744/153604305-3dc90273-ad1c-4571-ab6a-0ff8a0c217d6.png)
_<WebMvcProperties.Resources>_
<br>

- spring-boot-autoconfigurer 모듈의 META-INF/spring.factories의 WebMvcAutoConfiguration 안에 static하게 존재하는 WebMvcAutoConfigurationAdapter 클래스에 의해 application.properties 파일에서 mvc 관련 설정을 정의 할 수 있다
  ![image](https://user-images.githubusercontent.com/23889744/153606210-f6e29c39-d229-4682-a99d-4cfca4d3d17e.png)
- WebMvcAutoConfigurationAdapter의 생성자는 WebMvcProperties, WebProperties를 초기화하는데 이 클래스들은 각각 spring.mvc, spring.web 을 prefix로 갖는 properties들을 사용하여 mvc 설정을 하게된다

## @Configuration + Implements WebMvcConfigurer 설정

- boot가 해주는 자동설정에 이어서 사용자가 설정하는 값이 추가되어지는 형태

## @Configuration + @EnableWebMvc + Implements WebMvcConfigurer 설정

- boot가 해주는 자동설정을 사용하지 않고 mvc를 설정을 WebMvcConfigurer 제공해주는 메서드를 이용하여 설정하는 형태
- @EnableWebMvc에는 DelegatingWebMvcConfiguration을 Import 하는데 이 클래스는 WebMvcConfigurationSupport를 상속하고 있다
- boot 자동 설정인 WebMvcAutoConfiguration을 사용하기위해선 WebMvcConfigurationSupport 가 빈으로 존재하지 않아야 하기 때문에 자동설정이 적용되지 않는 것이다

<br>

## boot는 어떻게 이러한 설정들을 자동으로 설정해주는걸까?

![image](https://user-images.githubusercontent.com/23889744/153241229-3356fc43-bcda-4af6-962f-c7c0214d1dc8.png)

- @SpringBootApplication
  - **@EnableAutoConfiguration**
- @ConditionalOnXXX
  - 어떠한 조건에 Ture일때 해당 클래스를 빈으로 사용하겠다는 의미
    - @ConditionalOnWebApplication(type = Type.Servlet)
      - springboot WebApplication의 타입은 3가지: SERVLET, REACTIVE, NONE(spring-boot 모듈의 WebApplicationType에서 확인 가능하다)
    - @ConditionalOnClass(DispatcherServlet.class)
      - 인자값으로 들어온 클래스가 클래스패스에 있는 경우
  - DispatcherServletAutoConfiguration의 @ConditionalOnXXX 어노테이션을 살펴보자
    - bootApplication이 Servlet이고, 클래스패스에 DispatcherSerlvet.class(이것을 상속한 클래스가 있을때에도)가 있을때 DispatcherServletAutoConfiguration 설정을 사용하겠다는 말이다
