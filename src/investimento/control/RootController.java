package investimento.control;

import investimento.MainApp;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class RootController {

	@FXML
	private void changeScreen(ActionEvent event) {

		String cmd = ((Button) event.getSource()).getText();

		MainApp.changeScreen(cmd);

	}

}
