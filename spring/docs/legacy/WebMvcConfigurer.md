# WebMvcConfigurer 설정

## Formatter

- 어떤 객체를 문자열로 변환하거나, 어떤 문자열을 객체로 변환해주는 인터페이스
- UrlPath(@PathVariable), 쿼리 스트링(@RequestParam)으로 넘어오는 값에 대해서 적용 할 수 있다

```java
@RestController
public class SampleController {

	@GetMapping("/hello/{name}")
	public String hello(@PathVariable("name") Person person) {
		return "hello" + person.getName();
	}

  @GetMapping("/hello")
	public String helloRequestParam(@RequestParam("name") Person person) {
		return "hello " + person.getName();
	}
```

name이라는 문자열을 PathVariable로 받았을때 그 문자열을 객체로 변환하여 response로 보내고 싶은 상황이다.
<br>포매터 설정 없이 /hello/sjhello 요청을 보내도 Spring은 PathVariable에 해당하는 값을 객체로 변환할수 없다 그래서 포매터 만들고 등록을 해줘야 하는데..

```java
public class PersonFormatter implements Formatter<Person> {

	@Override
	public Person parse(String text, Locale locale) throws ParseException {
		Person person = new Person();
		person.setName(text);
		return person;
	}

	@Override
	public String print(Person person, Locale locale) {
		return person.toString();
	}
}
```

```java
package org.springframework.format;

/**
 * Formats objects of type T.
 * A Formatter is both a Printer <i>and</i> a Parser for an object type.
 *
 * @author Keith Donald
 * @since 3.0
 * @param <T> the type of object this Formatter formats
 */
public interface Formatter<T> extends Printer<T>, Parser<T> {

}
```

우선 PersonFormatter 만들어 주고 Formatter 인터페이스를 구현해줘야 한다. Formatter에는 두가지 클래스를 상속하는데 Printer와 Parser이다

- Printer의 print()는 들어온 객체를 문자열로 변환
- Parser의 parse()는 들어온 문자열을 객체로 변환해준다
- 두 메서드 모두 요청의 Locale 정보를 참고한다

<br>

```java
@Configuration
public class WebConfig implements WebMvcConfigurer {

	@Override
	public void addFormatters(FormatterRegistry registry) {
		registry.addFormatter(new PersonFormatter());
	}
}
```

작성한 PersonFormatter를 등록해줘야 하는데 WebMvcConfigurer 인터페이스를 이용하여 등록해준다. 참고로 @Configuration + implements WebMvcConfigurer 조합은 boot가 자동으로 설정해주는 설정값에 우리가 작성한 설정을 add 하는 방식이다(자세한 내용은 MVC Configure.md 파일 참고)

<br>

인자값으로 받는 registry는 Converter도 추가가 가능하다(FormatterRegistry가 ConverterRegistry를 상속받고 있기 때문에)

### WebConfig에 Formatter를 등록하지 않고 Formatter를 등록하는 방법

boot의 자동설정중 WebMvcAutoConfiguration 클래스의 addFormatter 부분을 살펴보자

```java
// WebMvcAutoConfiguration
@Override
		public void addFormatters(FormatterRegistry registry) {
			ApplicationConversionService.addBeans(registry, this.beanFactory);
		}
```

```java
// ApplicationConversionService.addBeans()
	/**
	 * Add {@link GenericConverter}, {@link Converter}, {@link Printer}, {@link Parser}
	 * and {@link Formatter} beans from the specified context.
	 * @param registry the service to register beans with
	 * @param beanFactory the bean factory to get the beans from
	 * @since 2.2.0
	 */
	public static void addBeans(FormatterRegistry registry, ListableBeanFactory beanFactory) {
		Set<Object> beans = new LinkedHashSet<>();
		beans.addAll(beanFactory.getBeansOfType(GenericConverter.class).values());
		beans.addAll(beanFactory.getBeansOfType(Converter.class).values());
		beans.addAll(beanFactory.getBeansOfType(Printer.class).values());
		beans.addAll(beanFactory.getBeansOfType(Parser.class).values());
		for (Object bean : beans) {
			if (bean instanceof GenericConverter) {
				registry.addConverter((GenericConverter) bean);
			}
			else if (bean instanceof Converter) {
				registry.addConverter((Converter<?, ?>) bean);
			}
			else if (bean instanceof Formatter) {
				registry.addFormatter((Formatter<?>) bean);
			}
			else if (bean instanceof Printer) {
				registry.addPrinter((Printer<?>) bean);
			}
			else if (bean instanceof Parser) {
				registry.addParser((Parser<?>) bean);
			}
		}
	}
```

ApplicationConversionService.addBeans 메서드에 의해 GenericConverter,Converter,Printer,Parser 타입의 빈을 수집하여 컨버터, 포매터, 프린터, 파서를 FormatterRegistry의 addXX 메서드를 이용하여 설정을 등록해준다
