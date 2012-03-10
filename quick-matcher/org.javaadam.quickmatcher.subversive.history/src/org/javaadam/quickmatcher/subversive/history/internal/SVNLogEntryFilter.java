package org.javaadam.quickmatcher.subversive.history.internal;

import org.eclipse.team.svn.core.connector.SVNLogEntry;
import org.eclipse.team.svn.ui.history.filter.ILogEntryFilter;

/**
 * Filters svn log entries when the author, the message or the revision is
 * containing the given filter text.
 * 
 * @author Adam Wehner
 * @since 1.0.0
 */
public class SVNLogEntryFilter implements ILogEntryFilter {

	private String textToAccept;

	public void setTextToAccept(final String textToAccept) {
		this.textToAccept = textToAccept;
	}

	public String getTextToAccept() {
		return textToAccept;
	}

	@Override
	public boolean accept(final SVNLogEntry logEntry) {
		final String author = logEntry.author.toLowerCase();
		final String message = logEntry.message.toLowerCase();
		final String revision = String.valueOf(logEntry.revision).toLowerCase();

		final String acceptedText = textToAccept.toLowerCase();

		if (author.contains(acceptedText) || message.contains(acceptedText)
				|| revision.contains(acceptedText)) {
			return true;
		} else {
			return false;
		}
	}

}
