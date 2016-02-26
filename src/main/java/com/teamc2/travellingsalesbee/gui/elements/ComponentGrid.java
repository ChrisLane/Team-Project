package com.teamc2.travellingsalesbee.gui.elements;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/*
 * 	Class to create a single grid line to display on the map
 * 	
 * 	extends JComponent so it can be added to the grid map as a child component
 */
public class ComponentGrid extends JComponent {

	private final int cellWidth;
	private final int cellHeight;
	private ArrayList<Line> lines;
	private Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	private int screenWidth = screenSize.width;
	private int screenHeight = screenSize.height;


	/**
	 * Create a new line
	 */
	public ComponentGrid(int cellWidth, int cellHeight) {
		this.cellWidth = cellWidth;
		this.cellHeight = cellHeight;

		setBounds(0, 0, screenWidth, screenHeight);
		addLines();
	}

	private static class Line {
		int x1;
		int y1;
		int x2;
		int y2;

		/**
		 * Create a new line
		 *
		 * @param x1 x1 position
		 * @param y1 y1 position
		 * @param x2 x2 position
		 * @param y2 y2 position
		 */
		Line(int x1, int y1, int x2, int y2) {
			this.x1 = x1;
			this.y1 = y1;
			this.x2 = x2;
			this.y2 = y2;
		}
	}

	/**
	 * Collate all lines for the grid
	 */
	private void addLines() {
		lines = new ArrayList<>();

		int widthCount = 0; //Keeps track of current horizontal line we're drawing
		int heightCount = 0;//Keeps track of current vertical we're drawing

		// While the widthCount is less than the width of the users screen, draw vertical lines
		while (widthCount < screenWidth) {
			lines.add(new Line(widthCount, 0, widthCount, screenHeight));
			widthCount += cellWidth;
		}

		// While the heightCount is less than the height of the users screen, draw horizontal lines
		while (heightCount < screenHeight) {
			lines.add(new Line(0, heightCount, screenWidth, heightCount));
			heightCount += cellHeight;
		}
	}

	/**
	 * Draw the line
	 */
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;

		g2.setStroke(new BasicStroke(5)); // Set the line thickness to 5

		// Set the line colour
		Color lineColor = new Color(255, 255, 255, 65);
		g2.setColor(lineColor);

		// Draw all lines
		for (Line line : lines) {
			g2.drawLine(line.x1, line.y1, line.x2, line.y2);
		}
	}
}