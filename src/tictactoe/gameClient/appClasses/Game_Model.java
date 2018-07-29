package tictactoe.gameClient.appClasses;

import java.io.IOException;

import tictactoe.gameClient.ServiceLocator;
import tictactoe.gameClient.abstractClasses.Model;

/**
 * This code is based on the jat_v2-Framework by Prof. Dr. Brad Richards, FHNW.
 * The class contains the game logic. all calcultations and data concerning the
 * game itsalf are managed by this class.
 */
public class Game_Model extends Model {
	ServiceLocator serviceLocator;
	private int maxMoves = 9;
	private int move;
	private Board gameBoard;
	private Player player;
	private Player winner;
	private boolean myTurn = false;
	private boolean mePlayer1 = false;

	public Game_Model() {
		serviceLocator = ServiceLocator.getServiceLocator();
		serviceLocator.getLogger().info("Application model initialized");
		move = 1;
		gameBoard = new Board();
	}

	public String getTileText(int column, int row) {
		Player content = gameBoard.getContent(column, row);
		if (content == Player.PLAYER2) {
			return "O";// Was hardcoded because of the case statement in the Game_View, which
						// needs a constant
		} else if (content == Player.PLAYER1) {
			return "X";
		} else {
			return "";
		}
	}

	public Player getPlayersTurn() {
		if (move % 2 == 1) {
			return Player.PLAYER1;
		} else {
			return Player.PLAYER2;
		}
	}

	public void setMove(int column, int row) {
		if (gameBoard.isEmpty(column, row)) {
			player = getPlayersTurn();
			gameBoard.setContent(column, row, player);
			move += 1;
			winner = gameBoard.getWinner();

			serviceLocator.getLogger().info("Move successfully set");
			String message = move + "." + gameBoard.toString();
			serviceLocator.getLogger().info("Message ready to send: " + message);
			try {
				serviceLocator.getConnector().send(message);
			} catch (IOException e) {
				serviceLocator.getLogger().severe(e.getMessage());
				throw new RuntimeException(e);
			}

		} else {
			serviceLocator.getLogger().warning(
					"Move cannot be set at this position in the grid as another move has already been set there");
			throw new IllegalArgumentException("Field already occupied by one of the players");
		}
		myTurn = false;
	}

	public void integrateNetworkInput() {
		String msg = serviceLocator.getConnector().receive();
		serviceLocator.getLogger().info("Response: " + msg);
		if (msg.equals("Reset")) {
			gameBoard.reset();
			winner = null;
			move = 1;
			myTurn = true;
			mePlayer1 = true;
		} else {
			serviceLocator.getLogger().info("Response: " + msg);// TODO
			if (!msg.equals("null")) {
				int separatorIndex = msg.indexOf(".");
				move = Integer.parseInt(msg.substring(0, separatorIndex));
				msg = msg.substring(separatorIndex + 1);

				gameBoard.fromString(msg);

				winner = gameBoard.getWinner();
				myTurn = true;
			} else {
				System.exit(0);
			}
		}

	}

	public boolean outOfMove() {
		return (move > maxMoves);
	}

	public int getGridSize() {
		return Board.GRID_SIZE;
	}

	public Board getBoard() {
		return gameBoard;
	}

	public boolean isGameWon() {
		return winner != null;
	}

	public void restartGame() {
		gameBoard.reset();
		winner = null;
		move = 1;
		myTurn = false;
		mePlayer1 = false;
	}

	public boolean isWinnerTile(int i, int j) {
		return gameBoard.isWinnerGrid(i, j);
	}

	public Player getWinner() {
		return winner;
	}

	public boolean isMyTurn() {
		return myTurn;
	}

	public boolean amIPlayer1() {
		return mePlayer1;
	}
}
