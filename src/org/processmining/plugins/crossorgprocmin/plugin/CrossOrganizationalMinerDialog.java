package org.processmining.plugins.crossorgprocmin.plugin;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.io.IOException;
import java.net.URISyntaxException;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;

/**
 * Informative screen for {@link CrossOrganizationalMiner}
 * 
 * @author onuryilmaz
 *
 */
public class CrossOrganizationalMinerDialog extends JPanel {

	private static final long serialVersionUID = 1638838400129140627L;

	String fontWhite8 = "<font color='white' face='helvetica,arial,sans-serif' size='8'>";
	String fontWhite6 = "<font color='white' face='helvetica,arial,sans-serif' size='6'>";
	String fontWhite4 = "<font color='white' face='helvetica,arial,sans-serif' size='4'>";
	String fontBlack8 = "<font color='black' face='helvetica,arial,sans-serif' size='8'>";
	String fontBlack6 = "<font color='black' face='helvetica,arial,sans-serif' size='6'>";
	String fontBlack4 = "<font color='black' face='helvetica,arial,sans-serif' size='4'>";
	String fontBlack3 = "<font color='black' face='helvetica,arial,sans-serif' size='3'>";
	String fontBlack = "<font color='black' face='helvetica,arial,sans-serif' >";
	String fontEnd = "</font>";

	/**
	 * Create the panel.
	 */
	public CrossOrganizationalMinerDialog() {

		JTextPane textPane = new JTextPane();
		// textPane.setContentType("text/html");
		HTMLEditorKit kit = new HTMLEditorKit();
		textPane.setEditorKit(kit);

		StyleSheet styleSheet = kit.getStyleSheet();
		styleSheet.addRule("ul { text-align: left;}");

		String header = fontBlack8 + "Cross-Organizational Process Miner" + fontEnd;
		String info = fontBlack4
				+ " This plugin is developed for the master's thesis <strong>\"Recommendation Generation for Performance Improvement by using Cross-Organizational Process Mining\"</strong> by Onur YILMAZ. In this plugin you can analyze the event logs of different organizations and generate recommendations based on their performance indicators."
				+ fontEnd;
		;
		String substeps = "<ul><li>" + fontBlack4 + "Select noise threshold for process mining," + fontEnd
				+ "</li><li>" + fontBlack4 + "Set naming patterns for activities." + fontEnd + "</li></ul>";

		String steps = fontBlack4 + "You are required to follow these steps:" + fontEnd + "<br><ul><li>" + fontBlack4
				+ "Set names for organizations," + fontEnd + "</li><li>" + fontBlack4 + "For each organization:"
				+ fontEnd + "<br>" + substeps + "</li><li>" + fontBlack4 + "Select cluster size based on SSE values."
				+ fontEnd + "</li></ul>" + fontEnd;

		String troubleshoot = fontBlack4
				+ "For the bugs and issues you can contact <a href='mailto:yilmaz.onur@metu.edu.tr?Subject=Cross-Organizational%20Process%20Miner' target='_top'>yilmaz.onur@metu.edu.tr</a> or check <a href='https://github.com/onuryilmaz/cross-org-proc-min' target='_top'>Github page</a>.";

		String image;
		textPane.setText(header + "<hr>" + "<p>" + info + "</p>" + "<p>" + steps + "</p>" + "<p>" + troubleshoot
				+ "</p>");

		textPane.setBackground(new Color(150, 150, 150));
		textPane.setBorder(BorderFactory.createEmptyBorder());
		textPane.setEditable(false);

		textPane.addHyperlinkListener(new HyperlinkListener() {
			public void hyperlinkUpdate(HyperlinkEvent e) {
				if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
					// Do something with e.getURL() here
					if (Desktop.isDesktopSupported()) {
						try {
							Desktop.getDesktop().browse(e.getURL().toURI());
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						} catch (URISyntaxException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
				}
			}
		});
		JScrollPane headerExplanationScrollPane = new JScrollPane(textPane);
		headerExplanationScrollPane.setBorder(BorderFactory.createEmptyBorder());
		headerExplanationScrollPane.setPreferredSize(new Dimension(750, 400));
		add(headerExplanationScrollPane);
	}
}
