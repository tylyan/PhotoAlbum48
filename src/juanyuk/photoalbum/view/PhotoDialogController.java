package juanyuk.photoalbum.view;

import java.io.File;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import juanyuk.photoalbum.model.Photo;

/**
 * Controller for adding a new photo.
 * @author yuky, jmm754
 */
public class PhotoDialogController {
	
	private Stage mStage;
	private Photo mPhoto;
	private File file;
	private int mode;
	
	@FXML
	private Button submitButton;
	@FXML
	private Button cancelButton;
	@FXML
	private TextField captionTextField;
	@FXML
	private Label messageLabel;
	
	/**
	 * Method to call when the "Submit" button is clicked.
	 * Depending on the mode, will either create a new photo
	 * or rename a photo's caption.
	 */
	public void submitButtonClicked(){
		if (mode == PhotoController.EDIT){
			mPhoto.setCaption(captionTextField.getText());
			cancelButtonClicked();
			return;
		}
		else{
			mPhoto = new Photo(file, captionTextField.getText());
			mStage.close();
		}
	}
	
	/**
	 * Method to call when the "Cancel" button is clicked.
	 * Closes the dialog.
	 */
	public void cancelButtonClicked(){
		mStage.close();
	}
	
	/**
	 * Sets the stage for this controller.
	 * @param stage
	 */
	public void setStage(Stage stage){
		this.mStage = stage;
	}
	
	/**
	 * Sets the file for this controller.
	 * @param file
	 */
	public void setFile(File file){
		this.file = file;
	}
	
	/**
	 * Sets the Photo for this controller.
	 * @param photo
	 */
	public void setPhoto(Photo photo){
		this.mPhoto = photo;
	}
	
	/**
	 * Returns the Photo for this controller.
	 * @return Photo photo for this controller
	 */
	public Photo getPhoto(){
		return this.mPhoto;
	}
	
	/**
	 * Sets the mode for this controller.
	 * @param mode specifies whether the dialog is creating a new photo
	 * or renaming a photo's caption
	 */
	public void setMode(int mode){
		this.mode = mode;
	}
	
	/**
	 * Initializes the layout for the dialog depending on what mode it is in.
	 */
	public void init(){
		captionTextField.requestFocus();
		if (mode == PhotoController.EDIT){
			submitButton.setAccessibleHelp("Submit");
			captionTextField.setText(mPhoto.getCaption());
			captionTextField.selectAll();
			captionTextField.setVisible(true);
			messageLabel.setText("Set caption");
			cancelButton.setVisible(true);
		}
		else{
			submitButton.setAccessibleHelp("Submit");
			captionTextField.setVisible(true);
			messageLabel.setText("Add a caption");
			cancelButton.setVisible(true);
		}
	}
}
