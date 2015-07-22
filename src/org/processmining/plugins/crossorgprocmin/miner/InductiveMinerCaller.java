package org.processmining.plugins.crossorgprocmin.miner;

import org.deckfour.uitopia.api.event.TaskListener.InteractionResult;
import org.deckfour.xes.model.XLog;
import org.processmining.contexts.uitopia.UIPluginContext;
import org.processmining.contexts.uitopia.annotations.UITopiaVariant;
import org.processmining.framework.plugin.annotations.Plugin;
import org.processmining.framework.plugin.annotations.PluginVariant;
import org.processmining.models.graphbased.directed.petrinet.Petrinet;
import org.processmining.models.semantics.petrinet.Marking;
import org.processmining.plugins.InductiveMiner.plugins.IM;
import org.processmining.plugins.InductiveMiner.plugins.IMPetriNet;

/**
 * Inductive Miner Caller plugin
 * 
 * @author onuryilmaz
 *
 */
public class InductiveMinerCaller extends IM {

	@Plugin(name = "Call Inductive Miner", returnLabels = { "Petri net", "Initial marking", "final marking" }, returnTypes = {
			Petrinet.class, Marking.class, Marking.class }, parameterLabels = { "Log" }, userAccessible = false)
	@UITopiaVariant(affiliation = "METU", author = "Onur Yilmaz", email = "yilmaz.onur@metu.edu.tr", website = "onuryilmaz.me/cross-org-proc-min", pack = "CrossOrgProcMin")
	@PluginVariant(variantLabel = "Mine a Process Tree, dialog", requiredParameterLabels = { 0 })
	public Object[] call(UIPluginContext context, XLog log) {

		InductiveMinerCallerDialog dialog = new InductiveMinerCallerDialog(log);

		InteractionResult result = context.showWizard("Set Noise Threshold", true, false, dialog);
		if (result != InteractionResult.NEXT) {
			return new Object[] { null, null, null };
		}
		return IMPetriNet.minePetriNet(context, log, dialog.getMiningParameters());
	}

}
