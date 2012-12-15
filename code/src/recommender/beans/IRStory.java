package recommender.beans;

import java.io.Serializable;
import java.util.List;

public class IRStory implements Serializable {

	private static final long serialVersionUID = 201211290150L;
	
	private long id;
	private String code;
	private String title;
	private String text;
	private List<IRKeyword> keywords;
	
	
	/**
	 * Default Constructor
	 */
	public IRStory() {
		super();
		this.clear();
	}
	
	
	
	
	/**
	 * Clears all the fields of the object
	 */
	private void clear() {
		this.id = Long.MIN_VALUE;
		this.code = null;
		this.title = null;
		this.text = null;
		this.keywords = null;
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
	 * @return the title
	 */
	public String getTitle() {
		return ((this.title == null) || (this.title.isEmpty()) ? this.text.substring(0, Math.min(30, this.text.length())) + "..." : title);
	}



	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}



	/**
	 * @return the text
	 */
	public String getText() {
		return this.text;
	}



	/**
	 * @param text the text to set
	 */
	public void setText(String text) {
		this.text = text;
	}




	/**
	 * @return the keywords
	 */
	public List<IRKeyword> getKeywords() {
		return keywords;
	}




	/**
	 * @param keywords the keywords to set
	 */
	public void setKeywords(List<IRKeyword> keywords) {
		this.keywords = keywords;
	}
	
	
	
}
