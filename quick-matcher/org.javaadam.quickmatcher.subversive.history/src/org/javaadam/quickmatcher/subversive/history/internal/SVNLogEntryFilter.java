package org.javaadam.quickmatcher.subversive.history.internal;

import org.eclipse.team.svn.core.connector.SVNLogEntry;
import org.eclipse.team.svn.ui.history.filter.ILogEntryFilter;
import org.javaadam.quickmatcher.common.TextMatcher;

/**
 * Filters svn log entries when the author, the message or the revision is
 * containing the given filter text.
 * 
 * @author Adam Wehner
 * @since 1.0.0
 */
public class SVNLogEntryFilter implements ILogEntryFilter {

	private final TextMatcher filterUtils;

	private String textToAccept;

	public SVNLogEntryFilter() {
		filterUtils = new TextMatcher();
	}

	public void setTextToAccept(final String textToAccept) {
		this.textToAccept = textToAccept;
	}

	public String getTextToAccept() {
		return textToAccept;
	}

	@Override
	public boolean accept(final SVNLogEntry logEntry) {
		final String author = logEntry.author;
		final String message = logEntry.message;
		final String revision = String.valueOf(logEntry.revision);

		if (matches(author) || matches(message) || matches(revision)) {
			return true;
		}
		return false;
	}

	private boolean matches(final String text) {
		return filterUtils.matches(text, textToAccept);
	}
}
