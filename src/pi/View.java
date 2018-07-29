package pi;

import java.util.Observable;
import java.util.Observer;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * The View class defines the GUI of the Pi-Simulation. It is an Observer
 * connected to the model.
 * 
 * @author leu
 */
public class View implements Observer {

	Model model;
	Stage stage;
	Label noPointsLabel;
	Label noPointsValue;

	// Top
	Label estimatedPiLabel;
	Label estimatedPiValue;
	Label spaceUpsideLabel;
	Label spaceMiddleLabel;
	Label spaceDownsideLabel;

	// Centre
	static final int CANVAS_SIZE = 350;
	static final int PI_CANVAS_SIZE_HEIGHT = CANVAS_SIZE / 3;
	static final int PI_LINE_Y_POSITION = PI_CANVAS_SIZE_HEIGHT / 2;
	GraphicsContext gcPoints;
	Canvas canvasPoints;
	GraphicsContext gcPi;
	Canvas canvasPi;
	int xPositionPi;

	// Bottom
	Button startPauseButton;
	Label speedButton;
	Slider speedSlider;
	Button clearButton;
	Label spaceRightLabel;
	Label spaceLeftLabel;
	Label spaceBottomLabel;
	Label spaceTopLabel;
	Label minLabel;
	Label maxLabel;

	protected View(Stage stage, Model model) {
		this.stage = stage;
		this.model = model;
		model.addObserver(this);
		stage.setTitle("Pi Simulator");

		// To put in the bottom
		startPauseButton = new Button("start");
		speedSlider = new Slider();
		minLabel = new Label("min");
		maxLabel = new Label("max");
		HBox sliderBox = new HBox(minLabel, speedSlider, maxLabel);
		speedButton = new Label("speed");
		speedSlider.setMin(0);
		speedSlider.setMax(100);
		speedSlider.setBlockIncrement(10);
		speedSlider.setValue(Model.STARTING_SPEED_INDEX);
		speedSlider.setShowTickLabels(false);
		speedSlider.setShowTickMarks(false);
		spaceBottomLabel = new Label();
		spaceTopLabel = new Label();
		VBox speedBox = new VBox(spaceTopLabel, speedButton, sliderBox, spaceBottomLabel);
		speedBox.setAlignment(Pos.CENTER);

		clearButton = new Button("clear");

		spaceLeftLabel = new Label();
		spaceLeftLabel.setMinWidth(25.0);
		spaceRightLabel = new Label();
		spaceRightLabel.setMinWidth(25.0);

		HBox buttonHolder = new HBox(startPauseButton, spaceLeftLabel, speedBox, spaceRightLabel, clearButton);
		buttonHolder.setAlignment(Pos.CENTER);

		// To put in the center
		// the points-canvas represents all points that were generated to estimate pi.
		canvasPoints = new Canvas(CANVAS_SIZE, CANVAS_SIZE);
		gcPoints = canvasPoints.getGraphicsContext2D();
		gcPoints.setFill(Color.LIGHTSEAGREEN);
		gcPoints.fillRect(0, 0, CANVAS_SIZE, CANVAS_SIZE);
		// the pi-canvas shows how close the estimated pi is to the real pi.
		canvasPi = new Canvas(CANVAS_SIZE, PI_CANVAS_SIZE_HEIGHT);
		resetCanvasPi();

		VBox canvas = new VBox(canvasPoints, canvasPi);

		// To put on the top
		estimatedPiLabel = new Label("Estimation of Pi: ");
		estimatedPiValue = new Label();
		HBox piBox = new HBox(estimatedPiLabel, estimatedPiValue);
		noPointsLabel = new Label("Number of Points: ");
		noPointsValue = new Label("");
		HBox pointBox = new HBox(noPointsLabel, noPointsValue);
		spaceUpsideLabel = new Label();
		spaceUpsideLabel.setMinHeight(8.0);
		spaceUpsideLabel.setMaxHeight(8.0);
		spaceMiddleLabel = new Label();
		spaceMiddleLabel.setMinHeight(5.0);
		spaceMiddleLabel.setMaxHeight(5.0);
		spaceDownsideLabel = new Label();
		spaceDownsideLabel.setMinHeight(15.0);
		spaceDownsideLabel.setMaxHeight(15.0);
		VBox top = new VBox(spaceUpsideLabel, pointBox, spaceMiddleLabel, piBox, spaceDownsideLabel);

		// Puts everything together
		BorderPane pane = new BorderPane();
		pane.setTop(top);
		pane.setCenter(canvas);
		pane.setBottom(buttonHolder);

		Scene scene = new Scene(pane);
		stage.setScene(scene);
	}

	/**
	 * This method sets up the initial appearance of the pi-canvas, without showing
	 * pi.
	 */
	private void resetCanvasPi() {
		xPositionPi = 0;
		gcPi = canvasPi.getGraphicsContext2D();
		gcPi.setFill(Color.LIGHTGRAY);
		gcPi.fillRect(0, 0, CANVAS_SIZE, PI_CANVAS_SIZE_HEIGHT);
		for (int i = 0; i < CANVAS_SIZE; i++) {
			gcPi.getPixelWriter().setColor(i, PI_LINE_Y_POSITION, Color.RED);
		}
		gcPi.setFill(Color.LIGHTSEAGREEN);
	}

	/**
	 * This method sets the whole GUI to its initial appearance, that means the
	 * appearance before any pi-simulation has been started.
	 */
	public void clear() {
		gcPoints.fillRect(0, 0, CANVAS_SIZE, CANVAS_SIZE);
		estimatedPiValue.setText("");
		noPointsValue.setText("");
		resetCanvasPi();
	}

	/**
	 * Method to initiate the presentation of the GUI
	 */
	public void start() {
		// It is called by the PiSimulatorClass to initiate the pop up of the pi
		// simulation presentation window
		stage.show();
	}

	/**
	 * Method to close the GUI-window
	 */
	public void stop() {
		stage.hide();
	}

	@Override
	public void update(Observable o, Object arg) {
		if (o == model) {
			Platform.runLater(() -> updateView(arg));
		} else {
			System.out.println("Unknown model");
		}
	}

	/**
	 * This method sets all the not fixed parameters in the view to their actual
	 * state. It is triggered when the observable uses the update-Method to send the
	 * information about a newly generated point that has to be additionally
	 * represented in the view.
	 * 
	 * @param arg
	 *            of the type Point. It is the object that is send by the
	 *            observable, that means the model.
	 */
	private void updateView(Object arg) {
		if (arg != null && arg instanceof Point) {
			Point point = (Point) arg;
			gcPoints.getPixelWriter().setColor((int) (point.x * CANVAS_SIZE), (int) (point.y * CANVAS_SIZE),
					point.isWithinCircle() ? Color.BLUE : Color.YELLOW);
			estimatedPiValue.setText(Double.toString(model.estimatePi()));
			noPointsValue.setText(Integer.toString(model.getTotalPoints()));
			if (xPositionPi < CANVAS_SIZE) {
				int yPositionPi = (int) ((PI_LINE_Y_POSITION) - (model.estimatePi() - Math.PI) * 250d);
				gcPi.fillRect(xPositionPi, yPositionPi, 1, 3);
				xPositionPi++;
			} else {
				resetCanvasPi();
			}
		}
	}
}
