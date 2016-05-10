import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import java.util.ArrayList;

public class Main extends Application {

    MainLoop mainLoop = new MainLoop();
    static Canvas canvas;

    @Override
    public void start(Stage primaryStage) throws Exception
    {
        canvas = new Canvas(500, 500);

        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.GREEN);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());


        primaryStage.setTitle("Super Moving Car Simulator");
        BorderPane root = new BorderPane();
        root.setLeft(canvas);
        root.setRight(new VBox());
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

