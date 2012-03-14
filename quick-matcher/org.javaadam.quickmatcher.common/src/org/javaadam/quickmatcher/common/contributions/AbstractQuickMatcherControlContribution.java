package org.javaadam.quickmatcher.common.contributions;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.menus.WorkbenchWindowControlContribution;
import org.javaadam.quickmatcher.common.internal.widgets.TextWithCancelButton;

public abstract class AbstractQuickMatcherControlContribution extends
		WorkbenchWindowControlContribution {

	private static final boolean isMac = System.getProperty("os.name")
			.equalsIgnoreCase("Mac OS X");

	private TextWithCancelButton matcherControl = null;

	public AbstractQuickMatcherControlContribution() {
		super();
	}

	public AbstractQuickMatcherControlContribution(final String id) {
		super(id);
	}

	protected int getWidth() {
		return SWT.DEFAULT;
	}

	protected int getHeight() {
		return isMac ? SWT.DEFAULT : 14;
	}

	protected String getInitialMatcherText() {
		return null;
	}

	protected abstract void matcherTextChanged(String newText);

	@Override
	protected final Control createControl(final Composite parent) {
		final Composite composite = createComposite(parent);
		final ModifyListener modifyListener = new ModifyListener() {

			@Override
			public void modifyText(final ModifyEvent e) {
				final String newFilterText = ((TextWithCancelButton) e.widget)
						.getText();
				matcherTextChanged(newFilterText);
			}
		};
		matcherControl = new TextWithCancelButton(composite);
		matcherControl.setLayoutData(GridDataFactory.fillDefaults()
				.hint(getWidth(), getHeight()).create());
		initText();
		matcherControl.addModifyListener(modifyListener);
		return composite;
	}

	public final void clearMatcherText() {
		matcherControl.clearText();
	}

	private Composite createComposite(final Composite parent) {
		final Composite composite = new Composite(parent, SWT.NONE);
		final GridLayout gridLayout = new GridLayout();
		gridLayout.marginWidth = 2;
		gridLayout.marginHeight = 0;
		gridLayout.marginTop = isMac ? 0 : 2;
		gridLayout.marginBottom = 0;
		gridLayout.horizontalSpacing = 0;
		gridLayout.verticalSpacing = 0;
		composite.setLayout(gridLayout);
		return composite;
	}

	private void initText() {
		final String initialFilterText = getInitialMatcherText();
		if (initialFilterText != null && initialFilterText.trim().length() > 0) {
			matcherControl.setText(initialFilterText);
		} else {
			matcherControl.clearText();
		}
	}
}
