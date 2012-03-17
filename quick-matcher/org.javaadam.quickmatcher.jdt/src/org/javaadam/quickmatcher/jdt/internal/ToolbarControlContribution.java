package org.javaadam.quickmatcher.jdt.internal;

import org.eclipse.jdt.ui.IPackagesViewPart;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.PlatformUI;
import org.javaadam.quickmatcher.common.contributions.AbstractQuickMatcherControlContribution;

public class ToolbarControlContribution extends
		AbstractQuickMatcherControlContribution {

	public ToolbarControlContribution() {
		this(null);
	}

	public ToolbarControlContribution(final String id) {
		super(id);
	}

	@Override
	protected int getWidth() {
		return 100;
	}

	@Override
	protected String getInitialMatcherText() {
		final ProjectMatcherModel model = Activator.getModel();
		final String matchString = model.getMatchString();
		if (matchString != null) {
			return matchString;
		} else {
			return null;
		}
	}

	@Override
	protected void matcherTextChanged(final String newFilterText) {
		final ProjectMatcherModel model = Activator.getModel();
		model.setMatchString(newFilterText);
		updatePackageExplorer();
	}

	private void updatePackageExplorer() {
		final IViewPart foundView = PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getActivePage()
				.findView(JavaUI.ID_PACKAGES);

		if (foundView instanceof IPackagesViewPart) {
			final IPackagesViewPart packageExplorerView = (IPackagesViewPart) foundView;
			final TreeViewer treeViewer = packageExplorerView.getTreeViewer();
			treeViewer.refresh();
		}

	}
}
