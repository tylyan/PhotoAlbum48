package         juanyuk.photoalbum.view;

import java.util.ArrayList;
import java.util.Collections;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import juanyuk.photoalbum.PhotoAlbum;
import juanyuk.photoalbum.model.Album;

/**
 * Controller for adding a new album dialog.
 * @author yuky, jmm754
 */
public class AlbumDialogController {
	
	public static final int ADD = -1;
	public static final int SEARCH = -2;
	
	private AlbumController albumController;
	private PhotoController photoController;
	private int index;
	private Album album;
	private Stage mStage;
	
	@FXML
	private TextField albumTextField;
	@FXML
	private Label statusLabel;
	
	/**
	 * Method to call when "Submit" button is clicked.
	 * Adds a new user if desired album name does not already exist.
	 */
	public void submitButtonClicked(){
		ArrayList<Album> albumList = null;
		if (albumController == null)
			albumList = photoController.getMainApp().getCurrUser().getAlbums();
		else
			albumList = albumController.getUser().getAlbums();
		
		String input = albumTextField.getText();
		if (input.equals("")){
			// System.out.println("No album name specified");
		}else{
			for (Album album : albumList){
				if (album.getIdName().equals(input.toLowerCase())){
					showMessage("Album " + input + " already exists!");
					return;
				}
			}
			if (index == ADD){
				// System.out.println("Adding album " + input);
				albumList.add(new Album(input));
				
			}else if(index == SEARCH){
				album.setName(input);
				albumList.add(album);
				photoController.getMainApp().startWindow(PhotoAlbum.ALBUM_WINDOW, photoController.getMainApp().getCurrUser(), 0);
			}else{
				albumList.get(index).setName(input);
			}
			Collections.sort(albumList);
			mStage.close();
		}
	}

	/**
	 * Method to call when "Cancel" button is clicked.
	 * Closes the dialog and does nothing.
	 */
	public void cancelButtonClicked(){
		// System.out.println("Add album cancelled");
		mStage.close();
	}
	
	/**
	 * Updates the status label with appropriate message.
	 * @param message message to show
	 */
	public void showMessage(String message){
		statusLabel.setText(message);
	}
	
	/**
	 * Sets the albumController.
	 * @param albumController album controller to set
	 */
	public void setAC(AlbumController albumController){
		this.albumController = albumController;
		albumTextField.requestFocus();
	} 
	
	/**
	 * Sets the photoController.
	 * @param photoController photo controller to set
	 */
	public void setPC(PhotoController photoController){
		this.photoController = photoController;
		albumTextField.requestFocus();
	}
	
	/**
	 * Sets the current index of the controller.
	 * Depending on the index, the album name TextField will be prefilled.
	 * @param index index of album to rename
	 */
	public void setIndex(int index){
		this.index = index;
		if (index == SEARCH){
			albumTextField.setText(album.getName());
			albumTextField.selectAll();
		}else if (index != ADD){
			albumTextField.setText(albumController.getUser().getAlbum(index).getName());
			albumTextField.selectAll();
		}
	}
	
	/**
	 * Sets the current working album for this controller.
	 * @param album album to set controller to
	 */
	public void setAlbum(Album album){
		this.album = album;
	}
	/**
	 * Sets the stage for this controller.
	 * @param stage stage to set controller to
	 */
	public void setStage(Stage stage){
		this.mStage = stage;
	}
}
