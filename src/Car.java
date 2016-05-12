import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Car implements Runnable {

    //velocity in rads/s
    Thread carThread;
    static double  velocity;
    static double  centerX = 250;
    static double  centerY = 250;
    static Image   carImage;
    private String carName;
    private double positionX;
    private double positionY;
    private double radius = 120;
    boolean moving;

    public Car(Image carImage, double centerX, double centerY, String carName) {
        this.carImage = carImage;
        this.carName = carName;
        this.centerX = centerX;
        this.positionY = centerY;
        this.velocity = Main.velocity;
        carThread = new Thread(this, carName);
    }
    public void startCar() {
        System.out.println("Starting car thread");
        carThread.start();
    }
    public void update(double time) {
        positionX = centerX + radius * Math.cos(time/(2*Math.PI));
        positionY = centerY + radius * Math.sin(time/(2*Math.PI));
    }

    public void stopCar() {
        moving = false;
    }

    public void render(GraphicsContext gc) {
        gc.drawImage(carImage, positionX, positionY);

    }

    public Rectangle2D getBoundary() {
        return new Rectangle2D(positionX, positionY, carImage.getWidth(), carImage.getHeight());
    }

    public boolean intersects(Car anotherCar) {
        return anotherCar.getBoundary().intersects(this.getBoundary());
    }

    @Override
    public void run() {
        moving = true;

        while(moving) {
            update(MainLoop.lastSecond);
            //System.out.println("Inside car run: updating car position");
            //System.out.println("Position " + positionX + ", " + positionY);
        }

    }
}
