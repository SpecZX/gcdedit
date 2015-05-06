package org.gcdedit;

public class Label {
	private int valX;
	private int valY;
	private String content;
	
	public Label(int xVal, int yVal, String content){
		this.valX = xVal;
		this.valY = yVal;
		this.content = content;
	}
	
	public int getValX(){
		return this.valX;
	}
	public int getValY(){
		return this.valY;
	}
	public String getContent(){
		return this.content;
	}
	
	
	@Override
	public boolean equals(Object o){
		if(!(o instanceof Label)) return false;
		Label l = (Label) o;
		return (l.getValX()==this.valX)&&(l.getValY()==this.valY);
	}
	
	@Override
	public int hashCode(){
		return (int) Math.pow(2, valX)*(2*valY-1);
	}
	
}
