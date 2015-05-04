package org.gcdedit;

import java.util.Scanner;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class CalGUI extends Application {


	//these are control variables to make adding arrows work correctly
	
	private boolean ARROW_MODE = false;
	private boolean ARROW_START = true;
	
	private int xStart;
	private int yStart;
	private int xEnd;
	private int yEnd;
	




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
					System.out.println("Input name");

					//temporary interface- will be replaced with a dialogue box
					Scanner sn = new Scanner(System.in);
					String s = sn.nextLine();
					diag.addLabel(new Label(i, j, s));
					sn.close();
				}
			}
		});
	}

	
	public void init(GridPane grid, Diagram diag, int xDim, int yDim){




		diag = new Diagram(xDim, yDim);


		Button[][] btns = new Button[xDim][yDim];

		//initialize buttons
		//right now, pressing a button will query the user for a name
		//and then add a label with that name to the diagram



		//We need to declare the arrow for arrow mode out here
		//otherwise it would be redeclared each time we press a button


		for(int i =0;i<xDim;i++){
			for(int j = 0; j<yDim;j++){
				btns[i][j] = new Button();
				btns[i][j].setPrefSize(100, 100);
				btns[i][j].setOnAction(gridButtonAction(i, j, diag));
				grid.add(btns[i][j], 2*i, 2*j);
			}
		}

		Button arrowMode = new Button();
		arrowMode.setPrefSize(150, 100);
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


		//for debugging purposes
		Button printDiagData = new Button();
		printDiagData.setPrefSize(150, 100);
		printDiagData.setText("Print Diagram to Console");
		grid.add(printDiagData, 4*xDim, 0);
		printDiagData.setOnAction(new EventHandler<ActionEvent>(){

			private Diagram anonDiag;
			@Override
			public void handle(ActionEvent arg){
				
				for(Label l : anonDiag.getLabels()){
					System.out.println(l.getContent());
				}
				for(Arrow e : anonDiag.getArrows()){
					System.out.print(e.getxStart() + " "+e.getyStart()+" "+e.getxEnd()+" "+e.getyEnd());	
				}
			}
			public EventHandler<ActionEvent> setter(Diagram diag){
				this.anonDiag = diag;
				return this;
			}
		}.setter(diag));


	}

	@Override
	public void start(Stage primarystage) throws Exception {

		
		
		
		Stage stage = new Stage();
		Group root = new Group();
		Scene scene = new Scene(root);
		stage.setScene(scene);


		Diagram diag = null;
		GridPane grid = new GridPane();

		//should query user about size of diagram- but for now we just use 5, 5

		init(grid, diag, 5, 5);


		root.getChildren().add(grid);
		stage.show();

	}

	public static void main(String[] args) {
		Application.launch(args);
	}


}
