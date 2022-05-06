package edu.javacourse.city.dao;

import java.time.LocalDate;

import org.junit.Assert;
import org.junit.Test;

import edu.javacourse.city.domain.PersonRequest;
import edu.javacourse.city.domain.PersonResponse;
import edu.javacourse.city.exception.PersonCheckException;

public class PersonCheckDaoTest {

	@Test
	public void testCheckPerson() throws PersonCheckException {

		PersonRequest pr = new PersonRequest(); // запрос
		// заполняю запрос, максимально полный
		pr.setSurName("Васильев");
		pr.setGivenName("ПАВЕЛ");
		pr.setPatronymic("николаевич");
		pr.setDateOfBirth(LocalDate.of(1995, 3, 18));
		pr.setStreetCode(1);
		pr.setBuilding("10");
		pr.setExtension("2");
		pr.setApartment("121");

		PersonCheckDao dao = new PersonCheckDao(); // запрос в базу ГРН и соединение с ней
		PersonResponse ps = dao.checkPerson(pr); // ответ на запрос
		Assert.assertTrue(ps.isRegistered()); // чел зарегистрирован постоянно
		Assert.assertFalse(ps.isTemporal()); // чел зарегистрирован постоянно
	}

	@Test
	public void testCheckPerson2() throws PersonCheckException {

		PersonRequest pr = new PersonRequest(); // запрос
		// заполняю запрос, запрос не полный
		pr.setSurName("Васильева");
		pr.setGivenName("ИринА");
		pr.setPatronymic("петровна");
		pr.setDateOfBirth(LocalDate.of(1997, 8, 21));
		pr.setStreetCode(1);
		pr.setBuilding("271");
		pr.setApartment("4");

		PersonCheckDao dao = new PersonCheckDao();
		PersonResponse ps = dao.checkPerson(pr);
		Assert.assertTrue(ps.isRegistered());
		Assert.assertFalse(ps.isTemporal());
	}

}
