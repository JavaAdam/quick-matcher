package org.javaadam.quickmatcher.jdt.internal;

import org.eclipse.jdt.ui.IPackagesViewPart;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
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
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.menus.WorkbenchWindowControlContribution;
import org.eclipse.ui.plugin.AbstractUIPlugin;

public class ToolbarControlContribution extends
		WorkbenchWindowControlContribution implements FocusListener,
		ModifyListener, DisposeListener, MouseListener, MouseMoveListener,
		MouseTrackListener {

	private static final String CLEAR_ICON_IMAGE_KEY = "org.eclipse.ui.internal.dialogs.CLEAR_ICON";
	private static final String DISABLED_CLEAR_ICON_IMAGE_KEY = "org.eclipse.ui.internal.dialogs.DCLEAR_ICON";

	private final TextUtils textUtils;

	private Image inactiveImage;
	private Image activeImage;
	private Image pressedImage;

	private Text text;
	private Label clearButton;

	private boolean isMouseInButton = true;
	private boolean isMouseMoveListenerRegistered = false;

	public ToolbarControlContribution() {
		this(null);
	}

	public ToolbarControlContribution(String id) {
		super(id);
		this.textUtils = new TextUtils();
	}

	@Override
	protected Control createControl(Composite parent) {
		Composite composite = createComposite(parent);
		Composite combinedComposite = createCombinedComposite(composite);

		text = createText(combinedComposite);
		clearButton = createClearButton(combinedComposite, text);

		initText(text);
		text.addModifyListener(this);
		text.addFocusListener(this);

		return composite;
	}

	private Composite createCombinedComposite(Composite composite) {
		Composite subComposite = new Composite(composite, SWT.BORDER);
		GridLayout gridLayout = new GridLayout(2, false);
		gridLayout.marginWidth = 0;
		gridLayout.marginHeight = 0;
		gridLayout.marginTop = 0;
		gridLayout.marginBottom = 0;
		gridLayout.horizontalSpacing = 0;
		gridLayout.verticalSpacing = 0;
		subComposite.setLayout(gridLayout);
		subComposite.setBackground(composite.getDisplay().getSystemColor(
				SWT.COLOR_LIST_BACKGROUND));
		subComposite.setLayoutData(GridDataFactory.fillDefaults().hint(100, 14)
				.create());
		return subComposite;
	}

	private void initText(Text text) {
		ProjectMatcherModel model = Activator.getModel();
		String matchString = model.getMatchString();
		if (matchString != null) {
			text.setText(matchString);
		} else {
			setEmptyText(text);
		}
	}

	private Text createText(Composite composite) {
		Text text = new Text(composite, SWT.NONE);
		changeFont(text);
		text.setLayoutData(GridDataFactory.fillDefaults().hint(80, 12).create());
		return text;
	}

	private Composite createComposite(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		GridLayout gridLayout = new GridLayout(1, false);
		gridLayout.marginWidth = 0;
		gridLayout.marginHeight = 0;
		gridLayout.marginTop = 2;
		gridLayout.marginBottom = 0;
		gridLayout.horizontalSpacing = 0;
		gridLayout.verticalSpacing = 0;
		composite.setLayout(gridLayout);
		return composite;
	}

	private Label createClearButton(Composite parent, final Text text) {
		initImages();
		inactiveImage = getImage(DISABLED_CLEAR_ICON_IMAGE_KEY);
		activeImage = getImage(CLEAR_ICON_IMAGE_KEY);
		pressedImage = new Image(text.getDisplay(), activeImage, SWT.IMAGE_GRAY);

		final Label clearButton = new Label(parent, SWT.NONE);
		clearButton.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER,
				false, false));
		clearButton.setImage(inactiveImage);
		clearButton.setBackground(parent.getDisplay().getSystemColor(
				SWT.COLOR_LIST_BACKGROUND));
		clearButton.setToolTipText(Messages.ToolTip_Clear);

		clearButton.addDisposeListener(this);
		clearButton.addMouseListener(this);
		clearButton.addMouseTrackListener(this);

		return clearButton;
	}

	private Image getImage(String key) {
		ImageRegistry imageRegistry = JFaceResources.getImageRegistry();
		if (imageRegistry != null) {
			if (imageRegistry.getDescriptor(key) != null) {
				return imageRegistry.getDescriptor(key).createImage();
			}
		}
		return null;
	}

	private void changeFont(Text text) {
		FontData[] fontData = text.getFont().getFontData();
		if (fontData.length > 0) {
			fontData[0].setHeight(fontData[0].getHeight() - 1);
			text.setFont(new Font(text.getDisplay(), fontData));
			text.addDisposeListener(this);
		}
	}

	private void setEmptyText(Text control) {
		Display display = control.getDisplay();
		control.setText(Messages.Text_EnterProjectName);
		control.setForeground(display.getSystemColor(SWT.COLOR_GRAY));
	}

	private boolean hasEmptyText(Text text) {
		return text.getText().equals(Messages.Text_EnterProjectName);
	}

	private void updatePackageExplorer() {
		IViewPart foundView = PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getActivePage()
				.findView(JavaUI.ID_PACKAGES);

		if (foundView instanceof IPackagesViewPart) {
			IPackagesViewPart packageExplorerView = (IPackagesViewPart) foundView;
			TreeViewer treeViewer = packageExplorerView.getTreeViewer();
			treeViewer.refresh();
		}

	}

	private void clearText(Text source) {
		source.setText("");
		source.setForeground(null);
	}

	private void initImages() {
		ImageRegistry imageRegistry = JFaceResources.getImageRegistry();
		if (imageRegistry != null) {
			if (imageRegistry.getDescriptor(CLEAR_ICON_IMAGE_KEY) == null) {
				ImageDescriptor descriptor = AbstractUIPlugin
						.imageDescriptorFromPlugin(PlatformUI.PLUGIN_ID,
								"$nl$/icons/full/etool16/clear_co.gif");
				if (descriptor != null) {
					imageRegistry.put(CLEAR_ICON_IMAGE_KEY, descriptor);
				}
			}

			if (imageRegistry.getDescriptor(DISABLED_CLEAR_ICON_IMAGE_KEY) == null) {
				ImageDescriptor descriptor = AbstractUIPlugin
						.imageDescriptorFromPlugin(PlatformUI.PLUGIN_ID,
								"$nl$/icons/full/dtool16/clear_co.gif");
				if (descriptor != null) {
					imageRegistry
							.put(DISABLED_CLEAR_ICON_IMAGE_KEY, descriptor);
				}
			}
		}
	}

	private boolean isMouseInButton(MouseEvent e) {
		Point buttonSize = clearButton.getSize();
		return 0 <= e.x && e.x < buttonSize.x && 0 <= e.y && e.y < buttonSize.y;
	}

	@Override
	public void modifyText(ModifyEvent e) {
		if (e.getSource() instanceof Text) {
			Text text = (Text) e.getSource();
			if (!hasEmptyText(text)) {
				ProjectMatcherModel model = Activator.getModel();
				model.setMatchString(textUtils.transformText(text.getText()));
				updatePackageExplorer();
			}
		}
	}

	@Override
	public void focusLost(FocusEvent e) {
		if (e.getSource() instanceof Text) {
			Text source = (Text) e.getSource();
			if (source.getText().isEmpty()) {
				setEmptyText(source);
			}
		}
	}

	@Override
	public void focusGained(FocusEvent e) {
		if (e.getSource() == text) {
			Text source = (Text) e.getSource();
			if (hasEmptyText(source)) {
				clearText(source);
			}
		}
	}

	@Override
	public void widgetDisposed(DisposeEvent e) {
		if (e.getSource() == text) {
			Text source = (Text) e.getSource();
			source.getFont().dispose();
		} else if (e.getSource() == clearButton) {
			if (inactiveImage != null) {
				inactiveImage.dispose();
			}
			if (activeImage != null) {
				activeImage.dispose();
			}
			if (pressedImage != null) {
				pressedImage.dispose();
			}
		}
	}

	@Override
	public void mouseDoubleClick(MouseEvent e) {
		// nothing to do
	}

	@Override
	public void mouseDown(MouseEvent e) {
		clearButton.setImage(pressedImage);
		clearButton.addMouseMoveListener(this);
		isMouseMoveListenerRegistered = true;
	}

	@Override
	public void mouseUp(MouseEvent e) {
		if (isMouseMoveListenerRegistered) {
			clearButton.removeMouseMoveListener(this);
			isMouseMoveListenerRegistered = false;
			boolean mouseInButton = isMouseInButton(e);
			clearButton.setImage(mouseInButton ? activeImage : inactiveImage);
			if (mouseInButton) {
				clearText(text);
				text.setFocus();
			}
		}
	}

	@Override
	public void mouseMove(MouseEvent e) {
		boolean mouseInButton = isMouseInButton(e);
		if (mouseInButton != isMouseInButton) {
			isMouseInButton = mouseInButton;
			clearButton.setImage(mouseInButton ? pressedImage : inactiveImage);
		}
	}

	@Override
	public void mouseEnter(MouseEvent e) {
		clearButton.setImage(activeImage);
	}

	@Override
	public void mouseExit(MouseEvent e) {
		clearButton.setImage(inactiveImage);
	}

	@Override
	public void mouseHover(MouseEvent e) {
		// nothing to do
	}
}
