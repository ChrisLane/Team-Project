package com.teamc2.travellingsalesbee.gui.view.map;

import com.teamc2.travellingsalesbee.gui.data.cells.Cell;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.net.URL;
import java.util.ArrayList;

/**
 * @author Melvyn Mathews (mxm499)
 */
public class PanelAnimalAnimation extends JFXPanel {

	private String url; //The URL string for the image

	private int width; //Width of the panel
	private int height; //height of the panel
	private int stepNum = 0; //Step number for the path
	private int popStepNum = 0; //Step number for the path of paths

	private double speed; //Speed of the bee

	private ArrayList<ArrayList<Cell>> pathOfPaths;

	private Rectangle animalIcon;
	private TranslateTransition transition;
	private Scene scene;
	private Pane root;

	private boolean singlePath = true; //Single path
	private boolean poPaths = false; //path of Paths
	private boolean stepThroughAllPaths = false; //set to true to step through each step in every path

	/**
	 * Create a new animal animation panel
	 *
	 * @param width  Width of panel
	 * @param height Height of panel
	 */
	public PanelAnimalAnimation(int width, int height) {

		this.width = width;
		this.height = height;

		//Platform.runLater(this::initScene);
		//this.initScene();
		initScene();
	}

	private void initScene() {
		Platform.runLater(() -> {
			root = new Pane();
			root.setId("beePane");
			scene = new Scene(root, width, height);
			scene.setFill(javafx.scene.paint.Color.rgb(0, 0, 0, 0));
			URL url1 = getClass().getResource("/assets/stylesheets/visualiser.css");
			scene.getStylesheets().add(url1.toExternalForm());
			animalIcon = createRectangle();
			animalIcon.setVisible(false);
			//double speed = 1.5 / (pathOfPaths.get(0).size());
			transition = new TranslateTransition(Duration.seconds(0.6), animalIcon);
			root.getChildren().add(animalIcon);
			setScene(scene);
		});
	}

	/**
	 * @return Rectangle of size 30, 30
	 */
	private Rectangle createRectangle() {
		final Rectangle rectangle = new Rectangle(-20, -20, 30, 30);
		rectangle.setOpacity(1);

		return rectangle;
	}

	/**
	 * @param end        End position for which the animal will move to
	 * @param animal     Animal image
	 * @param transition TranslateTransition transition for animation
	 */
	private void moveFromAToB(Cell end, Rectangle animal, TranslateTransition transition) {
		Platform.runLater(() -> {

			if ((end.getX() - animal.getX()) <= 0) {
				animal.setScaleX(-1);
			} else {
				animal.setScaleX(1);
			}

			//Set where to animate to and play the animation
			transition.setToX(end.getX() - animal.getX() - 15);
			transition.setToY(end.getY() - animal.getY() - 15);
			transition.playFromStart();

			transition.setOnFinished(AE -> {
				//System.out.println("STOPPED PLAYING");
				if (!singlePath) {
					//System.out.println("Calling animatePath");
					stepNum++;
					animatePath(pathOfPaths, popStepNum, pathOfPaths.get(popStepNum), stepNum, animal, transition);
				}
			});
		});
	}

	public void setUrl(String url) {
		this.url = url;
		System.out.println(url);
		Image image = new Image(url);
		Platform.runLater(() -> animalIcon.setFill(new ImagePattern(image, 0, 0, 1, 1, true)));
	}

	/**
	 * @param superPath  Path of all paths ArrayList<ArrayList<Cell>>
	 * @param superI     superPath position (gets the current path we're working with)
	 * @param path       Current path to animate
	 * @param i          Path position
	 * @param animal     The 'animal' to move
	 * @param transition The transition object that handles the animation itself
	 */
	private void animatePath(ArrayList<ArrayList<Cell>> superPath, final int superI, ArrayList<Cell> path, int i, Rectangle animal, TranslateTransition transition) {

		//If there are no more paths, do nothing
		if (superI >= superPath.size()) {
			System.out.println("Ran out of moves");
		} else if (i >= path.size()) {
			//Increment the global superI
			//popStepNum++;
			//System.out.println("Incremented popStepNum, new value is: " + popStepNum);
		} else {

			//Get end cell
			if (i < path.size()) {

				//Get the next point to move to
				Cell end = path.get(i);

				moveFromAToB(end, animal, transition);

			} else if (superI < superPath.size()) {
				animatePath(superPath, (superI + 1), superPath.get(superI + 1), 0, animal, transition);
			}
		}
	}

