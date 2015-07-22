package org.processmining.plugins.crossorgprocmin.replayer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.JComponent;

import nl.tue.astar.AStarException;

import org.deckfour.xes.classification.XEventClassifier;
import org.deckfour.xes.info.impl.XLogInfoImpl;
import org.deckfour.xes.model.XLog;
import org.processmining.contexts.uitopia.UIPluginContext;
import org.processmining.contexts.uitopia.annotations.UITopiaVariant;
import org.processmining.framework.connections.Connection;
import org.processmining.framework.connections.ConnectionCannotBeObtained;
import org.processmining.framework.connections.ConnectionManager;
import org.processmining.framework.connections.annotations.ConnectionObjectFactory;
import org.processmining.framework.plugin.PluginContext;
import org.processmining.framework.plugin.PluginExecutionResult;
import org.processmining.framework.plugin.PluginParameterBinding;
import org.processmining.framework.plugin.annotations.Plugin;
import org.processmining.framework.plugin.annotations.PluginVariant;
import org.processmining.framework.util.Pair;
import org.processmining.framework.util.ui.wizard.ListWizard;
import org.processmining.framework.util.ui.wizard.ProMWizardStep;
import org.processmining.models.connections.petrinets.behavioral.FinalMarkingConnection;
import org.processmining.models.connections.petrinets.behavioral.InitialMarkingConnection;
import org.processmining.models.graphbased.directed.petrinet.PetrinetGraph;
import org.processmining.models.semantics.petrinet.Marking;
import org.processmining.plugins.astar.petrinet.manifestreplay.ui.ChooseAlgorithmStep;
import org.processmining.plugins.astar.petrinet.manifestreplay.ui.CreatePatternPanel;
import org.processmining.plugins.astar.petrinet.manifestreplay.ui.CreatePatternStep;
import org.processmining.plugins.astar.petrinet.manifestreplay.ui.MapCostStep;
import org.processmining.plugins.crossorgprocmin.replayer.extended.MapPattern2TransStepExt;
import org.processmining.plugins.petrinet.manifestreplayer.PNManifestReplayer;
import org.processmining.plugins.petrinet.manifestreplayer.PNManifestReplayerParameter;
import org.processmining.plugins.petrinet.manifestreplayer.algorithms.IPNManifestReplayAlgorithm;
import org.processmining.plugins.petrinet.manifestreplayer.algorithms.PNManifestReplayerILPAlgorithm;
import org.processmining.plugins.petrinet.manifestreplayresult.Manifest;

/**
 * Automated replayer with the extension of {@link PNManifestReplayer}
 * 
 * @author onuryilmaz
 *
 */
@Plugin(name = "Automated Replay Plugin", returnLabels = { "Manifest" }, returnTypes = { Manifest.class }, parameterLabels = {
		"Petri net", "Event Log", "Algorithm", "Parameters" }, help = "Extension of PNManifestReplayer for automatic replaying", userAccessible = false)
public class AutomatedReplayer extends PNManifestReplayer {

