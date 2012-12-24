package recommender.beans;

import java.io.Serializable;

public class IRSubgenre implements Serializable {

	private static final long serialVersionUID = 201212101440L;
	
	private long id;
	private String name;
	private int total;
	
	/**
	 * Default Constructor
	 */
	public IRSubgenre() {
		super();
		this.clear();
	}
	
	
	/**
	 * Fields Constructor
	 * @param id Subgenre's Id
	 * @param name Subgenre's Name
	 */
	public IRSubgenre(long id, String name) {
		super();
		this.id = id;
		this.name = name;
	}
	

	/**
	 * Fields Constructor
	 * @param id Subgenre's Id
	 * @param name Subgenre's Name
	 * @param total Total of stories with this Subgenre
	 */
	public IRSubgenre(long id, String name, int total) {
		super();
		this.id = id;
		this.name = name;
		this.total = total;
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


	/**
	 * @return the total
	 */
	public int getTotal() {
		return total;
	}


	/**
	 * @param total the total to set
	 */
	public void setTotal(int total) {
		this.total = total;
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
		IRSubgenre other = (IRSubgenre) obj;
		if (id != other.id)
			return false;
		return true;
	}


	@Override
	public String toString() {
		return this.getName();
	}
}
