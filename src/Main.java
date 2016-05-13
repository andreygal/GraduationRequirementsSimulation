
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


        //Add UI Control Panel (right side) and store the reference to Panel Controls
        //Parent UIPanel = FXMLLoader.load(getClass().getResource("UIGraduationRequirements.fxml"));
        FXMLLoader fxmlLoader = new FXMLLoader();
        Parent UIPanel = fxmlLoader.load(getClass().getResource("UIGraduationRequirements.fxml").openStream());
        ControlPanel controlPanel = fxmlLoader.getController();
        //JavaFX setup
        primaryStage.setTitle("Graduation Requirements Simulator");
        BorderPane root = new BorderPane();
        root.setLeft(canvas);
        root.setRight(UIPanel);
        Scene scene = new Scene(root, 920, 720, Color.BLUE);
        primaryStage.setScene(scene);
        //Main animation loop
        mainLoop = new MainLoop();
        //add time listener
        mainLoop.addPropertyChangeListener( propertyChanged -> {
            controlPanel.setGlobalTime((double) propertyChanged.getNewValue());
        });

        //Dynamically draw the background
        gc.setFill(Color.GREEN);
        gc.fillRect(0, 0, Main.canvas.getWidth(), Main.canvas.getHeight());


        primaryStage.show();
    }
    
    

    public static void main(String[] args) {
        launch(args);
    }
}

