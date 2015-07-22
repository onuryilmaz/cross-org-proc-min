package org.processmining.plugins.crossorgprocmin.plugin;

import org.deckfour.uitopia.api.event.TaskListener.InteractionResult;
import org.deckfour.xes.model.XLog;
import org.processmining.contexts.uitopia.UIPluginContext;
import org.processmining.contexts.uitopia.annotations.UITopiaVariant;
import org.processmining.framework.connections.Connection;
import org.processmining.framework.plugin.annotations.Plugin;
import org.processmining.models.graphbased.directed.bpmn.BPMNDiagram;
import org.processmining.models.graphbased.directed.petrinet.Petrinet;
import org.processmining.plugins.crossorgprocmin.cluster.KMeansPerformanceClusterer;
import org.processmining.plugins.crossorgprocmin.mismatch.MismatchPatternAnalysis;
import org.processmining.plugins.crossorgprocmin.replayer.performance.PerformanceHandler;
import org.processmining.plugins.crossorgprocmin.replayer.performance.PerformanceMerger;
import org.processmining.plugins.crossorgprocmin.replayer.performance.PerformanceSummary;
import org.processmining.plugins.crossorgprocmin.utilities.OrganizationalLogs;
import org.processmining.plugins.crossorgprocmin.utilities.vis.OrganizationalLogOrgNameDialog;
import org.processmining.plugins.petrinet.manifestreplayresult.Manifest;

/**
 * Main plugin for cross organizational miner
 * 
 * @author onuryilmaz
 *
 */
public class CrossOrganizationalMiner {

	public PerformanceHandler performanceAnalyzer = new PerformanceHandler();
	public PerformanceMerger performanceMerger = new PerformanceMerger();

	@UITopiaVariant(affiliation = "METU", author = "Onur Yilmaz", email = "yilmaz.onur@metu.edu.tr", website = "onuryilmaz.me/cross-org-proc-min", pack = "CrossOrgProcMin")
	@Plugin(name = "Cross-Organizational Process Miner", parameterLabels = { "Log" }, returnLabels = { "Cross-Organizational Analysis Data" }, returnTypes = { OrganizationalLogs.class }, userAccessible = true, help = "Analyze logs of different organizations and create recommendations")
	public OrganizationalLogs analyze(UIPluginContext context, XLog... logs) throws Exception {

		CrossOrganizationalMinerDialog dialog = new CrossOrganizationalMinerDialog();
		InteractionResult result = context.showWizard("Information", true, false, dialog);
		if (result != InteractionResult.NEXT) {
			return null;
		}

		OrganizationalLogs organizationalLogs = new OrganizationalLogs();

		OrganizationalLogOrgNameDialog dialogName = new OrganizationalLogOrgNameDialog(logs.length);
		InteractionResult resultName = context.showWizard("Set Organization Names", true, false, dialogName);
		if (resultName != InteractionResult.NEXT) {
			return null;
		}
		organizationalLogs.organizationNames = dialogName.getOrganizationNames();

		int logCounter = 1;

		for (XLog log : logs) {
			Petrinet net = null;
			Manifest manifest = null;

			net = context.tryToFindOrConstructFirstNamedObject(Petrinet.class, "Call Inductive Miner", Connection.class, "", log);

			BPMNDiagram bpmnDiagram = context.tryToFindOrConstructFirstNamedObject(BPMNDiagram.class, "Convert Petri net to BPMN diagram", Connection.class, "", net);

			organizationalLogs.logs.put(logCounter, log);
			organizationalLogs.petriNets.put(logCounter, net);
			organizationalLogs.bpmnDiagrams.put(logCounter, bpmnDiagram);

			manifest = context.tryToFindOrConstructFirstNamedObject(Manifest.class, "Automated Replay Plugin", Connection.class, "", net, log);

			organizationalLogs.manifests.put(logCounter, manifest);

			PerformanceSummary performanceSummary = performanceAnalyzer.summary(manifest);
			organizationalLogs.performanceSummaries.put(logCounter, performanceSummary);

			logCounter++;

		}

		performanceMerger.merge(organizationalLogs);

		performanceMerger.clean(organizationalLogs);

		KMeansPerformanceClusterer clusterer = new KMeansPerformanceClusterer();
		organizationalLogs = clusterer.cluster(context, organizationalLogs);

		MismatchPatternAnalysis analysis = new MismatchPatternAnalysis();
		organizationalLogs = analysis.analyze(organizationalLogs);

		if (organizationalLogs != null) {
			return organizationalLogs;
		} else
			return null;

	}
}
