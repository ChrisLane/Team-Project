package com.teamc2.travellingsalesbee.gui.view.settings;

import com.teamc2.travellingsalesbee.algorithms.Ant;
import com.teamc2.travellingsalesbee.algorithms.Bee;
import com.teamc2.travellingsalesbee.algorithms.NearestNeighbour;
import com.teamc2.travellingsalesbee.algorithms.TwoOptSwap;
import com.teamc2.travellingsalesbee.algorithms.cost.CostMatrix;
import com.teamc2.travellingsalesbee.gui.data.Map;
import com.teamc2.travellingsalesbee.gui.data.cells.Cell;
import com.teamc2.travellingsalesbee.gui.data.steps.*;
import com.teamc2.travellingsalesbee.gui.view.map.PanelMap;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * A class for the running of algorithms
 *
 * @author Bradley Rowe (bmr455)
 * @author Neil Farrington (npf489)
 * @author Todd Waugh Ambridge (txw467)
 */
public class RunActionListener implements ActionListener {
	private final PanelSettings panelSettings;
	private final PanelMap panelMap;
	private final SettingsButtons settingsButtons;
	private Map map;

	/**
	 * Construct a new run algorithm ActionListener
	 *
	 * @param panelSettings   The settings panel
	 * @param panelMap        The map panel
	 * @param settingsButtons The settings buttons
	 */
	public RunActionListener(PanelSettings panelSettings, PanelMap panelMap, SettingsButtons settingsButtons) {
		this.panelSettings = panelSettings;
		this.panelMap = panelMap;
		this.settingsButtons = settingsButtons;
		map = panelMap.getMap();
	}

	/**
	 * When an action is performed, run the current algorithm
	 */
	@Override
	public void actionPerformed(ActionEvent arg0) {
		if (map.getNodes().size() > 0) {
			switch (panelSettings.getAlgorithmType()) {
				case BEE:
					runBeeAlgorithm();
					settingsButtons.setDistance();
					break;
				case ANT:
					runAntAlgorithm();
					break;
				case NEARESTNEIGHBOUR:
					runNearestNeighbourAlgorithm();
					break;
				case TWOOPT:
					runTwoOptAlgorithm();
					break;
			}
			panelMap.getPathComponent().setStepNum(0);
		}
	}

	/**
	 * Run the two opt swap algorithm
	 */
	private void runTwoOptAlgorithm() {
		try {
			settingsButtons.setStepNum(0);
			map = panelMap.getMap();
			map.setCostMatrix();
			TwoOptSwap tos = new TwoOptSwap(map, panelSettings.getNoOfRunsValue());
			StepController visualise = new StepController();
			tos.naiveRun();
			ArrayList<NaiveStep> naiveSteps = visualise.getNaiveSteps(tos.getPath());
			panelMap.getPathComponent().setTosObject(tos);
			panelMap.getPathComponent().setNaiveSteps(naiveSteps);

			ArrayList<ArrayList<Cell>> pathOfPaths = new ArrayList<>();
			ArrayList<Cell> origin = new ArrayList<>();
			origin.add(naiveSteps.get(0).getStart());

			origin.addAll(naiveSteps.stream().map(NaiveStep::getEnd).collect(Collectors.toList()));
			pathOfPaths.add(origin);

			panelMap.getPanelAnimalAnimation().setAnimalIcon("/assets/icons/Boat.png");
			panelMap.getPanelAnimalAnimation().setPathofPaths(pathOfPaths);
		} catch (NullPointerException e) {
			settingsButtons.getBtnPrev().setEnabled(false);
			settingsButtons.getBtnPlay().setEnabled(false);
			settingsButtons.getBtnNext().setEnabled(false);
		}
	}

	/**
	 * Run the nearest neighbor algorithm
	 */
	private void runNearestNeighbourAlgorithm() {
		try {
			settingsButtons.setStepNum(0);
			map = panelMap.getMap();
			map.setCostMatrix();
			NearestNeighbour nearestNeighbour = new NearestNeighbour(map);
			StepController visualise = new StepController();
			nearestNeighbour.naiveRun();
			ArrayList<NaiveStep> naiveSteps = visualise.getNaiveSteps(nearestNeighbour.getPath());
			panelMap.getPathComponent().setNaiveSteps(naiveSteps);

			ArrayList<ArrayList<Cell>> pathOfPaths = new ArrayList<>();
			ArrayList<Cell> origin = new ArrayList<>();
			origin.add(naiveSteps.get(0).getStart());

			origin.addAll(naiveSteps.stream().map(NaiveStep::getEnd).collect(Collectors.toList()));
			pathOfPaths.add(origin);

				/*----------------------------------------------*/
			// panelMap.getPanelAnimalAnimation().setPath(nearestNeighbour.getPath());
			panelMap.getPanelAnimalAnimation().setAnimalIcon("/assets/icons/MailVan.png");
			panelMap.getPanelAnimalAnimation().setPathofPaths(pathOfPaths);
		} catch (NullPointerException e) {
				/*----------------------------------------------*/
			// When no nodes are on the screen: disable the buttons.
			settingsButtons.getBtnPrev().setEnabled(false);
			settingsButtons.getBtnPlay().setEnabled(false);
			settingsButtons.getBtnNext().setEnabled(false);

		}
	}

