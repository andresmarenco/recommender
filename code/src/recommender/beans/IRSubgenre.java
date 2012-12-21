package recommender.beans;

import java.io.Serializable;

public class IRSubgenre implements Serializable, Comparable<IRSubgenre> {

	private static final long serialVersionUID = 201212101440L;
	
	private long id;
	private String name;
	private int total;
	
	/**
	 * Default Constructor
	 */
	public IRSubgenre() {
		
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


	@Override
	public int compareTo(IRSubgenre other) {
		return Long.valueOf(this.getId()).compareTo(other.getId());
	}
	
	
	@Override
	public String toString() {
		return this.getName();
	}
}
