package edu.javacourse.city.web;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.javacourse.city.dao.PersonCheckDao;
import edu.javacourse.city.dao.PoolConnectionBuilder;
import edu.javacourse.city.domain.PersonRequest;
import edu.javacourse.city.domain.PersonResponse;

@WebServlet(name = "CheckPersonServlet", urlPatterns = { "/checkPerson" })
public class CheckPersonServlet extends HttpServlet {

	private static final Logger LOGGER = LoggerFactory.getLogger(CheckPersonServlet.class);

	private PersonCheckDao dao; // отвечает только за проверку запроса

	@Override
	public void init() throws ServletException {

		LOGGER.info("SERVLET is created");
		dao = new PersonCheckDao(); // создаю только один раз объект
		dao.setConnectionBuilder(new PoolConnectionBuilder()); // dao получает объект который умеет соединяться с базой
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8"); // таблица шрифта

		PersonRequest pr = new PersonRequest(); // запрос

		pr.setSurName(req.getParameter("surName"));
		pr.setGivenName(req.getParameter("givenName"));
		pr.setPatronymic(req.getParameter("patronymic"));
		LocalDate dateOfBirth = LocalDate.parse(req.getParameter("dateOfBirth"), DateTimeFormatter.ofPattern("dd.MM.yyyy"));
		pr.setDateOfBirth(dateOfBirth);
		pr.setStreetCode(Integer.parseInt(req.getParameter("streetCode")));
		pr.setBuilding(req.getParameter("building"));
		pr.setExtension(req.getParameter("extention"));
		pr.setApartment(req.getParameter("apartment"));

		try { // плохой вариант
			PersonResponse ps = dao.checkPerson(pr); // ответ на запрос

			if (ps.isRegistered()) {
				resp.getWriter().write("Registered");
			} else {
				resp.getWriter().write("Not registered");
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

}
