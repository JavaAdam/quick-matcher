package org.javaadam.quickmatcher.common;

import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextMatcher {

	private static final String PLACEHOLDER_REGEX = ".*";
	private static final String PLACEHOLDER = "*";

	public boolean matches(final String text, final String toMatch) {
		if (text != null && toMatch != null) {

			String temp = "";

			final StringTokenizer tokenizer = new StringTokenizer(
					toMatch.trim(), PLACEHOLDER);

			while (tokenizer.hasMoreTokens()) {
				final String token = tokenizer.nextToken();
				temp += PLACEHOLDER_REGEX + Pattern.quote(token);
			}

			final Pattern pattern = Pattern.compile(temp + PLACEHOLDER_REGEX,
					Pattern.CASE_INSENSITIVE);
			final Matcher matcher = pattern.matcher(text.trim());

			return matcher.matches();
		}
		return false;
	}

}
