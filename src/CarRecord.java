import java.util.Comparator;

/**
 * Created by andre on 5/14/2016.
 */
public class CarRecord implements Comparator<CarRecord> {
    public int startTime;
    public int startIntersection;
    public int endIntersection;

    CarRecord(int startIntersection, int endIntersection, int startTime) {
        this.startIntersection = startIntersection;
        this.endIntersection = endIntersection;
        this.startTime = startTime;
    }
    @Override
    public int compare(CarRecord c1, CarRecord c2) {
        return (c1.startTime < c2.startTime) ? -1 : ((c1.startTime > c2.startTime) ? 1 : 0);
    }
}


