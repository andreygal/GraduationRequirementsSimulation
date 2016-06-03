import java.util.Comparator;

/**
 * Created by andre on 5/14/2016.
 */
public class CarRecord {
    public int startTime;
    public int startIntersection;
    public int endIntersection;
    public int travelTime;
    boolean myCar;

    CarRecord(int startIntersection, int endIntersection, int startTime, boolean myCar) {
        this.startIntersection = startIntersection;
        this.endIntersection = endIntersection;
        this.startTime = startTime;
        this.myCar = myCar;
        this.travelTime = 0;
    }

    CarRecord(int startIntersection, int endIntersection, int startTime, boolean myCar, int travelTime) {
        this(startIntersection, endIntersection, startTime, myCar);
        this.travelTime = travelTime;
    }


}


