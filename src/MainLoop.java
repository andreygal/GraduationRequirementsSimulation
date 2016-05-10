import javafx.animation.AnimationTimer;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;

public class MainLoop extends AnimationTimer {
    ArrayList<Car> cars = new ArrayList<>();

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
        gc.fillOval(225, 225, 50, 50);
        gc.strokeOval(150, 150, 200, 200);

        for(int i = 0; i < cars.size(); i++);
        //gc.drawImage(image, cars.get(i).getX(), cars.get(i).getY());
    }


}
