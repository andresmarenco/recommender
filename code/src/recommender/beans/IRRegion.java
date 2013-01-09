package recommender.beans;

import java.io.Serializable;

public class IRRegion implements Serializable {

	private static final long serialVersionUID = 201212101440L;
	
	private long id;
	private String name;
	
	/**
	 * Default Constructor
	 */
	public IRRegion() {
		super();
		this.clear();
	}
	
	
	/**
	 * Fields Constructor
	 * @param id Region Id
	 * @param name Region Name
	 */
	public IRRegion(long id, String name) {
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
		IRRegion other = (IRRegion) obj;
		if (id != other.id)
			return false;
		return true;
	}


	@Override
	public String toString() {
		return this.getName();
	}
}
