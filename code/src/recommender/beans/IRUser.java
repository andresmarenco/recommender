package recommender.beans;

import java.io.Serializable;
import java.sql.Timestamp;

import org.apache.commons.codec.binary.Hex;

import recommender.utils.CryptoUtil;
import recommender.utils.RecommenderException;

public class IRUser implements Serializable {

	private static final long serialVersionUID = 201211301513L;
	
	private long id;
	private String username;
	private String password;
	private String name;
	private Timestamp last_login;
	private boolean active;
	
	
	/**
	 * Default Constructor
	 */
	public IRUser() {
		super();
		this.clear();
	}
	
	
	
	
	/**
	 * Clears all the fields of the object
	 */
	private void clear() {
		this.id = Long.MIN_VALUE;
		this.username = null;
		this.password = null;
		this.name = null;
		this.last_login = null;
		this.active = false;
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
		this.clear();
		this.id = id;
	}


	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}


	/**
	 * Sets the username
	 * @param username The username to set
	 */
	public void setUsername(String username) {
		this.username = username;
		
		/*if(ValidationUtil.validateUsername(username)) {
			this.username = username;
		} else {
			throw new RecommenderException(RecommenderException.MSG_ERROR_USERNAME_POLICY);
		}*/
	}


	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}


	/**
	 * Sets the encrypted password
	 * @param password The password to set
	 * @throws RecommenderException
	 */
	public void setPassword(String password) throws RecommenderException {
		try
		{
			String salt = String.valueOf(System.currentTimeMillis());
			salt = salt.substring(salt.length() - 4);
			
			this.password = Hex.encodeHexString(CryptoUtil.digestToBinary(password + salt)) + salt;
		}
		catch(Exception ex) {
			ex.printStackTrace();
			this.password = null;
			
			throw new RecommenderException(RecommenderException.MSG_ERROR_CRYPTO);
		}
		
		/*if(ValidationUtil.validatePassword(password)) {
			try
			{
				String salt = String.valueOf(System.currentTimeMillis());
				salt = salt.substring(salt.length() - 4);
				
				this.password = Hex.encodeHexString(CryptoUtil.digestToBinary(password + salt)) + salt;
			}
			catch(Exception ex) {
				ex.printStackTrace();
				this.password = null;
				
				throw new RecommenderException(RecommenderException.MSG_ERROR_CRYPTO);
			}
		} else {
			throw new RecommenderException(RecommenderException.MSG_ERROR_PASSWORD_POLICY);
		}*/
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
	 * @return the last_login
	 */
	public Timestamp getLast_login() {
		return last_login;
	}


	/**
	 * @param last_login the last_login to set
	 */
	public void setLast_login(Timestamp last_login) {
		this.last_login = last_login;
	}


	/**
	 * @return the active
	 */
	public boolean isActive() {
		return active;
	}


	/**
	 * @param active the active to set
	 */
	public void setActive(boolean active) {
		this.active = active;
	}

	
	
	/**
	 * Determines if the user is logged
	 * @return
	 */
	public boolean isLogged() {
		return this.id != Long.MIN_VALUE;
	}
}
