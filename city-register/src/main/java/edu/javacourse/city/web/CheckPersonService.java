package edu.javacourse.city.web;

import javax.annotation.PostConstruct;
import javax.inject.Singleton;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.javacourse.city.dao.PersonCheckDao;
import edu.javacourse.city.dao.PoolConnectionBuilder;
import edu.javacourse.city.domain.PersonRequest;
import edu.javacourse.city.domain.PersonResponse;
import edu.javacourse.city.exception.PersonCheckException;

@Path("/check")
@Singleton
public class CheckPersonService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CheckPersonService.class);
	
	private PersonCheckDao dao; // отвечает только за проверку запроса

	@PostConstruct
	public void init() {

		LOGGER.info("SERVICE is created");
		dao = new PersonCheckDao(); // создаю только один раз объект
		dao.setConnectionBuilder(new PoolConnectionBuilder()); // dao получает объект который умеет соединяться с базой
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)

	public PersonResponse checkPerson(PersonRequest request) throws PersonCheckException {

		LOGGER.info(request.toString());

		return dao.checkPerson(request);
	}
}
