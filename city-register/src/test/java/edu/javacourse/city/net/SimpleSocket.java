package edu.javacourse.city.net;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import org.junit.Test;

public class SimpleSocket {

	@Test
	public void simpleSocket() throws IOException {

		Socket socket = new Socket("java-course.ru", 80);

		InputStream is = socket.getInputStream(); // входной поток
		OutputStream os = socket.getOutputStream(); // выходной поток

		// получаю данные из файла sitemap.xml
		String command = "GET /sitemap.xml HTTP/1.1\r\nHost:java-course.ru\r\n\r\n";
		os.write(command.getBytes()); // в os записываю данные в виде байтов
		os.flush(); // заставляю немедленно отправить данные вне зависимости от размера

		int c = 0;
		while ((c = is.read()) != -1) { // пока байты есть
			System.out.print((char) c); // важно print
		}

		socket.close();
	}


}
