package org.javaadam.quickmatcher.common.widgets;

import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.widgets.Control;
import org.javaadam.quickmatcher.common.internal.widgets.TextWithCancelButton;

public class CancelButtonStaticHandler implements ICancelButtonHandler,
		MouseListener {

	private TextWithCancelButton text;

	public void init(final TextWithCancelButton text, final Control cancelButton) {
		this.text = text;
		cancelButton.addMouseListener(this);
	}

	public void mouseDoubleClick(final MouseEvent e) {
		// nothing to do
	}

	public void mouseDown(final MouseEvent e) {
		text.highlightClearImage();
	}

	public void mouseUp(final MouseEvent e) {
		text.resetClearImage();
	}

}
