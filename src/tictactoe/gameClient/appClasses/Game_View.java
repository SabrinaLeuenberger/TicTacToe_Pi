package tictactoe.gameClient.appClasses;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import tictactoe.gameClient.ServiceLocator;
import tictactoe.gameClient.abstractClasses.View;

/**
 * This code is based on the jat_v2-Framework by Prof. Dr. Brad Richards, FHNW.
 * This class defines the GUI of the game.
 * 
 */
public class Game_View extends View<Game_Model> {
	// leaf elements of the GUI
	private Tile[][] boardTiles;
	private Label lblInstruction;
	private Label lblWonInfo;
	private Label lblWonIcon;
	private Button btnRestart;
	private Image tileX;
	private Image tileXwin;
	private Image tileO;
	private Image tileOwin;
	private Label lblNamePlayer1;
	private Label lblNamePlayer2;

	private Label iconPlayer1;
	private Label iconPlayer2;
	private Label lblIpAddress;
	private TextField enterIpAddress;
	private Label lblPort;
	private TextField enterPort;
	private Button btnConnect;

	// don't put anything belonging to the GUI in the View constructor
	public Game_View(Stage stage, Game_Model model) {
		super(stage, model);
		stage.setTitle("Tic Tac Toe");
		ServiceLocator.getServiceLocator().getLogger().info("Application view initialized");
	}

