package org.javaadam.quickmatcher.jdt.internal;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {

	private static final String BUNDLE_NAME = "org.javaadam.jdt.quickmatcher.internal.messages";

	static {
		// load message values from bundle file
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	public static String Text_EnterProjectName;
	public static String ToolTip_Clear;
}
