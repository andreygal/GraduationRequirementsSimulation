
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;


public class Main extends Application {
    static double velocity;
    static int numOfCars = 1;
    static int numOfIntersections = 1;

    static Canvas canvas;
    static MainLoop mainLoop;

    @Override
    public void start(Stage primaryStage) throws Exception
    {   //Main animation loop
        mainLoop = new MainLoop();
        //set up left side
        canvas = new Canvas(500, 500);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.GREEN);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        //Add UI Control Panel (right side)
        Parent UIPanel = FXMLLoader.load(getClass().getResource("UIGraduationRequirements.fxml"));
        //JavaFX setup
        primaryStage.setTitle("Graduation Requirements Simulator");
        BorderPane root = new BorderPane();
        root.setLeft(canvas);
        root.setRight(UIPanel);
        Scene scene = new Scene(root, 700, 500, Color.BLUE);
        primaryStage.setScene(scene);

        primaryStage.show();
    }
    
    

    public static void main(String[] args) {
        launch(args);
    }
}

