package com.example.demobootweb.config;

import java.text.ParseException;
import java.util.Locale;

import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

import com.example.demobootweb.domain.Person;

@Component
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
