package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.persistent;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.persistent.contracts.AccountContract;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.persistent.contracts.TransactionContract;

/**
 * Created by 130372T on 12/6/2015.
 */
public class SQLLiteHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 5;
    public static final String DATABASE_NAME = "130372T.db";

    private static SQLLiteHelper instance = null;


    private  static final String[] create_entries =
            {"CREATE TABLE IF NOT EXISTS " + AccountContract.AccountEntry.TABLE_NAME +
            " (" + AccountContract.AccountEntry.COLUMN_NAME_ACCOUNT_NO + " VARCHAR(20) PRIMARY KEY, " +
            AccountContract.AccountEntry.COLUMN_NAME_ACCOUNT_HOLDER_NAME  + " VARCHAR(50) NOT NULL, " +
            AccountContract.AccountEntry.COLUMN_NAME_BANK_NAME + " VARCHAR (50) NOT NULL, "+
            AccountContract.AccountEntry.COLUMN_NAME_BALANCE + " DECIMAL(10,2) NOT NULL )" ,
            "CREATE TABLE IF NOT EXISTS " + TransactionContract.TransactionEntry.TABLE_NAME +
            " (" + TransactionContract.TransactionEntry.COLUMN_NAME_ENTRY_INDEX + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"+
                    TransactionContract.TransactionEntry.COLUMN_NAME_ACCOUNT_NO +" VARCHAR(20) NOT NULL, " +
                    TransactionContract.TransactionEntry.COLUMN_NAME_DATE+ " DATE NOT NULL, "+
                    TransactionContract.TransactionEntry.COLUMN_NAME_AMOUNT+ " DECIMAL(10,2) NOT NULL, "+
                    TransactionContract.TransactionEntry.COLUMN_NAME_EXPENSE_TYPE + " VARCHAR(20) NOT NULL," +
                    "FOREIGN KEY ("+TransactionContract.TransactionEntry.COLUMN_NAME_ACCOUNT_NO+") REFERENCES "+AccountContract.AccountEntry.TABLE_NAME+"("+AccountContract.AccountEntry.COLUMN_NAME_ACCOUNT_NO+") ON UPDATE CASCADE ON DELETE NO ACTION)"
            };
    private static final String[] delete_entries = {"DROP TABLE IF EXISTS " + AccountContract.AccountEntry.TABLE_NAME ,
            "DROP TABLE IF EXISTS " + TransactionContract.TransactionEntry.TABLE_NAME};




    public static String dateToString(Date date, Context context){

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = dateFormat.format(date);
        return dateString;

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        for(String s : create_entries)
        {
            db.execSQL(s);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        for(String s : delete_entries)
        {
            db.execSQL(s);
        }
        onCreate(db);
    }

    public static Date dateFromString(String date) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date strDate = dateFormat.parse(date);
        return strDate;
    }

    public static SQLLiteHelper getInstance(Context context) {
        if(instance == null)
            instance = new SQLLiteHelper(context);
        return instance;
    }

    private SQLLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
}
