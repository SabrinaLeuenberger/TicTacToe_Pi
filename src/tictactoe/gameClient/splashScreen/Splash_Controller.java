package tictactoe.gameClient.splashScreen;

import javafx.concurrent.Worker;
import tictactoe.gameClient.JavaFX_App_Driver;
import tictactoe.gameClient.abstractClasses.Controller;

/**
 * Copyright 2015, FHNW, Prof. Dr. Brad Richards. All rights reserved. This code
 * is licensed under the terms of the BSD 3-clause license (see the file
 * license.txt).
 * 
 * @author Brad Richards
 */
public class Splash_Controller extends Controller<Splash_Model, Splash_View> {

	public Splash_Controller(final JavaFX_App_Driver main, Splash_Model model, Splash_View view) {
		super(model, view);

		// The stateProperty tells us the status of the task. When the state is
		// SUCCEEDED,
		// the task is finished, so we tell the main program to continue.

		// Below are two equivalent implementations - only one of these should be used!

		// Using an anonymous class
		// model.initializer.stateProperty().addListener(
		// new ChangeListener<Worker.State>() {
		// @Override
		// public void changed(
		// ObservableValue<? extends Worker.State> observable,
		// Worker.State oldValue, Worker.State newValue) {
		// if (newValue == Worker.State.SUCCEEDED)
		// main.startApp();
		// }
		// });

		// Using a lambda expression
		model.initializer.stateProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue == Worker.State.SUCCEEDED)
				main.startApp();
		});
	}
}
