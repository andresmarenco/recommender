package recommender.beans;

import java.io.Serializable;

public class IREventType implements Serializable {

	private static final long serialVersionUID = 201212212223L;
	
	public static final String EVENT_LOGIN = "login";
	public static final String EVENT_LOGOUT = "logout";
	public static final String EVENT_SIGNUP = "signup";
	public static final String EVENT_VIEW_STORY = "view_story";
	
	private long id;
	private String name;
	
	/**
	 * Default Constructor
	 */
	public IREventType() {
		super();
		this.clear();
	}

	
	/**
	 * Constructor with Fields
	 * @param id Id of the Event Type
	 * @param name Name of the Event Type
	 */
	public IREventType(long id, String name) {
		super();
		this.id = id;
		this.name = name;
	}
	
	
	/**
	 * Clears all the fields of the object
	 */
	public void clear() {
		this.id = Long.MIN_VALUE;
		this.name = null;
	}


	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}


	/**
	 * @param id the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}


	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}


	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	
	@Override
	public String toString() {
		return this.name;
	}


	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		return result;
	}


	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		IREventType other = (IREventType) obj;
		if (id != other.id)
			return false;
		return true;
	}
}
