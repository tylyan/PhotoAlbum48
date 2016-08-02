package juanyuk.photoalbum.model;

import java.io.File;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

/**
 * Class representation of a Photo.
 * @author yuky, jmm754
 */
public class Photo implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private String mCaption;
	private String mPath;
	private Date mDate;
	private File mFile;
	private ArrayList<Tag> mTags;
	
	/**
	 * Constructor for a Photo given a file and caption.
	 * @param file specified file to create photo from
	 * @param caption caption to set the photo to
	 */
	public Photo(File file, String caption){
		this.mPath = file.getPath();
		this.mDate = new Date(file.lastModified());
		this.mCaption = caption.equals("") ? "No caption set" : caption;
		this.mFile = file;
		mTags = new ArrayList<Tag>();
	}

	/**
	 * Return the number of tags on the photo
	 * @return the number of tags on the photo
	 */
	public int getNumTags(){
		if (mTags == null) return 0;
		return mTags.size();
	}
	
	/**
	 * Returns the tags associated to the photo as a collection.
	 * @return the tags associated to the photo as a collection
	 */
	public ArrayList<Tag> getTags(){
		return this.mTags;
	}
	
	/**
	 * Returns the caption of the photo
	 * @return the caption of the photo
	 */
	public String getCaption() {
		return mCaption;
	}

	/**
	 * Sets the caption of the photo
	 * @param caption caption to set
	 */
	public void setCaption(String caption) {
		mCaption = caption;
	}
	
	/**
	 * Adds a new tag to the photo, if the tag already exists, then no tag is added.
	 * @param tag tag to add
	 */
	public void addTag(Tag tag){
		if (mTags == null) mTags = new ArrayList<Tag>();
		for (Tag t : mTags){
			if (t.equals(tag)){
				System.out.println("Tag already exists in this photo");
				return;
			}
		}
		mTags.add(tag);
		Collections.sort(mTags);
	}

	/**
	 * Removes a tag from the photo given an index.
	 * @param index index of tag to remove
	 */
	public void deleteTag(int index){
		if (mTags == null) return;
		mTags.remove(index);
	}
	
	/**
	 * Returns the file associated to this photo.
	 * @return the file associated to this photo
	 */
	public File getFile(){
		return mFile;
	}

	/**
	 * Returns the full path to the photo in String.
	 * @return the full path to the photo
	 */
	public String getPath() {
		return mPath;
	}

	/**
	 * Returns the date associated to this photo, date is actually the last date modified.
	 * @return date associated to photo
	 */
	public Date getDate() {
		return mDate;
	}
	
	/**
	 * Returns the caption of the photo.
	 */
	public String toString(){
		return this.getCaption();
	}
	
	/**
	 * Checks whether this photo is equal to another by comparing the full path name.
	 * @return boolean true if this photo is equal to the other, false otherwise
	 */
	public boolean equals(Object o){
		if (o == null || !(o instanceof Photo))
			return false;
		Photo op = (Photo) o;
		return this.getPath().equals(op.getPath());
	}
	
	/**
	 * Returns the date of this photo in String formatted 'MM/dd/yy'.
	 * @return the date of this photo in 'MM/dd/yy'
	 */
	public String getDateString(){
		return new SimpleDateFormat("MM/dd/yy").format(this.getDate());
	}
}
