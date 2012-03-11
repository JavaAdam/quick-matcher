package org.javaadam.quickmatcher.common.internal.widgets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.MouseTrackListener;
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
		MouseMoveListener {

	private static final String textEmpty = Messages.TextWithCancelButton_empty_text;

	private static final String tooltipClear = Messages.TextWithCancelButton_tooltip_clear;

	private static final int BUTTON_HEIGHT = 16;
	private static final int BUTTON_WIDTH = BUTTON_HEIGHT;

	private static final boolean isMac = System.getProperty("os.name") //$NON-NLS-1$
			.equalsIgnoreCase("Mac OS X"); //$NON-NLS-1$

	private Text text = null;

	private Label cancelButton = null;

	private boolean isMouseMoveListenerRegistered = false;
	private boolean isMouseInButton = false;

	private String lastText = ""; //$NON-NLS-1$

	private Image activeImage = null;
	private Image inactiveImage = null;

	public TextWithCancelButton(Composite parent) {
		super(parent, isMac ? SWT.NONE : SWT.BORDER);
		createControls();
	}

	@Override
	public String getText() {
		checkWidget();
		return text.getText();
	}

	@Override
	public void setText(String textContent) {
		checkWidget();
		if (textContent != null && textContent.length() != 0
				&& !textContent.equals(textEmpty)) {
			text.setForeground(null);
		}
		text.setText(textContent);
	}

	@Override
	public void setLayoutData(Object layoutData) {
		if (layoutData instanceof GridData) {
			GridData gridData = (GridData) layoutData;
			if (gridData.widthHint != SWT.DEFAULT) {
				int width = gridData.widthHint;
				int borderWidth = 2;
				gridData.widthHint = isMac ? width : width - borderWidth;
			}
		}
		super.setLayoutData(layoutData);

	}

	private void createControls() {
		initImages();
		GridLayout gridLayout = new GridLayout(isMac ? 1 : 2, false);
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
		GridData gridData = new GridData(SWT.FILL, SWT.CENTER, true, false);
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
			cancelButton.setToolTipText(tooltipClear);
			cancelButton.addMouseListener(this);
			cancelButton.addMouseTrackListener(this);
		}
		final Label cancelButtonFinal = cancelButton;
		text.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				if (!hasEmptyText(text)) {
					String newText = text.getText();
					if (!lastText.equals(newText)) {
						if (cancelButtonFinal != null) {
							if (newText.length() == 0) {
								cancelButtonFinal.setImage(null);
							} else {
								if (cancelButtonFinal.getImage() == null) {
									Point buttonLocation = cancelButtonFinal
											.getParent().toDisplay(
													cancelButtonFinal
															.getLocation());
									Point cursorLocationOrig = cancelButtonFinal
											.getDisplay().getCursorLocation();
									Point cursorLocation = new Point(
											cursorLocationOrig.x
													- buttonLocation.x,
											cursorLocationOrig.y
													- buttonLocation.y);
									cancelButtonFinal
											.setImage(isMouseInButton(cursorLocation) ? activeImage
													: inactiveImage);
								}
							}
						}
						TextWithCancelButton.this.notifyListeners(SWT.Modify,
								null);
						lastText = newText;
					}
				}
			}
		});

		text.addFocusListener(new FocusListener() {

			@Override
			public void focusLost(FocusEvent e) {
				if (text.getText().isEmpty()) {
					setEmptyText(text);
				}
			}

			@Override
			public void focusGained(FocusEvent e) {
				if (hasEmptyText(text)) {
					clearText(text);
				}
			}
		});

		text.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				if (!text.isFocusControl()) {
					text.setFocus();
				}
			}

			@Override
			public void mouseDoubleClick(MouseEvent e) {
				mouseUp(e);
			}
		});

		IFocusService focusService = (IFocusService) PlatformUI.getWorkbench()
				.getService(IFocusService.class);
		focusService
				.addFocusTracker(text,
						"org.javaadam.quickmatcher.common.widgets.TextWithCancelButton.textfield"); //$NON-NLS-1$
	}

	private void initImages() {
		activeImage = PlatformUI.getWorkbench().getSharedImages()
				.getImage(ISharedImages.IMG_ETOOL_CLEAR);
		inactiveImage = PlatformUI.getWorkbench().getSharedImages()
				.getImage(ISharedImages.IMG_ETOOL_CLEAR_DISABLED);
	}

	@Override
	public void mouseDown(MouseEvent e) {
		if (!hasEmptyText(text) && text.getText().length() != 0) {
			cancelButton.addMouseMoveListener(this);
			isMouseMoveListenerRegistered = true;
		}
	}

	@Override
	public void mouseUp(MouseEvent e) {
		if (isMouseMoveListenerRegistered) {
			cancelButton.removeMouseMoveListener(this);
			isMouseMoveListenerRegistered = false;
			boolean mouseInButton = isMouseInButton(new Point(e.x, e.y));
			if (mouseInButton) {
				clearText(text);
				text.setFocus();
			}
		}
	}

	@Override
	public void mouseDoubleClick(MouseEvent e) {
		// nothing to do
	}

	@Override
	public void mouseEnter(MouseEvent e) {
		if (!hasEmptyText(text) && text.getText().length() != 0) {
			cancelButton.setImage(activeImage);
		}
	}

	@Override
	public void mouseExit(MouseEvent e) {
		if (!hasEmptyText(text) && text.getText().length() != 0) {
			cancelButton.setImage(inactiveImage);
		}
	}

	@Override
	public void mouseHover(MouseEvent e) {
		// nothing to do
	}

	@Override
	public void mouseMove(MouseEvent e) {
		boolean mouseInButton = isMouseInButton(new Point(e.x, e.y));
		if (!hasEmptyText(text) && mouseInButton != isMouseInButton) {
			isMouseInButton = mouseInButton;
			cancelButton.setImage(mouseInButton ? activeImage : inactiveImage);
		}
	}

	private boolean isMouseInButton(Point mousePoint) {
		Point buttonSize = cancelButton.getSize();
		return 0 <= mousePoint.x && mousePoint.x < buttonSize.x
				&& 0 <= mousePoint.y && mousePoint.y < buttonSize.y;
	}

	private void setEmptyText(Text control) {
		Display display = control.getDisplay();
		control.setText(textEmpty);
		control.setForeground(display.getSystemColor(SWT.COLOR_GRAY));
	}

	private boolean hasEmptyText(Text text) {
		return text.getText().equals(textEmpty);
	}

	private void clearText(Text source) {
		source.setText(""); //$NON-NLS-1$
		source.setForeground(null);
	}

	public void addModifyListener(ModifyListener listener) {
		checkWidget();
		if (listener == null) {
			SWT.error(SWT.ERROR_NULL_ARGUMENT);
		}
		TypedListener typedListener = new TypedListener(listener);
		addListener(SWT.Modify, typedListener);
	}

	public void removeModifyListener(ModifyListener listener) {
		removeListener(SWT.Modify, listener);
	}

	public void clearText() {
		setEmptyText(text);
	}

}
