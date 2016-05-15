
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
    //static int numOfCars = 1;
    //static int numOfIntersections = 1;
    //static double arcAngle = 2 * Math.PI / (double) numOfIntersections;
    //canvas is a square
    final static double canvasSide = 720;
    final static double canvasCenterX = canvasSide / 2.0;
    final static double canvasCenterY = canvasSide / 2.0;

    static Canvas canvas;
    static MainLoop mainLoop;
    //input management
    File inputFile;
    static ArrayList<CaseRecord> cases = new ArrayList<>();

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
            int X = sc.nextInt();
            int N = sc.nextInt();
            CaseRecord caseRec = new CaseRecord(i, C, N, X);

            for (int j = 0; j < C; j++) {
                int s = sc.nextInt(); //start intersection
                int e = sc.nextInt(); //end intersection
                int t = sc.nextInt(); //start time
                caseRec.carQueue.add(new CarRecord(s, e, t));
            }
            cases.add(caseRec);
        }

        sc.close();
    }

    @Override
    public void start(Stage primaryStage) throws Exception
    {
        //set up left side
        canvas = new Canvas(canvasSide, canvasSide);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.GREEN);
        gc.fillRect(0, 0, canvasSide, canvasSide);

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

        });

        primaryStage.show();
    }
    


    public static void main(String[] args) {
        launch(args);
    }
}

