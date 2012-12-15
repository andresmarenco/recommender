package recommender.web;

import javax.servlet.jsp.tagext.TagSupport;

public class TagUtil extends TagSupport {

	private static final long serialVersionUID = 201212152037L;

	/**
	 * Escapes the newline character into the HTML break tag
	 * @param text Text to escape
	 * @return Text with the break tag instead of newline characters
	 */
	public static String escapeNewline(String text) {
		return text.replaceAll("[\r\n]+", "<br />");
	}
}
