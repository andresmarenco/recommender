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
	private Long views;
	private String dateRecording;
	private String dateCreation;
	private boolean copyright;
	private boolean extreme;
	private IRLanguage language;
	private IRFolktaleType folktaleType;
	private IRStoryTeller storyTeller;
	private IRScriptSource scriptSource;
	private IRRegion region;
	private IRSubgenre subgenre;
	private long recommendationRank;
	
	
	
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
	public void clear() {
		this.id = Long.MIN_VALUE;
		this.code = null;
		this.title = null;
		this.text = null;
		this.keywords = null;
		this.views = null;
		this.dateRecording = null;
		this.dateCreation = null;
		this.copyright = false;
		this.extreme = false;
		this.language = null;
		this.folktaleType = null;
		this.storyTeller = null;
		this.scriptSource = null;
		this.region = null;
		this.subgenre = null;
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




	/**
	 * @return the views
	 */
	public Long getViews() {
		return views;
	}




	/**
	 * @param views the views to set
	 */
	public void setViews(Long views) {
		this.views = views;
	}
	



	/**
	 * @return the dateRecording
	 */
	public String getDateRecording() {
		return dateRecording;
	}




	/**
	 * @param dateRecording the dateRecording to set
	 */
	public void setDateRecording(String dateRecording) {
		this.dateRecording = dateRecording;
	}




	/**
	 * @return the dateCreation
	 */
	public String getDateCreation() {
		return dateCreation;
	}




	/**
	 * @param dateCreation the dateCreation to set
	 */
	public void setDateCreation(String dateCreation) {
		this.dateCreation = dateCreation;
	}




	/**
	 * @return the copyright
	 */
	public boolean isCopyright() {
		return copyright;
	}




	/**
	 * @param copyright the copyright to set
	 */
	public void setCopyright(boolean copyright) {
		this.copyright = copyright;
	}




	/**
	 * @return the extreme
	 */
	public boolean isExtreme() {
		return extreme;
	}




	/**
	 * @param extreme the extreme to set
	 */
	public void setExtreme(boolean extreme) {
		this.extreme = extreme;
	}




	/**
	 * @return the language
	 */
	public IRLanguage getLanguage() {
		return language;
	}




	/**
	 * @param language the language to set
	 */
	public void setLanguage(IRLanguage language) {
		this.language = language;
	}




	/**
	 * @return the folktaleType
	 */
	public IRFolktaleType getFolktaleType() {
		return folktaleType;
	}




	/**
	 * @param folktaleType the folktaleType to set
	 */
	public void setFolktaleType(IRFolktaleType folktaleType) {
		this.folktaleType = folktaleType;
	}




	/**
	 * @return the storyTeller
	 */
	public IRStoryTeller getStoryTeller() {
		return storyTeller;
	}




	/**
	 * @param storyTeller the storyTeller to set
	 */
	public void setStoryTeller(IRStoryTeller storyTeller) {
		this.storyTeller = storyTeller;
	}




	/**
	 * @return the scriptSource
	 */
	public IRScriptSource getScriptSource() {
		return scriptSource;
	}




	/**
	 * @param scriptSource the scriptSource to set
	 */
	public void setScriptSource(IRScriptSource scriptSource) {
		this.scriptSource = scriptSource;
	}




	/**
	 * @return the region
	 */
	public IRRegion getRegion() {
		return region;
	}




	/**
	 * @param region the region to set
	 */
	public void setRegion(IRRegion region) {
		this.region = region;
	}




	/**
	 * @return the subgenre
	 */
	public IRSubgenre getSubgenre() {
		return subgenre;
	}




	/**
	 * @param subgenre the subgenre to set
	 */
	public void setSubgenre(IRSubgenre subgenre) {
		this.subgenre = subgenre;
	}
	




	/**
	 * @return the recommendationRank
	 */
	public long getRecommendationRank() {
		return recommendationRank;
	}




	/**
	 * @param recommendationRank the recommendationRank to set
	 */
	public void setRecommendationRank(long recommendationRank) {
		this.recommendationRank = recommendationRank;
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
		IRStory other = (IRStory) obj;
		if (id != other.id)
			return false;
		return true;
	}
}
