import javafx.animation.AnimationTimer;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;

public class MainLoop extends AnimationTimer {
    ArrayList<Car> cars = new ArrayList<>();
    final private double CENTER_WIDTH  = 50;
    final private double CENTER_HEIGHT = 50;
    
    private double canvasCenterX = Main.canvas.getWidth()/2.0;
    private double canvasCenterY = Main.canvas.getHeight()/2.0;
    
    double currentTime = 0.0;
    long lastSecond = 0;

    @Override
    public void handle(long currentNanoTime)
    {
        currentTime += currentNanoTime / 1e9;

        if( Math.floor(currentTime) > lastSecond) {

            //do the logic

            lastSecond++;
        }

        GraphicsContext gc = Main.canvas.getGraphicsContext2D();

        gc.setFill(Color.BLACK);
        gc.fillOval(225, 225, CENTER_WIDTH, CENTER_HEIGHT);
        //https://jaxenter.com/tutorial-a-glimpse-at-javafxs-canvas-api-105696.html
        for (int i = 1; i <= Main.numOfCars) {
            
        }
        gc.strokeOval(150, 150, 200, 200);

        for(int i = 0; i < cars.size(); i++);
        //gc.drawImage(image, cars.get(i).getX(), cars.get(i).getY());
    }


}
