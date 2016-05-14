import java.util.Comparator;

/**
 * Created by andre on 5/14/2016.
 */
public class CarRecord {
    public int startTime;
    public int startIntersection;
    public int endIntersection;

    CarRecord(int startIntersection, int endIntersection, int startTime) {
        this.startIntersection = startIntersection;
        this.endIntersection = endIntersection;
        this.startTime = startTime;
    }

}


