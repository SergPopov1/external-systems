package edu.javacourse.greet;

import edu.javacourse.net.Greetable;

public class EveningGreet extends Greetable {

	@Override
	public String buuildResponseString(String userName) {

		return "Good evening, " + userName;
	}

}
