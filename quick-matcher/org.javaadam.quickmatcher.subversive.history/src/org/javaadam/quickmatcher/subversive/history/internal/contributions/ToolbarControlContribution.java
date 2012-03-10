package org.javaadam.quickmatcher.subversive.history.internal.contributions;

import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.widgets.Control;
import org.eclipse.team.svn.ui.history.LogMessagesComposite;
import org.eclipse.team.svn.ui.history.SVNHistoryPage;
import org.eclipse.team.svn.ui.history.filter.CommentLogEntryFilter;
import org.eclipse.team.ui.history.IHistoryPage;
import org.eclipse.team.ui.history.IHistoryView;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.PlatformUI;
import org.javaadam.quickmatcher.common.contributions.AbstractQuickMatcherControlContribution;

public class ToolbarControlContribution extends
		AbstractQuickMatcherControlContribution {

	private CommentLogEntryFilter commentLogEntryFilter = null;
	private SVNHistoryPage currentPage = null;

	public ToolbarControlContribution() {
		this(null);
	}

	public ToolbarControlContribution(String id) {
		super(id);
	}

	@Override
	protected int getWidth() {
		return 100;
	}

	@Override
	protected void filterChanged(String newFilterText) {
		IViewPart foundView = PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getActivePage()
				.findView("org.eclipse.team.ui.GenericHistoryView");

		if (foundView instanceof IHistoryView) {
			IHistoryView historyView = (IHistoryView) foundView;
			IHistoryPage historyPage = historyView.getHistoryPage();
			if (historyPage instanceof SVNHistoryPage) {
				final SVNHistoryPage svnHistoryPage = (SVNHistoryPage) historyPage;
				if (commentLogEntryFilter == null) {
					commentLogEntryFilter = new CommentLogEntryFilter();
				}

				if (currentPage != null && currentPage != svnHistoryPage) {
					// remove from old page and refresh if needed and possible
					Control control = currentPage.getControl();
					if (!control.isDisposed()) {
						currentPage.removeFilter(commentLogEntryFilter);
						((LogMessagesComposite) control)
								.refresh(LogMessagesComposite.REFRESH_ALL);
					}
				}
				if (currentPage == svnHistoryPage) {
					updateFilter(newFilterText, currentPage.getControl());
				} else {
					currentPage = svnHistoryPage;
					Control control = currentPage.getControl();
					control.addDisposeListener(new DisposeListener() {

						@Override
						public void widgetDisposed(DisposeEvent e) {
							clearFilterText();
						}
					});
					currentPage.addFilter(commentLogEntryFilter);
					updateFilter(newFilterText, currentPage.getControl());
				}
			}
		}
	}

	private void updateFilter(String newFilterText, Control control) {
		String filterText = "*" + newFilterText + "*";
		if (!filterText.equals(commentLogEntryFilter.getCommentToAccept())) {
			commentLogEntryFilter.setCommentToAccept(filterText);
			((LogMessagesComposite) control)
					.refresh(LogMessagesComposite.REFRESH_ALL);
		}
	}
}
