package juanyuk.photoalbum.view;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import juanyuk.photoalbum.PhotoAlbum;
import juanyuk.photoalbum.model.Album;
import juanyuk.photoalbum.model.Photo;
import juanyuk.photoalbum.model.Tag;

/**
 * Controller for the Slideshow window.
 * @author yuky, jmm754
 */
public class SlideshowController extends Controller {

	private Album mAlbum;
	private int currIndex;
	private Photo mPhoto;

	@FXML
	private Label captionLabel;
	@FXML
	private Label dateLabel;
	@FXML
	private Label tagsLabel;
	@FXML
	private Button backButton;
	@FXML
	private Button nextButton;
	@FXML
	private Button previousButton;
	@FXML
	private Button editButton;
	@FXML
	private AnchorPane anchor;
	@FXML
	private ImageView imageView;

	/**
	 * Method to call when "Back" button is clicked.
	 * Exits the slide show screen and returns to the photos screen.
	 */
	public void backButtonClicked() {
		this.getMainApp().startWindow(PhotoAlbum.PHOTO_WINDOW, mAlbum, 0);
		return;
	}

	/**
	 * Method to call when the "Previous" button is clicked.
	 * Starts the slideshow window with the photo that comes before the current displayed photo,
	 * if the current photo is the first photo in the album, then the slideshow displays the last photo in the album.
	 */
	public void previousButtonClicked() {
		int nextIndex = currIndex - 1;
		nextIndex = nextIndex < 0 ? mAlbum.getNumPhotos() - 1 : nextIndex;
		this.getMainApp().startWindow(PhotoAlbum.SLIDESHOW_WINDOW, mAlbum, nextIndex);
		return;
	}

	/**
	 * Method to call when the "Next" button is clicked.
	 * Starts the slideshow window with the photo that comes after the current displayed photo,
	 * if the current photo is the last photo in the album, then the slideshow displays the first photo in the album.
	 */
	public void nextButtonClicked() {
		int nextIndex = currIndex + 1;
		nextIndex = nextIndex > mAlbum.getNumPhotos() - 1 ? 0 : nextIndex;
		this.getMainApp().startWindow(PhotoAlbum.SLIDESHOW_WINDOW, mAlbum, nextIndex);
		return;
	}

	/**
	 * Method to call when the "Edit" button is clicked.
	 * Shows the show edit photo dialog.
	 */
	public void editButtonClicked(){
		if (mPhoto == null) return;
		showEditPhotoDialog(mPhoto, mAlbum);
	}
	
	/**
	 * Shows the edit photo dialog for the specified photo and album that the photo belongs in.
	 * @param photo photo to edit
	 * @param album album that the photo belongs to
	 * @return Photo photo that was edited
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
			controller.setSource(EditPhotoController.SLIDESHOW_WINDOW);
			controller.setSC(this);
			controller.setMainApp(this.getMainApp());
			controller.setStage(dialogStage);
			controller.setPhoto(photo);
			controller.setAlbum(album);
			controller.init();
			dialogStage.showAndWait();
			updateSlideshow();
			
			return controller.getPhoto();
		}catch(IOException e){
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Initializes the layout of the Slideshow Window.
	 * @param obj Album that the slideshow is displaying
	 * @param index index of the photo in the album
	 */
	@Override
	public void init(Object obj, int index) {
		// TODO Auto-generated method stub
		this.currIndex = index;
		this.mAlbum = (Album) obj;
		if (mAlbum.getNumPhotos() != 0)
			mPhoto = mAlbum.getPhoto(index);
		updateSlideshow();
		imageView.requestFocus();
	}

	/**
	 * Updates the slideshow image along with the details of the currently displayed photo.
	 */
	public void updateSlideshow() {
		if (mPhoto == null || currIndex == -1) return;
		if(mAlbum.getNumPhotos() == 0) return;
		mPhoto = mAlbum.getPhoto(currIndex);
		//imageView.setPreserveRatio(true);
		imageView.fitWidthProperty().bind(anchor.widthProperty());
		imageView.fitHeightProperty().bind(anchor.heightProperty());
		imageView.setX(100);
		imageView.setY(100);
		//imageView.setSmooth(true);
		//imageView.setCache(true);
		imageView.setImage(new Image(mPhoto.getFile().toURI().toString(), imageView.getFitWidth(), imageView.getFitHeight(), true, true));
		captionLabel.setText(mPhoto.getCaption());
		dateLabel.setText(mPhoto.getDateString());
		String tags = "";
		for (Tag t : mPhoto.getTags()){
			tags += t.toString() + ", ";
		}
		tags = tags.equals("") ? "No tags" : tags.substring(0, tags.length() - 2);
		tagsLabel.setText(tags);
	}
}
