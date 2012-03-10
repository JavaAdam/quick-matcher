package org.javaadam.quickmatcher.jdt.internal;

public class TextUtils {

	public String transformText(String text) {
		if (text != null && text.length() > 0) {
			return text.trim();
		} else {
			return null;
		}
	}

}
