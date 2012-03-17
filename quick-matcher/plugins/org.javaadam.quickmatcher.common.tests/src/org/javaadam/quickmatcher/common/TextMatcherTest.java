package org.javaadam.quickmatcher.common;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class TextMatcherTest {

	private TextMatcher matcher;

	@Before
	public void setUp() {
		matcher = new TextMatcher();
	}

	@Test
	public void matchesWithNulls() {
		final String message = "some message";
		final String messageWithLineBreak = "\nsome \nmessage\n";
		final String messageWithTab = "\tsome \tmessage\t";

		assertTrue(matcher.matches(message, message));
		assertTrue(matcher.matches(message, "age"));
		assertTrue(matcher.matches(message, "some"));
		assertTrue(matcher.matches(message, "me"));
		assertTrue(matcher.matches(message, "MeSs"));
		assertTrue(matcher.matches(message, "so*ge"));
		assertTrue(matcher.matches(message, "so*"));
		assertTrue(matcher.matches(message, "*ge"));
		assertTrue(matcher.matches(message, "*so*e*m*ge*"));
		assertTrue(matcher.matches(message, ""));
		assertTrue(matcher.matches(message, " "));

		assertTrue(matcher.matches(messageWithLineBreak, "some"));
		assertTrue(matcher.matches(messageWithTab, "some"));

		assertFalse(matcher.matches(message, "!§$%&/()=?"));
		assertFalse(matcher.matches(message, "another"));
		assertFalse(matcher.matches(message, null));
		assertFalse(matcher.matches(null, null));
	}
}
