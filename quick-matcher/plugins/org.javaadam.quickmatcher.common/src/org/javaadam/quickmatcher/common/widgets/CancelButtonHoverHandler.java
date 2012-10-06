package org.javaadam.quickmatcher.common.widgets;

import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseTrackListener;
import org.eclipse.swt.widgets.Control;
import org.javaadam.quickmatcher.common.internal.widgets.TextWithCancelButton;

public class CancelButtonHoverHandler implements ICancelButtonHandler,
		MouseTrackListener {

	private TextWithCancelButton text;

	public void init(final TextWithCancelButton text, final Control cancelButton) {
		this.text = text;
		cancelButton.addMouseTrackListener(this);
	}

	public void mouseHover(final MouseEvent e) {
		// nothing to do
	}

	public void mouseEnter(final MouseEvent e) {
		text.highlightClearImage();
	}

	public void mouseExit(final MouseEvent e) {
		text.resetClearImage();
	}

}
