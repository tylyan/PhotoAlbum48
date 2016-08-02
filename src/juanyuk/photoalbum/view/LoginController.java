package juanyuk.photoalbum.view;

import java.util.ArrayList;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import juanyuk.photoalbum.PhotoAlbum;
import juanyuk.photoalbum.model.User;

/**
 * Controller for the login window.
 * @author yuky, jmm754
 */
public class LoginController extends Controller{
	
	private ArrayList<User> userList;
	
	@FXML
	private TextField usernameTextField;
	@FXML
	private Button loginButton;
	@FXML
	private Label statusLabel;
	
	/**
	 * Method to call when "Login" is clicked.
	 * Checks the textfield for input, then checks if input is 'admin'
	 * or normal user.
	 * Shows appropriate message if user is not found.
	 * Starts the Album Window with the specified user if user is found.
	 */
	public void login(){
		// TextField must be filled out
		if (usernameTextField.getText().equals("")){
			showMessage("Please specify a username.");
			return;
		}
		String input = usernameTextField.getText().toLowerCase();
		
		// Admin log in
		if (input.equals("admin")){
			// System.out.println("Admin logging in");
			// Start window admin
			// How to end this window and start another from main app?
			// this.getMainApp().setCurrUser(userList.get(userList.indexOf("admin")))
			this.getMainApp().setCurrUser(null);
			this.getMainApp().startWindow(PhotoAlbum.ADMIN_WINDOW, userList, 0);
			return;
		}
		// User does not exist
		for (User user : userList){
			if (user.getName().equals(input)){
				// System.out.println("User " + input + " logging in.");
				this.getMainApp().setCurrUser(user);
				this.getMainApp().startWindow(PhotoAlbum.ALBUM_WINDOW, user, 0);
				return;
			}
		}
		showMessage(input + " does not exist!");
		return;
	}

	/**
	 * Shows the specified message as feedback to the user.
	 * @param message message to show
	 */
	private void showMessage(String message){
		statusLabel.setText(message);
	}
	
	/**
	 * Initializes the layout for the login window.
	 * @param obj list of users to check input against
	 * @param index supplemental integer value for other controllers
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void init(Object obj, int index) {
		userList = (ArrayList<User>) obj;
		usernameTextField.requestFocus();
	}
}
