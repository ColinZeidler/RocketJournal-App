package zeidler.colin.rocketjournal.data;

import java.io.Serializable;
import java.util.Date;

import zeidler.colin.rocketjournal.dbmanager.DataManager;

/**
 * Created by Colin on 2014-07-08.
 *
 * Represents a Journal Object, which is a flight log that contains
 * the name, motor, delay, weight of the rocket as well as the launch date,
 * result of the flight, and any notes the user wants.
 *
 * Also defines the Enum that is used for all the possible flight results.
 */
public class FlightLog implements Serializable {

    private final int id;
    private int rocketID;
    private String motor;
    private int delay;
    private Date date;
    private String notes;
    private LaunchRes result;

    public FlightLog(int id) {
        if (id == -1)
            id = DataModel.getNextFlightLogID();
        this.id = id;
        this.motor = "No Motor";
        this.delay = -1;
        this.date = new Date();
        this.notes = "";
        this.result = null;
    }

    public FlightLog(int id, int rocketID) {
        this(id);
        this.rocketID = rocketID;
    }

    public FlightLog(int id, int rocketID, String motor, int delay,
                     Date date, String notes, LaunchRes result) {
        this(id, rocketID);
        this.motor = motor;
        this.delay = delay;
        this.date = date;
        this.notes = notes;
        this.result = result;
    }

    public int getId() {
        return id;
    }

    public int getRocketID() {
        return rocketID;
    }

    public void setRocketID(int rocketID) {
        this.rocketID = rocketID;
    }

    public String getMotor() {
        return motor;
    }

    public void setMotor(String motor) {
        this.motor = motor;
    }

    public int getDelay() {
        return delay;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public LaunchRes getResult() {
        return result;
    }

    public void setResult(LaunchRes result) {
        this.result = result;
    }

    public static enum LaunchRes {
        SUCCESS("Success"),
        NO_FIRE("No Fire"),
        LATE_DEPLOY("Late Deploy"),
        NO_DEPLOY("No Deploy"),
        CATO("CATO");

        private String text;

        LaunchRes(String text) {
            this.text = text;
        }

        public static LaunchRes fromString(String text) {
            if (text != null) {
                for (LaunchRes l: LaunchRes.values()) {
                    if (text.equalsIgnoreCase(l.text))
                        return l;
                }
            }
            return null;
        }

        @Override
        public String toString() {
            return text;
        }

        public static String[] names() {
            LaunchRes[] reses = values();
            String[] names = new String[reses.length];

            for (int i = 0; i < reses.length; i++)
                names[i] = reses[i].toString();

            return names;
        }
    }
}
