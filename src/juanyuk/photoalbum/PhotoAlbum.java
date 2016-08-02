package juanyuk.photoalbum;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import juanyuk.photoalbum.model.User;
import juanyuk.photoalbum.view.Controller;
import juanyuk.photoalbum.view.windows.AdminWindow;
import juanyuk.photoalbum.view.windows.AlbumWindow;
import juanyuk.photoalbum.view.windows.LoginWindow;
import juanyuk.photoalbum.view.windows.PAWindow;
import juanyuk.photoalbum.view.windows.PhotoWindow;
import juanyuk.photoalbum.view.windows.SlideshowWindow;

/**
 * Main application class that runs the photo album.
 * @author yuky, jmm754
 */
public class PhotoAlbum extends Application {
	
	private static final String dir = "data";			// Use this for serialization later
	private static final String filename = "data.dat";	// Use this for serialization later
	// These values below are used to transition between windows, use them in other controllers 
	// when calling startWindow();
	public static final int LOGIN_WINDOW = 0;
	public static final int ADMIN_WINDOW = 1;
	public static final int ALBUM_WINDOW = 2;
	public static final int PHOTO_WINDOW = 3;
	public static final int SLIDESHOW_WINDOW = 4;
	
	// Fields
	private Stage mPrimaryStage;
	private Controller controller;  	// Controller for current window running
	private PAWindow currWindow;		// Current window that is running
	private PAWindow[] windows;			// Collection of all windows
	private ArrayList<User> userList;	// User list (testing for now, need to remember to initialize later)
	private User currUser;				// User that is currenty logged in
	
	@Override
	public void start(Stage primaryStage) {
		this.mPrimaryStage = primaryStage;
		this.mPrimaryStage.setTitle("Photo Album");
		initAll();
		
		//initRootLayout();
		startWindow(LOGIN_WINDOW, userList, 0);
	}
	
	/**
	 * Initializes all required resources before moving onto anything else
	 */
	public void initAll(){
		this.mPrimaryStage.setOnCloseRequest(new EventHandler<WindowEvent>(){
			@Override
			public void handle(WindowEvent event) {
				saveData();
			}
		});
		initWindows();
		loadData();
	}
	
	/**
	 * Initializes all instances of windows and stores them in the windows array
	 */
	public void initWindows(){
		windows = new PAWindow[5];
		windows[LOGIN_WINDOW] = new LoginWindow(this);
		windows[ADMIN_WINDOW] = new AdminWindow(this);
		windows[ALBUM_WINDOW] = new AlbumWindow(this);
		windows[PHOTO_WINDOW] = new PhotoWindow(this);
		windows[SLIDESHOW_WINDOW] = new SlideshowWindow(this);
	}
	
	/**
	 * Loads saved data
	 */
	@SuppressWarnings("unchecked")
	public void loadData(){
		try{
			FileInputStream fi = new FileInputStream(dir + File.separator + filename);
			ObjectInputStream in = new ObjectInputStream(fi);
			userList = (ArrayList<User>) in.readObject();
			if (userList == null){
				userList = new ArrayList<User>();
			}
			in.close();
			fi.close();
			System.out.println("User list loaded");
		}catch(FileNotFoundException e){
			userList = new ArrayList<User>();
			System.out.println("User list created");
		}catch(IOException e){
			e.printStackTrace();
		}catch(ClassNotFoundException e){
			e.printStackTrace();
		}
	}
	
	/**
	 * Saves data, writes to data/data.dat
	 */
	public void saveData(){
		File file = new File(dir);
		if (!file.exists()){
			file.mkdir();
		}
		
		try{
			FileOutputStream fo = new FileOutputStream(dir + File.separator + filename);
			ObjectOutputStream out = new ObjectOutputStream(fo);
			out.writeObject(userList);
			out.close();
			fo.close();
			System.out.println("User list saved.");
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
	/**
	 * Starts a certain window based off of the given index
	 * @param i Selection of which window to start
	 * 			0 - Login  Window
	 * 			1 - Admin Window
	 * 			2 - Albums Window
	 * 			3 - Photos Window
	 * 			4 - SlideShow Window
	 * 
	 * This method should not be called by passing in an actual integer,
	 * it should only be called by the Controller classes using the final
	 * values listed in this class in order to change the window.
	 */
	public void startWindow(int window, Object obj, int index){
		if (window < 0 || window > 4) return;
		currWindow = windows[window];
		showWindow(obj, index);
	}
	
	/**
	 * Shows the actual window to the user
	 */
	public void showWindow(Object obj, int index){
		currWindow.start(mPrimaryStage);
		controller = currWindow.getController();
		controller.setMainApp(this);
		controller.init(obj, index);
	}
	
	/**
	 * Returns the list of users associated to the application.
	 * @return List of users in an ArrayList
	 */
	public ArrayList<User> getUserList(){
		return userList;
	}

	/**
	 * Returns the user that is currently logged in to the application.
	 * @return the user that is currently logged in to the application
	 */
	public User getCurrUser() {
		return currUser;
	}

	/**
	 * Sets the user that is currently logged into the application.
	 * This should be set only when a user logs in through the log in page,
	 * so the only place this method should be called with an actual username
	 * is in the LoginController.
	 * In any window, when a user logs out, the current user should be set to null.
	 * @param currUser
	 */
	public void setCurrUser(User currUser) {
		this.currUser = currUser;
	}
	
	/**
	 * Returns the primary stage so other classes may interact with the main stage.
	 * @return the primary stage
	 */
	public Stage getPrimaryStage(){
		return mPrimaryStage;
	}
	
	/**
	 * Main method that runs the actual program.
	 * @param args
	 */
	public static void main(String[] args) {
		launch(args);
	}
}
