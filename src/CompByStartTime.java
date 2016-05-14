import java.util.Comparator;

/**
 * Created by andre on 5/14/2016.
 */
public class CompByStartTime implements Comparator<CarRecord> {

    @Override
    public int compare(CarRecord c1, CarRecord c2) {
        return (c1.startTime < c2.startTime) ? -1 : ((c1.startTime > c2.startTime) ? 1 : 0);
    }
}
