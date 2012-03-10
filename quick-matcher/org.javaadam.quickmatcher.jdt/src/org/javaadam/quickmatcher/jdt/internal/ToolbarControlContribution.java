package org.javaadam.quickmatcher.jdt.internal;

import org.eclipse.jdt.ui.IPackagesViewPart;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.PlatformUI;
import org.javaadam.quickmatcher.common.contributions.AbstractQuickMatcherControlContribution;

public class ToolbarControlContribution extends AbstractQuickMatcherControlContribution{

	private final TextUtils textUtils;
	
	public ToolbarControlContribution() {
		this(null);
	}

	public ToolbarControlContribution(String id) {
		super(id);
		this.textUtils = new TextUtils();
	}
	
	@Override
	protected int getWidth() {
		return 100;
	}

	@Override
	protected String getInitialFilterText() {
		ProjectMatcherModel model = Activator.getModel();
		String matchString = model.getMatchString();
		if (matchString != null) {
			return matchString;
		} else {
			return null;
		}
	}

	@Override
	protected void filterChanged(String newFilterText) {
		ProjectMatcherModel model = Activator.getModel();
		model.setMatchString(textUtils.transformText(newFilterText));
		updatePackageExplorer();
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
}
