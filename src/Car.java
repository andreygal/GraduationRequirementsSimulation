import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Car implements Runnable {

    //velocity in rads/s
    static double  velocity;
    static double  centerX;
    static double  centerY;
    static Image   carImage;
    private double positionX;
    private double positionY;
    private double radius;

    public Car(Image carImage, double positionX, double positionY) {
        this.carImage = carImage;
        this.positionX = positionX;
        this.positionY = positionY;

    }
    public void update(double time) {
        positionX = centerX + radius * Math.cos(velocity * time);
        positionY = centerY + radius * Math.sin(velocity * time);
    }

    public void render(GraphicsContext gc) {
        gc.drawImage(carImage, positionX, positionY);
    }

    public Rectangle2D getBoundary() {
        return new Rectangle2D(positionX, positionY, carImage.getWidth(), carImage.getHeight());
    }

    public boolean intersercts(Car anotherCar) {
        return anotherCar.getBoundary().intersects(this.getBoundary());
    }

    @Override
    public void run() {


    }
}
