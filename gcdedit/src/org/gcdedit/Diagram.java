package org.gcdedit;

import java.util.ArrayList;

public class Diagram {

	
	private int xDim;
	private int yDim;
	
	private ArrayList<Label> labels;
	private ArrayList<Arrow> arrows;
	
	public Diagram(int xDim, int yDim){
		this.xDim = xDim;
		this.yDim = yDim;
		labels = new ArrayList<Label>();
		arrows = new ArrayList<Arrow>();
		
	}
	
	public ArrayList<Arrow> getArrows(){
		return this.arrows;
	}
	
	public ArrayList<Label> getLabels(){
		return this.labels;
	}
	
	public boolean addLabel(Label l){
		//should probably check if label has valid location
		
		
		return this.labels.add(l);
	}
	
	public boolean addArrow(Arrow a){
		//should probably check if arrow has valid end points
		
		return this.arrows.add(a);
	}
	
	public boolean removeLabel(Label l){
		return this.labels.remove(l);
	}
	
	public boolean removeArrow(Arrow a){
		return this.arrows.remove(a);
	}
	
	public boolean updateLabel(){
		//TODO
		return false;
	}
	
	public boolean updateArrow(){
		//TODO
		return false;
	}
	
	
	public void save(){
		
	}
	
	public void load(){
		
	}
}
