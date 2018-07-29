package tictactoe.gameClient.appClasses;

public class GridContent {

	private boolean isWinnerGrid = false;
	private Player player = null;

	public GridContent() {

	}

	public boolean isWinnerGrid() {
		return isWinnerGrid;
	}

	public void setWinnerGrid(boolean isWinnerGrid) {
		this.isWinnerGrid = isWinnerGrid;
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

}
