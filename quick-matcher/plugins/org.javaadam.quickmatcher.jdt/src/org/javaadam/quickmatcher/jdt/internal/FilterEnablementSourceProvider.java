package org.javaadam.quickmatcher.jdt.internal;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jdt.internal.ui.JavaPlugin;
import org.eclipse.jdt.internal.ui.filters.FilterDescriptor;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.ui.AbstractSourceProvider;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.ISources;
import org.eclipse.ui.IWindowListener;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.internal.services.IWorkbenchLocationService;
import org.eclipse.ui.services.IServiceLocator;

@SuppressWarnings("restriction")
public class FilterEnablementSourceProvider extends AbstractSourceProvider
		implements IPropertyChangeListener, IWindowListener, IPartListener {

	// copyied from org.eclipse.jdt.ui.actions.CustomFiltersActionGroup
	private static final String TAG_DUMMY_TO_TEST_EXISTENCE = "CustomFiltersActionGroup."
			+ JavaUI.ID_PACKAGES + ".TAG_DUMMY_TO_TEST_EXISTENCE";

	private static final String FILTER_ID = "org.eclipse.jdt.java.ui.filters.ProjectMatcherFilter";
	private boolean cachedEnablement;

	@Override
	public void dispose() {
		// does nothing
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Map getCurrentState() {
		Map state = new HashMap();
		state.put(FILTER_ID, cachedEnablement);
		return state;
	}

	@Override
	public String[] getProvidedSourceNames() {
		return new String[] { FILTER_ID };
	}

	@Override
	public void propertyChange(PropertyChangeEvent event) {
		if (event.getProperty().equals(FILTER_ID)
				&& event.getNewValue() instanceof Boolean) {
			propagateEnablement();
		}
	}

	@Override
	public void initialize(IServiceLocator locator) {
		super.initialize(locator);
		IWorkbenchLocationService wls = (IWorkbenchLocationService) locator
				.getService(IWorkbenchLocationService.class);
		IWorkbench workbench = wls.getWorkbench();
		workbench.addWindowListener(this);
	}

	@Override
	public void windowOpened(IWorkbenchWindow window) {
		window.getPartService().addPartListener(this);
		JavaPlugin.getDefault().getPreferenceStore()
				.addPropertyChangeListener(this);

		correctInitialState();
		propagateEnablement();
	}

	/**
	 * workaround: set initial filter enablement as it is defined in extension
	 * point.
	 */
	private void correctInitialState() {
		IPreferenceStore preferenceStore = JavaPlugin.getDefault()
				.getPreferenceStore();

		if (!preferenceStore.contains(TAG_DUMMY_TO_TEST_EXISTENCE)) {
			boolean filterInitialEnablement = true;

			FilterDescriptor[] filterDescriptors = FilterDescriptor
					.getFilterDescriptors(JavaUI.ID_PACKAGES);
			for (FilterDescriptor filterDescriptor : filterDescriptors) {
				if (filterDescriptor.getId().equals(FILTER_ID)) {
					filterInitialEnablement = filterDescriptor.isEnabled();
					break;
				}
			}

			preferenceStore.setDefault(FILTER_ID, filterInitialEnablement);
		}
	}

	@Override
	public void windowActivated(IWorkbenchWindow window) {
		// does nothing
	}

	@Override
	public void windowDeactivated(IWorkbenchWindow window) {
		// does nothing
	}

	@Override
	public void windowClosed(IWorkbenchWindow window) {
		// does nothing
	}

	@Override
	public void partActivated(IWorkbenchPart part) {
		// does nothing
	}

	@Override
	public void partBroughtToTop(IWorkbenchPart part) {
		// does nothing
	}

	@Override
	public void partClosed(IWorkbenchPart part) {

	}

	@Override
	public void partDeactivated(IWorkbenchPart part) {
		// does nothing
	}

	@Override
	public void partOpened(IWorkbenchPart part) {
		if (part.getSite().getId().equals(JavaUI.ID_PACKAGES)) {
			propagateEnablement();
		}
	}

	private void propagateEnablement() {
		IPreferenceStore preferenceStore = JavaPlugin.getDefault()
				.getPreferenceStore();

		boolean savedEnablement = preferenceStore.getBoolean(FILTER_ID);

		if (cachedEnablement != savedEnablement) {
			cachedEnablement = savedEnablement;
			fireStateChange();
		}
	}

	private void fireStateChange() {
		fireSourceChanged(ISources.WORKBENCH, FILTER_ID, cachedEnablement);
	}
}
