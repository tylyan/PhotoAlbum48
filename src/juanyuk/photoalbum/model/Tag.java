package juanyuk.photoalbum.model;

import java.io.Serializable;
import java.util.Comparator;

/**
 * Class representation of a tag.
 * @author yuky, jmm754
 */
public class Tag implements Comparable<Tag>, Comparator<Tag>, Serializable{

	private static final long serialVersionUID = 1L;
	
	private String mType;
	private String mValue;
	
	/**
	 * Constructor for a tag.
	 * @param type type to set the tag to
	 * @param value value to set the tag to
	 */
	public Tag(String type, String value){
		this.setType(type);
		this.setValue(value);
	}
	
	/**
	 * Returns the type of the tag
	 * @return the type of the tag
	 */
	public String getType() {
		return mType;
	}
	
	/**
	 * Sets the type of the tag
	 * @param type type to set tag to
	 */
	public void setType(String type) {
		mType = type;
	}
	
	/**
	 * Returns the value of the tag
	 * @return the value of the tag
	 */
	public String getValue() {
		return mValue;
	}
	
	/**
	 * Sets the value of the tag
	 * @param value value to set tag to
	 */
	public void setValue(String value) {
		mValue = value;
	}
	
	/**
	 * Returns the tag in String as 'type: value' pair
	 */
	public String toString(){
		return this.getType() + ": " + this.getValue();
	}
	
	/**
	 * Checks if this tag is equal to another tag, if value in other tag is not specified, checks if types match,
	 * if type in the other tag is not specified, checks if the value match.  If both type and value of other tag are specified,
	 * then checks if both type and value of this tag match the type and value of other tag.
	 * @return boolean true if tags are equal, false otherwise
	 */
	public boolean equals(Object o){
		if (o == null || !(o instanceof Tag)) return false;
		Tag ot = (Tag) o;
		if (ot.getType().equals("")){
			return this.getValue().toLowerCase().equals(ot.getValue().toLowerCase());
		}
		if (ot.getValue().equals("")){
			return this.getType().toLowerCase().equals(ot.getType().toLowerCase());
		}
		return (this.getType().toLowerCase().equals(ot.getType().toLowerCase()) 
				&& this.getValue().toLowerCase().equals(ot.getValue().toLowerCase()));
	}

	/**
	 * Used for sorting a collection of tags.
	 */
	@Override
	public int compare(Tag o1, Tag o2) {
		return o1.compareTo(o2);
	}

	/**
	 * Compares this tag to another tag.  First compares the type of the tag, if types are equal, then tag values are compared.
	 */
	@Override
	public int compareTo(Tag other) {
		if (this.getType().toLowerCase().equals(other.getType().toLowerCase())){
			return this.getValue().toLowerCase().compareTo(other.getValue().toLowerCase());
		}else{
			return this.getType().toLowerCase().compareTo(other.getType().toLowerCase());
		}
	}
	
	
}
