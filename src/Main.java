
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;


public class Main extends Application {
    static double velocity;
    static int numberOfCars;
    static int numOfIntersections;

    MainLoop mainLoop = new MainLoop();
    static Canvas canvas;

    public VBox createControlPan() {
        VBox panel = new VBox(10);
        panel.setPadding(new Insets(10));
        Button start = new Button("Start");
        panel.setAlignment(Pos.CENTER);
        panel.getChildren().addAll(start);

        return panel;
    }

    @Override
    public void start(Stage primaryStage) throws Exception
    {
        canvas = new Canvas(500, 500);

        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.GREEN);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());


        primaryStage.setTitle("Graduation Requirements Simulator");
        BorderPane root = new BorderPane();
        root.setLeft(canvas);
        root.setRight(createControlPan());
        Scene scene = new Scene(root, 700, 500, Color.BLUE);
        primaryStage.setScene(scene);


        //add as a listener to a Start button
        mainLoop.start();

        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

