package juanyuk.photoalbum.view;

import java.io.File;
import java.io.IOException;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.TilePane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import juanyuk.photoalbum.PhotoAlbum;
import juanyuk.photoalbum.model.Album;
import juanyuk.photoalbum.model.Photo;

/**
 * Controller for the Photo Window.
 * @author yuky, jmm754
 */
public class PhotoController extends Controller{
	
	public static final int CAPTION = 0;
	public static final int EDIT = 1;
	
	private Album album;
	private Photo selectedPhoto;
	private int currIndex;
	
	@FXML
	private TilePane tile;
	@FXML
	private ScrollPane scrollPane;
	@FXML
	private Label captionLabel;
	@FXML
	private Label albumLabel;
	@FXML
	private Button createAlbumButton;

	/**
	 * Method to call when the "Create Album" button is clicked.
	 * The button is only visible if the Photo window was entered via search results.
	 * Shows the add new album dialog to rename the new album.
	 */
	public void createAlbumClicked(){
		showAddNewAlbumDialog(AlbumDialogController.SEARCH);
	}
	
	/**
	 * Method to call when the "Back" button is clicked.
	 * Starts the Album window with the current user.
	 */
	public void backButtonClicked(){
		this.getMainApp().startWindow(PhotoAlbum.ALBUM_WINDOW, this.getMainApp().getCurrUser(), 0);
	}
	
	/**
	 * Method to call when the "Slideshow" button is clicked.
	 * Starts the slideshow button with the selected photo,
	 * displays an error dialog if no photo was selected.
	 */
	public void slideshowButtonClicked(){
		if (album.getNumPhotos() == 0){
			showErrorDialog("No photos to display.");
			return;
		}
		if (currIndex == -1){
			currIndex = 0;
		}
		this.getMainApp().startWindow(PhotoAlbum.SLIDESHOW_WINDOW, album, currIndex);
	}
	
	/**
	 * Method to call when the "Add" button is clicked.
	 * Opens up a file chooser and checks if selected file is an invalid photo format.
	 * Also checks if the album already contains the selected photo.
	 * An error dialog is shown if the above conditions are met.
	 * Adds the selected photo to the album upon success.
	 */
	public void addButtonClicked(){
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Choose a Photo");
		File selectedFile = fileChooser.showOpenDialog(this.getMainApp().getPrimaryStage());
		if (selectedFile != null){
			// get the file extension
			String ext = "";
			int i = selectedFile.getName().indexOf('.');
			if (i > 0)
				ext = selectedFile.getName().substring(i + 1).toLowerCase();
			
			if (!ext.equals("jpg") && !ext.equals("png") && !ext.equals("jpeg") && !ext.equals("gif") && !ext.equals("bmp")){
				showErrorDialog("File type must be jpg, jpeg, png, gif, or bmp only please.");
			}else{
				// System.out.println("Photo chosen: ");
				// System.out.println(selectedFile.getAbsolutePath());
				Photo newPhoto = showAddNewPhotoDialog(selectedFile, CAPTION);
				for (Photo p : album.getPhotos()){
					if (p.equals(newPhoto)){
						showErrorDialog("Photo already exists in this album.");
						return;
					}
				}
				if (newPhoto == null) return;
				album.addPhoto(newPhoto);
				updatePhotoView();
			}
		}
	}

