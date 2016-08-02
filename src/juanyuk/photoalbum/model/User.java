package juanyuk.photoalbum.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;

/**
 * A class representation of a User
 * A user will have a collection of Albums with the ability to create and remove album.
 * @author yuky, jmm754
 */
public class User implements Comparable<User>, Comparator<User>, Serializable{
	
	private static final long serialVersionUID = 1L;

	private String name;
	private ArrayList<Album> mAlbums;
	
	/**
	 * Constructor for a user.
	 * @param name name to set the user to
	 */
	public User(String name){
		this.setName(name);
		mAlbums = new ArrayList<Album>();
	}

	/**
	 * Returns an album given an index
	 * @param index index of the album
	 * @return Album corresponding to the index
	 */
	public Album getAlbum(int index){
		if (mAlbums == null) return null;
		return mAlbums.get(index);
	}
	
	/**
	 * Returns an album given an album
	 * @param album
	 * @return
	 */
	public Album getAlbum(Album album){ // not sure if we will need this method
		if (mAlbums == null) return null;
		if (!mAlbums.contains(album)) return null;
		return mAlbums.get(mAlbums.indexOf(album));
	}
	
	/**
	 * Returns the user's albums as a collection.
	 * @return the user's albums
	 */
	public ArrayList<Album> getAlbums(){
		return mAlbums;
	}
	
	/**
	 * Returns the number of albums user has
	 * @return the number of albums user has
	 */
	public int getNumAlbums(){
		if (mAlbums == null) return 0;
		return mAlbums.size();
	}
	
	/**
	 * Adds an album to album collection
	 * @param album album to add
	 */
	public void addAlbum(Album album){
		if (mAlbums == null) return;
		mAlbums.add(album);
	}
	
	/**
	 * Removes an album from album collection
	 * @param album album to remove
	 */
	public void deleteAlbum(Album album){
		if (mAlbums == null) return;
		if (!mAlbums.contains(album)) return;
		mAlbums.remove(album);
	}
	
	/**
	 * Deletes an album from the album collection
	 * @param index index of album to remove
	 */
	public void deleteAlbum(int index){
		if (mAlbums == null) return;
		mAlbums.remove(index);
	}
	
	/**
	 * Returns the name of the user
	 * @return the name of the user
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name of the user
	 * @param name new desired name 
	 */
	private void setName(String name) {
		this.name = name;
	}
	
	/**
	 * Returns in a String the name of the user.
	 */
	@Override
	public String toString(){
		return getName();
	}
	
	/**
	 * Checks if this user's name is equal to a given string.
	 * @param s string to compare to user's name
	 * @return true if s is equal to user's name, false otherwise
	 */
	public boolean equals(String s){
		if (s == null) return false;
		return this.getName().equals(s);
	}
	
	/**
	 * Checks if this user is equal to another user.
	 * @param o other user to compare this user to
	 * @return true if this user is equal to other user, false otherwise
	 */
	public boolean equals(User o){
		if (!(o instanceof User) || o == null) return false;
		return this.getName().equals(o.getName());
	}

	/**
	 * Compare this user to another user by name.
	 */
	@Override
	public int compareTo(User o) {
		return this.getName().compareTo(o.getName());
	}

	/**
	 * Used to sort a collection of users.
	 */
	@Override
	public int compare(User user1, User user2) {
		return user1.compareTo(user2);
	}
	
}
