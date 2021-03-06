/*******************************************************************************
 * Copyright (c) 2006, 2019 Mountainminds GmbH & Co. KG and Contributors
 * This program and the accompanying materials are made available under
 * the terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    Marc R. Hoffmann - initial API and implementation
 *
 * Adapted by Alexander Kapralov
 *
 ******************************************************************************/
package ru.capralow.dt.coverage.ui.launching;

import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.ui.AbstractLaunchConfigurationTab;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;

import com._1c.g5.v8.dt.core.platform.IResourceLookup;
import com._1c.g5.v8.dt.core.platform.IV8ProjectManager;

import ru.capralow.dt.coverage.core.ScopeUtils;
import ru.capralow.dt.coverage.core.launching.ICoverageLaunchConfigurationConstants;
import ru.capralow.dt.coverage.internal.ui.ContextHelp;
import ru.capralow.dt.coverage.internal.ui.CoverageUIPlugin;
import ru.capralow.dt.coverage.internal.ui.ScopeViewer;
import ru.capralow.dt.coverage.internal.ui.UIMessages;

/**
 * The "Coverage" tab of the launch configuration dialog.
 */
public class CoverageTab extends AbstractLaunchConfigurationTab {

	private ScopeViewer classesViewer;

	private IV8ProjectManager projectManager;
	private IResourceLookup resourceLookup;

	public CoverageTab(IV8ProjectManager projectManager, IResourceLookup resourceLookup) {
		this.projectManager = projectManager;
		this.resourceLookup = resourceLookup;
	}

	@Override
	public void createControl(Composite parent) {
		parent = new Composite(parent, SWT.NONE);
		ContextHelp.setHelp(parent, ContextHelp.COVERAGE_LAUNCH_TAB);
		GridLayout layout = new GridLayout();
		layout.verticalSpacing = 0;
		parent.setLayout(layout);
		setControl(parent);
		createAnalysisScope(parent);
	}

	private void createAnalysisScope(Composite parent) {
		Group group = new Group(parent, SWT.NONE);
		group.setLayoutData(new GridData(GridData.FILL_BOTH));
		group.setText(UIMessages.CoverageTabAnalysisScopeGroup_label);
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		group.setLayout(layout);
		classesViewer = new ScopeViewer(group, SWT.BORDER, projectManager, resourceLookup);
		GridData gd = new GridData(GridData.FILL_BOTH);
		gd.horizontalSpan = 2;
		classesViewer.getTable().setLayoutData(gd);
		classesViewer.addSelectionChangedListener(event -> {
			setDirty(true);
			updateErrorStatus();
			updateLaunchConfigurationDialog();
		});

		Button buttonSelectAll = createPushButton(group, UIMessages.SelectAllAction_label, null);
		buttonSelectAll.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				classesViewer.selectAll();
				setDirty(true);
				updateLaunchConfigurationDialog();
			}
		});
		buttonSelectAll.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL | GridData.HORIZONTAL_ALIGN_END));
		Button buttonDeselectAll = createPushButton(group, UIMessages.DeselectAllAction_label, null);
		buttonDeselectAll.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				classesViewer.deselectAll();
				setDirty(true);
				updateLaunchConfigurationDialog();
			}
		});
	}

	@Override
	public void setDefaults(ILaunchConfigurationWorkingCopy configuration) {
		// Нечего делать
	}

	@Override
	public void initializeFrom(ILaunchConfiguration configuration) {
		try {
			classesViewer.setInput(ScopeUtils.getOverallScope(configuration));
			classesViewer.setSelectedScope(ScopeUtils.getConfiguredScope(configuration));
		} catch (CoreException e) {
			CoverageUIPlugin.log(e);
		}
		updateErrorStatus();
		setDirty(false);
	}

	@Override
	public void performApply(ILaunchConfigurationWorkingCopy configuration) {
		if (isDirty()) {
			final List<String> ids = ScopeUtils.writeScope(classesViewer.getSelectedScope());
			configuration.setAttribute(ICoverageLaunchConfigurationConstants.ATTR_SCOPE_IDS, ids);
		}
	}

	@Override
	public boolean isValid(ILaunchConfiguration launchConfig) {
		return !classesViewer.getSelection().isEmpty();
	}

	@Override
	public String getName() {
		return UIMessages.CoverageTab_title;
	}

	@Override
	public Image getImage() {
		return CoverageUIPlugin.getImage(CoverageUIPlugin.EVIEW_COVERAGE);
	}

	private void updateErrorStatus() {
		if (classesViewer.getSelection().isEmpty()) {
			setErrorMessage(UIMessages.CoverageTabEmptyAnalysisScope_message);
		} else {
			setErrorMessage(null);
		}
	}

}