	/**
	 * {@inheritDoc}
	 */
	public Object[] chooseAlgorithmAndParam(UIPluginContext context, PetrinetGraph net, XLog log) {

		/**
		 * Utilities
		 */
		// generate create pattern GUI
		// list possible classifiers
		List<XEventClassifier> classList = new ArrayList<XEventClassifier>(log.getClassifiers());
		// add default classifiers
		if (!classList.contains(XLogInfoImpl.RESOURCE_CLASSIFIER)) {
			classList.add(0, XLogInfoImpl.RESOURCE_CLASSIFIER);
		}
		if (!classList.contains(XLogInfoImpl.NAME_CLASSIFIER)) {
			classList.add(0, XLogInfoImpl.NAME_CLASSIFIER);
		}
		if (!classList.contains(XLogInfoImpl.STANDARD_CLASSIFIER)) {
			classList.add(0, XLogInfoImpl.STANDARD_CLASSIFIER);
		}

		XEventClassifier[] availableClassifiers = classList.toArray(new XEventClassifier[classList.size()]);
		CreatePatternStep createPatternStep = new CreatePatternStep(log, availableClassifiers);

		// results, required earlier for wizard
		PNManifestReplayerParameter parameter = new PNManifestReplayerParameter();

		// generate pattern mapping GUI
		MapPattern2TransStepExt mapPatternStep = new MapPattern2TransStepExt(net, log,
				(CreatePatternPanel) createPatternStep.getComponent(parameter));

		// generate algorithm selection GUI, look for initial marking and final
		// markings
		Marking initialMarking = null;
		ConnectionManager connManager = context.getConnectionManager();
		//
		// InitMarkingFactoryExt factoryOnur = new InitMarkingFactoryExt();
		// initialMarking = factoryOnur.constructInitMarking2(context, net);
		// check existence of initial marking
		try {
			InitialMarkingConnection initCon = connManager.getFirstConnection(InitialMarkingConnection.class, context,
					net);

			initialMarking = (Marking) initCon.getObjectWithRole(InitialMarkingConnection.MARKING);

		} catch (ConnectionCannotBeObtained exc) {

		}

		// Marking[] finalMarkings;
		// FinalMarkingFactoryExt factoryOnur2 = new FinalMarkingFactoryExt();
		// finalMarkings = (Marking[])
		// factoryOnur2.constructFinalMarking2(context, net);
		Marking[] finalMarkings;
		try {
			Collection<FinalMarkingConnection> conns = connManager.getConnections(FinalMarkingConnection.class,
					context, net);
			finalMarkings = new Marking[conns.size()];
			if (conns != null) {
				int i = 0;
				for (FinalMarkingConnection fmConn : conns) {
					finalMarkings[i] = fmConn.getObjectWithRole(FinalMarkingConnection.MARKING);
					i++;
				}
			}
		}

		catch (Exception exc) {
			finalMarkings = new Marking[0];
		}

		// generate cost setting GUI
		MapCostStep mapCostStep = new MapCostStep(createPatternStep.getPatternCreatorPanel(),
				mapPatternStep.getPatternMappingPanel());

		ChooseAlgorithmStep chooseAlgorithmStep = new ChooseAlgorithmStep(net, log, initialMarking, finalMarkings);

		// construct dialog wizard
		ArrayList<ProMWizardStep<PNManifestReplayerParameter>> listSteps = new ArrayList<ProMWizardStep<PNManifestReplayerParameter>>(
				4);

		// listSteps.add(createPatternStep);
		// listSteps.add(mapPatternStep);
		// listSteps.add(chooseAlgorithmStep);
		// listSteps.add(mapCostStep);

		ListWizard<PNManifestReplayerParameter> wizard = new ListWizard<PNManifestReplayerParameter>(listSteps);

		parameter = createPatternStep.apply(parameter, null);
		JComponent component = mapPatternStep.getComponent(parameter);
		parameter = mapPatternStep.apply(parameter, component);
		parameter = chooseAlgorithmStep.apply(parameter, null);
		component = mapCostStep.getComponent(parameter);
		parameter = mapCostStep.apply(parameter, component);

		// parameter = chooseAlgorithmStep.apply(parameter, null);
		// parameter = mapCostStep.apply(parameter, null);

		// show wizard
		// parameter = ProMWizardDisplay.show(context, wizard, parameter);

		if (parameter == null) {
			return null;
		}

		// show message: GUI mode
		parameter.setGUIMode(false);

		IPNManifestReplayAlgorithm alg = new PNManifestReplayerILPAlgorithm();
		return new Object[] { alg, parameter };
	}

	/**
	 * {@inheritDoc}
	 */
	private boolean createMarking(UIPluginContext context, PetrinetGraph net, Class<? extends Connection> classType) {
		boolean result = false;
		Collection<Pair<Integer, PluginParameterBinding>> plugins = context.getPluginManager().find(
				ConnectionObjectFactory.class, classType, context.getClass(), true, false, false, net.getClass());
		PluginContext c2 = context.createChildContext("Creating connection of Type " + classType);
		Pair<Integer, PluginParameterBinding> pair = plugins.iterator().next();
		PluginParameterBinding binding = pair.getSecond();
		try {
			PluginExecutionResult pluginResult = binding.invoke(c2, net);
			pluginResult.synchronize();
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			c2.getParentContext().deleteChild(c2);
		}
		return result;
	}

	/**
	 * {@inheritDoc}
	 */
	@Plugin(name = "Automated Replay Plugin", returnLabels = { "Manifest" }, returnTypes = { Manifest.class }, parameterLabels = {
			"Petri net", "Event Log", "Algorithm", "Parameters" }, help = "Extension of PNManifestReplayer for automatic replaying", userAccessible = false)
	@UITopiaVariant(affiliation = "METU", author = "Onur Yilmaz", email = "yilmaz.onur@metu.edu.tr", website = "onuryilmaz.me/cross-org-proc-min", pack = "CrossOrgProcMin")
	@PluginVariant(requiredParameterLabels = { 0, 1 })
	public Manifest replayLog(UIPluginContext context, PetrinetGraph net, XLog log) throws AStarException {
		Manifest manifest = super.replayLog(context, net, log);

		return manifest;
	}

}
