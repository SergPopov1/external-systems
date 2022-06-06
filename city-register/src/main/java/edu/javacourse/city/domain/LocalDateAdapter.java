package edu.javacourse.city.domain;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class LocalDateAdapter extends XmlAdapter<String, LocalDate> {

	public static final String PATTERN = "dd.MM.yyyy";

	@Override
	public LocalDate unmarshal(String v) throws Exception { // преобразование из строки в дату

		return LocalDate.parse(v, DateTimeFormatter.ofPattern(PATTERN));
	}

	@Override
	public String marshal(LocalDate v) throws Exception { // преобразование из даты в строку

		return v.format(DateTimeFormatter.ofPattern(PATTERN));
	}

}
