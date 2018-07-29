package tictactoe.gameClient.appClasses;

import javafx.scene.control.Button;

/**
 * The Tile class extends the Button class with position-information of the
 * buttons. It was only used for buttons representing the game-board in the
 * view.
 */
public class Tile extends Button {
	private int row;
	private int column;

	public Tile(int column, int row) {
		this.column = column;
		this.row = row;
	}

	public int getColumn() {
		return column;
	}

	public int getRow() {
		return row;
	}

}
