package org.gcdedit;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import org.gcdedit.Arrow.ArrowStyle;
import org.gcdedit.Arrow.ArrowType;

public class GUI extends Application{
	
	private static int REND_DIAG_CIRC = 2;
	private static int RECT_SIZE = 50;
	private static int REND_TEXT_OFFSET = 10;
	private static double ARROW_HEAD_LENGTH = 5.0;
	private static double ARROW_HEAD_WIDTH = 2.5;


	//these are control variables to make adding arrows work correctly

	private boolean ARROW_MODE = false;
	private boolean ARROW_START = true;

	private int xStart;
	private int yStart;
	private int xEnd;
	private int yEnd;

	private Parser parser;
	
	
	private int signOf(double d){
		if(d<0) return -1;
		if(d>0) return 1;
		return 0;
	}


	//initializes our main gridpane with all the buttons


	public void initPane(Pane pane, Diagram diag, int xDim, int yDim, Group root){
		diag = new Diagram(xDim, yDim);
		parser.setDiagram(diag);


		Rectangle[][] rects = new Rectangle[xDim][yDim];


		Rectangle arrowModeIndicator = new Rectangle(150, 75);
		arrowModeIndicator.setFill(Color.RED);
		arrowModeIndicator.setTranslateX(xDim*RECT_SIZE);
		arrowModeIndicator.setTranslateY(0);
		pane.getChildren().add(arrowModeIndicator);

		for(int i = 0; i<xDim;i++){
			for(int j = 0;j<yDim;j++){
				rects[i][j] = new Rectangle(RECT_SIZE, RECT_SIZE);
				rects[i][j].setTranslateX(i*RECT_SIZE);
				rects[i][j].setTranslateY(j*RECT_SIZE);
				rects[i][j].setOnMouseClicked(rectClickAction(i, j, diag, pane, arrowModeIndicator, root));
				rects[i][j].setFill(Color.WHEAT);
				rects[i][j].setStroke(Color.BLACK);
				rects[i][j].setOpacity(.5);
				pane.getChildren().add(rects[i][j]);

			}
		}




		Button arrowModeButton = new Button();


		arrowModeButton.setTranslateX(xDim*RECT_SIZE);
		arrowModeButton.setTranslateY(75);


		arrowModeButton.setPrefSize(150, 75);
		arrowModeButton.setText("Arrow Mode");
		pane.getChildren().add(arrowModeButton);
		arrowModeButton.setOnAction(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent arg){
				ARROW_MODE = !ARROW_MODE;
				ARROW_START = true;
				arrowModeIndicator.setFill(Color.GREEN);

				//Want to change the color of the button whenever
				//arrow mode is active





			}			
		});


		Button parseButton = new Button();
		parseButton.setPrefSize(150,75);
		parseButton.setText("Generate TikzCD");

		parseButton.setTranslateX(xDim*RECT_SIZE);
		parseButton.setTranslateY(225);

		pane.getChildren().add(parseButton);
		parseButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg) {
				try {
					File tempfile = File.createTempFile("gcdedit", ".txt");
					parser.to_tikzcd(tempfile);
					System.out.println("Output contained in " + tempfile.getPath());
				}
				catch (IOException e) {
					System.out.println(e.getMessage());
					e.printStackTrace();
				}
			}
		});






		Button editArrowsButton = new Button();
		editArrowsButton.setPrefSize(150, 75);
		editArrowsButton.setText("Edit Arrows");

		editArrowsButton.setTranslateX(xDim*RECT_SIZE);
		editArrowsButton.setTranslateY(150);

		pane.getChildren().add(editArrowsButton);
		editArrowsButton.setOnAction(editArrowsButtonAction(diag, pane, root));


	}



	public void renderDiagOnPane(Pane pane, Diagram diag, Group root){
		//first wipe pane clean


		ArrayList<Node> toDelete = new ArrayList<Node>();

		for(Node n : pane.getChildren()){
			if(n instanceof Line) toDelete.add(n);
			if(n instanceof Circle) toDelete.add(n);
			if(n instanceof Polygon) toDelete.add(n);
		}

		pane.getChildren().removeAll(toDelete);			



		//then repopulate from the diagram

		for(Label lbl : diag.getLabels()){
			Circle circ = new Circle(lbl.getValX()*RECT_SIZE+(RECT_SIZE/2), lbl.getValY()*RECT_SIZE+(RECT_SIZE/2), REND_DIAG_CIRC);
			Text text = new Text(lbl.getValX()*RECT_SIZE+(RECT_SIZE/2)-REND_TEXT_OFFSET, 
					lbl.getValY()*RECT_SIZE+(RECT_SIZE/2)+REND_TEXT_OFFSET, lbl.getContent());
			
			pane.getChildren().addAll(circ, text);
			
		}

		for(Arrow a : diag.getArrows()){

			Line l = new Line(a.getStartX()*RECT_SIZE+(RECT_SIZE/2), a.getStartY()*RECT_SIZE+(RECT_SIZE/2), 
					a.getEndX()*RECT_SIZE+(RECT_SIZE/2), a.getEndY()*RECT_SIZE+(RECT_SIZE/2));
			
			//this is some fiddly thing to get the head of the arrow
			//perhaps there is a smarter way to do this...
			
			
			
			
			
			Polygon head = new Polygon();

			double x1 = (double) a.getEndX();
			double y1 = (double) a.getEndY();

			double x0 = (double) a.getStartX();
			double y0 = (double) a.getStartY();
			
			//need to fix the head so it actually points correctly
			
			
			
			head.getPoints().addAll(new Double[]{ARROW_HEAD_LENGTH*signOf(x1-x0), ARROW_HEAD_LENGTH*signOf(y1-y0),
					ARROW_HEAD_WIDTH*signOf(y1-y0), -ARROW_HEAD_WIDTH*signOf(x1-x0),
					-ARROW_HEAD_WIDTH*signOf(y1-y0), ARROW_HEAD_WIDTH*signOf(x1-x0)
					});
			
			head.setTranslateX(x1*RECT_SIZE+(RECT_SIZE/2));
			head.setTranslateY(y1*RECT_SIZE+(RECT_SIZE/2));

			switch(a.getStyle()){
			case SOLID:
				break;

			case DASHED:
				l.getStrokeDashArray().addAll(25d, 10d);

			default:
				break;


			}
			
			
			pane.getChildren().addAll(l, head);


		}



	}

	private EventHandler<MouseEvent> rectClickAction(int i, int j,
			Diagram diag, Pane pane, Rectangle arrowModeIndicator, Group root) {
		return (new EventHandler<MouseEvent>(){

			@Override
			public void handle(MouseEvent arg){

				if(ARROW_MODE){
					if(ARROW_START){
						xStart = i;
						yStart = j;
						ARROW_START = false;
						arrowModeIndicator.setFill(Color.YELLOW);
					}else{
						xEnd = i;
						yEnd = j;
						diag.addArrow(new Arrow(xStart, yStart, xEnd, yEnd));
						ARROW_START = true;
						ARROW_MODE = false;
						//						Line l = new Line(xStart*rectSize+(rectSize/2), yStart*rectSize+(rectSize/2), 
						//								xEnd*rectSize+(rectSize/2), yEnd*rectSize+(rectSize/2));
						//						pane.getChildren().add(l);

						renderDiagOnPane(pane, diag, root);
						arrowModeIndicator.setFill(Color.RED);


					}
				}
				else{

					Group labelRoot = new Group();
					Stage labelStage = new Stage();
					labelStage.setScene(new Scene(labelRoot, 200, 110));
					labelStage.setTitle("Name Label");

					TextField labelname = new TextField("Label Name");
					Button okbtn = new Button();
					Button cancelbtn = new Button();


					labelname.setTranslateY(25);
					labelname.setTranslateX(25);
					okbtn.setTranslateX(25);
					okbtn.setTranslateY(75);
					okbtn.setText("OK");
					okbtn.setOnAction(new EventHandler<ActionEvent>(){

						@Override
						public void handle(ActionEvent arg0) {
							if(!labelname.getText().equals("Label Name")){
								diag.addLabel(new Label(i, j, labelname.getText()));

								//this is just temporary, later we should put in a dot or something
								//								rects[i][j].setFill(Color.BLACK);
							}
							labelStage.close();
							renderDiagOnPane(pane, diag, root);
						}

					});

					cancelbtn.setTranslateX(100);
					cancelbtn.setTranslateY(75);
					cancelbtn.setText("Cancel");
					cancelbtn.setOnAction(new EventHandler<ActionEvent>(){

						@Override
						public void handle(ActionEvent arg0) {
							labelStage.close();
						}

					});

					labelRoot.getChildren().addAll(okbtn, labelname, cancelbtn);


					labelStage.show();
				}
			}
		});



	}

	public EventHandler<ActionEvent> editArrowsButtonAction(Diagram diag, Pane pane, Group root){
		return (new EventHandler<ActionEvent>(){



			@Override
			public void handle(ActionEvent event) {
				Group editArrowsRoot = new Group();
				Stage editArrowsStage = new Stage();
				int y = diag.getArrows().size();
				editArrowsStage.setScene(new Scene(editArrowsRoot, 300, (y+2)*30+10)); //(y+4)*30+10
				editArrowsStage.setTitle("Modify Diagram Arrows");

				int i = 0;

				for(Arrow a : diag.getArrows()){
					Button btn = new Button();
					btn.setOnAction(editSingleArrowButtonAction(a, diag, pane, root));
					btn.setText("Arrow: ("+a.getStartX()+", "+ a.getStartY()+") to ("+ a.getEndX()+", "+ a.getEndY()+")");
					btn.setTranslateX(25);
					btn.setTranslateY(i*30+10);
					i++;
					editArrowsRoot.getChildren().add(btn);
				}

				Button okbtn = new Button();
				okbtn.setText("OK");

				okbtn.setTranslateY((y+1)*30+10);
				okbtn.setTranslateX(25);
				okbtn.setOnAction(new EventHandler<ActionEvent>(){

					@Override
					public void handle(ActionEvent event) {
						editArrowsStage.close();
					}

				});

				editArrowsRoot.getChildren().add(okbtn);


				editArrowsStage.show();

			}

		});



	}


	public EventHandler<ActionEvent> editSingleArrowButtonAction(Arrow a, Diagram diag, Pane pane, Group root){

		return (new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent event){


				Group arrowEditRoot = new Group();
				Stage arrowEditStage = new Stage();
				arrowEditStage.setScene(new Scene(arrowEditRoot, 200, 125));
				arrowEditStage.setTitle("Modify Arrow");

				ComboBox<ArrowStyle> arrowStyle = new ComboBox<ArrowStyle>();
				arrowStyle.setPromptText("Current Style: "+a.getStyle());

				for(ArrowStyle style : ArrowStyle.values()){
					arrowStyle.getItems().add(style);
				}


				ComboBox<ArrowType> arrowType = new ComboBox<ArrowType>();
				arrowType.setPromptText("Current Type: "+a.getType());

				for(ArrowType type : ArrowType.values()){
					arrowType.getItems().add(type);
				}



				TextField arrowName = new TextField(a.getName());



				Button okbtn = new Button();
				okbtn.setText("OK");
				okbtn.setOnAction(new EventHandler<ActionEvent>(){

					@Override
					public void handle(ActionEvent event) {
						a.setStyle(arrowStyle.getValue());
						a.setType(arrowType.getValue());
						a.setName(arrowName.getText());
						renderDiagOnPane(pane, diag, root);
						arrowEditStage.close();
					}

				});

				Button dltbtn = new Button();
				dltbtn.setText("Delete Arrow");
				dltbtn.setOnAction(new EventHandler<ActionEvent>(){

					@Override
					public void handle(ActionEvent event) {
						diag.getArrows().remove(a);
						renderDiagOnPane(pane, diag, root);
						arrowEditStage.close();
					}

				});




				okbtn.setTranslateY(95);
				okbtn.setTranslateX(10);

				dltbtn.setTranslateY(95);
				dltbtn.setTranslateX(55);

				arrowStyle.setTranslateX(10);
				arrowStyle.setTranslateY(10);

				arrowType.setTranslateX(10);
				arrowType.setTranslateY(37);

				arrowName.setTranslateX(10);
				arrowName.setTranslateY(64);


				arrowEditRoot.getChildren().addAll(okbtn, dltbtn, arrowStyle, arrowType, arrowName);

				arrowEditStage.show();


			}
		});



	}




	public void initLoadingMenu(Group loadingRoot, Stage loadingStage){
		TextField xVal = new TextField("X dimension");
		TextField yVal = new TextField("Y dimension");
		Button okbtn = new Button();
		Button cancelbtn = new Button();


		xVal.setTranslateX(25);
		xVal.setTranslateY(25);

		yVal.setTranslateX(25);
		yVal.setTranslateY(55);



		okbtn.setTranslateX(25);
		okbtn.setTranslateY(95);
		okbtn.setText("OK");
		okbtn.setOnAction(new EventHandler<ActionEvent>(){

			@Override
			public void handle(ActionEvent arg0) {
				try{
					primaryStage(Integer.parseInt(xVal.getText()), Integer.parseInt(yVal.getText()));
				}catch(Exception e){
					primaryStage(5, 5); //5x5 is chosen as the default size- no real reason for it
				}
				loadingStage.close();
			}

		});

		cancelbtn.setTranslateX(100);
		cancelbtn.setTranslateY(95);
		cancelbtn.setText("Cancel");
		cancelbtn.setOnAction(new EventHandler<ActionEvent>(){

			@Override
			public void handle(ActionEvent arg0) {
				loadingStage.close();
			}

		});

		loadingRoot.getChildren().addAll(xVal, yVal, okbtn, cancelbtn);
		loadingStage.show();


	}



	public void primaryStage(int dimXDiagram, int dimYDiagram){
		Stage stage = new Stage();
		Group root = new Group();
		stage.setScene(new Scene(root));
		stage.setTitle("Commutative Diagram Builder");

		Diagram diag = null;
		Pane pane = new Pane();

		parser = new Parser();

		initPane(pane, diag, dimXDiagram, dimYDiagram, root);

		//parser.setDiagram(diag);

		//		root.getChildren().add(grid);
		root.getChildren().add(pane);
		stage.show();

	}

	@Override
	public void start(Stage base) throws Exception {

		Stage loadingStage = new Stage();
		Group loadingRoot = new Group();
		loadingStage.setScene(new Scene(loadingRoot, 200, 125));
		loadingStage.setTitle("Specify Diagram Dimensions");
		initLoadingMenu(loadingRoot, loadingStage);
	}

	public static void main(String[] args) {
		Application.launch(args);
	}

}
