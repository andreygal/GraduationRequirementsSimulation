import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Car implements Runnable {

    //angular velocity in rads/s
    private static double  angularVelocity;
    private static int numOfIntersections;
    //axis is shifted to correctly render the position of a car due to image's rotation point being the upper left corner
    private static double  rotCenterX;
    private static double  rotCenterY;
    private double centXoffset;
    private double centYoffset;
    //position is calculated from polar coordinates
    private double positionX;
    private double positionY;
    private double radius;
    //private static double  imageWidth;
    //private static double  imageHeight;
    private static Image carImage;
    private double startStopRadius;
    private int startIntersection;
    private int endIntersection;
    private double exitTime;
    //update arc is the subtended arc after which update is called.
    //we want to update the image every 2 pixels
    private double updateArc      =  2.0 / radius;
    //interval is in seconds
    static double arcAngle = 2 * Math.PI / (double) numOfIntersections;
    private double updateInterval = (updateArc / arcAngle);
    private long updateIntervalMilli = (long)(updateInterval * 1000.0);

    double currAngle;
    double prevTime;
    double newTime;

    boolean moving;
    Thread carThread;

    public Car(Image carImage, int lane, int startIntersection, int endIntersection, int numOfIntersections) {
        this.carImage = carImage;
        this.startIntersection = startIntersection;
        this.endIntersection = endIntersection;
        this.numOfIntersections = numOfIntersections;
        //calculate offset for the axis of rotation
        centXoffset = this.carImage.getWidth() / 2.0;
        centYoffset = this.carImage.getHeight() / 2.0;
        //set axis of rotation
        rotCenterX = Main.canvasCenterX - centXoffset;
        rotCenterY = Main.canvasCenterY - centYoffset;
        //calculate the radius for a given lane
        radius = MainLoop.ISLAND_WIDTH / 2.0 + (MainLoop.dashedMarkOffset * (0.5 + lane));
        //parameters for steering the car onto the lane
        this.startStopRadius = MainLoop.outerBound + 20;
        //set the starting position to be at a given intersection
        this.positionX = rotCenterX + startStopRadius * Math.cos(MainLoop.intersectRads.get(startIntersection - 1));
        this.positionY = rotCenterY + startStopRadius * Math.sin(MainLoop.intersectRads.get(startIntersection - 1));
        //calculate time when the car will exit the circle
        exitTime = MainLoop.globalTime + (endIntersection - startIntersection);
        if (exitTime < 0 ) exitTime += numOfIntersections;
        exitTime += MainLoop.enterInterTimeOffset;
        //cars move counter-clockwise and car array is flushed before each case so the velocity can be reset
        this.angularVelocity = -( (2 * Math.PI) / ((double) numOfIntersections));
        carThread = new Thread(this);
    }

    public void startCar() {
        System.out.println("Starting car thread");
        carThread.start();
    }

    public void update() {
        //interserctRads array stores negative angles as canvas uses clockwise rotation as positive
        //better to use relative positioning -> dTheta/dt than absolute positioning
        prevTime = MainLoop.globalTime;
        try {
            Thread.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        newTime = MainLoop.globalTime;
        currAngle = (currAngle + ((newTime - prevTime) * angularVelocity)) % (2 * Math.PI);
        positionX = rotCenterX + radius * Math.cos(currAngle);
        positionY = rotCenterY + radius * Math.sin(currAngle);

        //positionX = rotCenterX + radius * Math.cos((Math.abs(time + MainLoop.enterInterTimeOffset)) * angularVelocity - offset);
        //positionY = rotCenterY + radius * Math.sin((Math.abs(time + MainLoop.enterInterTimeOffset)) * angularVelocity - offset);
        //Thread needs to yield to handle() for proper rendering.
        //System.out.println("Updating position x " + positionX + " y " + positionY);
        Thread.yield();
    }

    public void stopCar() {
        moving = false;
    }

    private void enterCircle() {
        System.out.println("Car approaching lane");
        double prevTime = MainLoop.globalTime;
        double startTime = MainLoop.globalTime;
        double timeToLane = Math.abs(Math.floor(prevTime + MainLoop.enterInterTimeOffset) - prevTime);
        double radReductionRate = (startStopRadius - radius) / timeToLane;

        double currRadius = startStopRadius;
        //we care only care about delata t after starting the while loop 
        while(Math.abs(MainLoop.globalTime - startTime) < timeToLane) {
                currRadius -= (radReductionRate * (MainLoop.globalTime - prevTime));
                //calculate new position based on reduced radius
                positionX = rotCenterX + currRadius * Math.cos(MainLoop.intersectRads.get(startIntersection - 1));
                positionY = rotCenterY + currRadius * Math.sin(MainLoop.intersectRads.get(startIntersection - 1));
                prevTime = MainLoop.globalTime;
                Thread.yield();
        }
        moving = true;
        currAngle =  MainLoop.intersectRads.get(startIntersection - 1);
    }
    
     private void leaveCircle() {
        System.out.println("Car leaving the circle");
        double prevTime = MainLoop.globalTime;
        double startTime = MainLoop.globalTime;
        double timeToInter = Math.abs(Math.floor(prevTime + MainLoop.enterInterTimeOffset) - prevTime);
        double radElongRate = (startStopRadius - radius) / timeToInter;

        double currRadius = startStopRadius;
        while(MainLoop.globalTime - startTime < timeToInter) {
                currRadius += (radElongRate * (MainLoop.globalTime - prevTime));
                //calculate new position based on reduced radius
                positionX = rotCenterX + currRadius * Math.cos(MainLoop.intersectRads.get(endIntersection - 1));
                positionY = rotCenterY + currRadius * Math.sin(MainLoop.intersectRads.get(endIntersection - 1));
                prevTime = MainLoop.globalTime;
                Thread.yield();
        }
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
    
    public boolean isMoving() { return moving; }

    @Override
    public void run() {
        moving = true;
        long updateStartTime = System.currentTimeMillis();
        long elapsedTime = 0;

        enterCircle();

        while(moving && (MainLoop.globalTime <= exitTime)) {
            if (elapsedTime >= updateIntervalMilli) {
                update();
                elapsedTime = 0;
                updateStartTime = System.currentTimeMillis();
            }

            elapsedTime = System.currentTimeMillis() - updateStartTime;
            //System.out.println("Inside car run: updating car position");
            //System.out.println("Position " + positionX + ", " + positionY);
        }
        //leave the traffic circle
        leaveCircle();
    }
}
