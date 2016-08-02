package juanyuk.photoalbum.model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;

/**
 * Class representation of a photo album.
 * Album will have a collection of photos with the ability to create and remove photos.
 * @author yuky, jmm754
 */
public class Album implements Comparable<Album>, Comparator<Album>, Serializable{
	private static final long serialVersionUID = 1L;

	private String mName;
	private String idName;
	private Date oldestDate;
	private Date newestDate;
	
	private ArrayList<Photo> mPhotos;
		
	/**
	 * Constructor for an album.
	 * @param name value in String to name the album
	 */
	public Album(String name){
		this.setName(name);
		mPhotos = new ArrayList<Photo>();
		setOldestDate(null);
		setNewestDate(null);
	}

	/**
	 * Adds a photo to the photo collection, if photo already exists in the album, then it is not added.
	 * The date range for the album is also updated on each add.
	 * @param photo photo to be added
	 */
	public void addPhoto(Photo photo){
		if (mPhotos == null) return;
		for (Photo p : mPhotos){
			if (p.equals(photo)){
				// System.out.println(photo.getPath() + " already exists in this album");
				return;
			}
		}
		mPhotos.add(photo);
		
		// Set date ranges!
		if (oldestDate == null || photo.getDate().compareTo(oldestDate) < 0)
				setOldestDate(photo.getDate());
		
		if (newestDate == null || photo.getDate().compareTo(newestDate) > 0)
				setNewestDate(photo.getDate());
	}
	
	/**
	 * Deletes a photo from an the album, date range for album is also recalculated per delete.
	 * @param photo photo to delete
	 */
	public void deletePhoto(Photo photo){
		if (mPhotos == null) return;
		if (!mPhotos.contains(photo)) return;
		mPhotos.remove(photo);
		recalcDates();
	}

	/**
	 * Move a photo from this album to another album, if photo already exists in destination album, photo is not moved.
	 * @param target photo to be moved
	 * @param destination album to move photo to
	 * @return
	 */
	public boolean moveTo(Photo target, Album destination){
		if (target == null || destination == null) return false;
		for (Photo p : destination.getPhotos()){
			if (p.equals(target)){
				return false;
			}
		}
		this.deletePhoto(target);
		destination.addPhoto(target);
		return true;
		
	}
	
	/**
	 * Recalculates the date ranges for this album. This method is called whenever a photo is deleted from the album.
	 */
	public void recalcDates(){
		if (this.getNumPhotos() == 0){
			this.setOldestDate(null);
			this.setNewestDate(null);
		}
		for (Photo p : this.getPhotos()){
			if (oldestDate == null || p.getDate().compareTo(oldestDate) < 0)
				setOldestDate(p.getDate());
		
			if (newestDate == null || p.getDate().compareTo(newestDate) > 0)
				setNewestDate(p.getDate());
		}
	}
	
	/**
	 * Returns the photo with the corresponding index
	 * @param index index corresponding to photo
	 * @return photo
	 */
	public Photo getPhoto(int index){
		if (mPhotos == null) return null;
		return mPhotos.get(index);
	}
	
	/**
	 * Returns the number of photos in the album
	 * @return the number of photos in the album
	 */
	public int getNumPhotos(){
		if (mPhotos == null) return 0;
		return mPhotos.size();
	}
	
	/**
	 * Returns the name of the album
	 * @return the name of the album
	 */
	public String getName() {
		return mName;
	}

	/**
	 * Sets the name of the album
	 * @param name name to set album to
	 */
	public void setName(String name) {
		mName = name;
		setIdName(name.toLowerCase());
	}

	/**
	 * Prints this album's name.
	 */
	public String toString(){
		return this.getName();
	}
	
	/**
	 * Used for sorting a collection of albums.
	 */
	@Override
	public int compare(Album o1, Album o2) {
		return o1.compareTo(o2);
	}

	/**
	 * Comparing one album to another, uses the ID name.
	 */
	@Override
	public int compareTo(Album o) {
		return this.getIdName().compareTo(o.getIdName());
	}
	
	/**
	 * Returns the index of a given photo within the album.
	 * @param photo photo to find index of
	 * @return index of the photo, -1 if photo does not exist in album
	 */
	public int indexOf(Photo photo){
		int ret = -1;
		for (Photo p : mPhotos){
			if (p.equals(photo)) ret = mPhotos.indexOf(p);
		}
		return ret;
	}

	/**
	 * Returns the ID name of the album.
	 * @return the ID name of the album
	 */
	public String getIdName() {
		return idName;
	}

	/**
	 * Sets the ID name of the album.
	 * @param idName desired ID name to set to
	 */
	public void setIdName(String idName) {
		this.idName = idName;
	}
	
	/**
	 * Returns the Photos in the album as a collection.
	 * @return the photos in the album
	 */
	public ArrayList<Photo> getPhotos(){
		return mPhotos;
	}
	
	/**
	 * Returns the oldest date in this album in String formatted in 'MM/dd/yy'.
	 * @return the oldest date of the album in 'MM/dd/yy'
	 */
	public String getOldestDateString(){
		if (this.getOldestDate() == null){
			return "N/A";
		}
		return new SimpleDateFormat("MM/dd/yy").format(this.getOldestDate());
	}
	
	/**
	 * Returns the newest date in this album in String formatted in 'MM/dd/yy'.
	 * @return the newest date of the album in 'MM/dd/yy'
	 */
	public String getNewestDateString(){
		if (this.getNewestDate() == null){
			return "N/A";
		}
		return new SimpleDateFormat("MM/dd/yy").format(this.getNewestDate());
	}

	/**
	 * Returns the oldest date in this album as a Date object.
	 * @return the oldest date in this album as a Date object
	 */
	public Date getOldestDate() {
		return oldestDate;
	}

	/**
	 * Sets the oldest date of this album.
	 * @param oldestDate date to set oldestDate to
	 */
	public void setOldestDate(Date oldestDate) {
		this.oldestDate = oldestDate;
	}

	/**
	 * Returns the newest date in this album as a Date object.
	 * @return the newest date in this album as a Date object
	 */
	public Date getNewestDate() {
		return newestDate;
	}

	/**
	 * Sets the newest date of this album.
	 * @param newestDate date to set newestDate to
	 */
	public void setNewestDate(Date newestDate) {
		this.newestDate = newestDate;
	}
	
}

