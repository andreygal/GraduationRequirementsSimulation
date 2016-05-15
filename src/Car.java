import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Car implements Runnable {

    //angular velocity in rads/s
    private static double  angularVelocity;
    private static double  rotCenterX;
    private static double  rotCenterY;
    private static double  imageWidth;
    private static double  imageHeight;
    private static Image   carImage;
    private double positionX;
    private double positionY;
    private double centXoffset;
    private double centYoffset;
    private double radius = 34;
    private double startStopRadius;
    private int startIntersection;
    //update arc is the subtended arc after which update is called.
    //we want to update the image every 2 pixels
    private double updateArc      =  2.0 / radius;
    //interval is in seconds
    private double updateInterval = (updateArc/Main.arcAngle);
    private long updateIntervalMilli = (long)(updateInterval * 1000.0);

    boolean moving;
    Thread carThread;

    public Car(Image carImage, double centerX, double centerY, double radius, int startIntersection) {
        this.carImage = carImage;
        //calculate offset for the axis of rotation
        centXoffset = this.carImage.getWidth() / 2.0;
        centYoffset = this.carImage.getHeight() / 2.0;
        //set axis of rotation
        
        this.startIntersection = startIntersection;
        this.centerX = centerX;
        this.centerY = centerY;
        
        this.imageWidth = carImage.getWidth();
        this.imageHeight = carImage.getHeight();
        this.radius = radius;
        this.startStopRadius = MainLoop.outerBound + 20;
        //set the starting position to be the intersection with traffic circle
        this.positionX = centerX + startStopRadius * Math.cos(MainLoop.intersectRads.get(startIntersection - 1));
        this.positionY = centerY + startStopRadius * Math.sin(MainLoop.intersectRads.get(startIntersection - 1));
        //cars move counter-clockwise and car array is flushed before each case so the velocity can be reset
        this.angularVelocity = -( (2 * Math.PI) / ((double) Main.numOfIntersections));
        carThread = new Thread(this);
    }

    public void startCar() {
        System.out.println("Starting car thread");
        carThread.start();
    }

    public void update(double time) {
        double offset = MainLoop.intersectRads.get(startIntersection - 1);
        //interserctRads array strores negative angles as canvas uses clockwise rotation as positive
        positionX = centerX + radius * Math.cos(time * angularVelocity - offset);
        positionY = centerY + radius * Math.sin(time * angularVelocity - offset);
        //System.out.println("Updating position x " + positionX + " y " + positionY);
    }

    public void stopCar() {
        moving = false;
    }

    private void enterIntersection() throws InterruptedException {
        double prevTime = MainLoop.globalTime;
        double startTime = MainLoop.globalTime;
        double timeToLane = Math.floor(prevTime + 5) - prevTime;
        double radReductionRate = (startStopRadius - radius) / timeToLane;

        double currRadius = startStopRadius;
        while(MainLoop.globalTime - startTime < timeToLane) {
                currRadius -= (radReductionRate * (MainLoop.globalTime - prevTime));
                //calculate new position based on reduced radius
                positionX = centerX + currRadius * Math.cos(MainLoop.intersectRads.get(startIntersection - 1));
                positionY = centerY + currRadius * Math.sin(MainLoop.intersectRads.get(startIntersection - 1));
                prevTime = MainLoop.globalTime;
                Thread.sleep(20);
        }
    }

    private void leaveCircle() {

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
        long startTime = System.currentTimeMillis();
        long elapsedTime = 0;

        try {
            enterIntersection();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        while(moving) {
            if (elapsedTime >= updateIntervalMilli) {
                update(MainLoop.globalTime);
                elapsedTime = 0;
                startTime = System.currentTimeMillis();
            }

            elapsedTime = System.currentTimeMillis() - startTime;
            //System.out.println("Inside car run: updating car position");
            //System.out.println("Position " + positionX + ", " + positionY);
        }

    }
}
