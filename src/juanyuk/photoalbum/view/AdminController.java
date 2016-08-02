package juanyuk.photoalbum.view;

import java.io.IOException;
import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import juanyuk.photoalbum.PhotoAlbum;
import juanyuk.photoalbum.model.User;

/**
 * Controller for the Admin Window.
 * @author yuky, jmm754
 */
public class AdminController extends Controller {
	
	private ObservableList<User> obsList;
	private ArrayList<User> userList;
	
	@FXML
	private Button createButton;
	@FXML
	private Button deleteButton;
	@FXML
	private Button logoutButton;
	@FXML
	private ListView<User> userListView;
	
	/**
	 * Method to call when "Create" button is clicked.
	 * Shows the add new user dialog to add new user.
	 */
	public void createButtonClicked(){
		showAddNewUserDialog();
		updateList();
	}

	/**
	 * Method to call when "Delete" button is clicked.
	 * Removes user if selection is made, does nothing otherwise.
	 */
	public void deleteButtonClicked(){
		int index = userListView.getSelectionModel().getSelectedIndex();
		if (index == -1) return;
		userList.remove(index);
		updateList();
	}
	
	/**
	 * Method to call when "Logout" button is clicked.
	 * Exits the admin screen and returns to the log-in screen.
	 */
	public void logoutButtonClicked(){
		// System.out.println("Admin logging out");
		this.getMainApp().startWindow(PhotoAlbum.LOGIN_WINDOW, userList, 0);
		return;
	}
	
	/**
	 * Updates the list view with the current list of users.
	 */
	private void updateList(){
		obsList = FXCollections.observableArrayList(userList);
		userListView.setItems(obsList);
		userListView.requestFocus();
		if(userList != null)
			userListView.getSelectionModel().select(0);
	}
	
	/**
	 * Shows a dialog to add new users.
	 */
	private void showAddNewUserDialog() {
		try{
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(PhotoAlbum.class.getResource("view/AddUserDialog.fxml"));
			AnchorPane addDialog = (AnchorPane) loader.load();
			
			//Create the stage for the dialog
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Add User");
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(this.getMainApp().getPrimaryStage());
			Scene scene = new Scene(addDialog);
			dialogStage.setScene(scene);
			dialogStage.setResizable(false);
			
			//Set the Controller
			AddUserController controller = loader.getController();
			controller.setAC(this);
			controller.setStage(dialogStage);
			
			dialogStage.showAndWait();
			
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
	/**
	 * Initializes the user list passed in from previous method.
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void init(Object obj, int index) {
		userList = (ArrayList<User>) obj;
		updateList();
	}
	
	/**
	 * Returns the current user list as a collection.
	 * @return the current user list
	 */
	public ArrayList<User> getUsers() {
		return userList;
	}
}
