package teamcortana.mycontactlist;

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabaseLockedException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


/**
 * Created by zubairrauf on 4/21/17.
 */

public class ContactDBHelper extends SQLiteOpenHelper {

    private static final String database = "mycontacts.db";
    private static final int database_version = 1;

    //create your SQL database
    private static final String CREATE_TABLE_CONTACT = "create table contact (_id integer primary key autoincrement, "
            + "contactname text not null, streetaddress text, "
            + "city text, state text, zipcode text, "
            + "phonenumber text, cellnumber text, "
            + "email text, birthday text, "
            + "memo text); ";

    public ContactDBHelper(Context context) {
        super(context, database, null, database_version);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(CREATE_TABLE_CONTACT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(ContactDBHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS contact");
        onCreate(db);
    }

}

