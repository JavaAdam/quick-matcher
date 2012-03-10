package org.javaadam.quickmatcher.common.internal.widgets;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "org.javaadam.quickmatcher.common.internal.widgets.messages"; //$NON-NLS-1$
	public static String TextWithCancelButton_empty_text;
	public static String TextWithCancelButton_tooltip_clear;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
