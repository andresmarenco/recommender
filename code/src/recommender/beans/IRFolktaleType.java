package recommender.beans;

import java.io.Serializable;

public class IRFolktaleType implements Serializable {

	private static final long serialVersionUID = 201212101440L;
	
	private long id;
	private String code;
	private double IFW;
	
	/**
	 * Default Constructor
	 */
	public IRFolktaleType() {
		super();
		this.clear();
	}
	
	
	/**
	 * Fields Constructor
	 * @param id Folktale Type Id
	 * @param code Folktale Type Code
	 * @param IFW Folktale Type IFW
	 */
	public IRFolktaleType(long id, String code, double IFW) {
		super();
		this.id = id;
		this.code = code;
		this.IFW = IFW;
	}

	
	
	/**
	 * Clears all the fields of the object
	 */
	public void clear() {
		this.id = Long.MIN_VALUE;
		this.code = null;
		this.IFW = Double.MIN_VALUE;
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
	 * @return the code
	 */
	public String getCode() {
		return code;
	}

	/**
	 * @param code the code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}
	
	
	/**
	 * @return the iFW
	 */
	public double getIFW() {
		return IFW;
	}


	/**
	 * @param iFW the iFW to set
	 */
	public void setIFW(double iFW) {
		IFW = iFW;
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
		IRFolktaleType other = (IRFolktaleType) obj;
		if (id != other.id)
			return false;
		return true;
	}


	@Override
	public String toString() {
		return this.getCode();
	}
}
