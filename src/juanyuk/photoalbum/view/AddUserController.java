package juanyuk.photoalbum.view;

import java.util.ArrayList;
import java.util.Collections;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import juanyuk.photoalbum.model.User;

/**
 * Controller used to add a new user.
 * @author yuky, jmm754
 */
public class AddUserController {
	
	private AdminController adminController;
	private Stage mStage;
	
	@FXML
	private TextField usernameTextField;
	@FXML
	private Label statusLabel;
	
	/**
	 * Method to call when "Add" button is clicked.
	 * Adds a new user if desired username does not already exist.
	 */
	public void addButtonClicked(){
		ArrayList<User> userList = adminController.getUsers();
		String input = usernameTextField.getText().toLowerCase();
		if (input.equals("")){
			System.out.println("No username specified");
		}else{
			for (User user : userList){
				if (user.getName().equals(input)){
					System.out.println("User exists");
					showMessage("User " + input + " already exists!");
					return;
				}
			}
			System.out.println("Adding user " + input);
			userList.add(new User(input));
			Collections.sort(userList);
			mStage.close();
		}
	}

	/**
	 * Method to call when "Cancel" button is clicked, closes the dialog.
	 */
	public void cancelButtonClicked(){
		System.out.println("Add user cancelled");
		mStage.close();
	}
	
	/**
	 * Updates the status label with appropriate message.
	 * @param message message to show the user
	 */
	public void showMessage(String message){
		statusLabel.setText(message);
	}
	
	/**
	 * Sets the controller.
	 * @param adminController controller to set
	 */
	public void setAC(AdminController adminController){
		this.adminController = adminController;
		usernameTextField.requestFocus();
	}
	
	/**
	 * Sets the stage.
	 * @param stage stage to set
	 */
	public void setStage(Stage stage){
		this.mStage = stage;
	}
}
