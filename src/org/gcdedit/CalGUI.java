package org.gcdedit;


import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

public class CalGUI extends Application {



	private static int REND_DIAG = 50;
	
	private static int REND_DIAG_CIRC = 10;


	//these are control variables to make adding arrows work correctly

	private boolean ARROW_MODE = false;
	private boolean ARROW_START = true;

	private int xStart;
	private int yStart;
	private int xEnd;
	private int yEnd;



	//produces an event handler to control what happens when the (i, j) button is selected

	public EventHandler<ActionEvent> gridButtonAction(int i, int j, Diagram diag){
		return (new EventHandler<ActionEvent>(){

			@Override
			public void handle(ActionEvent arg){

				if(ARROW_MODE){
					if(ARROW_START){
						xStart = i;
						yStart = j;
						ARROW_START = false;
					}else{
						xEnd = i;
						yEnd = j;
						diag.addArrow(new Arrow(xStart, yStart, xEnd, yEnd));
						ARROW_START = true;
						ARROW_MODE = false;

					}
				}
				else{

					Group labelRoot = new Group();
					Stage labelStage = new Stage();
					labelStage.setScene(new Scene(labelRoot, 200, 125));
					labelStage.setTitle("Input name for this node");

					TextField labelname = new TextField("Label Name");
					Button okbtn = new Button();
					Button cancelbtn = new Button();


					labelname.setTranslateY(25);
					labelname.setTranslateX(25);
					okbtn.setTranslateY(100);
					okbtn.setText("OK");
					okbtn.setOnAction(new EventHandler<ActionEvent>(){

						@Override
						public void handle(ActionEvent arg0) {
							if(!labelname.getText().equals("Label Name")){
								diag.addLabel(new Label(i, j, labelname.getText()));
							}
							labelStage.close();
						}

					});

					cancelbtn.setTranslateX(150);
					cancelbtn.setTranslateY(100);
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



	//initializes our main gridpane with all the buttons

	public void initGrid(GridPane grid, Diagram diag, int xDim, int yDim){




		diag = new Diagram(xDim, yDim);


		Button[][] btns = new Button[xDim][yDim];

		//initialize buttons
		//right now, pressing a button will query the user for a name
		//and then add a label with that name to the diagram


		for(int i =0;i<xDim;i++){
			for(int j = 0; j<yDim;j++){
				btns[i][j] = new Button();
				btns[i][j].setPrefSize(75, 75);
				btns[i][j].setOnAction(gridButtonAction(i, j, diag));
				grid.add(btns[i][j], 2*i, 2*j);
			}
		}

		Button arrowMode = new Button();
		arrowMode.setPrefSize(150, 75);
		arrowMode.setText("Arrow Mode");
		grid.add(arrowMode, 2*xDim, 0);
		arrowMode.setOnAction(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent arg){
				ARROW_MODE = !ARROW_MODE;
				ARROW_START = true;
				System.out.println("in arrow mode? "+ARROW_MODE);
			}			
		});



		Button renderBtn = new Button();
		renderBtn.setPrefSize(150, 75);
		renderBtn.setText("Render Diagram");
		grid.add(renderBtn, 4*xDim, 0);
		renderBtn.setOnAction(new EventHandler<ActionEvent>(){

			private Diagram anonDiag;

			@Override
			public void handle(ActionEvent arg){
				renderDiag(anonDiag);
			}
			public EventHandler<ActionEvent> setter(Diagram diag){
				this.anonDiag = diag;
				return this;
			}
		}.setter(diag));


//		//for debugging purposes
//		Button printDiagData = new Button();
//		printDiagData.setPrefSize(150, 75);
//		printDiagData.setText("Print Diagram to Console");
//		grid.add(printDiagData, 4*xDim, 0);
//		printDiagData.setOnAction(new EventHandler<ActionEvent>(){
//
//			private Diagram anonDiag;
//			@Override
//			public void handle(ActionEvent arg){
//
//				for(Label l : anonDiag.getLabels()){
//					System.out.println(l.getContent()+" "+l.getValX()+ " "+l.getValY());
//				}
//				for(Arrow e : anonDiag.getArrows()){
//					System.out.print(e.getStartX() + " "+e.getStartY()+" "+e.getEndX()+" "+e.getEndY());	
//				}
//			}
//			public EventHandler<ActionEvent> setter(Diagram diag){
//				this.anonDiag = diag;
//				return this;
//			}
//		}.setter(diag));


	}



	//produces a pictorial reprsentation of our diagram
	//currently creates a new Stage to display the diagram- this is just because it easier to do
	//it this way.
	//We should consider have it attached to the previous stage.

	public void renderDiag(Diagram diag){



		Group renderRoot = new Group();
		Stage renderStage = new Stage();
		renderStage.setTitle("Diagram Schematic");

		//We include the +2 to make our circle nodes fit in the diagram

		renderStage.setScene(new Scene(renderRoot, REND_DIAG*(diag.getxDim()+2), REND_DIAG*(diag.getyDim()+2)));
		for(Label l : diag.getLabels()){
			Circle c = new Circle(REND_DIAG_CIRC);
			c.setTranslateX(REND_DIAG*(l.getValX()+1));
			c.setTranslateY(REND_DIAG*(l.getValY()+1));
			c.setOpacity(.5);
			
			javafx.scene.control.Label lbl = new javafx.scene.control.Label(l.getContent());
			lbl.setTranslateX(REND_DIAG*(l.getValX()+1)-REND_DIAG_CIRC);
			lbl.setTranslateY(REND_DIAG*(l.getValY()+1)-2.5*REND_DIAG_CIRC);
			
			renderRoot.getChildren().addAll(lbl, c);
		}

		for(Arrow a : diag.getArrows()){
			Line l = new Line();
			l.setStartX((a.getStartX()+1)*REND_DIAG);
			l.setStartY((a.getStartY()+1)*REND_DIAG);
			l.setEndX((a.getEndX()+1)*REND_DIAG);
			l.setEndY((a.getEndY()+1)*REND_DIAG);
			l.setOpacity(.5);
			renderRoot.getChildren().add(l);
		}

		renderStage.show();

	}

	public void initLoadingMenu(Group loadingRoot, Stage loadingStage){
		TextField xVal = new TextField("X dimension");
		TextField yVal = new TextField("Y dimension");
		Button okbtn = new Button();
		Button cancelbtn = new Button();


		xVal.setTranslateY(25);
		xVal.setTranslateX(25);


		yVal.setTranslateY(65);
		yVal.setTranslateX(25);

		okbtn.setTranslateY(100);
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

		cancelbtn.setTranslateX(125);
		cancelbtn.setTranslateY(100);
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
		GridPane grid = new GridPane();

		initGrid(grid, diag, dimXDiagram, dimYDiagram);

		root.getChildren().add(grid);
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
