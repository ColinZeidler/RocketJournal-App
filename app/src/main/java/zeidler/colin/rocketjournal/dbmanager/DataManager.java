package zeidler.colin.rocketjournal.dbmanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import zeidler.colin.rocketjournal.Journal;

/**
 * Created by Colin on 2014-07-08.
 *
 * Database manager that ensures there is only one instance at any time.
 */
public class DataManager extends SQLiteOpenHelper{

    private static final int DATABASE_VERSION   = 2;
    private static final String DATABASE_NAME   = "Rocket Journal";

    private static final String TABLENAME       = "Journals";
    private static final String KEY             = "id";
    private static final String RNAME           = "rname";
    private static final String MOTOR           = "motor";
    private static final String DELAY           = "delay";
    private static final String DATE            = "date";
    private static final String NOTES           = "notes";
    private static final String RESULT          = "result";
    private static final String WEIGHT          = "weight";

    private static DataManager instance;
    private SQLiteDatabase db;

    public DataManager (Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        db = this.getWritableDatabase();
    }

    public static synchronized DataManager getInstance(Context context) {
        if (instance == null)
            instance = new DataManager(context);

        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLENAME + "("
                + KEY + "INTEGER PRIMARY KEY," + RNAME + " TEXT,"
                + MOTOR + " TEXT," + DELAY + " INTEGER,"
                + DATE + " INTEGER," + NOTES + " TEXT,"
                + RESULT + " TEXT," + WEIGHT + " REAL)";

        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLENAME);
        onCreate(db);
    }

    /**
     * Add a single journal to the database
     * @param journal the journal to add
     */
    public void addJournal(Journal journal) {
        ContentValues values = new ContentValues();
        values.put(RNAME, journal.getrName());
        values.put(MOTOR, journal.getMotor());
        values.put(DELAY, journal.getDelay());
        values.put(DATE, journal.getLaunchDate().getTime());
        values.put(NOTES, journal.getNotes());
        values.put(RESULT, journal.getResult().toString());
        values.put(WEIGHT, journal.getWeight());

        //insert row
        db.insert(TABLENAME, null, values);
    }

    /**
     * Add a list of Journals to the database
     * @param journals the list of journals to add
     */
    public void addJournals(List<Journal> journals) {
        for (Journal j: journals)
            addJournal(j);
    }

    /**
     * Return all Journals with the given rName,
     * @param rocket the name to use when selecting the journals
     * @return the list of Journals with the given rocket name
     */
    public List<Journal> getJournalsOf(String rocket) {
        return null;
    }

    /**
     * Return all Journals in the database
     * @return the list of Journals in the database
     */
    public List<Journal> getAllJournals() {
        List<Journal> jList = new ArrayList<Journal>();

        String selectQuery = "SELECT * FROM " + TABLENAME;
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                /*
                1. Name, 2. Motor, 3. delay, 4. Date,
                5. notes, 6. result, 7. weight
                 */
                jList.add(new Journal(cursor.getString(1), cursor.getString(2), cursor.getInt(3),
                        new Date(cursor.getLong(4)), cursor.getString(5),
                        Journal.LaunchRes.fromString(cursor.getString(6)), cursor.getFloat(7)));
            }while (cursor.moveToNext());
        }

        return jList;
    }

    //TODO remove Journal function
    public void deleteAll() {
        db.delete(TABLENAME, null, null);
    }
}
