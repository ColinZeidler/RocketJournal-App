package zeidler.colin.rocketjournal.data;

import java.io.Serializable;
import java.util.ArrayList;

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
    private String image;
    private ArrayList<Integer> flightLogIDs;

    public Rocket(int id) {
        if (id == -1)
            id = DataModel.getNextRocketID();
        this.id = id;
        this.flightCount = 0;
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

    public Rocket(int id, String name, float weight, int flightCount, String image) {
        this(id, name, weight);
        this.flightCount = flightCount;
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

    @Override
    public String toString() {
        return this.name;
    }
}