	public void setSpeed(double speed) {
		this.speed = speed;

		//THIS MAKES THE ANIMATION DISGUSTING, LET'S DO SOMETHING DIFFERENT PLZ
		//transition.setRate(speed);
	}

	/**
	 * Increment Step Number
	 * If stepNum goes out of bounds of the current path, then stepNum is set to 0 and popStemNum is incremented
	 */
	public void incrStepNum() {
		stepNum++;
		System.out.println("Step num: " + stepNum);
		System.out.println("PopStemNum: " + popStepNum);
		System.out.println("Path size: " + pathOfPaths.get(popStepNum).size());
		System.out.println("---------------------------");

		//Un/Show bee based on a positive stepNum
		if (stepNum < 0) {
			setVisible(false);
		} else {
			setVisible(true);
		}

		//When our stepNum is greater than the size of the path we're animating, set it to 0 and increment popStepNum
		if (stepNum >= pathOfPaths.get(popStepNum).size() - 1) {
			stepNum = 0;

			if (singlePath && !stepThroughAllPaths) {
				singlePath = false;
				poPaths = true;
			}
		}

		//If animating a single step at a time, ensure singlePath is set to true
		if (singlePath) {
			Cell end = pathOfPaths.get(popStepNum).get(stepNum);
			moveFromAToB(end, animalIcon, transition);
		}

		//if animating an entire path at a time, set poPaths to true
		if (poPaths) {
			popStepNum++;

			animatePath(pathOfPaths, popStepNum, pathOfPaths.get(popStepNum), 0, animalIcon, transition);
		}
	}

	/**
	 * Decrement Step Number
	 */
	public void decrStepNum() {
		stepNum--;

		//Un/Show bee based on a positive stepNum
		if (stepNum < 0) {
			setVisible(false);
		} else {
			setVisible(true);
		}

		if (stepNum <= 0) {
			stepNum = 0;

			popStepNum--;
		}

		//If animating a single step at a time, ensure singlePath is set to true
		if (singlePath) {
			Cell end = pathOfPaths.get(popStepNum).get(stepNum);
			moveFromAToB(end, animalIcon, transition);
		}

		//if animating an entire path at a time, set poPaths to true
		if (poPaths) {
			animatePath(pathOfPaths, popStepNum, pathOfPaths.get(popStepNum), 0, animalIcon, transition);
		}
	}

	/**
	 * @param path Path of type: ArrayList<ArrayList<Cell>>
	 *             Gets the first position from the first path in the list and sets the bee to that position
	 *             If the path needs to be stepped through the entire path, set the singlePath boolean to true
	 */
	public void setPathofPaths(ArrayList<ArrayList<Cell>> path) {
		pathOfPaths = path;
		popStepNum = 0;
		stepNum = 0;

		Platform.runLater(() -> {
			animalIcon.setX(pathOfPaths.get(0).get(0).getX() - 15);
			animalIcon.setY(pathOfPaths.get(0).get(0).getY() - 15);
			animalIcon.setVisible(true);
			setVisible(true);
		});
	}

	/**
	 * @param bool True to animate each step at a time
	 *             Sets poPaths to !bool
	 */
	public void animateSteps(boolean bool) {
		singlePath = bool;
		poPaths = !bool;
	}

	/**
	 * @param bool True to animate entire paths at a time
	 *             Sets singlePath to !bool
	 */
	public void animatePath(boolean bool) {
		poPaths = bool;
		singlePath = !bool;
	}

	/**
	 * @return Return step number int
	 */
	public int getPopStepNum() {
		return popStepNum;
	}

	/**
	 * @return Return step number int
	 */
	public int getStepNum() {
		return stepNum;
	}

	/**
	 * @return The single path boolean
	 */
	public boolean getSinglePathBool() {
		return singlePath;
	}

	/**
	 * @return The path of paths boolean value
	 */
	public boolean getPoPathsBool() {
		return poPaths;
	}

	public ArrayList<ArrayList<Cell>> getPathOfPaths() {
		return pathOfPaths;
	}

	public void setDelay(double delay) {
		transition.setDelay(Duration.seconds(delay));
	}
}