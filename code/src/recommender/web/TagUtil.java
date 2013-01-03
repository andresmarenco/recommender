package recommender.web;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;
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
	
	
	
	
	/**
	 * Encodes the current URL with its parameters
	 * @param request Servlet Request
	 * @return Encoded URL
	 */
	public static String findReturnUrl(HttpServletRequest request) {
		String result = "";
		
		try
		{
			Object request_uri = request.getAttribute("javax.servlet.forward.request_uri");
			StringBuilder url = new StringBuilder(request_uri != null ? request_uri.toString() : request.getContextPath() + "/index.jsp");
			String parameters = request.getQueryString();
			
			if(parameters != null) {
				url.append("?").append(parameters);
			}
			
			result = URLEncoder.encode(url.toString(), "ISO-8859-1");
		}
		catch(UnsupportedEncodingException ex) {
			ex.printStackTrace();
		}
		
		return result;
	}
}
