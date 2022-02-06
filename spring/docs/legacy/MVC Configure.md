# MVC 설정

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
