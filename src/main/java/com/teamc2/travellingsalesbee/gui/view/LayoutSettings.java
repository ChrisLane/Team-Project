package com.teamc2.travellingsalesbee.gui.view;

import javax.swing.*;
import java.awt.*;

public class LayoutSettings extends GroupLayout {

	/**
	 * Creates a {@code GroupLayout} for the specified {@code Container}.
	 *
	 * @param host the {@code Container} the {@code GroupLayout} is
	 *             the {@code LayoutManager} for
	 * @throws IllegalArgumentException if host is {@code null}
	 */
	public LayoutSettings(Container host, JLabel infoLabel, JLabel lblExperimentRuns, JLabel lblNoOfRuns,
						  JSlider slider, JButton btnRun, JButton btnPrev, JButton btnNext, JTextPane txtPaneTextWillAppear) {
		super(host);

		setHorizontalGroup(
				createParallelGroup(Alignment.LEADING)
						.addGroup(createSequentialGroup()
								.addContainerGap()
								.addGroup(createParallelGroup(Alignment.LEADING)
										.addComponent(infoLabel, PREFERRED_SIZE, 350, PREFERRED_SIZE)
										.addGroup(createSequentialGroup()
												.addComponent(lblExperimentRuns)
												.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
												.addComponent(lblNoOfRuns))
										.addComponent(slider, PREFERRED_SIZE, 457, PREFERRED_SIZE))
								.addGap(10)
								.addGroup(createParallelGroup(Alignment.TRAILING)
										.addGroup(createSequentialGroup()
												.addComponent(btnRun, PREFERRED_SIZE, 93, PREFERRED_SIZE)
												.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 113, Short.MAX_VALUE)
												.addComponent(btnPrev)
												.addGap(18)
												.addComponent(btnNext))
										.addComponent(txtPaneTextWillAppear, DEFAULT_SIZE, 314, Short.MAX_VALUE))
								.addContainerGap())
		);
		setVerticalGroup(
				createParallelGroup(Alignment.TRAILING)
						.addGroup(createSequentialGroup()
								.addContainerGap()
								.addComponent(infoLabel)
								.addGap(18)
								.addGroup(createParallelGroup(Alignment.BASELINE)
										.addComponent(lblExperimentRuns)
										.addComponent(lblNoOfRuns))
								.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
								.addComponent(slider, PREFERRED_SIZE, DEFAULT_SIZE, PREFERRED_SIZE)
								.addContainerGap(211, Short.MAX_VALUE))
						.addGroup(createSequentialGroup()
								.addContainerGap(DEFAULT_SIZE, Short.MAX_VALUE)
								.addGroup(createParallelGroup(Alignment.BASELINE)
										.addComponent(btnNext, PREFERRED_SIZE, 30, PREFERRED_SIZE)
										.addComponent(btnPrev, PREFERRED_SIZE, 30, PREFERRED_SIZE)
										.addComponent(btnRun))
								.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
								.addComponent(txtPaneTextWillAppear, PREFERRED_SIZE, 170, PREFERRED_SIZE)
								.addGap(69))
		);
	}
}