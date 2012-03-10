package org.javaadam.quickmatcher.common.contributions;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.menus.WorkbenchWindowControlContribution;
import org.javaadam.quickmatcher.common.internal.widgets.TextWithCancelButton;

public abstract class AbstractQuickMatcherControlContribution extends WorkbenchWindowControlContribution {

	private static final boolean isMac = System.getProperty("os.name") //$NON-NLS-1$
			.equalsIgnoreCase("Mac OS X"); //$NON-NLS-1$

	private TextWithCancelButton filterControl = null;

	public AbstractQuickMatcherControlContribution() {
		super();
	}

	public AbstractQuickMatcherControlContribution(String id) {
		super(id);
	}

	protected int getWidth() {
		return SWT.DEFAULT;
	}

	protected String getInitialFilterText() {
		return null;
	}

	protected abstract void filterChanged(String newFilterText);

	@Override
	protected final Control createControl(Composite parent) {
		Composite composite = createComposite(parent);
		ModifyListener modifyListener = new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				String newFilterText = ((TextWithCancelButton) e.widget).getText();
				filterChanged(newFilterText);
			}
		};
		filterControl = new TextWithCancelButton(composite);
		filterControl.setLayoutData(new GridData(getWidth(), SWT.DEFAULT));
		initText();
		filterControl.addModifyListener(modifyListener);
		return composite;
	}

	public final void clearFilterText() {
		filterControl.clearText();
	}

	private Composite createComposite(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		GridLayout gridLayout = new GridLayout();
		gridLayout.marginWidth = 2;
		gridLayout.marginHeight = 0;
		gridLayout.marginTop = isMac ? 0 : 1;
		gridLayout.marginBottom = 0;
		gridLayout.horizontalSpacing = 0;
		gridLayout.verticalSpacing = 0;
		composite.setLayout(gridLayout);
		return composite;
	}

	private void initText() {
		String initialFilterText = getInitialFilterText();
		if (initialFilterText != null && initialFilterText.trim().length() > 0) {
			filterControl.setText(initialFilterText);
		}
		else {
			filterControl.clearText();
		}
	}
}
