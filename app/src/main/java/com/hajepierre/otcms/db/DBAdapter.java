package com.hajepierre.otcms.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by jeanpierre on 4/30/18.
 */

public class DBAdapter
{
    // 1. profine
    private static final String ID = "id";

    private static final String FIRSTNAME = "firstName";

    private static final String LASTNAME = "lastName";

    private static final String PHONENUMBER = "phoneNumber";

    private static final String EMAIL = "email";

    private static final String NATIONALITY = "nationality";

    private static final String GENDER = "gender";

    private static final String YEAROFBIRTH = "yearOfBirth";

    private static final String NATIONALID = "nationalId";

    private static final String STATUS = "status";

    // 2. complaint
    private static final String ISSUECATEGORY = "issueCategory";

    private static final String ISSUE = "issue";

    private static final String ISSUENAME = "issueName";

    private static final String BORDERID = "borderId";

    private static final String BORDER = "border";

    private static final String COMMENTS = "comments";

    //3. other tables
    private static final String NAME = "name";

    private static final String CATEGORYID = "categoryId";

    // TABLES
    private static final String PROFILE_TABLE = "prolile";

    private static final String COMPLAINTS_TABLE = "complaints";

    private static final String ISSUE_CATEGORY_TABLE = "issueCategory";

    private static final String ISSUE_TABLE = "issue";

    private static final String NATIONALITY_TABLE = "nationality";

    private static final String BORDER_TABLE = "border";

    private static final String LANGUAGE_TABLE = "language";


    // OTHER SETTINGS
    private int currentUserGCMID;

    // URL prefix -- THIS IS JUST AN EXAMPLE, IT WILL BE CHANGED LATER
    // private final String URL_PREXIF =
    // "http://52.10.125.144:8080/webservices";

    // Table creation

    private static final String CREATE_PROFILE_TABLE = "CREATE TABLE " + PROFILE_TABLE + " (" + ID + " integer primary key AUTOINCREMENT, " + "" + FIRSTNAME + " text," + "" + LASTNAME + " text," + "" + PHONENUMBER + " text," + "" + EMAIL + " text," + "" + NATIONALITY + " text," + "" + GENDER + " text," + "" + YEAROFBIRTH + " text," + "" + NATIONALID + " text, " + STATUS + " integer)";

    private static final String CREATE_COMPLAINT_TABLE = "CREATE TABLE " + COMPLAINTS_TABLE + " (" + ID + " integer primary key AUTOINCREMENT, " + "" + ISSUECATEGORY + " text," + "" + ISSUE + " text," + "" + BORDER + " text," + "" + COMMENTS + " text," + "" + NATIONALID + " text, "+ISSUENAME+" text, "+BORDERID+" integer)";

    private static final String CREATE_ISSUE_CATEGORY_TABLE = "CREATE TABLE " + ISSUE_CATEGORY_TABLE + " (" + ID + " integer primary key AUTOINCREMENT, " + "" + NAME + " text)";

    private static final String CREATE_ISSUE_TABLE = "CREATE TABLE " + ISSUE_TABLE + " (" + ID + " integer primary key AUTOINCREMENT," + " " + NAME + " text," + " " + CATEGORYID + " integer )";

    private static final String CREATE_NATIONALITY_TABLE = "CREATE TABLE " + NATIONALITY_TABLE + " (" + ID + " integer primary key AUTOINCREMENT, " + "" + NAME + " text)";

    private static final String CREATE_BORDER_TABLE = "CREATE TABLE " + BORDER_TABLE + " (" + ID + " integer primary key AUTOINCREMENT, " + "" + NAME + " text)";

    private static final String CREATE_LANGUAGE_TABLE = "CREATE TABLE " + LANGUAGE_TABLE + " (" + ID + " integer primary key AUTOINCREMENT, " + "" + NAME + " text)";


    // DB parameters
    private static final String DB_NAME = "complaints.db";

    private static final int DB_VERSION = 2;

    private static final String TAG = "DBAdapter";

    // Adapter parameters
    @SuppressWarnings("unused")
    private Context context; // the actual activity

    private DatabaseHelper dbHelper;

    private SQLiteDatabase db;

    // Database retrieving query formatting
    private final String[] PROFILE_COLUMNS = new String[]{ID, FIRSTNAME, LASTNAME, PHONENUMBER, EMAIL, NATIONALITY, GENDER, YEAROFBIRTH, NATIONALID, STATUS};

