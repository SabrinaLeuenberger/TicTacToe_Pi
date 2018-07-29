package tictactoe.gameClient.appClasses;

public class Board {

	public final static int GRID_SIZE = 3;
	private GridContent[][] grid = new GridContent[GRID_SIZE][GRID_SIZE];

	public Board() {
		reset();
	}

	public void setContent(int column, int row, Player player) {
		grid[column][row].setPlayer(player);
		;
	}

	public Player getContent(int column, int row) {
		if (!isEmpty(column, row)) {
			return grid[column][row].getPlayer();
		} else {
			return null;
		}
	}

	public boolean isEmpty(int column, int row) {
		return grid[column][row].getPlayer() == null;
	}

	public void reset() {
		for (int i = 0; i < GRID_SIZE; i++) {
			for (int j = 0; j < GRID_SIZE; j++) {
				grid[i][j] = new GridContent();
			}
		}
	}

	public Player getWinner() {
		// check columns
		for (int i = 0; i < 3; i++) {
			if (!isEmpty(i, 0) && getContent(i, 0) == getContent(i, 1) && getContent(i, 1) == getContent(i, 2)) {
				grid[i][0].setWinnerGrid(true);
				grid[i][1].setWinnerGrid(true);
				grid[i][2].setWinnerGrid(true);
				return grid[i][0].getPlayer();
			}
		}

		// check rows
		for (int i = 0; i < 3; i++) {
			if (!isEmpty(0, i) && getContent(0, i) == getContent(1, i) && getContent(1, i) == getContent(2, i)) {
				grid[0][i].setWinnerGrid(true);
				grid[1][i].setWinnerGrid(true);
				grid[2][i].setWinnerGrid(true);
				return grid[0][i].getPlayer();
			}
		}

		// check first diagonal
		if (!isEmpty(0, 0) && getContent(0, 0) == getContent(1, 1) && getContent(1, 1) == getContent(2, 2)) {
			grid[0][0].setWinnerGrid(true);
			grid[1][1].setWinnerGrid(true);
			grid[2][2].setWinnerGrid(true);
			return grid[0][0].getPlayer();
		}

		// check second diagonal
		if (!isEmpty(2, 0) && getContent(0, 2) == getContent(1, 1) && getContent(1, 1) == getContent(2, 0)) {
			grid[0][2].setWinnerGrid(true);
			grid[1][1].setWinnerGrid(true);
			grid[2][0].setWinnerGrid(true);
			return grid[0][2].getPlayer();
		}
		return null;
	}

	public boolean isWinnerGrid(int i, int j) {
		return grid[i][j].isWinnerGrid();
	}

	public void checkWinner() {
		getWinner();
	}

	@Override
	public String toString() {
		String boardRepresentation = "";
		for (int i = 0; i < GRID_SIZE; i++) {
			for (int j = 0; j < GRID_SIZE; j++) {
				if (grid[i][j].getPlayer() == Player.PLAYER1 && !grid[i][j].isWinnerGrid()) {
					boardRepresentation = boardRepresentation + "x";
				} else if (grid[i][j].getPlayer() == Player.PLAYER1 && grid[i][j].isWinnerGrid()) {
					boardRepresentation = boardRepresentation + "X";
				} else if (grid[i][j].getPlayer() == Player.PLAYER2 && !grid[i][j].isWinnerGrid()) {
					boardRepresentation = boardRepresentation + "o";
				} else if (grid[i][j].getPlayer() == Player.PLAYER2 && grid[i][j].isWinnerGrid()) {
					boardRepresentation = boardRepresentation + "O";
				} else {
					boardRepresentation = boardRepresentation + "_";
				}
			}
		}
		return boardRepresentation;
	}

	public void fromString(String boardRepresentation) {
		for (int i = 0; i < boardRepresentation.length(); i++) {
			int row = i % GRID_SIZE;
			int column = (i - row) / GRID_SIZE;
			if (boardRepresentation.charAt(i) == 'x') {
				grid[column][row].setPlayer(Player.PLAYER1);
				grid[column][row].setWinnerGrid(false);
			} else if (boardRepresentation.charAt(i) == 'X') {
				grid[column][row].setPlayer(Player.PLAYER1);
				grid[column][row].setWinnerGrid(true);
			} else if (boardRepresentation.charAt(i) == 'o') {
				grid[column][row].setPlayer(Player.PLAYER2);
				grid[column][row].setWinnerGrid(false);
			} else if (boardRepresentation.charAt(i) == 'O') {
				grid[column][row].setPlayer(Player.PLAYER2);
				grid[column][row].setWinnerGrid(true);
			} else {
				grid[column][row].setPlayer(null);
				grid[column][row].setWinnerGrid(false);
			}
		}
	}
}
