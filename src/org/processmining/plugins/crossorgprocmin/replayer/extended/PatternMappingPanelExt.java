package org.processmining.plugins.crossorgprocmin.replayer.extended;

import org.processmining.models.graphbased.directed.petrinet.PetrinetGraph;
import org.processmining.plugins.astar.petrinet.manifestreplay.ui.PatternMappingPanel;
import org.processmining.plugins.petrinet.manifestreplayer.transclassifier.ITransClassifier;

/**
 * Extended for pattern-mapping panel handling
 * 
 * @author onuryilmaz
 *
 */
public class PatternMappingPanelExt extends PatternMappingPanel {

	private static final long serialVersionUID = -8301565106621235624L;

	/**
	 * {@inheritDoc}
	 */
	public void updateTransClass(PetrinetGraph net, ITransClassifier transClassifier, Object[] patternsWithNone,
			boolean showOneMatchDialog) {

		super.updateTransClass(net, transClassifier, patternsWithNone, false);

	}
}
