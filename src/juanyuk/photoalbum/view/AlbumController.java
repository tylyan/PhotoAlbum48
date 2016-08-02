package juanyuk.photoalbum.view;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import juanyuk.photoalbum.PhotoAlbum;
import juanyuk.photoalbum.model.Album;
import juanyuk.photoalbum.model.Photo;
import juanyuk.photoalbum.model.Tag;
import juanyuk.photoalbum.model.User;

/**
 * Controller for the Album Window.
 * @author yuky, jmm754
 */
public class AlbumController extends Controller{
	
	public static final int NORMAL = 0;
	public static final int SEARCH = 1;
	
	private User user;
	private ObservableList<Album> obsList;
	
	@FXML
	private ListView<Album> albumListView;
	@FXML
	private TextField startDateTextField;
	@FXML
	private TextField endDateTextField;
	@FXML
	private TextField tagTypeTextField;
	@FXML
	private TextField tagValueTextField;
	@FXML
	private Label numPhotosLabel;
	@FXML
	private Label oldestDateLabel;
	@FXML
	private Label dateRangeLabel;
	
	/**
	 * Method to call when "Add" button is clicked.
	 * Starts add new album dialog.
	 */
	public void addButtonClicked(){
		showAddNewAlbumDialog(AlbumDialogController.ADD);
		updateList();
		if (albumListView != null)
			albumListView.getSelectionModel().select(0);
			populateDetails(user.getAlbum(0));
	}
	
	/**
	 * Method to call when "Delete" button is clicked.
	 * Deletes an album if selection is made, shows error dialog otherwise.
	 */
	public void deleteButtonClicked(){
		int index = albumListView.getSelectionModel().getSelectedIndex();
		if (index == -1){
			showErrorDialog("No album selected.");
			return;
		}
		user.deleteAlbum(index);
		updateList();
		if (albumListView != null)
			albumListView.getSelectionModel().select(0);
		else{
			populateDetails(null);
		}
	}
	
	/**
	 * Method to call when "Rename" button is clicked.
	 * Shows the rename album dialog if selection is made, shows error dialog otherwise.
	 */
	public void renameButtonClicked(){
		int index = -1;
		if (albumListView != null)
			index = albumListView.getSelectionModel().getSelectedIndex();
		if (index == -1){
			showErrorDialog("No album selected.");
			return;
		}
		showAddNewAlbumDialog(index);
		updateList();
		albumListView.getSelectionModel().select(index);
	}
	
	/**
	 * Method to call when "Open" button is clicked.
	 * Photo Window is started to open selected album, error dialog is shown if no album selected.
	 */
	public void openButtonClicked(){
		int index = albumListView.getSelectionModel().getSelectedIndex();
		if (index == -1){
			showErrorDialog("No album selected.");
			return;
		}
		// System.out.println("Opening album " + user.getAlbum(index).getName());
		this.getMainApp().startWindow(PhotoAlbum.PHOTO_WINDOW, user.getAlbum(index), NORMAL);
	}
	
	/**
	 * Method to call when "Search" by Date button is clicked.
	 * Searches through all of user's photos, compiles a result album of photos that match the criteria.
	 * Photo window is started with the resulting album.
	 */
	public void searchByDateClicked(){
		if (startDateTextField.getText().equals("") || endDateTextField.getText().equals("")){
			showErrorDialog("Please enter a valid date range.");
			return;
		}
		SimpleDateFormat format = new SimpleDateFormat("MM/dd/yy");
		Date startDate = null;
		Date endDate = null;
		Album resultAlbum = new Album("Search Results");
		try {
			startDate = format.parse(startDateTextField.getText());
			endDate = format.parse(endDateTextField.getText());
		} catch (ParseException e) {
			showErrorDialog("Please enter date as mm/dd/yy.");
			return;
		}
		if (startDate == null || endDate == null) return;
		if (startDate.compareTo(endDate) > 0 || startDate.equals(endDate)){
			showErrorDialog("Start date must be before end date.");
			return;
		}
		for (Album a : user.getAlbums()){
			for (Photo p : a.getPhotos()){
				if (p.getDate().compareTo(startDate) >= 0 && p.getDate().compareTo(endDate) <= 0)
						resultAlbum.addPhoto(p);
			}
		}
		if (resultAlbum.getNumPhotos() == 0){
			showErrorDialog("No photos matching your search criteria was found.");
		}else{
			this.getMainApp().startWindow(PhotoAlbum.PHOTO_WINDOW, resultAlbum, SEARCH);
		}
	}
	
