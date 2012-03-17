package org.javaadam.jdt.quickmatcher;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.when;

import org.eclipse.core.resources.IProject;
import org.eclipse.jdt.core.IJavaProject;
import org.javaadam.quickmatcher.jdt.internal.Activator;
import org.javaadam.quickmatcher.jdt.internal.ProjectMatcherFilter;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ProjectMatcherFilterTest {

	private static final String PROJECT_NAME = "someProjectName";

	@Mock
	IProject iProject;

	@Mock
	IJavaProject javaProject;

	private Object anyProject = new Object();

	private ProjectMatcherFilter filter;

	@Before
	public void setUp() {
		filter = new ProjectMatcherFilter();
		when(javaProject.getProject()).thenReturn(iProject);
		when(iProject.getProject()).thenReturn(iProject);
		when(iProject.getName()).thenReturn(PROJECT_NAME);
	}

	@Test
	public void selectMatchingNull() {
		Activator.getModel().setMatchString(null);
		assertTrue(filter.select(null, null, iProject));
		assertTrue(filter.select(null, null, javaProject));
		assertTrue(filter.select(null, null, anyProject));
	}

	@Test
	public void selectMatching() {
		Activator.getModel().setMatchString("Project");
		assertTrue(filter.select(null, null, iProject));
		assertTrue(filter.select(null, null, javaProject));
		assertTrue(filter.select(null, null, anyProject));
	}

	@Test
	public void selectNotMatching() {
		Activator.getModel().setMatchString("JavaProject");
		assertFalse(filter.select(null, null, iProject));
		assertFalse(filter.select(null, null, javaProject));
		assertTrue(filter.select(null, null, anyProject));
	}
}
