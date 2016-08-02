package juanyuk.photoalbum.view.windows;

/**
 * Abstract class for a PhotoAlbum Window.
 * Each Window will have a controller and main application associated to it.
 */

import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import juanyuk.photoalbum.PhotoAlbum;
import juanyuk.photoalbum.view.Controller;

public abstract class PAWindow {

	private Controller controller;
	private Pane mPane;
	private PhotoAlbum photoAlbum;
	
	/**
	 * Starts the window on a given stage.
	 * @param stage
	 */
	public void start(Stage stage){
		stage.setScene(this.getScene());
		stage.show();
	}
	
	/**
	 * Returns the scene.
	 * @return Scene scene
	 */
	public Scene getScene(){
		return new Scene(mPane);
	}
	
	/**
	 * Sets the pane for the window.
	 * @param pane
	 */
	public void setPane(Pane pane){
		this.mPane = pane;
	}
	
	/**
	 * Returns the controller for the current window.
	 * @return the controller for the current window
	 */
	public Controller getController(){
		return controller;
	}
	
	/**
	 * Sets the controller for the current window.
	 * @param controller
	 */
	public void setController(Controller controller){
		this.controller = controller;
	}

	/**
	 * Returns the main application for the window.
	 * @return the main application for the window
	 */
	public PhotoAlbum getPhotoAlbum() {
		return photoAlbum;
	}

	/**
	 * Sets the main application for the window.
	 * @param photoAlbum main application
	 */
	public void setPhotoAlbum(PhotoAlbum photoAlbum) {
		this.photoAlbum = photoAlbum;
	}
}