	/**
	 * Method to call when "Search" by Tag button is clicked.
	 * Searches through all of user's photos, compiles a result album of photos that match the criteria.
	 * Photo window is started with the resulting album.
	 */
	public void searchByTagClicked(){
		if (tagTypeTextField.getText().equals("") && tagValueTextField.getText().equals("")){
			showErrorDialog("Please enter either a tag type or value to search for.");
			return;
		}
		Tag searchTag = new Tag(tagTypeTextField.getText(), tagValueTextField.getText());
		Album resultAlbum = new Album("Search Results");
		for (Album a : user.getAlbums()){
			for (Photo p : a.getPhotos()){
				for (Tag t : p.getTags()){
					if (t.equals(searchTag)){
						resultAlbum.addPhoto(p);
					}
				}
			}
		}
		if (resultAlbum.getNumPhotos() == 0){
			showErrorDialog("No photos matching your search criteria was found.");
		}else{
			this.getMainApp().startWindow(PhotoAlbum.PHOTO_WINDOW, resultAlbum, SEARCH);
		}
	}
	
	/**
	 * Method to call when "Logout" button is clicked.
	 * The Login window is started.
	 */
	public void logoutButtonClicked(){
		// System.out.println("User " + user + " logging out");
		this.getMainApp().setCurrUser(null);
		this.getMainApp().startWindow(PhotoAlbum.LOGIN_WINDOW, this.getMainApp().getUserList(), 0);
	}
	
	/**
	 * Updates the list view with the current list of users.
	 */
	public void updateList(){
		// System.out.println("Updating album list");
		if(obsList != null)
			obsList.clear();
		obsList = FXCollections.observableArrayList(user.getAlbums());
		albumListView.setItems(obsList);
		albumListView.requestFocus();
		
	}
	
	/**
	 * Shows a dialog to add or edit album.
	 * @param index source that the new album dialog is called from, either from SEARCH or ADD.
	 * SEARCH - album dialog was called from search results
	 * ADD - album dialog was called from Album window
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
			controller.setAC(this);
			controller.setStage(dialogStage);
			controller.setIndex(index);
			
			
			dialogStage.showAndWait();
		}catch(IOException e){
			e.printStackTrace();
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
	 * Returns the user.
	 * @return user to return
	 */
	public User getUser(){
		return user;
	}
	
	/**
	 * Initializes the Album window given an object on first open.
	 * Populates the album list view to show all albums for the user.
	 * @param obj User object that the Album window opened with.
	 * @param index
	 */
	@Override
	public void init(Object obj, int index) {
		user = (User) obj;	
		// System.out.println("Showing albums for user " + user);
		updateList();
		if (!user.getAlbums().isEmpty()){
			albumListView.getSelectionModel().select(0);
			populateDetails(user.getAlbum(0));
		}else{
			populateDetails(null);
		}
			
		albumListView.setOnMouseClicked(new EventHandler<MouseEvent>(){

			@Override
			public void handle(MouseEvent arg0) {
				if (albumListView.getSelectionModel().getSelectedItem() == null) return;
				populateDetails(albumListView.getSelectionModel().getSelectedItem());
			}
			
		});
	}
	
	/**
	 * Populates the album detail labels for a specified album.
	 * @param album album to show details for
	 */
	private void populateDetails(Album album){
		if (album == null){
			numPhotosLabel.setText("N/A");
			oldestDateLabel.setText("N/A");
			dateRangeLabel.setText("N/A");
		}else{
			numPhotosLabel.setText(album.getNumPhotos() + "");
			oldestDateLabel.setText(album.getOldestDateString());
			dateRangeLabel.setText(album.getOldestDateString() + " - " + album.getNewestDateString());
		}
	}
}
