package recommender.beans;

import java.io.Serializable;
import java.sql.Timestamp;

public class IREvent implements Serializable {

	private static final long serialVersionUID = 201212212249L;
	
	private long id;
	private Timestamp triggered_date;
	private IRUser user;
	private IREventType event_type;

	
	/**
	 * Default Constructor
	 */
	public IREvent() {
		super();
		this.clear();
	}
	
	
	/**
	 * Clears all the fields of the object
	 */
	private void clear() {
		this.id = Long.MIN_VALUE;
		this.triggered_date = null;
		this.user = null;
		this.event_type = null;
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
	 * @return the triggered_date
	 */
	public Timestamp getTriggered_date() {
		return triggered_date;
	}


	/**
	 * @param triggered_date the triggered_date to set
	 */
	public void setTriggered_date(Timestamp triggered_date) {
		this.triggered_date = triggered_date;
	}


	/**
	 * @return the user
	 */
	public IRUser getUser() {
		return user;
	}


	/**
	 * @param user the user to set
	 */
	public void setUser(IRUser user) {
		this.user = user;
	}


	/**
	 * @return the event_type
	 */
	public IREventType getEvent_type() {
		return event_type;
	}


	/**
	 * @param event_type the event_type to set
	 */
	public void setEvent_type(IREventType event_type) {
		this.event_type = event_type;
	}
	
	
	
	
}
