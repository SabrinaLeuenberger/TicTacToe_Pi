package tictactoe.gameServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

/**
 * Tic Tac Toe Server. Allowing two Tic Tac Toe clients to play a game over the
 * Network. Can handle one game at a time.
 * 
 * @author Sabrina Leuenberger
 */
public class TicTacToeServer {

	/**
	 * Port for incoming connections
	 */
	private static int portNumber;

	/**
	 * Starting and running the server.
	 * 
	 * @param args
	 *            Ignored
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {

		// in will be closed once the program finishes to avoid closing System.in early
		Scanner in = new Scanner(System.in);
		System.out.println("Enter port number: ");
		portNumber = in.nextInt();

		// Running forever. If a client disconnects it causes the run method to throw
		// an exception and starts to prepare for a new game.
		while (true) {
			try {
				run();
			} catch (Exception e) {
				System.out.println("Connection closed");
			}
		}
	}

	/**
	 * Waiting for two client connections. Then forwarding data in turns from one
	 * client to the other.
	 * 
	 * @throws Exception
	 *             Is thrown when the connection is lost to a Client
	 */
	public static void run() throws Exception {

		try (ServerSocket listener = new ServerSocket(portNumber, 10, null);) {
			System.out.println("Listening on port: " + portNumber);

			Socket client1 = listener.accept();
			System.out.println("Client 1 Connected");
			Socket client2 = listener.accept();
			System.out.println("Client 2 Connected");

			// Once both clients are connected, signaling the first that he can start
			OutputStreamWriter out1 = new OutputStreamWriter(client1.getOutputStream());
			out1.write("Reset\n");
			out1.flush();
			System.out.println("Ready");

			try {
				// an exception is thrown when one client is closed, this causes
				// the current game to end.
				while (true) {
					forward(client1, client2);
					forward(client2, client1);
				}
			} finally {
				client1.close();
				client2.close();
			}
		}
	}

	/**
	 * Reading one line from a Socket and writing it to another
	 * 
	 * @param from
	 *            Reading from this Socket
	 * @param to
	 *            Writing to this Socket
	 * @throws IOException
	 *             Is thrown when the from Socket is closed by the client
	 */
	private static void forward(Socket from, Socket to) throws IOException {
		BufferedReader in = new BufferedReader(new InputStreamReader(from.getInputStream()));
		OutputStreamWriter out = new OutputStreamWriter(to.getOutputStream());
		String msg = in.readLine();
		System.out.println("Forwarding: " + msg);
		out.write(msg + "\n");
		out.flush();
	}

}
