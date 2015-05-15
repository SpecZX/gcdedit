package org.gcdedit;

public class Arrow {
	
	

	private int startX;
	private int startY;
	private int endX;
	private int endY;
	
	
	//I've just added this enum for the time being...
	
	public enum ArrowStyle{
		SOLID, DASHED;
	}
	
	
	private ArrowStyle style;
	
	
	public int getStartX() {
		return startX;
	}

	public void setStart(int xStart, int yStart) {
		this.startX = xStart;
		this.startY = yStart;
	}

	public int getStartY() {
		return startY;
	}


	public int getEndX() {
		return endX;
	}

	public void setEnd(int xEnd, int yEnd) {
		this.endX = xEnd;
		this.endY = yEnd;
	}

	public int getEndY() {
		return endY;
	}

	public Arrow(int xStart, int yStart, int xEnd, int yEnd){
		this.startX = xStart;
		this.startY = yStart;
		this.endX = xEnd;
		this.endY = yEnd;
		this.style = ArrowStyle.SOLID;
	}
	
	public Arrow(){
		
	}

	
	public ArrowStyle getStyle() {
		return style;
	}

	public void setStyle(ArrowStyle style) {
		this.style = style;
	}

	
	
	
}
