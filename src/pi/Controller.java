package pi;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

public class Controller {

	Model model;
	View view;

	public Controller(Model model, View view) {
		this.model = model;
		this.view = view;

		view.startPauseButton.setOnAction((event) -> {
			model.startStop();
			if (view.startPauseButton.getText().equals("start")) {
				view.startPauseButton.setText("pause");
				view.clearButton.setDisable(true);
			} else {
				view.startPauseButton.setText("start");
				view.clearButton.setDisable(false);
			}
		});

		view.speedSlider.valueProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				model.setSpeed(newValue);

			}
		});

		view.clearButton.setOnAction((event) -> {
			model.clear();
			view.clear();
		});

	}
}
