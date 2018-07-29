package pi;

import javafx.application.Application;
import javafx.stage.Stage;

/**
 * The main class of the Pi-Simulator-Application, that drives the starting and
 * the stopping of it.
 * 
 * @author leu
 */
public class PiSimulator extends Application {

	private View view;
	private Controller controller;
	private Model model;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) {
		model = new Model();
		view = new View(primaryStage, model);
		controller = new Controller(model, view);
		view.start();
	}

	@Override
	public void stop() {
		if (model != null) {
			model.stop();
		}
		if (view != null)
			view.stop();
	}

}
