package zeidler.colin.rocketjournal.data;

import java.io.Serializable;
import java.util.Comparator;
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
public class FlightLog implements Serializable {

    private final int id;
    private int rocketID;
    private String motor;
    private int delay;
    private int altitude;
    private Date date;
    private String notes;
    private LaunchRes result;

    public FlightLog(int id) {
        if (id == -1)
            id = DataModel.getNextFlightLogID();
        this.id = id;
        this.motor = "No Motor";
        this.delay = -1;
        this.altitude = -1;
        this.date = new Date();
        this.notes = "";
        this.result = null;
    }

    public FlightLog(int id, int rocketID) {
        this(id);
        this.rocketID = rocketID;
    }

    public FlightLog(int id, int rocketID, String motor, int delay, int altitude,
                     Date date, String notes, LaunchRes result) {
        this(id, rocketID);
        this.motor = motor;
        this.delay = delay;
        this.altitude = altitude;
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

    public int getAltitude() {
        return altitude;
    }

    public void setAltitude(int altitude) {
        this.altitude = altitude;
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

    static public abstract class LogCompare implements Comparator<FlightLog> {
        public static final boolean ASCENDING_SORT = true;
        public static final boolean DESCENDING_SORT = false;

        protected boolean sortBy;

        public LogCompare() {
            sortBy = DESCENDING_SORT;
        }

        public void sortAs(boolean method) {
            sortBy = method;
        }
        public void flipSort() { sortBy = !sortBy; }
        abstract public int getType();

    }

    static public class DateCompare extends LogCompare {
        @Override
        public int compare(FlightLog lhs, FlightLog rhs) {
            if (sortBy)
                return lhs.getDate().compareTo(rhs.getDate());
            return rhs.getDate().compareTo(lhs.getDate());
        }

        @Override
        public int getType() {
            return 0;
        }
    }

    static public class ResultCompare extends LogCompare {
        @Override
        public int compare(FlightLog lhs, FlightLog rhs) {
            if (sortBy)
                return lhs.getResult().compareTo(rhs.getResult());
            return rhs.getResult().compareTo(lhs.getResult());
        }

        @Override
        public int getType() {
            return 1;
        }
    }

    static public class MotorCompare extends LogCompare {
        @Override
        public int compare(FlightLog lhs, FlightLog rhs) {
            if (sortBy)
                return lhs.getMotor().charAt(0) - rhs.getMotor().charAt(0);
            return rhs.getMotor().charAt(0) - lhs.getMotor().charAt(0);
        }

        @Override
        public int getType() {
            return 2;
        }
    }

    static public class AltitudeCompare extends LogCompare {

        @Override
        public int compare(FlightLog lhs, FlightLog rhs) {
            if (sortBy)
                return lhs.getAltitude() - rhs.getAltitude();
            return rhs.getAltitude() - lhs.getAltitude();
        }

        @Override
        public int getType() {
            return 3;
        }
    }
}
