package zeidler.colin.rocketjournal.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;


/**
 * Created by Colin on 2014-07-21.
 *
 * Data structure to contain data about a rocket
 */
public class Rocket implements Serializable {

    private final int id;
    private String name;
    private float weight;
    private int flightCount;
    private int maxAltitude;
    private String image;
    private ArrayList<Integer> flightLogIDs;

    public Rocket(int id) {
        if (id == -1)
            id = DataModel.getNextRocketID();
        this.id = id;
        this.flightCount = 0;
        this.maxAltitude = -1;
        this.flightLogIDs = new ArrayList<Integer>();
        this.image = "";
    }

    /**
     * Create a new Rocket object
     * @param id        The ID of the rocket (returned from the DataManager)
     * @param name      The Name of the Rocket
     * @param weight    The weight of the Rocket in pounds
     */
    public Rocket(int id, String name, float weight) {
        this(id);
        this.name = name;
        this.weight = weight;
    }

    public Rocket(int id, String name, float weight, int flightCount, int maxAltitude, String image) {
        this(id, name, weight);
        this.flightCount = flightCount;
        this.maxAltitude = maxAltitude;
        this.image = image;
    }

    public ArrayList<Integer> getFlightLogIDs() {
        return flightLogIDs;
    }

    public void setFlightLogIDs(ArrayList<Integer> flightLogIDs) {
        this.flightLogIDs = flightLogIDs;
        this.flightCount = this.flightLogIDs.size();
    }

    public void addFlightLogID(int flightLogID) {
        this.flightLogIDs.add(flightLogID);
        this.flightCount++;
    }

    public void removeFlightLog(int flightLogID) {
        if (this.flightLogIDs.contains(flightLogID)) {
            this.flightCount--;
            this.flightLogIDs.remove(this.flightLogIDs.indexOf(flightLogID));
        }
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public int getFlightCount() {
        return flightCount;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getMaxAltitude() {
        return maxAltitude;
    }

    public void setMaxAltitude(int maxAltitude) {
        if (maxAltitude > this.maxAltitude)
            this.maxAltitude = maxAltitude;
    }

    @Override
    public String toString() {
        return this.name;
    }


    static public abstract class RocketCompare implements Comparator<Rocket> {
        public static final boolean ASCENDING_SORT = true;
        public static final boolean DESCENDING_SORT = false;

        protected boolean sortBy;

        public RocketCompare() {
            sortBy = DESCENDING_SORT;
        }

        public void sortAs(boolean method) {
            sortBy = method;
        }
        public void flipSort() { sortBy = !sortBy; }
        abstract public int getType();
    }

    static public class NameCompare extends RocketCompare {
        @Override
        public int compare(Rocket lhs, Rocket rhs) {
            if (sortBy)
                return lhs.getName().compareTo(rhs.getName());
            return  rhs.getName().compareTo(lhs.getName());
        }

        @Override
        public int getType() {
            return 0;
        }
    }

    static public class FlightCountCompare extends RocketCompare {
        @Override
        public int compare(Rocket lhs, Rocket rhs) {
            if (sortBy)
                return lhs.getFlightCount() - rhs.getFlightCount();
            return rhs.getFlightCount() - lhs.getFlightCount();
        }

        @Override
        public int getType() {
            return 1;
        }
    }

    static public class AltitudeCompare extends RocketCompare {
        @Override
        public int compare(Rocket lhs, Rocket rhs) {
            if (sortBy)
                return lhs.getMaxAltitude() - rhs.getMaxAltitude();
            return rhs.getMaxAltitude() - lhs.getMaxAltitude();
        }

        @Override
        public int getType() {
            return 2;
        }
    }
}
