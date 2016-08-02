package juanyuk.photoalbum.view;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;

/**
 * Controller for the error dialog.
 * @author yuky, jmm754
 */
public class ErrorDialogController {

	private Stage mStage;
	
	@FXML
	private Label messageLabel;
	
	/**
	 * Method to call when the "Ok" button is clicked,
	 * closes the dialog.
	 */
	public void okButtonClicked(){
		mStage.close();
	}
	
	/**
	 * Updates the status label with appropriate message
	 * @param message message to show
	 */
	public void showMessage(String message){
		messageLabel.setText(message);
		messageLabel.setWrapText(true);
	}
	
	/**
	 * Sets the stage
	 * @param stage
	 */
	public void setStage(Stage stage){
		this.mStage = stage;
	}
}
