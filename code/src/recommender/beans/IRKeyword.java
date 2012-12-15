package recommender.beans;

import java.io.Serializable;

public class IRKeyword implements Serializable, Comparable<IRKeyword> {

	private static final long serialVersionUID = 201211300220L;
	private long id;
	private String name;
	
	
	/**
	 * Default Constructor
	 */
	public IRKeyword() {
		super();
	}
	
	
	
	/**
	 * Fields Constructor
	 * @param id Keyword's Id
	 * @param name Keyword's Name
	 */
	public IRKeyword(long id, String name) {
		super();
		this.id = id;
		this.name = name;
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
	public int compareTo(IRKeyword other) {
		return Long.valueOf(this.getId()).compareTo(Long.valueOf(other.getId()));
	}
	
	
	@Override
	public String toString() {
		return this.getName();
	}
}
