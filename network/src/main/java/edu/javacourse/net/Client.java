package edu.javacourse.net;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class Client {

	public static void main(String[] args) throws IOException {

		for (int i = 0; i < 8; i++) {
			SimpleClient sc = new SimpleClient(i); // создаю объект
			sc.start(); // запускаю поток
		}
	}

}

class SimpleClient extends Thread {
	
	private static final String[] COMMAND = {
			"HELLO", "MORNING", "DAY", "EVENING"		
	};
	
	private int cmdNumber;	

	public SimpleClient(int cmdNumber) {
		this.cmdNumber = cmdNumber;
	}

	@Override
	public void run() {

		try {
			// System.out.println("Started: " + LocalDateTime.now());
			Socket socket = new Socket("127.0.0.1", 25225);

			// читаю или записываю строку преобразованную в char полученную из потока байтов
			BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

			String command = COMMAND[cmdNumber % COMMAND.length];
			String sb = command + " " + "Serg";

			bw.write(sb); // строку отправил во внещний поток
			bw.newLine(); // вставляю разделитель строк
			bw.flush(); // посылаю

			String answer = br.readLine(); // читаю полученную строку
			System.out.println("Client got string:" + answer);

			br.close();
			bw.close();
			// System.out.println("Finished: " + LocalDateTime.now());
	} catch (IOException ex) {
		ex.printStackTrace(System.out);
	}
  }
}
