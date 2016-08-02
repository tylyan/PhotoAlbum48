package juanyuk.photoalbum.view.windows;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import juanyuk.photoalbum.PhotoAlbum;

/**
 * Window for the album screen.
 * @author yuky, jmm754
 */
public class AlbumWindow extends PAWindow{
	
	private final static String VIEWPATH = "view/Album.fxml";
	
	/**
	 * Constructor for the AlbumWindow.
	 * @param photoAlbum main application to link window to
	 */
	public AlbumWindow(PhotoAlbum photoAlbum){
		this.setPhotoAlbum(photoAlbum);
	}
	
	/**
	 * Starts the album window on the stage.
	 * @param stage
	 */
	public void start(Stage stage){
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(PhotoAlbum.class.getResource(VIEWPATH));
		try {
			this.setPane((AnchorPane) loader.load());
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.setController(loader.getController());
		super.start(stage);
	}
}
