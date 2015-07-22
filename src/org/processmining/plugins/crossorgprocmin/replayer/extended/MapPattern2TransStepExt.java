package org.processmining.plugins.crossorgprocmin.replayer.extended;

import javax.swing.JComponent;

import org.deckfour.xes.model.XLog;
import org.processmining.framework.util.ui.wizard.ProMWizardStep;
import org.processmining.models.graphbased.directed.petrinet.PetrinetGraph;
import org.processmining.plugins.astar.petrinet.manifestreplay.ui.CreatePatternPanel;
import org.processmining.plugins.astar.petrinet.manifestreplay.ui.PatternMappingPanel;
import org.processmining.plugins.petrinet.manifestreplayer.PNManifestReplayerParameter;
import org.processmining.plugins.petrinet.manifestreplayer.TransClass2PatternMap;
import org.processmining.plugins.petrinet.manifestreplayer.transclassifier.TransClasses;

/**
 * Extended to override mapping patterns to transitions
 * 
 * @author onuryilmaz
 *
 */
public class MapPattern2TransStepExt implements ProMWizardStep<PNManifestReplayerParameter> {

	private PatternMappingPanel patternMappingPanel;
	private CreatePatternPanel patternCreatorPanel;
	private PetrinetGraph net;
	private XLog log;

	/**
	 * {@inheritDoc}
	 */
	public MapPattern2TransStepExt(PetrinetGraph net, XLog log, CreatePatternPanel patternCreatorPanel) {
		this.patternMappingPanel = new PatternMappingPanelExt();
		this.patternCreatorPanel = patternCreatorPanel;
		this.net = net;
		this.log = log;
	}

	/**
	 * {@inheritDoc}
	 */
	public PNManifestReplayerParameter apply(PNManifestReplayerParameter model, JComponent component) {
		// the mapping needs to be stored
		TransClasses transClasses = patternMappingPanel.getTransClasses();
		TransClass2PatternMap mapping = new TransClass2PatternMap(log, net,
				patternCreatorPanel.getSelectedEvClassifier(), transClasses, patternMappingPanel.getMapPattern());
		model.setMapping(mapping);
		return model;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean canApply(PNManifestReplayerParameter model, JComponent component) {
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	public JComponent getComponent(PNManifestReplayerParameter model) {
		patternMappingPanel.initiateTransClass(net, patternMappingPanel.getSelectedClassifier(),
				patternCreatorPanel.getEvClassPatternArr());
		return patternMappingPanel;
	}

	/**
	 * {@inheritDoc}
	 */
	public PatternMappingPanel getPatternMappingPanel() {
		return patternMappingPanel;
	}

	/**
	 * {@inheritDoc}
	 */
	public String getTitle() {
		return "Map Transition Classes to Patterns";
	}
}
