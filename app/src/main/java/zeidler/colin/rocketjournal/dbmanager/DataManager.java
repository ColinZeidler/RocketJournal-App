package zeidler.colin.rocketjournal.dbmanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import zeidler.colin.rocketjournal.data.FlightLog;
import zeidler.colin.rocketjournal.data.Rocket;

/**
 * Created by Colin on 2014-07-08.
 *
 * Database manager that ensures there is only one instance at any time.
 */
public class DataManager extends SQLiteOpenHelper{

    private static final int DATABASE_VERSION   = 6;
    private static final String DATABASE_NAME   = "Rocket Journal";

    private static final String J_TABLENAME = "Journals";
    private static final String J_KEY = "_id";
    private static final String J_MOTOR = "motor";
    private static final String J_DELAY = "delay";
    private static final String J_DATE = "date";
    private static final String J_NOTES = "notes";
    private static final String J_RESULT = "result";
    private static final String J_ROCKET = "rocket";

    private static final String R_TABLENAME = "Rockets";
    private static final String R_KEY = "_id";
    private static final String R_NAME = "name";
    private static final String R_WEIGHT = "weight";
    private static final String R_FLIGHTS = "flights";
    private static final String R_IMAGE = "image";

    private static DataManager instance;
    private SQLiteDatabase db;

    public DataManager (Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        Log.i(getClass().toString(), "Creating new DataManger");
        db = this.getWritableDatabase();
    }

    public static synchronized DataManager getInstance(Context context) {
        if (instance == null)
            instance = new DataManager(context);

        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //create Rockets table
        String createTable = "CREATE TABLE " + R_TABLENAME + "("
                + R_KEY + " INTEGER PRIMARY KEY," + R_NAME + " TEXT,"
                + R_WEIGHT + " REAL," + R_FLIGHTS + " INTEGER,"
                + R_IMAGE + " TEXT);";

        db.execSQL(createTable);

        //create Journals table
        createTable = "CREATE TABLE " + J_TABLENAME + "("
                + J_KEY + " INTEGER PRIMARY KEY,"
                + J_MOTOR + " TEXT," + J_DELAY + " INTEGER,"
                + J_DATE + " INTEGER," + J_NOTES + " TEXT,"
                + J_RESULT + " TEXT," + J_ROCKET + " INTEGER,"
                + " FOREIGN KEY (" + J_ROCKET + ") REFERENCES " + R_TABLENAME + " (" + R_KEY + "));";

        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + J_TABLENAME);
        db.execSQL("DROP TABLE IF EXISTS " + R_TABLENAME);
        onCreate(db);
    }

    /**
     * Delete everything in the database
     */
    public void deleteAllObjects() {
        deleteAllFlightLogs();
        deleteAllRockets();
    }

    // FLIGHTLOGS

    /**
     * Return all Journals in the database
     * @return the list of Journals in the database
     */
    public List<FlightLog> getAllFlightLogs() {
        List<FlightLog> jList = new ArrayList<FlightLog>();

        String selectQuery = "SELECT * FROM " + J_TABLENAME;
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                jList.add(buildFlightLogFromCursor(cursor));
            }while (cursor.moveToNext());
        }

        return jList;
    }

    private FlightLog buildFlightLogFromCursor(Cursor cursor) {
                /* 0. ID,
                1. Motor, 2. delay, 3. Date,
                4. notes, 5. result, 6. Rocket.ID
                 */
        return new FlightLog(cursor.getInt(0),  //ID
                cursor.getInt(6),               //Rocket ID
                cursor.getString(1),            //Motor
                cursor.getInt(2),               //Motor Delay
                new Date(cursor.getLong(3)),    //Launch Date
                cursor.getString(4),            //Notes
                FlightLog.LaunchRes.fromString(cursor.getString(5)));   //Launch Result
    }

    /**
     * Add a single flightLog to the database
     * @param flightLog the flightLog to add
     */
    public void addFlightLog(FlightLog flightLog) {
        ContentValues values = new ContentValues();
        values.put(J_MOTOR, flightLog.getMotor());
        values.put(J_DELAY, flightLog.getDelay());
        values.put(J_DATE, flightLog.getDate().getTime());
        values.put(J_NOTES, flightLog.getNotes());
        values.put(J_RESULT, flightLog.getResult().toString());
        values.put(J_ROCKET, flightLog.getRocketID());

        //insert row
        db.insert(J_TABLENAME, null, values);
    }

    public void deleteAllFlightLogs() {
        db.delete(J_TABLENAME, null, null);
    }

    public void saveFlightLogs(List<FlightLog> flightLogs) {
        for (FlightLog flightLog : flightLogs)
            addFlightLog(flightLog);
    }

    // ROCKETS

    /**
     * Get all rockets from the database
     * @return List of all rockets
     */
    public List<Rocket> getAllRockets() {
        String selectQuery = "SELECT * FROM " + R_TABLENAME;
        Cursor cursor = db.rawQuery(selectQuery, null);
        List<Rocket> rockets = new ArrayList<Rocket>();

        if (cursor.moveToFirst()) {
            do {
                rockets.add(buildRocketFromCursor(cursor));
            }while (cursor.moveToNext());
        }

        return rockets;
    }

    /**
     * Create a rocket from the data contained in the db cursor
     * @param cursor the cursor to use
     * @return the rocket from data in the provided cursor
     */
    private Rocket buildRocketFromCursor(Cursor cursor) {
        return new Rocket(cursor.getInt(0),
                cursor.getString(1),    //Name
                cursor.getFloat(2),     //weight
                cursor.getInt(3),       //flight count
                cursor.getString(4));   //image
    }

    /**
     * Add the given Rocket to the database
     * @param rocket the Rocket to add to the database
     */
    public void addRocket(Rocket rocket) {
        ContentValues values = new ContentValues();
        values.put(R_KEY, rocket.getId());
        values.put(R_NAME, rocket.getName());
        values.put(R_WEIGHT, rocket.getWeight());
        values.put(R_FLIGHTS, rocket.getFlightCount());
        values.put(R_IMAGE, rocket.getImage());

        db.insert(R_TABLENAME, null, values);
    }

    /**
     * Delete all Rockets in the Database
     */
    public void deleteAllRockets() {
        db.delete(R_TABLENAME, null, null);
    }

    public void saveRockets(List<Rocket> rockets) {
        for (Rocket rocket : rockets)
            addRocket(rocket);
    }
}