    private final String[] COMPLAINTS_COLUMNS = new String[]{ID, ISSUECATEGORY, ISSUE, BORDER, COMMENTS, NATIONALID, ISSUENAME, BORDERID};

    private final String[] OTHER_TABLES_COLUMNS = new String[]{ID, NAME};

    private final String[] ISSUE_TABLES_COLUMNS = new String[]{ID, NAME, CATEGORYID};


    public DBAdapter(Context context)
    {

        this.context = context;
        dbHelper = new DatabaseHelper(context);
    }

    private static final class DatabaseHelper extends SQLiteOpenHelper
    {
        DatabaseHelper(Context context)
        {

            super(context, DB_NAME, null, DB_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db)
        {

            try
            {
                db.execSQL(CREATE_PROFILE_TABLE);
                db.execSQL(CREATE_COMPLAINT_TABLE);
                db.execSQL(CREATE_ISSUE_CATEGORY_TABLE);
                db.execSQL(CREATE_ISSUE_TABLE);
                db.execSQL(CREATE_NATIONALITY_TABLE);
                db.execSQL(CREATE_BORDER_TABLE);
                db.execSQL(CREATE_LANGUAGE_TABLE);
                Log.i(TAG, "TABLES ARE CREATED");
            }
            catch (Exception ex)
            {
                Log.e(TAG, "failure to create tables");
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
        {

            Log.w(TAG,
                    "Upgrading database from version " + oldVersion + " to version " + newVersion + " and all old data will be destroyed");

            db.execSQL("drop table if exists " + PROFILE_TABLE);
            db.execSQL("drop table if exists " + COMPLAINTS_TABLE);
            db.execSQL("drop table if exists " + ISSUE_CATEGORY_TABLE);
            db.execSQL("drop table if exists " + ISSUE_TABLE);
            db.execSQL("drop table if exists " + NATIONALITY_TABLE);
            db.execSQL("drop table if exists " + BORDER_TABLE);
            db.execSQL("drop table if exists " + LANGUAGE_TABLE);
            onCreate(db);
        }
    }

    public DBAdapter open()
    {

        try
        {
            db = dbHelper.getWritableDatabase();
            return this;
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
            return null;
        }
    }

    @SuppressWarnings("unused")
    private SQLiteDatabase openToRead()
    {

        try
        {
            return dbHelper.getReadableDatabase();
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
            return null;
        }
    }

    public void close()
    {

        dbHelper.close();
    }

    // Insert Methods

    public void insertProfile(Profile profile)
    {

        try
        {
            db.beginTransaction();
            ContentValues values = new ContentValues();
            values.put(FIRSTNAME, profile.getFirstName());
            values.put(LASTNAME, profile.getLastName());
            values.put(PHONENUMBER, profile.getPhoneNumber());
            values.put(EMAIL, profile.getEmail());
            values.put(NATIONALITY, profile.getNationality());
            values.put(GENDER, profile.getGender());
            values.put(YEAROFBIRTH, profile.getYearOfBirth());
            values.put(NATIONALID, profile.getNationalId());
            values.put(STATUS, profile.getStatus());

            db.insertOrThrow(PROFILE_TABLE, null, values);
            db.setTransactionSuccessful();
            db.endTransaction();
        }
        catch (Exception ex)
        {
            Log.e("Error: ", ex.getMessage());
        }
    }

    public void insertComplaint(Complaint complaint)
    {

        try
        {
            db.beginTransaction();
            ContentValues values = new ContentValues();
            values.put(ISSUECATEGORY, complaint.getIssueCategory());
            values.put(ISSUE, complaint.getIssue());
            values.put(BORDER, complaint.getBorder());
            values.put(COMMENTS, complaint.getComments());
            values.put(NATIONALID, complaint.getNationalId());

            db.insertOrThrow(COMPLAINTS_TABLE, null, values);
            db.setTransactionSuccessful();
            db.endTransaction();
        }
        catch (Exception ex)
        {
            Log.e("ERROR: ", ex.getMessage());
        }
    }

    public void insertBorder(Border border)
    {

        try
        {
            boolean canSave = true;
            if (border.getId() != null)
            {
                canSave = !exists(BORDER_TABLE, border.getId());
            }
            if (canSave)
            {
                db.beginTransaction();
                ContentValues values = new ContentValues();
                if (border.getId() != null)
                {
                    values.put(ID, border.getId());
                }
                values.put(NAME, border.getName());

                db.insertOrThrow(BORDER_TABLE, null, values);
                db.setTransactionSuccessful();
                db.endTransaction();
            }
        }
        catch (Exception ex)
        {
            Log.e("ERROR: ", ex.getMessage());
        }
    }

    public void insertLanguage(String name)
    {

        try
        {
            if (!exists(LANGUAGE_TABLE, 1))
            {
                db.beginTransaction();
                ContentValues values = new ContentValues();

                values.put(ID, 1);
                values.put(NAME, name);

                db.insertOrThrow(LANGUAGE_TABLE, null, values);
                db.setTransactionSuccessful();
                db.endTransaction();
            }
            else
            {
                editLanguage(name);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void editLanguage(String name)
    {

        try
        {
            ContentValues values = new ContentValues();
            values.put(NAME, name);
            db.update(LANGUAGE_TABLE, values, ID + "=1", null);
        }
        catch (SQLException e)
        {
            Log.e("ERROR: ", e.getMessage());
        }
    }

    public void editProfile(int id, int status)
    {

        try
        {
            ContentValues values = new ContentValues();
            values.put(STATUS, status);
            db.update(PROFILE_TABLE, values, ID + "=" + id, null);
        }
        catch (SQLException e)
        {
            Log.e("ERROR: ", e.getMessage());
        }
    }

    public void insertIssue(Issue issue)
    {

        try
        {
            boolean canSave = true;
            if (issue.getId() != null)
            {
                canSave = !exists(ISSUE_TABLE, issue.getId());
            }
            if (canSave)
            {
                db.beginTransaction();
                ContentValues values = new ContentValues();
                if (issue.getId() != null)
                {
                    values.put(ID, issue.getId());
                }
                values.put(NAME, issue.getName());
                values.put(CATEGORYID, issue.getCategoryId());

                db.insertOrThrow(ISSUE_TABLE, null, values);
                db.setTransactionSuccessful();
                db.endTransaction();
            }
        }
        catch (Exception ex)
        {
        }
    }

    public void insertIssueCategory(IssueCategory issueCategory)
    {

        try
        {
            boolean canSave = true;
            if (issueCategory.getId() != null)
            {
                canSave = !exists(ISSUE_CATEGORY_TABLE, issueCategory.getId());
            }
            if (canSave)
            {
                db.beginTransaction();
                ContentValues values = new ContentValues();
                if (issueCategory.getId() != null)
                {
                    values.put(ID, issueCategory.getId());
                }
                values.put(NAME, issueCategory.getName());

                db.insertOrThrow(ISSUE_CATEGORY_TABLE, null, values);
                db.setTransactionSuccessful();
                db.endTransaction();
            }
        }
        catch (Exception ex)
        {
            Log.e("ERROR: ", ex.getMessage());
        }
    }

    public void insertNationality(Nationality nationality)
    {

        try
        {
            db.beginTransaction();
            ContentValues values = new ContentValues();
            if (nationality.getId() != null)
            {
                values.put(ID, nationality.getId());
            }
            values.put(NAME, nationality.getName());

            db.insertOrThrow(NATIONALITY_TABLE, null, values);
            db.setTransactionSuccessful();
            db.endTransaction();
        }
        catch (Exception ex)
        {
            Log.e("ERROR: ", ex.getMessage());
        }
    }


    // Delete Methods

    public boolean deleteComplaints()
    {

        try
        {
            return db.delete(COMPLAINTS_TABLE, null, null) > 0;
        }
        catch (Exception ex)
        {
            Log.e("ERROR: ", ex.getMessage());
            return false;
        }
    }

    public boolean deleteComplaintById(int id)
    {

        try
        {
            return db.delete(COMPLAINTS_TABLE, ID + "=" + id, null) > 0;
        }
        catch (Exception ex)
        {
            Log.e("ERROR: ", ex.getMessage());
            return false;
        }
    }


    public boolean delateProfile()
    {

        try
        {
            return db.delete(COMPLAINTS_TABLE, null, null) > 0;
        }
        catch (Exception ex)
        {
            Log.e("ERROR: ", ex.getMessage());
            return false;
        }
    }

    public boolean delateProfileById(int id)
    {

        try
        {
            return db.delete(COMPLAINTS_TABLE, ID + "=" + id, null) > 0;
        }
        catch (Exception ex)
        {
            Log.e("ERROR: ", ex.getMessage());
            return false;
        }
    }

    // Cursor method
    private Cursor getCursor(String tableName, String[] params, String filter, String value)
    {

        try
        {
            db = openToRead();
            return db.query(true, tableName, params, filter + "='" + value + "'", null, null, null,
                    null, null);
        }
        catch (SQLException e)
        {
            Log.e("ERROR: ", e.getMessage());
            return null;
        }
    }

    private Cursor getCursor(String tableName, String[] params)
    {

        try
        {
            db = openToRead();
            return db.query(tableName, params, null, null, null, null, null);
        }
        catch (SQLException ex)
        {
            Log.e("ERROR: ", ex.getMessage());
            return null;
        }
    }

    private Cursor getCursor(String query)
    {

        db = openToRead();
        return db.rawQuery(query, null);
    }

    private boolean complaintsExist()
    {

        Cursor cursor = getCursor(COMPLAINTS_TABLE, COMPLAINTS_COLUMNS);
        if (cursor != null)
        {
            boolean exists = cursor.moveToFirst();
            cursor.close();
            return exists;
        }
        return false;
    }

    public Cursor get(String tableName, int id)
    {

        System.out.println(tableName);

        try
        {
            return db.query(tableName, OTHER_TABLES_COLUMNS, ID + "=" + id, null, null, null, null,
                    null);

        }
        catch (SQLException e)
        {

            return null;
        }
    }

    public Cursor getIssueByCategoryId(int id)
    {

        try
        {
            return db.query(ISSUE_TABLE, ISSUE_TABLES_COLUMNS, CATEGORYID + "=" + id, null, null,
                    null, null, null);

        }
        catch (SQLException e)
        {
            return null;
        }
    }

    public Cursor getIssueById(int id)
    {

        try
        {
            return db.query(ISSUE_TABLE, ISSUE_TABLES_COLUMNS, ID + "=" + id, null, null, null,
                    null, null);

        }
        catch (SQLException e)
        {
            return null;
        }
    }

    public Cursor getBorderByName(String name)
    {

        try
        {
            return db.query(BORDER_TABLE, OTHER_TABLES_COLUMNS, NAME + "='" + name + "'", null,
                    null, null, null, null);

        }
        catch (SQLException e)
        {
            return null;
        }
    }

    public Cursor getCategoryName(String name)
    {

        try
        {
            return db.query(ISSUE_CATEGORY_TABLE, OTHER_TABLES_COLUMNS, NAME + "='" + name + "'",
                    null, null, null, null, null);

        }
        catch (SQLException e)
        {
            return null;
        }
    }


    public Cursor getProfileByNationId(String nationalId)
    {

        try
        {
            return db.query(PROFILE_TABLE, PROFILE_COLUMNS, NATIONALID + "='" + nationalId + "'",
                    null, null, null, null, null);

        }
        catch (SQLException e)
        {
            return null;
        }
    }

    public Cursor getIssueCategoryByName(String name)
    {

        try
        {
            return db.query(ISSUE_CATEGORY_TABLE, OTHER_TABLES_COLUMNS, NAME + "='" + name + "'",
                    null, null, null, null, null);

        }
        catch (SQLException e)
        {
            e.printStackTrace();
            return null;
        }
    }

    private boolean exists(String tableName, int id)
    {

        return get(tableName, id).moveToFirst();
    }


    public Profile findProfile()
    {

        List<Profile> data = formatProfile(getCursor(PROFILE_TABLE, PROFILE_COLUMNS));

        if (data != null)
        {
            if (!data.isEmpty())
            {
                return data.get(0);
            }

        }
        return null;
    }

    public Profile findProfileByNationalId(String nationalId)
    {

        List<Profile> data = formatProfile(getProfileByNationId(nationalId));

        if (data != null)
        {
            if (!data.isEmpty())
            {
                data.get(0);
            }
        }
        return null;
    }


    public List<Complaint> findComplains()
    {

        return formatComplaints(getCursor(PROFILE_TABLE, PROFILE_COLUMNS));
    }

    public List<RemoteData> findBorder()
    {

        return formatRemoteData(getCursor(BORDER_TABLE, OTHER_TABLES_COLUMNS));
    }

    public List<Issue> findIssues()
    {

        return formatIssue(getCursor(ISSUE_TABLE, ISSUE_TABLES_COLUMNS));
    }

    public List<Issue> findIssuesByCategoryId(int categoryId)
    {

        return formatIssue(getIssueByCategoryId(categoryId));
    }

    public Issue findIssuesById(int id)
    {

        List<Issue> issues = formatIssue(getIssueById(id));
        return issues != null ? (!issues.isEmpty() ? issues.get(0) : null) : null;
    }

    public RemoteData findBorderByName(String name)
    {

        List<RemoteData> borders = formatRemoteData(getBorderByName(name));
        return borders != null ? (!borders.isEmpty() ? borders.get(0) : null) : null;
    }

    public RemoteData findCategoryByName(String name)
    {

        List<RemoteData> categories = formatRemoteData(getIssueCategoryByName(name));
        return categories != null ? (!categories.isEmpty() ? categories.get(0) : null) : null;
    }

    public List<RemoteData> findIssueCategories()
    {

        return formatRemoteData(getCursor(ISSUE_CATEGORY_TABLE, OTHER_TABLES_COLUMNS));
    }

    public List<RemoteData> findNationality()
    {

        return formatRemoteData(getCursor(NATIONALITY_TABLE, OTHER_TABLES_COLUMNS));
    }

    public RemoteData findLanguage()
    {

        try
        {
            return formatRemoteData(getCursor(LANGUAGE_TABLE, OTHER_TABLES_COLUMNS)).get(0);
        }
        catch (Exception ex)
        {
            return null;
        }
    }


    // Formatting method
    private List<Profile> formatProfile(Cursor cursor)
    {

        if (cursor != null)
        {
            List<Profile> result = new ArrayList<Profile>();
            if (cursor.moveToFirst())
            {
                do
                {
                    Profile profile = new Profile();
                    profile.setId(cursor.getInt(0));
                    profile.setFirstName(cursor.getString(1));
                    profile.setLastName(cursor.getString(2));
                    profile.setPhoneNumber(cursor.getString(3));
                    profile.setEmail(cursor.getString(4));
                    profile.setNationality(cursor.getString(5));
                    profile.setGender(cursor.getString(6));
                    profile.setYearOfBirth(cursor.getString(7));
                    profile.setNationalId(cursor.getString(8));

                    result.add(profile);
                }
                while (cursor.moveToNext());
            }
            cursor.close();
            return result;
        }
        return null;
    }

    private List<Complaint> formatComplaints(Cursor cursor)
    {

        if (cursor != null)
        {
            List<Complaint> result = new ArrayList<Complaint>();
            if (cursor.moveToFirst())
            {
                do
                {
                    Complaint complaint = new Complaint();
                    complaint.setId(cursor.getInt(0));
                    complaint.setIssueCategory(cursor.getString(1));
                    complaint.setIssue(cursor.getInt(2));
                    complaint.setBorder(cursor.getString(3));
                    complaint.setComments(cursor.getString(4));
                    complaint.setNationalId(cursor.getString(5));
                    complaint.setIssueName(cursor.getString(6));
                    complaint.setBorderId(cursor.getInt(7));

                    result.add(complaint);
                }
                while (cursor.moveToNext());
            }
            cursor.close();
            return result;
        }
        return null;
    }

    private List<RemoteData> formatRemoteData(Cursor cursor)
    {

        if (cursor != null)
        {
            List<RemoteData> result = new ArrayList<RemoteData>();
            if (cursor.moveToFirst())
            {
                do
                {
                    RemoteData remoteData = new RemoteData();
                    remoteData.setId(cursor.getInt(0));
                    remoteData.setName(cursor.getString(1));

                    result.add(remoteData);
                }
                while (cursor.moveToNext());
            }
            cursor.close();
            return result;
        }
        return null;
    }

    private List<Issue> formatIssue(Cursor cursor)
    {

        if (cursor != null)
        {
            List<Issue> result = new ArrayList<Issue>();
            if (cursor.moveToFirst())
            {
                do
                {
                    Issue data = new Issue();
                    data.setId(cursor.getInt(0));
                    data.setName(cursor.getString(1));
                    data.setCategoryId(cursor.getInt(2));

                    result.add(data);
                }
                while (cursor.moveToNext());
            }
            cursor.close();
            return result;
        }
        return null;
    }

}