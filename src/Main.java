
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;


public class Main extends Application {
    static int numOfCars = 1;
    static int numOfIntersections = 1;
    static double arcAngle = 2 * Math.PI / (double) numOfIntersections;

    static Canvas canvas;
    static MainLoop mainLoop;
    //input management
    File inputFile;
    ArrayList<CaseRecord> cases = new ArrayList<>();

    private void readFile(File inputFile){
        Scanner sc = null;
        try {
            sc = new Scanner(new FileReader(inputFile));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        int caseCnt = sc.nextInt();
        for (int i = 1; i <= caseCnt; i++) {
            int C = sc.nextInt();
            int N = sc.nextInt();
            int X = sc.nextInt();
            CaseRecord caseRec = new CaseRecord(i, C, N, X);

            for (int j = 0; i < C; i++) {
                int s = sc.nextInt(); //start intersection
                int e = sc.nextInt(); //end intersection
                int t = sc.nextInt(); //start time
                caseRec.carQueue.add(new CarRecord(s, e, t));
            }
        }


    }

    @Override
    public void start(Stage primaryStage) throws Exception
    {
        //set up left side
        canvas = new Canvas(720, 720);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.GREEN);
        gc.fillRect(0, 0, Main.canvas.getWidth(), Main.canvas.getHeight());

        //Add UI Control Panel (right side) and store a reference to Panel Control
        FXMLLoader fxmlLoader = new FXMLLoader();
        Parent UIPanel = fxmlLoader.load(getClass().getResource("UIGraduationRequirements.fxml").openStream());
        ControlPanel controlPanel = fxmlLoader.getController();

        //JavaFX setup: setup up Border Pane
        primaryStage.setTitle("Graduation Requirements Simulator");
        BorderPane root = new BorderPane();
        root.setCenter(canvas);
        FlowPane pane = new FlowPane(UIPanel);
        root.setRight(pane);
        Scene scene = new Scene(root, 920, 720, Color.BLUE);
        primaryStage.setScene(scene);

        //Main animation loop
        mainLoop = new MainLoop();

        //add time listener
        mainLoop.addPropertyChangeListener( propertyChanged -> {
            controlPanel.setGlobalTime((double) propertyChanged.getNewValue());
        });

        //set up a stream for reading the input file
        controlPanel.addPropertyChangeListener( propertyChanged -> {
            inputFile = (File) propertyChanged.getNewValue();
            readFile(inputFile);
            System.out.println(inputFile.getAbsoluteFile());
            for (CaseRecord caseRec : cases) {
              System.out.println("Case num " + caseRec.caseNum +
                                 " Num of cars " + caseRec.numOfCars +
                                 " Num of inter " + caseRec.numOfIntersections +
                                 " Sim End Time " + caseRec.simEndTime);
                for (CarRecord carRec : caseRec.carQueue) {
                    System.out.println("s " + carRec.startIntersection +
                                       "e " + carRec.endIntersection +
                                       "t " + carRec.startTime);
                }
            }
        });


        primaryStage.show();
    }
    
    

    public static void main(String[] args) {
        launch(args);
    }
}

