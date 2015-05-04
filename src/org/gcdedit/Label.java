package org.gcdedit;

public class Label {
	private int xVal;
	private int yVal;
	private String content;
	
	public Label(int xVal, int yVal, String content){
		this.xVal = xVal;
		this.yVal = yVal;
		this.content = content;
	}
	
	public int getxVal(){
		return this.xVal;
	}
	public int getyVal(){
		return this.yVal;
	}
	public String getContent(){
		return this.content;
	}
	
	
	@Override
	public boolean equals(Object o){
		if(!(o instanceof Label)) return false;
		Label l = (Label) o;
		return (l.getxVal()==this.xVal)&&(l.getyVal()==this.yVal);
	}
	
	@Override
	public int hashCode(){
		return (int) Math.pow(2, xVal)*(2*yVal-1);
	}
	
}
