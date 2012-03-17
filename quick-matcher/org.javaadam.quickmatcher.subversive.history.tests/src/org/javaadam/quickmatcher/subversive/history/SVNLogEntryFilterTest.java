package org.javaadam.quickmatcher.subversive.history;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.eclipse.team.svn.core.connector.SVNLogEntry;
import org.javaadam.quickmatcher.subversive.history.internal.SVNLogEntryFilter;
import org.junit.Before;
import org.junit.Test;

public class SVNLogEntryFilterTest {

	private SVNLogEntryFilter filter;
	private final String author = "someauthor";
	private final String message = "some message";
	private final long revision = 12345;

	@Before
	public void setUp() {
		filter = new SVNLogEntryFilter();
	}

	@Test
	public void acceptAuthor() {
		final SVNLogEntry entry = new SVNLogEntry(revision, 0, author, message,
				null, false);

		filter.setTextToAccept("auth");
		assertTrue(filter.accept(entry));

		filter.setTextToAccept("some");
		assertTrue(filter.accept(entry));

		filter.setTextToAccept("or");
		assertTrue(filter.accept(entry));

		filter.setTextToAccept("HoR");
		assertTrue(filter.accept(entry));

		filter.setTextToAccept(author);
		assertTrue(filter.accept(entry));

		filter.setTextToAccept("so*or");
		assertTrue(filter.accept(entry));

		filter.setTextToAccept("so*");
		assertTrue(filter.accept(entry));

		filter.setTextToAccept("*or");
		assertTrue(filter.accept(entry));

		filter.setTextToAccept("*so*a*or*");
		assertTrue(filter.accept(entry));

		filter.setTextToAccept("");
		assertTrue(filter.accept(entry));

		filter.setTextToAccept(" ");
		assertTrue(filter.accept(entry));

		filter.setTextToAccept("another");
		assertFalse(filter.accept(entry));

		filter.setTextToAccept(null);
		assertFalse(filter.accept(entry));
	}

	@Test
	public void acceptMessage() {
		final SVNLogEntry entry = new SVNLogEntry(revision, 0, author, message,
				null, false);

		filter.setTextToAccept("sa");
		assertTrue(filter.accept(entry));

		filter.setTextToAccept("some");
		assertTrue(filter.accept(entry));

		filter.setTextToAccept("ge");
		assertTrue(filter.accept(entry));

		filter.setTextToAccept(message);
		assertTrue(filter.accept(entry));

		filter.setTextToAccept("some ");
		assertTrue(filter.accept(entry));

		filter.setTextToAccept("me me");
		assertTrue(filter.accept(entry));

		filter.setTextToAccept("");
		assertTrue(filter.accept(entry));

		filter.setTextToAccept("another");
		assertFalse(filter.accept(entry));

		filter.setTextToAccept(null);
		assertFalse(filter.accept(entry));
	}

	@Test
	public void acceptRevision() {
		final SVNLogEntry entry = new SVNLogEntry(revision, 0, author, message,
				null, false);

		filter.setTextToAccept("34");
		assertTrue(filter.accept(entry));

		filter.setTextToAccept("123");
		assertTrue(filter.accept(entry));

		filter.setTextToAccept("45");
		assertTrue(filter.accept(entry));

		filter.setTextToAccept(String.valueOf(revision));
		assertTrue(filter.accept(entry));

		filter.setTextToAccept("");
		assertTrue(filter.accept(entry));

		filter.setTextToAccept("876");
		assertFalse(filter.accept(entry));

		filter.setTextToAccept(null);
		assertFalse(filter.accept(entry));
	}

	@Test
	public void acceptTextIsNull() {
		final SVNLogEntry entry = new SVNLogEntry(0, 0, null, null, null, false);

		filter.setTextToAccept("something");
		assertFalse(filter.accept(entry));

		filter.setTextToAccept(null);
		assertFalse(filter.accept(entry));
	}

	@Test
	public void getTextToAccept() {
		filter.setTextToAccept(message);
		assertEquals(message, filter.getTextToAccept());
	}
}
