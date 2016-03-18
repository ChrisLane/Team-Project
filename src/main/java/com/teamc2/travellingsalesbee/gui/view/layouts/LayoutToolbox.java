package com.teamc2.travellingsalesbee.gui.view.layouts;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class LayoutToolbox extends GroupLayout {
	/**
	 * Creates a layout for the toolbox container
	 *
	 * @param host            Container for the layout to be applied to
	 * @param backButton      Back button for the toolbox
	 * @param randomiseButton Button to randomise nodes on the map
	 * @param clearButton     Button to clear the map
	 * @param imgLabel 
	 */
	public LayoutToolbox(Container host, JButton backButton, JLabel imgLabel) {
		super(host);

		setHorizontalGroup(
				createParallelGroup(Alignment.CENTER)
						.addComponent(backButton)
						.addComponent(imgLabel)
		);
		setVerticalGroup(
				createParallelGroup()
						.addGroup(createSequentialGroup()
								.addComponent(backButton)
								.addGap(50)
								.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
								.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
								.addGap(50)
								.addComponent(imgLabel)
								.addContainerGap(500, Short.MAX_VALUE))
		);
	}
}
