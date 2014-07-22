package zeidler.colin.rocketjournal.data;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Colin on 2014-07-08.
 *
 * Represents a Journal Object, which is a flight log that contains
 * the name, motor, delay, weight of the rocket as well as the launch date,
 * result of the flight, and any notes the user wants.
 *
 * Also defines the Enum that is used for all the possible flight results.
 */
public class Journal implements Serializable {
    private String rName;
    private String motor;
    private int delay;
    private Date launchDate;
    private String notes;
    private LaunchRes result;
    private float weight;   //weight of the rocket in pounds

    public static enum LaunchRes {
        SUCCESS("Success"),
        NO_FIRE("No Fire"),
        CATO("CATO"),
        NO_DEPLOY("No Deploy");

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
    }

    public Journal(String name, String motor, int delay, Date date, String notes, LaunchRes result, float weight) {
        this.rName = name;
        this.motor = motor;
        this.delay = delay;
        this.launchDate = date;
        this.notes = notes;
        this.result = result;
        this.weight = weight;
    }

    @Override
    public String toString() {
        return rName + " on a " + motor + " " + delay + "s, flight was: " + result.toString();
    }

    public Date getLaunchDate() {
        return launchDate;
    }

    public float getWeight() {
        return weight;
    }

    public int getDelay() {
        return delay;
    }

    public LaunchRes getResult() {
        return result;
    }

    public String getMotor() {
        return motor;
    }

    public String getNotes() {
        return notes;
    }

    public String getrName() {
        return rName;
    }
}
