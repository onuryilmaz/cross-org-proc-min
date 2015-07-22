package org.processmining.plugins.crossorgprocmin.utilities;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import org.processmining.contexts.uitopia.annotations.UIExportPlugin;
import org.processmining.framework.plugin.PluginContext;
import org.processmining.framework.plugin.annotations.Plugin;
import org.processmining.framework.plugin.annotations.PluginVariant;

/**
 * Exporter for {@link OrganizationalLogs}
 * 
 * @author onuryilmaz
 *
 */
@Plugin(name = "Cross-Organizational Analysis Export", returnLabels = {}, returnTypes = {}, parameterLabels = {
		"Cross-Organizational Analysis", "File" }, userAccessible = true)
@UIExportPlugin(description = "Cross-Organizational Analysis", extension = "coa")
public class OrganizationalLogsExport {

	/**
	 * 
	 * @param context
	 * @param analysis
	 * @param file
	 * @throws IOException
	 */
	@PluginVariant(variantLabel = "Cross-Organizational Analysis", requiredParameterLabels = { 0, 1 })
	public static void export(PluginContext context, OrganizationalLogs analysis, File file) throws IOException {
		export(analysis, file);
	}

	/**
	 * File writer
	 * 
	 * @param analysis
	 * @param file
	 * @throws IOException
	 */
	private static void export(OrganizationalLogs analysis, File file) throws IOException {

		FileOutputStream fileOut = new FileOutputStream(file);
		ObjectOutputStream out = new ObjectOutputStream(fileOut);
		out.writeObject(analysis);
		out.close();
		fileOut.close();

	}

}