	/**
	 * Run the ant algorithm
	 */
	private void runAntAlgorithm() {
		settingsButtons.setStepNum(0);
		map.setCostMatrix();
		ArrayList<Ant> ants = new ArrayList<>();
		Ant ant = new Ant(map);
		ants.add(ant);
		ArrayList<ArrayList<Cell>> setOfRuns = new ArrayList<>();
		ArrayList<CostMatrix> setOfMatrices = new ArrayList<>();
		CostMatrix initialMatrix;
		initialMatrix = map.getCostMatrix().copy();

		int numberOfAnts = panelSettings.getNoOfRunsValue() / 20;
		for (int i = 1; i < numberOfAnts; i++) {
			Map cloneMap = new Map();
			cloneMap.setCostMatrix(initialMatrix.copy());
			ant = new Ant(cloneMap);
			ants.add(ant);
		}

		for (int j = 0; j < 50; j++) {
			ants.get(0).pheromoneRun();
			CostMatrix updatedMatrix = map.getCostMatrix().copy();
			for (int i = 1; i < numberOfAnts; i++) {
				ant = ants.get(i);
				ant.pheromoneRun();
				CostMatrix nextUpdatedMatrix = ant.getMap().getCostMatrix().copy();
				updatedMatrix.combine(nextUpdatedMatrix);
			}
			setOfMatrices.add(updatedMatrix);
			setOfRuns.add(ants.get(0).getPath());
			for (int i = 1; i < numberOfAnts; i++) {
				ants.get(i).getMap().setCostMatrix(updatedMatrix.copy());
			}
		}




			/*Below is an example of how to pass through the url name and set the pathOfPaths*/

		panelMap.getPanelAnimalAnimation().setAnimalIcon("/assets/icons/Ant.png");
		panelMap.getPanelAnimalAnimation().setPathofPaths(setOfRuns);

		StepController stepController = new StepController();
		ArrayList<AntStep> antSteps = stepController.getAntSteps(setOfRuns, setOfMatrices, initialMatrix);
		panelMap.getPathComponent().setAntSteps(antSteps);
	}

	/**
	 * Run the bee inspired algorithm
	 */
	private void runBeeAlgorithm() {
		try {
			settingsButtons.setStepNum(0);

			map.setCostMatrix();
			Bee bee = new Bee(map, panelSettings.getNoOfRunsValue());
			StepController visualise = new StepController();
			bee.naiveRun();
			ArrayList<NaiveStep> naiveSteps = visualise.getNaiveSteps(bee.getPath());
			panelMap.getPathComponent().setNaiveSteps(naiveSteps);

			bee.experimentalRun();
			ArrayList<ExperimentalStep> experimentalSteps = visualise.getExperimentalSteps(bee.getCellComparisons(),
					bee.getIntermediaryPaths(), bee.getIntermediaryPathCosts());
			panelMap.getPathComponent().setExperimentalSteps(experimentalSteps);

			ArrayList<ArrayList<Cell>> pathOfPaths = new ArrayList<>();
			ArrayList<Cell> hive = new ArrayList<>();
			hive.add(naiveSteps.get(0).getStart());

			hive.addAll(naiveSteps.stream().map(NaiveStep::getEnd).collect(Collectors.toList()));
			pathOfPaths.add(hive);

			for (ExperimentalStep experimentalStep : experimentalSteps) {
				ArrayList<Cell> setOfPoints = new ArrayList<>();

				if (experimentalStep.getType() == SwapType.INSPECTED) {
					setOfPoints = experimentalStep.getPath();
				}

				pathOfPaths.add(setOfPoints);
			}

			panelMap.getPathComponent().setPath(bee.getPath());

				/*----------------------------------------------*/
			// panelMap.getPanelAnimalAnimation().setPath(bee.getPath());
			panelMap.getPanelAnimalAnimation().setAnimalIcon("/assets/icons/SalesBee.png");
			panelMap.getPanelAnimalAnimation().setPathofPaths(pathOfPaths);
		} catch (NullPointerException e) {
				/*----------------------------------------------*/
			// When no nodes are on the screen: disable the buttons.
			settingsButtons.getBtnPrev().setEnabled(false);
			settingsButtons.getBtnPlay().setEnabled(false);
			settingsButtons.getBtnNext().setEnabled(false);
		}
	}
}
