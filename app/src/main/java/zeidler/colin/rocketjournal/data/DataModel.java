package zeidler.colin.rocketjournal.data;

import android.content.Context;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import zeidler.colin.rocketjournal.dbmanager.DataManager;

/**
 * Created by Colin on 2014-07-25.
 *
 * Class to maintain an in memory model of the current set of data objects.
 */
public class DataModel {
    private List<Rocket> rockets;
    private List<FlightLog> flightLogs;
    private static DataModel instance;
    private DataManager dbManager;
    private int tabPosition = 0;

    public DataModel(Context context) {
        dbManager = DataManager.getInstance(context);
        rockets = dbManager.getAllRockets();
        flightLogs = dbManager.getAllFlightLogs();
        for (Rocket rocket : rockets) {
            rocket.setFlightLogIDs((ArrayList<Integer>) getFlightLogIDs(rocket.getId()));
        }
    }

    public static synchronized DataModel getInstance(Context context) {
        if (instance == null)
            instance = new DataModel(context);
        return instance;
    }

    //Kind of bad to do, but not really sure how else to do this
    //never will get called when there is no instance of DataModel
    public static int getNextRocketID() {
        int id = 0;
        for (Rocket rocket : DataModel.instance.rockets) {
            id = Math.max(id, rocket.getId() + 1);
        }
        return id;
    }

    //Kind of bad to do, but not really sure how else to do this
    //never will get called when there is no instance of DataModel
    public static int getNextFlightLogID() {
        int id = 0;
        for (FlightLog flightLog : DataModel.instance.flightLogs) {
            id = Math.max(id, flightLog.getId()+1);
        }
        return id;
    }

    public void saveAllData() {
        dbManager.deleteAllObjects();
        dbManager.saveRockets(rockets);
        dbManager.saveFlightLogs(flightLogs);
    }

    //FLIGHTLOGS

    /**
     *
     * @return List of all existing FlightLogs
     */
    public List<FlightLog> getFlightLogs() {
        return flightLogs;
    }

    /**
     *
     * @param rocketID ID of the Rocket to use for looking up FlightLogs
     * @return List of FlightLogs containing the RocketID
     */
    public List<FlightLog> getFlightLogs(int rocketID) {
        List<FlightLog> logs = new ArrayList<FlightLog>();
        for (int id : getRocket(rocketID).getFlightLogIDs()) {
            logs.add(getFlightLog(id));
        }

        return logs;
    }

    public List<Integer> getFlightLogIDs(int rocketID) {
        List<Integer> ids = new ArrayList<Integer>();
        for (FlightLog flightLog : flightLogs) {
            if (flightLog.getRocketID() == rocketID)
                ids.add(flightLog.getId());
        }

        return ids;
    }

    /**
     *
     * @param logID ID of the Flight Log to lookup
     * @return the FlightLog with the given ID
     */
    public FlightLog getFlightLog(int logID) {
        for (FlightLog flightLog : flightLogs) {
            if (flightLog.getId() == logID)
                return flightLog;
        }
        return null;
    }

    /**
     *
     * @param flightLog the FlightLog to add
     */
    public void addFlightLog(FlightLog flightLog) {
        flightLogs.add(flightLog);
        getRocket(flightLog.getRocketID()).addFlightLogID(flightLog.getId());
    }

    public int getFlightLogPos(int flightLogID) {
        return flightLogs.indexOf(getFlightLog(flightLogID));
    }

    /**
     *
     * @param flightLog the FlightLog to remove
     */
    public void deleteFlightLog(FlightLog flightLog) {
        flightLogs.remove(flightLog);
        Rocket r = getRocket(flightLog.getRocketID());
        r.removeFlightLog(flightLog.getId());
        update(r);
    }

    public void deleteAllFlightLogs() {
        while (flightLogs.size() >= 1) {
            deleteFlightLog(flightLogs.get(0));
        }
        dbManager.deleteAllFlightLogs();
    }

    /**
     *
     * @param flightLogID the FLightLog ID to use for looking up the Log to remove
     */
    public void deleteFlightLog(int flightLogID) {
        deleteFlightLog(getFlightLog(flightLogID));
    }

    /**
     * Update a flight log with the ID of the one passed in
     * @param flightLog the flight log to update with
     */
    public void update(FlightLog flightLog) {
        int pos = getFlightLogPos(flightLog.getId());
        if (pos == -1)
            addFlightLog(flightLog);
        else
            flightLogs.set(pos, flightLog);
    }

    //ROCKETS

    /**
     *
     * @return List of all Rockets
     */
    public List<Rocket> getRockets() {
        return rockets;
    }

    /**
     *
     * @param rocketID ID of the Rocket to look up
     * @return The Rocket with the given ID
     */
    public Rocket getRocket(int rocketID) {
        for (Rocket rocket : rockets) {
            if (rocket.getId() == rocketID)
                return rocket;
        }
        return null;
    }

    public int getRocketCount() { return rockets.size(); }

    public int getRocketPos(int rocketID) {
        return rockets.indexOf(getRocket(rocketID));
    }

    /**
     *
     * @param rocket The Rocket to add
     */
    public void addRocket(Rocket rocket) {
        rockets.add(rocket);

    }

    /**
     *  Remove a Rocket from the data list, will also remove all associated FlightLogs,
     *  since they require a Rocket.
     * @param rocket The Rocket to remove
     */
    public void deleteRocket(Rocket rocket) {
        flightLogs.removeAll(getFlightLogs(rocket.getId()));
        File image = new File(rocket.getImage());
        image.delete();
        rockets.remove(rocket);
    }

    /**
     *  Remove a Rocket from the data list, will also remove all associated FlightLogs,
     *  since they require a Rocket.
     * @param rocketID ID of the Rocket to remove
     */
    public void deleteRocket(int rocketID) {
        deleteRocket(getRocket(rocketID));
    }

    public void deleteAllRockets() {
        deleteAllFlightLogs();
        rockets.clear();
        dbManager.deleteAllRockets();
    }

    public void update(Rocket rocket) {
        int pos = getRocketPos(rocket.getId());
        if (pos == -1)
            addRocket(rocket);
        else
            rockets.set(pos, rocket);
    }

    public int getTabPosition() {
        return tabPosition;
    }

    public void setTabPosition(int tabPosition) {
        this.tabPosition = tabPosition;
    }
}
