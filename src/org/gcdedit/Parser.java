package org.gcdedit;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import java.util.Comparator;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Collections;

public class Parser implements Comparator<Label> {

	private Diagram diagram;

	public Parser() {
		this.diagram = null;
	}

	public Parser(Diagram d) {
		this.diagram = d;
	}

	public void setDiagram(Diagram d) {
		this.diagram = d;
	}

	public void to_tikzcd(File f) {
		/* Basic idea: a TikzCD diagram is written the same way English
		 * is, with "blank" spaces where there is no data. So what we do
		 * is iterate over each space and check if there's an arrow or
		 * object there, then record it appropriately. */
		try {
			FileWriter w = new FileWriter(f);
		
			int[] size = diagramSize();
			int numRows = size[0]; 
			int numCols = size[1];
			System.out.println("Rows: " + numRows + "; Cols: " + numCols);
			LinkedList<Arrow> arrowList = new LinkedList<Arrow>(diagram.getArrows()); // assumes that the diagram keeps track of which order to draw arrows
			LinkedList<Label> labelList = new LinkedList<Label>(getSortedLabels());

			for (int row = 0; row < numRows; row++) {
				for (int col = 0; col < numCols; col++) {
					System.out.println(labelList.peek().getValX() + " : " + labelList.peek().getValY() + " ; " + row + "," + col);
					if ((labelList.peek().getValX() == col) && labelList.peek().getValY() == row) {
						w.write(labelList.remove().getContent() + " ");
					}
					/* This is a dumb way to do things, but we're
					 * assuming that the number of arrows is pretty
					 * small, so iterating over the list won't be
					 * that expensive. Idea for improvement: come up
					 * with a list of positions that might have
					 * arrows drawn from/to them. Then we wouldn't
					 * have to make a bunch of stupid checks. */
					ListIterator<Arrow> arrowIterator = arrowList.listIterator(0);
					while (arrowIterator.hasNext()) {
						Arrow a = arrowIterator.next();
						if (arrowStartsAt(a, row, col)) {
							w.write(arrowCommand(a));
							arrowIterator.remove();
						}
						else if(arrowEndsAt(a, row, col)) {
							w.write(arrowCommandReverse(a));
							arrowIterator.remove();
						}
					}
					w.write("& ");
				}
				w.write("\\\\\n");
			}
			w.close();
		}

		catch (IOException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}

	public void to_tikz() {
		//TODO
	}

	public int compare(Label lab1, Label lab2) {
		if (lab1.getValY() > lab2.getValY()) return 1;
		if (lab1.getValY() < lab2.getValY()) return -1;
		if (lab1.getValX() < lab2.getValX()) return 1;
		if (lab1.getValX() > lab2.getValY()) return -1;
		return 0;
	}

	/* returns [numRows,numCols] -- note that this may actually differ from
	 * the dimensions of the array itself (as in, it could be smaller). The
	 * reason to determine this is so that we come away with a minimal
	 * amount of code for a tikzcd */
	private int[] diagramSize() {
		int[] size = new int[2];
		if (diagram == null) System.out.println("Null Diagram!");
		int numRows = 0;
		int numCols = 0;
		for (Label lab : diagram.getLabels()) {
			if (lab.getValX() > numCols) numCols = lab.getValX();
			if (lab.getValY() > numRows) numRows = lab.getValY();
		}
		size[0] = numRows + 1;
		size[1] = numCols + 1;
		return size;
	}

	private ArrayList<Label> getSortedLabels() {
		ArrayList<Label> labels = new ArrayList<Label>(diagram.getLabels());
		Collections.sort(labels, this);
		return labels;
	}

	private boolean arrowStartsAt(Arrow a, int row, int col) {
		return (a.getStartX() == col && a.getStartY() == row);
	}

	private boolean arrowEndsAt(Arrow a, int row, int col) {
		return (a.getEndX() == col && a.getEndY() == row);
	}

	private String arrowCommand(Arrow a) {
		return "";
	}

	private String arrowCommandReverse(Arrow a) {
		return "";
	}
}
