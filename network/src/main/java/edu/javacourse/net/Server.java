package edu.javacourse.net;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class Server {

	public static void main(String[] args) throws IOException, InterruptedException {

		ServerSocket socket = new ServerSocket(25225, 1000);

		Map<String, Greetable> handlers = loadHandlers();

		System.out.println("Server is started");

		while (true) {
			Socket client = socket.accept(); 
			// слушаю socket серверный(нужной проги) жду изменений,
			// после изменений создается клиентский сокет(этой проги)

			new SimpleServer(client, handlers).start(); // сервер создает объект и передает ему (Socket client)
		}
	}

	private static Map<String, Greetable> loadHandlers() {

		Map<String, Greetable> result = new HashMap<>();

		// открываю файл и использую его как поток байтов
		try (InputStream is = Server.class.getClassLoader().getResourceAsStream("server.properties")) {

			Properties properties = new Properties();
			properties.load(is); // загружаю инфу из потока байтов

			for (Object command : properties.keySet()) { // для каждой команды по названию ищу класс

				String className = properties.getProperty(command.toString()); // имя класса
// Загружаю описание этого класса. По описанию класса можно работать с его обЪектами используя Reflection	
				Class<Greetable> cl = (Class<Greetable>) Class.forName(className); // приведение
				Greetable handler = cl.getConstructor().newInstance(); // создаю новый объект
				result.put(command.toString(), handler); // объект помещаю в Map
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException(ex);// если файл не загрузится приложение вылетит
		}

		return result;
	}

}

class SimpleServer extends Thread {

	private Socket client;
	private Map<String, Greetable> handlers;

	public SimpleServer(Socket client, Map<String, Greetable> handlers) {
		this.client = client;
		this.handlers = handlers;
	}

	@Override
	public void run() {
		handleRequest();
	}

	private void handleRequest() {

		try {
			// читаю или записываю строку преобразованную в char полученных из потока байтов
			BufferedReader br = new BufferedReader(new InputStreamReader(client.getInputStream()));
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));

			String request = br.readLine(); // читаю строку из потока
			String[] lines = request.split("\\s+"); // делю строку на слова
			String command = lines[0]; // первое слово строки
			String userName = lines[1]; // второе слово строки
			System.out.println("Server got string1:" + command);
			System.out.println("Server got string2:" + userName);

			// Thread.sleep(2000); // останавливаю server на 2 сек
			StringBuilder sb = new StringBuilder("Hello, "); // шаблон начала строки

			String response = buildResponse(command, userName); 
			bw.write(response); // полученную строку отправил во внещний поток
			bw.newLine(); // вставляю разделитель строк
			bw.flush(); // посылаю

			br.close();
			bw.close();

			client.close();

		} catch (Exception ex) {
			ex.printStackTrace(System.out);
		}
	}

	private String buildResponse(String command, String userName) {
		
		Greetable handler = handlers.get(command);
		if (handler != null) {
			return handler.buuildResponseString(userName);
		}

		return "Hey, " + userName;
	}

}
