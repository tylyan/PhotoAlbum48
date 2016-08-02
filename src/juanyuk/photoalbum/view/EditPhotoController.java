package juanyuk.photoalbum.view;

import java.io.File;
import java.io.IOException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import juanyuk.photoalbum.PhotoAlbum;
import juanyuk.photoalbum.model.Album;
import juanyuk.photoalbum.model.Photo;
import juanyuk.photoalbum.model.Tag;

/**
 * Controller for the Edit Photo dialog.
 * @author yuky, jmm754
 */
public class EditPhotoController {
	
	public static final int PHOTO_WINDOW = 0;
	public static final int SLIDESHOW_WINDOW = 1;
	
	private ObservableList<Tag> tagsObsList;
	private ObservableList<Album> albumObsList;
	private PhotoController photoController;
	private SlideshowController slideshowController;
	private Stage mStage;
	private Album mAlbum;
	private Photo mPhoto;
	private int source;
	private PhotoAlbum photoAlbum;	
	
	@FXML
	private Label captionLabel;
	@FXML
	private ListView<Tag> tagListView;
	@FXML
	private ComboBox<Album> albumsComboBox;
	

	/**
	 * Method to call when "Add" Tag button is clicked.
	 * Shows the add tag dialog.
	 */
	public void addTagButtonClicked(){
		showAddTagDialog();
	} 
	
	/**
	 * Method to call when "Delete" Tag button is clicked.
	 * Deletes selected Tag, does nothing if no selection is made.
	 */
	public void deleteTagButtonClicked(){
		int index = tagListView.getSelectionModel().getSelectedIndex();
		if (index == -1) return;
		mPhoto.deleteTag(index);
		updateTagList();
	}
	
	/**
	 * Method to call when "Move to..." button is clicked.
	 * Does nothing if no album is selected from the combo box.
	 * Checks if the move was successful, will transition to either the photo window or slideshow window
	 * depending on if the photo moved was the last photo in the album.
	 */
	public void moveButtonClicked(){
		Album destAlbum = albumsComboBox.getValue();
		if (destAlbum == null) return;
		boolean check = this.mAlbum.moveTo(mPhoto, destAlbum);
		if (check){
			if (source == PHOTO_WINDOW)
				photoController.init(mAlbum, 0);
			else{
				if (mAlbum.getNumPhotos() == 0){
					slideshowController.getMainApp().startWindow(PhotoAlbum.PHOTO_WINDOW, mAlbum, AlbumController.NORMAL);
				}else{
					slideshowController.init(mAlbum, 0);
				}
				
			}
			mStage.close();
		}else
			showErrorDialog("Photo already exists in that album!");
	}
	
	/**
	 * Method to call when the "Change Caption" button is clicked.
	 * Shows a dialog to edit the photo's caption.
	 */
	public void captionButtonClicked(){
		showAddNewPhotoDialog(null, PhotoController.EDIT);
		init();
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
			dialogStage.initOwner(photoAlbum.getPrimaryStage());
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
	 * Shows dialog to add or edit a photo's caption.
	 * @param file file to add, null when editing photo's caption
	 * @param mode specified mode to notify the dialog whether to add or edit a photo
	 * @return Photo photo that was added or edited
	 */
	private Photo showAddNewPhotoDialog(File file, int mode) {
		try{
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(PhotoAlbum.class.getResource("view/AddPhotoDialog.fxml"));
			AnchorPane addDialog = (AnchorPane) loader.load();
			
			//Create the stage for the dialog
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Edit Caption");
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(photoAlbum.getPrimaryStage());
			Scene scene = new Scene(addDialog);
			dialogStage.setScene(scene);
			dialogStage.setResizable(false);
			
			//Set the Controller
			PhotoDialogController controller = loader.getController();
			controller.setPhoto(mPhoto);
			controller.setMode(mode);
			controller.setStage(dialogStage);
			controller.init();
			
			dialogStage.showAndWait();
			
			return controller.getPhoto();
		}catch(IOException e){
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Shows the add tag dialog.
	 * @return Photo photo that tag was added to
	 */
	private Photo showAddTagDialog() {
		try{
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(PhotoAlbum.class.getResource("view/AddTagDialog.fxml"));
			AnchorPane addDialog = (AnchorPane) loader.load();
			
			//Create the stage for the dialog
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Edit Caption");
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(photoAlbum.getPrimaryStage());
			Scene scene = new Scene(addDialog);
			dialogStage.setScene(scene);
			dialogStage.setResizable(false);
			
			//Set the Controller
			TagDialogController controller = loader.getController();
			controller.setPhoto(mPhoto);
			controller.setMainApp(this.photoAlbum);
			controller.setStage(dialogStage);
			controller.init();
			
			dialogStage.showAndWait();
			updateTagList();
			return controller.getPhoto();
		}catch(IOException e){
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Sets the slideshow controller for this controller.
	 * @param slideshowController
	 */
	public void setSC(SlideshowController slideshowController){
		this.slideshowController = slideshowController;
	}
	
	/**
	 * Sets the photo controller for this controller
	 * @param photoController
	 */
	public void setPC(PhotoController photoController){
		this.photoController = photoController;
	}
	
	/**
	 * Sets the current album for this controller,
	 * used to photo from one album to another.
	 * @param album album of current working photo
	 */
	public void setAlbum(Album album){
		this.mAlbum = album;
	}
	
	/**
	 * Sets the stage for this controller.
	 * @param stage
	 */
	public void setStage(Stage stage){
		this.mStage = stage;
	}
	
	/**
	 * Sets the current working photo for this controller.
	 * @param photo
	 */
	public void setPhoto(Photo photo){
		this.mPhoto = photo;
	}
	
	/**
	 * Sets the source of what window called this controller.
	 * Source can either be PHOTO_WINDOW or SLIDESHOW_WINDOW 
	 * since both windows have the ability to edit a photo.
	 * @param source source that called this controller
	 */
	public void setSource(int source){
		this.source = source;
	}
	
	/**
	 * Returns the current working photo for this controller.
	 * @return photo that this controller is currently working on
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
	 * Populates the tag list for the current photo.
	 */
	public void updateTagList(){
		if (tagsObsList != null)
			tagsObsList.clear();
		tagsObsList = FXCollections.observableArrayList(mPhoto.getTags());
		tagListView.setItems(tagsObsList);
		if (mPhoto.getNumTags() > 0)
			tagListView.getSelectionModel().select(0);
		tagListView.requestFocus();
	}
	
	/**
	 * Initializes the layout, sets the caption to the Photo's caption,
	 * updates the tag list, and populates the combo box for albums.
	 */
	public void init(){
		if (mPhoto == null) return;
		captionLabel.setText(mPhoto.getCaption());
		updateTagList();
		albumObsList = FXCollections.observableArrayList(this.photoAlbum.getCurrUser().getAlbums());
		albumsComboBox.setItems(albumObsList);
	}
}
