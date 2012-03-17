package org.javaadam.quickmatcher.subversive.history.internal.contributions;

import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.widgets.Control;
import org.eclipse.team.svn.ui.history.LogMessagesComposite;
import org.eclipse.team.svn.ui.history.SVNHistoryPage;
import org.eclipse.team.ui.history.IHistoryPage;
import org.eclipse.team.ui.history.IHistoryView;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.PlatformUI;
import org.javaadam.quickmatcher.common.contributions.AbstractQuickMatcherControlContribution;
import org.javaadam.quickmatcher.subversive.history.internal.SVNLogEntryFilter;

public class ToolbarControlContribution extends
		AbstractQuickMatcherControlContribution {

	private SVNLogEntryFilter svnLogEntryFilter = null;
	private SVNHistoryPage currentPage = null;

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
	protected void matcherTextChanged(final String newFilterText) {
		final IViewPart foundView = PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getActivePage()
				.findView("org.eclipse.team.ui.GenericHistoryView");

		if (foundView instanceof IHistoryView) {
			final IHistoryView historyView = (IHistoryView) foundView;
			final IHistoryPage historyPage = historyView.getHistoryPage();
			if (historyPage instanceof SVNHistoryPage) {
				final SVNHistoryPage svnHistoryPage = (SVNHistoryPage) historyPage;
				if (svnLogEntryFilter == null) {
					svnLogEntryFilter = new SVNLogEntryFilter();
				}

				if (currentPage != null && currentPage != svnHistoryPage) {
					// remove from old page and refresh if needed and possible
					final Control control = currentPage.getControl();
					if (!control.isDisposed()) {
						currentPage.removeFilter(svnLogEntryFilter);
						((LogMessagesComposite) control)
								.refresh(LogMessagesComposite.REFRESH_ALL);
					}
				}
				if (currentPage == svnHistoryPage) {
					updateFilter(newFilterText, currentPage.getControl());
				} else {
					currentPage = svnHistoryPage;
					final Control control = currentPage.getControl();
					control.addDisposeListener(new DisposeListener() {

						@Override
						public void widgetDisposed(final DisposeEvent e) {
							clearMatcherText();
						}
					});
					currentPage.addFilter(svnLogEntryFilter);
					updateFilter(newFilterText, currentPage.getControl());
				}
			}
		}
	}

	private void updateFilter(final String newFilterText, final Control control) {
		if (!newFilterText.equals(svnLogEntryFilter.getTextToAccept())) {
			svnLogEntryFilter.setTextToAccept(newFilterText);
			((LogMessagesComposite) control)
					.refresh(LogMessagesComposite.REFRESH_ALL);
		}
	}
}
