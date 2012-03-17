package org.javaadam.quickmatcher.common.internal.widgets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.MouseTrackListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.TypedListener;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.swt.IFocusService;

public class TextWithCancelButton extends Composite implements
		ITextContentWidget, MouseListener, MouseTrackListener,
		MouseMoveListener, DisposeListener, ModifyListener {

	private static final String textEmpty = Messages.TextWithCancelButton_empty_text;

	private static final String tooltipClear = Messages.TextWithCancelButton_tooltip_clear;

	private static final int BUTTON_HEIGHT = 16;
	private static final int BUTTON_WIDTH = BUTTON_HEIGHT;

	private static final boolean isMac = System.getProperty("os.name")
			.equalsIgnoreCase("Mac OS X");

	private Text text = null;

	private Label cancelButton = null;

	private boolean isMouseMoveListenerRegistered = false;
	private boolean isMouseInButton = false;

	private String lastText = "";

	private Image activeImage = null;
	private Image inactiveImage = null;

	public TextWithCancelButton(final Composite parent, final int fontSize) {
		super(parent, isMac ? SWT.NONE : SWT.BORDER);
		createControls(fontSize);
	}

	@Override
	public String getText() {
		checkWidget();
		return text.getText();
	}

	@Override
	public void setText(final String textContent) {
		checkWidget();
		if (textContent != null && textContent.length() != 0
				&& !textContent.equals(textEmpty)) {
			text.setForeground(null);
		}
		text.setText(textContent);
	}

	@Override
	public void setLayoutData(final Object layoutData) {
		if (layoutData instanceof GridData) {
			final GridData gridData = (GridData) layoutData;
			if (gridData.widthHint != SWT.DEFAULT) {
				final int width = gridData.widthHint;
				final int borderWidth = 2;
				gridData.widthHint = isMac ? width : width - borderWidth;
			}
		}
		super.setLayoutData(layoutData);

	}

	private void createControls(final int fontSize) {
		initImages();
		final GridLayout gridLayout = new GridLayout(isMac ? 1 : 2, false);
		gridLayout.marginWidth = 0;
		gridLayout.marginHeight = 0;
		gridLayout.marginTop = 0;
		gridLayout.marginBottom = 0;
		gridLayout.horizontalSpacing = 0;
		gridLayout.verticalSpacing = 0;
		setLayout(gridLayout);

		int style = SWT.SINGLE;
		if (isMac) {
			style = style | SWT.SEARCH | SWT.ICON_CANCEL;
		}
		text = new Text(this, style);
		changeFont(text, fontSize);

		final GridData gridData = new GridData(SWT.FILL, SWT.CENTER, true,
				false);
		if (!isMac) {
			gridData.heightHint = BUTTON_HEIGHT;
		}
		text.setLayoutData(gridData);
		setEmptyText(text);

		if (!isMac) {
			cancelButton = new Label(this, SWT.PUSH);
			cancelButton
					.setLayoutData(new GridData(BUTTON_WIDTH, BUTTON_WIDTH));
			cancelButton.setBackground(text.getBackground());
			cancelButton.addMouseListener(this);
			cancelButton.addMouseTrackListener(this);
		}
		text.addModifyListener(this);

		text.addFocusListener(new FocusListener() {

			@Override
			public void focusLost(final FocusEvent e) {
				if (text.getText().isEmpty()) {
					setEmptyText(text);
				}
			}

			@Override
			public void focusGained(final FocusEvent e) {
				if (hasEmptyText(text)) {
					clearText(text);
				}
			}
		});

		text.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(final MouseEvent e) {
				if (!text.isFocusControl()) {
					text.setFocus();
				}
			}

			@Override
			public void mouseDoubleClick(final MouseEvent e) {
				mouseUp(e);
			}
		});

		final IFocusService focusService = (IFocusService) PlatformUI
				.getWorkbench().getService(IFocusService.class);
		focusService
				.addFocusTracker(text,
						"org.javaadam.quickmatcher.common.widgets.TextWithCancelButton.textfield");
	}

	private void changeFont(final Text text, final int fontSize) {
		final FontData[] fontData = text.getFont().getFontData();
		if (fontData.length > 0) {
			fontData[0].setHeight(fontSize);
			text.setFont(new Font(text.getDisplay(), fontData));
			text.addDisposeListener(this);
		}
	}

	private void initImages() {
		activeImage = PlatformUI.getWorkbench().getSharedImages()
				.getImage(ISharedImages.IMG_ETOOL_CLEAR);
		inactiveImage = PlatformUI.getWorkbench().getSharedImages()
				.getImage(ISharedImages.IMG_ETOOL_CLEAR_DISABLED);
	}

	@Override
	public void mouseDown(final MouseEvent e) {
		if (!hasEmptyText(text) && text.getText().length() != 0) {
			cancelButton.addMouseMoveListener(this);
			isMouseMoveListenerRegistered = true;
		}
	}

	@Override
	public void mouseUp(final MouseEvent e) {
		if (isMouseMoveListenerRegistered) {
			cancelButton.removeMouseMoveListener(this);
			isMouseMoveListenerRegistered = false;
			final boolean mouseInButton = isMouseInButton(new Point(e.x, e.y));
			if (mouseInButton) {
				clearText(text);
				text.setFocus();
			}
		}
	}

	@Override
	public void mouseDoubleClick(final MouseEvent e) {
		// nothing to do
	}

	@Override
	public void mouseEnter(final MouseEvent e) {
		if (!hasEmptyText(text) && text.getText().length() != 0) {
			cancelButton.setImage(activeImage);
		}
	}

	@Override
	public void mouseExit(final MouseEvent e) {
		if (!hasEmptyText(text) && text.getText().length() != 0) {
			cancelButton.setImage(inactiveImage);
		}
	}

	@Override
	public void mouseHover(final MouseEvent e) {
		// nothing to do
	}

	@Override
	public void mouseMove(final MouseEvent e) {
		final boolean mouseInButton = isMouseInButton(new Point(e.x, e.y));
		if (!hasEmptyText(text) && mouseInButton != isMouseInButton) {
			isMouseInButton = mouseInButton;
			cancelButton.setImage(mouseInButton ? activeImage : inactiveImage);
		}
	}

	private boolean isMouseInButton(final Point mousePoint) {
		final Point buttonSize = cancelButton.getSize();
		return 0 <= mousePoint.x && mousePoint.x < buttonSize.x
				&& 0 <= mousePoint.y && mousePoint.y < buttonSize.y;
	}

	private void setEmptyText(final Text control) {
		final Display display = control.getDisplay();
		control.setText(textEmpty);
		control.setForeground(display.getSystemColor(SWT.COLOR_GRAY));
	}

	private boolean hasEmptyText(final Text text) {
		return text.getText().equals(textEmpty);
	}

	private void clearText(final Text source) {
		source.setText("");
		source.setForeground(null);
	}

	public void addModifyListener(final ModifyListener listener) {
		checkWidget();
		if (listener == null) {
			SWT.error(SWT.ERROR_NULL_ARGUMENT);
		}
		final TypedListener typedListener = new TypedListener(listener);
		addListener(SWT.Modify, typedListener);
	}

	public void removeModifyListener(final ModifyListener listener) {
		removeListener(SWT.Modify, listener);
	}

	public void clearText() {
		setEmptyText(text);
	}

	@Override
	public void widgetDisposed(final DisposeEvent e) {
		if (e.getSource() == text) {
			final Text source = (Text) e.getSource();
			source.getFont().dispose();
		}
	}

	@Override
	public void modifyText(final ModifyEvent e) {
		if (!hasEmptyText(text)) {
			final String newText = text.getText();
			if (!lastText.equals(newText)) {
				if (cancelButton != null) {
					if (newText.length() == 0) {
						cancelButton.setImage(null);
						cancelButton.setToolTipText(null);
					} else {
						if (cancelButton.getImage() == null) {
							final Point buttonLocation = cancelButton
									.getParent().toDisplay(
											cancelButton.getLocation());

							final Point cursorLocationOrig = cancelButton
									.getDisplay().getCursorLocation();

							final Point cursorLocation = new Point(
									cursorLocationOrig.x - buttonLocation.x,
									cursorLocationOrig.y - buttonLocation.y);

							cancelButton
									.setImage(isMouseInButton(cursorLocation) ? activeImage
											: inactiveImage);

							cancelButton.setToolTipText(tooltipClear);
						}
					}
				}
				TextWithCancelButton.this.notifyListeners(SWT.Modify, null);
				lastText = newText;
			}
		}
	}

}
