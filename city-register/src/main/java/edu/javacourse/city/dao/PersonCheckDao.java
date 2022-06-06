package edu.javacourse.city.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import edu.javacourse.city.domain.PersonRequest;
import edu.javacourse.city.domain.PersonResponse;
import edu.javacourse.city.exception.PersonCheckException;

public class PersonCheckDao {

	private static final String SQL_REQUEST = 
		"select temporal from cr_address_person AS ap "
			+ "inner join cr_person AS p ON p.person_id = ap.person_id "
			+ "inner join cr_address AS a ON a.address_id = ap.address_id "
			+ "where "
			// выясняю на момент запроса человек зарегистрирован или нет
			+ "CURRENT_DATE >= ap.start_date and (CURRENT_DATE <= ap.end_date or ap.end_date is null) "
			+ "and upper(p.sur_name) = upper(?) "
			+ "and upper(p.given_name) = upper(?) "
			+ "and upper(p.patronymic) = upper(?) "
			+ "and p.date_of_birth = ? "
			+ "and a.street_code = ? "
					+ "and upper(a.building) = upper(?) ";

	private ConnectionBuilder connectionBuilder; // отвечает только за соединение с базой

	public void setConnectionBuilder(ConnectionBuilder connectionBuilder) { // setter
		this.connectionBuilder = connectionBuilder;
	}

	private Connection getConnection() throws SQLException {

		return connectionBuilder.getConnection();
	}

	public PersonResponse checkPerson(PersonRequest request) throws PersonCheckException {

		PersonResponse response = new PersonResponse();

		String sql = SQL_REQUEST; // обязательные данные в запросе

		// не обязательные данные в запросе
		if (request.getExtension() != null) { // в адресе есть номер корпуса
			sql += "and upper(a.extension) = upper(?) ";
		} else {
			sql += "and a.extension is null ";
		}

		if (request.getApartment() != null) { // в адресе есть номер квартиры
			sql += "and upper(a.apartment) = upper(?) ";
		} else {
			sql += "and a.apartment is null ";
		}

		try (Connection con = getConnection(); 
				PreparedStatement stmt = con.prepareStatement(sql)) {
			
			int count = 1; // счетчик полей запроса

			stmt.setString(count++, request.getSurName());
			stmt.setString(count++, request.getGivenName());
			stmt.setString(count++, request.getPatronymic());
			stmt.setDate(count++, Date.valueOf(request.getDateOfBirth()));
			stmt.setInt(count++, request.getStreetCode());
			stmt.setString(count++, request.getBuilding());

			if (request.getExtension() != null) // в адресе есть номер корпуса
				stmt.setString(count++, request.getExtension());

			if (request.getApartment() != null) // в адресе есть номер квартиры
				stmt.setString(count, request.getApartment());

			  ResultSet rs = stmt.executeQuery();
				if (rs.next()) { // есть запись
				  response.setRegistered(true); // человек прописан постоянно
				  response.setTemporal(rs.getBoolean("temporal")); // беру значение из колонки temporal
			}

		} catch (SQLException ex) {
			throw new PersonCheckException(ex); // проблема при соединении
		}

		return response;
	}


}