	/**
	 * Shows a dialog to add or edit album.
	 * @param index mode specified either for adding or renaming album
	 */
	private void showAddNewAlbumDialog(int index) {
		try{
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(PhotoAlbum.class.getResource("view/AlbumDialog.fxml"));
			AnchorPane addDialog = (AnchorPane) loader.load();
			
			//Create the stage for the dialog
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Add or Edit Album");
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(this.getMainApp().getPrimaryStage());
			Scene scene = new Scene(addDialog);
			dialogStage.setScene(scene);
			dialogStage.setResizable(false);
			
			//Set the Controller
			AlbumDialogController controller = loader.getController();
			controller.setPC(this);
			controller.setAlbum(this.album);
			controller.setStage(dialogStage);
			controller.setIndex(index);
			
			
			dialogStage.showAndWait();
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
	/**
	 * Updates the photo view with all of the photos in the current open album.
	 * Also attaches on-click handlers for when a photo is clicked on.
	 */
	private void updatePhotoView() {
		// TODO Auto-generated method stub
		tile.getChildren().clear();
		// System.out.println("updating photo view");
		for(Photo photo: album.getPhotos()){
			ImageView imageView = new ImageView(
					new Image(photo.getFile().toURI().toString(), 110, 110, false, true));
			imageView.setUserData(photo);
			imageView.setFitWidth(110);
			imageView.setPreserveRatio(true);
			imageView.setSmooth(true);
            imageView.setCache(true);
            imageView.setFocusTraversable(true);
            tile.getChildren().add(imageView);
            imageView.setOnMouseClicked(new EventHandler<MouseEvent>(){

				@Override
				public void handle(MouseEvent arg0) {
					for (Node n : tile.getChildren()){
						n.setEffect(null);
					}
					imageView.setEffect(new DropShadow(20, Color.DODGERBLUE));
					selectedPhoto = (Photo) imageView.getUserData();
					currIndex = album.indexOf(selectedPhoto);
					captionLabel.setText(selectedPhoto.getCaption());
					// System.out.println(selectedPhoto);
				}
            	
            });
		}
	}

	/**
	 * Method to call when the "Delete" button is clicked.
	 * Removes the selected photo from the album,
	 * error dialog is shown if no photo was selected.
	 */
	public void deleteButtonClicked(){
		if (currIndex == -1){
			showErrorDialog("No photo selected.");
			return;
		}
		if (selectedPhoto == null) return;
		album.deletePhoto(selectedPhoto);
		updatePhotoView();
	}
	
	/**
	 * Method to call when the "Edit" button is clicked.
	 * Shows the edit photo dialog for the selected photo,
	 * error dialog is shown if no photo was selected.
	 */
	public void editButtonClicked(){
		if (currIndex == -1){
			showErrorDialog("No photo selected");
			return;
		}
		if (selectedPhoto == null) return;
		showEditPhotoDialog(selectedPhoto, album);
	}
	
	/**
	 * Shows the edit photo dialog for a given photo and album that the photos in.
	 * @param photo photo to edit
	 * @param album album that the photo belongs to
	 * @return Photo that was edited
	 */
	private Photo showEditPhotoDialog(Photo photo, Album album) {
		try{
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(PhotoAlbum.class.getResource("view/EditPhotoDialog.fxml"));
			AnchorPane addDialog = (AnchorPane) loader.load();
			
			//Create the stage for the dialog
			Stage dialogStage = new Stage();
			
			dialogStage.setTitle("Edit Photo");
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(this.getMainApp().getPrimaryStage());
			Scene scene = new Scene(addDialog);
			dialogStage.setScene(scene);
			dialogStage.setResizable(false);
			
			//Set the Controller
			EditPhotoController controller = loader.getController();
			controller.setSource(EditPhotoController.PHOTO_WINDOW);
			controller.setPC(this);
			controller.setMainApp(this.getMainApp());
			controller.setStage(dialogStage);
			controller.setPhoto(photo);
			controller.setAlbum(album);
			controller.init();
			dialogStage.showAndWait();
			captionLabel.setText(photo.getCaption());
			
			return controller.getPhoto();
		}catch(IOException e){
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Shows the add new photo dialog, also used to rename photo captions.
	 * @param file file to add new photo, unused if editing photo caption
	 * @param mode specified mode to either add new photo or to rename photo caption
	 * @return Photo that was added or edited
	 */
	private Photo showAddNewPhotoDialog(File file, int mode) {
		try{
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(PhotoAlbum.class.getResource("view/AddPhotoDialog.fxml"));
			AnchorPane addDialog = (AnchorPane) loader.load();
			
			//Create the stage for the dialog
			Stage dialogStage = new Stage();
			if (mode == CAPTION)
				dialogStage.setTitle("Adding Photo");
			else
				dialogStage.setTitle("Oops!");
			
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(this.getMainApp().getPrimaryStage());
			Scene scene = new Scene(addDialog);
			dialogStage.setScene(scene);
			dialogStage.setResizable(false);
			
			//Set the Controller
			PhotoDialogController controller = loader.getController();
			controller.setMode(mode);
			controller.init();
			controller.setStage(dialogStage);
			controller.setFile(file);
			dialogStage.showAndWait();
			
			return controller.getPhoto();
		}catch(IOException e){
			e.printStackTrace();
			return null;
		}
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
			dialogStage.initOwner(this.getMainApp().getPrimaryStage());
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
	 * Initializes the Photo window upon first start up.
	 * @param obj album that was opened 
	 * @param index mode that the photo window was opened
	 * either from a normal album open, or a search result
	 */
	@Override
	public void init(Object obj, int index) {
		tile.setPrefSize(scrollPane.getWidth(), scrollPane.getHeight());
		if (index == AlbumController.NORMAL)
			createAlbumButton.setVisible(false);
		else
			createAlbumButton.setVisible(true);
		
		album = (Album) obj;
		currIndex = -1;
		updatePhotoView();
		
		if (album != null){
			albumLabel.setText("Album: " + album.getName());
		}
		
		captionLabel.setText("Caption");
		
		// System.out.println("Showing photos for album " + album);
	}
}
