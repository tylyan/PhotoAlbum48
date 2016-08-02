package juanyuk.photoalbum.view;

import juanyuk.photoalbum.PhotoAlbum;

/**
 * Abstract controller class for different windows of the Photo Album.
 * @author yuky, jmm754
 */
public abstract class Controller {

	private PhotoAlbum photoAlbum;
	
	/**
	 * Sets the main application to the controller.
	 * @param photoAlbum main application
	 */
	public void setMainApp(PhotoAlbum photoAlbum){
		this.photoAlbum = photoAlbum;
	}
	
	/**
	 * Returns the main application for the controller.
	 * @return the main application for the controller
	 */
	public PhotoAlbum getMainApp(){
		return this.photoAlbum;
	}
	
	/**
	 * Initializes the window upon first call.
	 * @param obj supplemental object to be used with the controller
	 * @param index supplemental integer value to be used with the controller
	 */
	public abstract void init(Object obj, int index);
	
	/**
	 * Log out of the application, will return the application to the login screen with the user logged out.
	 */
	public void logout(){
		photoAlbum.setCurrUser(null);
		photoAlbum.startWindow(PhotoAlbum.LOGIN_WINDOW, null, 0);
	}
}