	@Override
	protected Scene create_GUI() {
		/*
		 * the GUI-Elements have to be initialised in this method and can't be
		 * initialised in the constructor of the view as the GUI is used before the
		 * constructor has completed the initialisation of the instance
		 */

		tileX = new Image(getClass().getResourceAsStream("X.png"));
		tileXwin = new Image(getClass().getResourceAsStream("Xwin.png"));
		tileO = new Image(getClass().getResourceAsStream("O.png"));
		tileOwin = new Image(getClass().getResourceAsStream("Owin.png"));

		// build connection bar to be put on the top of the root
		lblIpAddress = new Label("IP-Address: ");
		enterIpAddress = new TextField();
		lblPort = new Label("Port: ");
		enterPort = new TextField();
		btnConnect = new Button("connect");
		HBox connectionField = new HBox(lblIpAddress, enterIpAddress, lblPort, enterPort, btnConnect);

		// build gameBoard to be put in the center of the root
		GridPane playField = new GridPane();
		playField.setAlignment(Pos.CENTER);
		boardTiles = new Tile[model.getGridSize()][model.getGridSize()];
		for (int i = 0; i < model.getGridSize(); i++) {
			for (int j = 0; j < model.getGridSize(); j++) {
				Tile btn = new Tile(i, j);
				btn.setMinSize(120.0, 120.0);
				boardTiles[i][j] = btn;
				playField.add(btn, i, j);
			}
		}

		// build a game-progress-bar to put on the bottom of the root
		lblInstruction = new Label("");
		lblInstruction.setMinWidth(220.0);
		lblInstruction.setMaxWidth(220.0);

		lblWonInfo = new Label("");
		lblWonInfo.setMinWidth(220.0);
		lblWonInfo.setMaxWidth(220.0);

		lblWonIcon = new Label();
		lblWonIcon.setMinWidth(50);
		lblWonIcon.setMaxWidth(50);
		lblWonIcon.setMinHeight(50);
		lblWonIcon.setMaxWidth(50);
		btnRestart = new Button("Restart");
		VBox control = new VBox(lblWonIcon, btnRestart);
		control.setAlignment(Pos.CENTER);
		HBox gameProgress = new HBox(lblInstruction, control, lblWonInfo);
		gameProgress.setAlignment(Pos.TOP_CENTER);

		// build player1 bar to be put left
		iconPlayer1 = new Label();
		ImageView iconX = new ImageView(tileX);
		iconX.setFitHeight(40);
		iconX.setFitWidth(40);
		iconPlayer1.setGraphic(iconX);
		lblNamePlayer1 = new Label("Player X");
		VBox player1 = new VBox(lblNamePlayer1, iconPlayer1);
		player1.setAlignment(Pos.CENTER_LEFT);

		// build player2 bar to be put right
		ImageView iconO = new ImageView(tileO);
		iconO.setFitHeight(40);
		iconO.setFitWidth(40);
		iconPlayer2 = new Label();
		lblNamePlayer2 = new Label("Player O");
		VBox player2 = new VBox(lblNamePlayer2, iconPlayer2);
		player2.setAlignment(Pos.CENTER_RIGHT);
		iconPlayer2.setGraphic(iconO);
		iconPlayer2.setDisable(true);

		// puts all elements together into the root
		BorderPane root = new BorderPane();
		root.setCenter(playField);
		root.setLeft(player1);
		root.setRight(player2);
		root.setBottom(gameProgress);
		root.setTop(connectionField);

		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("app.css").toExternalForm());
		updateGameBoard();
		updateGameProgress();
		return scene;
	}

	private void updateGameBoard() {
		for (int i = 0; i < model.getGridSize(); i++) {
			for (int j = 0; j < model.getGridSize(); j++) {
				boolean winnerTile = model.isWinnerTile(i, j);
				String status = model.getTileText(i, j);
				switch (status) {
				case "X":
					if (winnerTile) {
						ImageView imgXwin = new ImageView(tileXwin);
						imgXwin.setFitHeight(100);
						imgXwin.setFitWidth(100);
						boardTiles[i][j].setGraphic(imgXwin);

					} else {
						ImageView imgX = new ImageView(tileX);
						imgX.setFitHeight(100);
						imgX.setFitWidth(100);
						boardTiles[i][j].setGraphic(imgX);
					}
					break;

				case "O":
					if (winnerTile) {
						ImageView imgOwin = new ImageView(tileOwin);
						imgOwin.setFitHeight(100);
						imgOwin.setFitWidth(100);
						boardTiles[i][j].setGraphic(imgOwin);

					} else {
						ImageView imgO = new ImageView(tileO);
						imgO.setFitHeight(100);
						imgO.setFitWidth(100);
						boardTiles[i][j].setGraphic(imgO);
					}
					break;

				default:
					boardTiles[i][j].setGraphic(null);
				}

				if (!model.getBoard().isEmpty(i, j) || model.isGameWon() || !model.isMyTurn()) {
					boardTiles[i][j].setDisable(true);
				} else {
					boardTiles[i][j].setDisable(false);
				}

				if (model.amIPlayer1()) {
					lblNamePlayer1.setText(" You ");
					lblNamePlayer2.setText("Other");
				} else {
					lblNamePlayer1.setText("Other");
					lblNamePlayer2.setText(" You ");
				}
			}
		}
	}

	private void updateGameProgress() {
		if (model.isGameWon()) {
			if (model.getWinner() == Player.PLAYER1) {
				ImageView iconX = new ImageView(tileX);
				iconX.setFitHeight(40);
				iconX.setFitWidth(40);
				lblWonIcon.setGraphic(iconX);
			} else {
				ImageView iconO = new ImageView(tileO);
				iconO.setFitHeight(40);
				iconO.setFitWidth(40);
				lblWonIcon.setGraphic(iconO);
			}
			lblWonInfo.setText("...has won!");

		} else {
			lblWonIcon.setGraphic(null);
			lblWonInfo.setText("");
		}
		if (!model.isGameWon()) {
			if (!ServiceLocator.getServiceLocator().getConnector().isConnected()) {
				lblInstruction.setText("Please connect..");
			} else if (model.isMyTurn()) {
				lblInstruction.setText("Your turn...");
			} else {
				lblInstruction.setText("Please wait...");
			}
		}
		btnRestart.setDisable(!model.isMyTurn());
	}

	public void update() {
		try {
			updateGameBoard();
			updateGameProgress();
		} catch (IllegalArgumentException e) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error occurred");
			alert.setHeaderText("ERROR");
			alert.setContentText(e.getMessage());
			alert.showAndWait();
		}
	}

	protected Tile[][] getBoardTiles() {
		return boardTiles;
	}

	protected Button getRestartButton() {
		return btnRestart;
	}

	protected TextField enterIpAddress() {
		return enterIpAddress;
	}

	protected TextField enterPort() {
		return enterPort;
	}

	protected Button connectButton() {
		return btnConnect;
	}

	protected void updateTexts() {
		// Other controls
	}

}