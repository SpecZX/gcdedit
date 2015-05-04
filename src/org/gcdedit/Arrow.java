package org.gcdedit;

public class Arrow {
	
	
	private int xStart;
	private int yStart;
	private int xEnd;
	private int yEnd;
	
	public int getxStart() {
		return xStart;
	}

	public void setStart(int xStart, int yStart) {
		this.xStart = xStart;
		this.yStart = yStart;
	}

	public int getyStart() {
		return yStart;
	}


	public int getxEnd() {
		return xEnd;
	}

	public void setEnd(int xEnd, int yEnd) {
		this.xEnd = xEnd;
		this.yEnd = yEnd;
	}

	public int getyEnd() {
		return yEnd;
	}

	public Arrow(int xStart, int yStart, int xEnd, int yEnd){
		this.xStart = xStart;
		this.yStart = yStart;
		this.xEnd = xEnd;
		this.yEnd = yEnd;
	}
	
	public Arrow(){
		
	}
	
	//TODO various static variables for arrow traits
	
	
}
