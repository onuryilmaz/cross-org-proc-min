package org.processmining.plugins.crossorgprocmin.utilities;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;

import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.processmining.contexts.uitopia.annotations.UIImportPlugin;
import org.processmining.framework.abstractplugins.AbstractImportPlugin;
import org.processmining.framework.plugin.PluginContext;
import org.processmining.framework.plugin.annotations.Plugin;
import org.processmining.framework.plugin.annotations.PluginVariant;

/**
 * Importer for {@link OrganizationalLogs}
 * 
 * @author onuryilmaz
 *
 */
@Plugin(name = "Cross-Organizational Analysis Import", parameterLabels = { "Filename" }, returnLabels = { "Cross-Organizational Analysis" }, returnTypes = { OrganizationalLogs.class })
@UIImportPlugin(description = "Cross-Organizational Analysis", extensions = { "coa" })
public class OrganizationalLogsImport extends AbstractImportPlugin {

	/**
	 * {@inheritDoc}
	 */
	protected FileFilter getFileFilter() {
		return new FileNameExtensionFilter("Cross-Organizational Analysis", "COA");
	}

	/**
	 * {@inheritDoc}
	 */
	@PluginVariant(variantLabel = "Cross-Organizational Analysis", requiredParameterLabels = { 0, 1, 2 })
	protected Object importFromStream(PluginContext context, InputStream input, String filename, long fileSizeInBytes)
			throws IOException {

		OrganizationalLogs e = null;
		try {
			ObjectInputStream in = new ObjectInputStream(input);
			e = (OrganizationalLogs) in.readObject();
			in.close();
			input.close();
		} catch (IOException i) {
			i.printStackTrace();

		} catch (ClassNotFoundException c) {
			c.printStackTrace();

		}

		return e;
	}

}
