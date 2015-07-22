package org.processmining.plugins.crossorgprocmin.utilities.vis;

import javax.swing.JComponent;

import org.processmining.contexts.uitopia.annotations.Visualizer;
import org.processmining.framework.plugin.PluginContext;
import org.processmining.framework.plugin.annotations.Plugin;
import org.processmining.framework.plugin.annotations.PluginVariant;
import org.processmining.plugins.crossorgprocmin.utilities.OrganizationalLogs;

/**
 * Visualizer for {@link OrganizationalLogs} for test purposes
 * 
 * @author onuryilmaz
 *
 */
@Plugin(name = "Mismatch Patterns w/ Clustering (Test Mode)", returnLabels = { "Cross-Organizational Analysis Report" }, returnTypes = { JComponent.class }, parameterLabels = { "Cross-Organizational Analysis Data" }, userAccessible = false)
@Visualizer
public class OrganizationalLogsPerformanceTestVisualizer {

	@PluginVariant(requiredParameterLabels = { 0 })
	public JComponent visualize(PluginContext context, OrganizationalLogs logs) {

		OrganizationalLogPerformanceMainPanel logMainPanel = new OrganizationalLogPerformanceMainPanel();
		logMainPanel.testMode = true;
		return logMainPanel.create(logs);
	}
}
