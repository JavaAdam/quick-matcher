package org.javaadam.jdt.quickmatcher;

import org.javaadam.quickmatcher.jdt.internal.TextUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TextUtilsTest {

	private TextUtils textUtils;

	@Before
	public void setUp() {
		textUtils = new TextUtils();
	}

	@Test
	public void testTransformText() {
		test("sometext", "sometext");
		test(" sometext ", "sometext");
		test(" sometext", "sometext");
		test("sometext ", "sometext");
		test("", null);
		test(" ", "");
		test(null, null);
	}

	private void test(String input, String expectedResult) {
		String result = textUtils.transformText(input);
		Assert.assertEquals(expectedResult, result);
	}
}
