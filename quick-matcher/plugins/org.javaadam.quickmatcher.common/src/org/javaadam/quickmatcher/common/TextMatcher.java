package org.javaadam.quickmatcher.common;

import java.util.ArrayList;
import java.util.Collection;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextMatcher {

	private static final String PLACEHOLDER_REGEX = ".*";
	private static final String PLACEHOLDER = "*";
	private static final String OR_OPERATOR = "|";

	public boolean matches(final String text, final String toMatch) {
		if (text != null && toMatch != null) {

			final Collection<Pattern> patterns = createSplittedPatternsByOr(toMatch);
			for (final Pattern pattern : patterns) {
				final Matcher matcher = pattern.matcher(text.replace("\n", "")
						.trim());

				if (matcher.matches()) {
					return true;
				}
			}
		}
		return false;
	}

	private Collection<Pattern> createSplittedPatternsByOr(final String toMatch) {
		final ArrayList<Pattern> patterns = new ArrayList<Pattern>();

		if (toMatch.contains(OR_OPERATOR)) {
			final StringTokenizer tokenizer = new StringTokenizer(
					toMatch.trim(), OR_OPERATOR);
			while (tokenizer.hasMoreTokens()) {
				final String token = tokenizer.nextToken();
				patterns.add(createPatternWithWildcards(token));
			}
		} else {
			patterns.add(createPatternWithWildcards(toMatch));
		}

		return patterns;
	}

	private Pattern createPatternWithWildcards(final String toMatch) {
		String temp = "";

		final StringTokenizer tokenizer = new StringTokenizer(toMatch.trim(),
				PLACEHOLDER);

		while (tokenizer.hasMoreTokens()) {
			final String token = tokenizer.nextToken();
			temp += PLACEHOLDER_REGEX + Pattern.quote(token);
		}

		final Pattern pattern = Pattern.compile(temp + PLACEHOLDER_REGEX,
				Pattern.CASE_INSENSITIVE);
		return pattern;
	}

}
