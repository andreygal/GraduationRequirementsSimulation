
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;



public class Main extends Application {
    static int numOfCars = 1;
    static int numOfIntersections = 1;
    static double arcAngle = 2 * Math.PI / (double) numOfIntersections;

    static Canvas canvas;
    static MainLoop mainLoop;

    @Override
    public void start(Stage primaryStage) throws Exception
    {
        //set up left side
        canvas = new Canvas(720, 720);
        GraphicsContext gc = canvas.getGraphicsContext2D();


        //Add UI Control Panel (right side)
        Parent UIPanel = FXMLLoader.load(getClass().getResource("UIGraduationRequirements.fxml"));
        //JavaFX setup
        primaryStage.setTitle("Graduation Requirements Simulator");
        BorderPane root = new BorderPane();
        root.setLeft(canvas);
        root.setRight(UIPanel);
        Scene scene = new Scene(root, 920, 720, Color.BLUE);
        primaryStage.setScene(scene);

        //Main animation loop
        mainLoop = new MainLoop();

        //Dynamically draw the background
        gc.setFill(Color.GREEN);
        gc.fillRect(0, 0, Main.canvas.getWidth(), Main.canvas.getHeight());
        //draw the traffic circle, lanes and intersections
        gc.setFill(Color.BLACK);
        gc.fillOval(mainLoop.canvasCenterX - mainLoop.ISLAND_WIDTH/2,
                    mainLoop.canvasCenterY - mainLoop.ISLAND_HEIGHT/2, mainLoop.ISLAND_WIDTH, mainLoop.ISLAND_HEIGHT);
        //Draw the lanes
        double trackRadiusOfCurv;
        for (int i = 1; i <= numOfCars; i++) {
            trackRadiusOfCurv = (mainLoop.ISLAND_WIDTH / 2.0) + 20.0 * i;
            gc.strokeOval(mainLoop.canvasCenterX - trackRadiusOfCurv,
                          mainLoop.canvasCenterY - trackRadiusOfCurv,
                          trackRadiusOfCurv * 2, trackRadiusOfCurv * 2);
        }

        primaryStage.show();
    }
    
    

    public static void main(String[] args) {
        launch(args);
    }
}

