package tictactoe.gameClient.clientClasses;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TicTacToeClient {

	private String ipAddress;
	private int portNumber;
	private Socket socket;
	private BufferedReader netIn;
	private OutputStreamWriter netOut;

	public void setIpAddress(String newValue) {
		ipAddress = newValue;
	}

	private boolean validateIpAddress() {

		String regexIP = "^(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)(\\.(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)){3}$";
		Pattern pIP = Pattern.compile(regexIP);
		Matcher mIP = pIP.matcher(ipAddress);
		boolean validIP = mIP.find();
		return (validIP);
	}

	public void setPort(String newValue) {
		try {
			portNumber = Integer.parseInt(newValue);
		} catch (NumberFormatException e) {
			portNumber = 0;
		}
	}

	public boolean validatePort() {
		return (portNumber >= 1 && portNumber <= 65535);
	}

	public boolean isConnectionValid() {
		return (validateIpAddress() && validatePort());
	}

	public void connect() {
		try {
			socket = new Socket(ipAddress, portNumber);
			netIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			netOut = new OutputStreamWriter(socket.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void disconnect() {
		try {
			netIn.close();
			netOut.close();
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public boolean isConnected() {
		return socket != null && socket.isConnected();
	}

	public void send(String boardRepresentation) throws IOException {
		netOut.write(boardRepresentation + "\n");
		netOut.flush();
	}

	public String receive() {
		try {
			return netIn.readLine().trim();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
