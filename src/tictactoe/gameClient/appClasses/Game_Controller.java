package tictactoe.gameClient.appClasses;

import java.io.IOException;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.WindowEvent;
import tictactoe.gameClient.ServiceLocator;
import tictactoe.gameClient.abstractClasses.Controller;

/**
 * This code is based on the jat_v2-Framework by Prof. Dr. Brad Richards, FHNW.
 * The controller class defines and triggers the events based on the actions
 * that are taken on the GUI
 */
public class Game_Controller extends Controller<Game_Model, Game_View> {
	ServiceLocator serviceLocator;

	// makes the controller know the model- and the view instance in order to
	// coordinate them.
	public Game_Controller(Game_Model model, Game_View view) {
		super(model, view);

		// register ourselves to listen for clicks to set moves
		for (int i = 0; i < model.getGridSize(); i++) {
			for (int j = 0; j < model.getGridSize(); j++) {
				view.getBoardTiles()[i][j].setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent event) {
						Tile btn = (Tile) (event.getSource());
						tileClick(btn.getColumn(), btn.getRow());
					}
				});
			}
		}
		// restarting the game
		view.getRestartButton().setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				try {
					model.restartGame();
					serviceLocator.getConnector().send("Reset");
					view.update();
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
				new Thread(() -> {
					model.integrateNetworkInput();
					// JavaFX-Thread has to update the GUI
					Platform.runLater(() -> view.update());
				}).start();
			}
		}

		);

		// register ourselves to handle window-closing event
		view.getStage().setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent event) {
				Platform.exit();
			}
		});

		serviceLocator = ServiceLocator.getServiceLocator();

		view.enterIpAddress().textProperty().addListener((observable, oldValue, newValue) -> {
			serviceLocator.getConnector().setIpAddress(newValue);
			view.connectButton().setDisable(!serviceLocator.getConnector().isConnectionValid());
		});

		view.enterPort().textProperty().addListener((observable, oldValue, newValue) -> {
			serviceLocator.getConnector().setPort(newValue);
			view.connectButton().setDisable(!serviceLocator.getConnector().isConnectionValid());
		});

		view.connectButton().setOnAction((event) -> {
			serviceLocator.getConnector().connect();
			view.update();
			new Thread(() -> {
				model.integrateNetworkInput();
				// JavaFX-Thread has to update the GUI
				Platform.runLater(() -> view.update());
			}).start();
		});

		serviceLocator.getLogger().info("Application controller initialized");
	}

	public void tileClick(int column, int row) {
		model.setMove(column, row);
		view.update();
		new Thread(() -> {
			model.integrateNetworkInput();
			// JavaFX-Thread has to update the GUI
			Platform.runLater(() -> view.update());
		}).start();
	}
}
