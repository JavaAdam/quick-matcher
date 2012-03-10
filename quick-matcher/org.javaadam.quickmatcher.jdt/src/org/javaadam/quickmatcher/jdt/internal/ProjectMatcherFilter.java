package org.javaadam.quickmatcher.jdt.internal;

import org.eclipse.core.resources.IProject;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;

public class ProjectMatcherFilter extends ViewerFilter {

	@Override
	public boolean select(Viewer viewer, Object parentElement, Object element) {
		String matchString = Activator.getModel().getMatchString();

		if (matchString == null) {
			return true;
		} else {
			IProject project = null;
			if (element instanceof IProject) {
				project = (IProject) element;
			} else if (element instanceof IJavaProject) {
				IJavaProject javaProject = (IJavaProject) element;
				project = javaProject.getProject();
			}

			if (project != null) {
				String projectName = project.getProject().getName();
				return projectName.contains(matchString);
			}
		}

		return true;
	}

}
