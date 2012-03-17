package org.javaadam.quickmatcher.jdt.internal;

import org.eclipse.core.resources.IProject;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.javaadam.quickmatcher.common.TextMatcher;

public class ProjectMatcherFilter extends ViewerFilter {

	private final TextMatcher textMatcher;

	public ProjectMatcherFilter() {
		textMatcher = new TextMatcher();
	}

	@Override
	public boolean select(final Viewer viewer, final Object parentElement,
			final Object element) {
		final String matchString = Activator.getModel().getMatchString();

		if (matchString == null) {
			return true;
		} else {
			IProject project = null;
			if (element instanceof IProject) {
				project = (IProject) element;
			} else if (element instanceof IJavaProject) {
				final IJavaProject javaProject = (IJavaProject) element;
				project = javaProject.getProject();
			}

			if (project != null) {
				final String projectName = project.getProject().getName();
				return textMatcher.matches(projectName, matchString);
			}
		}

		return true;
	}

}
