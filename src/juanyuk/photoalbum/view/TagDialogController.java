package juanyuk.photoalbum.view;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import juanyuk.photoalbum.PhotoAlbum;
import juanyuk.photoalbum.model.Photo;
import juanyuk.photoalbum.model.Tag;

/**
 * Controller for adding a new tag dialog.
 * @author yuky, jmm754
 */
public class TagDialogController {
	
	private PhotoAlbum photoAlbum;
	private Stage mStage;
	private Photo mPhoto;
	
	@FXML
	private TextField typeTextField;
	@FXML
	private TextField valueTextField;
	
	/**
	 * Method to call when the "Submit" button is clicked.
	 * Both fields must be filled in order to add a tag.
	 */
	public void submitButtonClicked(){
		if (typeTextField.getText().equals("") || valueTextField.getText().equals("")){
			showErrorDialog("Fields cannot be blank.");
			return;
		}
		mPhoto.addTag(new Tag(typeTextField.getText(), valueTextField.getText()));
		cancelButtonClicked();
	}
	
	/**
	 * Method to call when the "Cancel" button is clicked.
	 * Closes the dialog.
	 */
	public void cancelButtonClicked(){
		mStage.close();
	}
	
	/**
	 * Sets the stage of this controller.
	 * @param stage
	 */
	public void setStage(Stage stage){
		this.mStage = stage;
	}
	
	/**
	 * Sets the photo for this controller.
	 * @param photo
	 */
	public void setPhoto(Photo photo){
		this.mPhoto = photo;
	}
	
	/**
	 * Returns the photo for this controller.
	 * @return Photo photo for this controller
	 */
	public Photo getPhoto(){
		return this.mPhoto;
	}
	
	/**
	 * Sets the main application for this controller.
	 * @param photoAlbum main application
	 */
	public void setMainApp(PhotoAlbum photoAlbum){
		this.photoAlbum = photoAlbum;
	}
	
	/**
	 * Shows error dialog with specified message.
	 * @param message message to show on error dialog
	 */
	private void showErrorDialog(String message) {
		try{
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(PhotoAlbum.class.getResource("view/ErrorDialog.fxml"));
			AnchorPane addDialog = (AnchorPane) loader.load();
			
			//Create the stage for the dialog
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Oops!");
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(this.photoAlbum.getPrimaryStage());
			Scene scene = new Scene(addDialog);
			dialogStage.setScene(scene);
			dialogStage.setResizable(false);
			
			//Set the Controller
			ErrorDialogController controller = loader.getController();
			controller.setStage(dialogStage);
			controller.showMessage(message);
			
			
			dialogStage.showAndWait();
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
	/**
	 * Initializes the layout of the dialog.
	 * Sets the focus on the text field.
	 */
	public void init(){
		typeTextField.requestFocus();
	}
}
