package zeidler.colin.rocketjournal.data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Colin on 2014-07-21.
 *
 * Data structure to contain data about a rocket
 */
public class Rocket {

    private int id;
    private String name;
    private float weight;
    private int flightCount;
    private ArrayList<Journal> journals;

    /**
     * Create a new Rocket object
     * @param id        The ID of the rocket (returned from the DataManager)
     * @param name      The Name of the Rocket
     * @param weight    The weight of the Rocket in pounds
     */
    public Rocket(int id, String name, float weight) {
        this.id = id;
        this.name = name;
        this.weight = weight;
        this.flightCount = 0;
        this.journals = new ArrayList<Journal>();
    }

    /**
     * Create New Rocket object
     * Used mainly when loading from the database
     * @param id        The ID of the rocket (returned from the DataManager)
     * @param name      The Name of the Rocket
     * @param weight    The weight of the Rocket in pounds
     * @param journals  The Journals that the Rocket is listed in
     */
    public Rocket(int id, String name, float weight, ArrayList<Journal> journals) {
        this(id, name, weight);
        this.journals = journals;
        this.flightCount = journals.size();
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public ArrayList<Journal> getJournals() {
        return journals;
    }

    public void addJournal(Journal journal) {
        journals.add(journal);
    }

    public int getFlightCount() {
        return flightCount;
    }
}
