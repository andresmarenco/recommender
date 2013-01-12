package recommender.model.beans;

public enum FeatureField {
	KEYWORD("trefwoorden"),
	FOLKTALE_TYPE("volksverhaaltype"),
	LANGUAGE("taal"),
	REGION("regio"),
	SCRIPT_SOURCE("schriftbron"),
	STORY_TELLER("verteller"),
	SUBGENRE("subgenre"),
	EXTREME("extreem");
	
	private String index_tag;
	private FeatureField(String tag) {
		this.index_tag = tag;
	}
	
	public String getIndexTag() {
		return this.index_tag;
	}
}
