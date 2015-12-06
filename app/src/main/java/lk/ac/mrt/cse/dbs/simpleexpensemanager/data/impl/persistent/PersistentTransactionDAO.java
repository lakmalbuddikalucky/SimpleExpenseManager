package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.persistent;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.Nullable;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.persistent.contracts.TransactionContract;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

/**
 * Created by 130372T on 12/6/2015.
 */
public class PersistentTransactionDAO implements TransactionDAO {

    private Context context;

    public PersistentTransactionDAO(Context context)
    {
        this.context = context;
    }
    @Override
    public void logTransaction(Date date, String accountNo, ExpenseType expenseType, double amount) {
        SQLLiteHelper helper = SQLLiteHelper.getInstance(context);
        SQLiteDatabase db = helper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(TransactionContract.TransactionEntry.COLUMN_NAME_ACCOUNT_NO,accountNo);
        values.put(TransactionContract.TransactionEntry.COLUMN_NAME_DATE,SQLLiteHelper.dateToString(date,context));
        values.put(TransactionContract.TransactionEntry.COLUMN_NAME_AMOUNT,amount);
        values.put(TransactionContract.TransactionEntry.COLUMN_NAME_EXPENSE_TYPE,expenseType.toString());

        db.insert(TransactionContract.TransactionEntry.TABLE_NAME,null,values);
    }

    @Override
    public List<Transaction> getAllTransactionLogs() {
        return getPaginatedTransactionLogsImpl(null);
    }


    @Override
    public List<Transaction> getPaginatedTransactionLogs(int limit) {
        return getPaginatedTransactionLogsImpl(String.valueOf(limit));
    }

    private List<Transaction> getPaginatedTransactionLogsImpl(String limit)
    {
        SQLLiteHelper helper = SQLLiteHelper.getInstance(context);
        SQLiteDatabase db = helper.getReadableDatabase();

        String[] projection = {
                TransactionContract.TransactionEntry.COLUMN_NAME_ACCOUNT_NO,
                TransactionContract.TransactionEntry.COLUMN_NAME_DATE,
                TransactionContract.TransactionEntry.COLUMN_NAME_EXPENSE_TYPE,
                TransactionContract.TransactionEntry.COLUMN_NAME_AMOUNT
        };
        String sortOrder = TransactionContract.TransactionEntry.COLUMN_NAME_ENTRY_INDEX;
        Cursor c = db.query(TransactionContract.TransactionEntry.TABLE_NAME, projection,null,null,null,null,sortOrder,limit);

        ArrayList<Transaction> results = new ArrayList<>();
        while (c.moveToNext())
        {
            ExpenseType expenseType = ExpenseType.EXPENSE;
            if(ExpenseType.INCOME.toString().equals( c.getString(c.getColumnIndex(TransactionContract.TransactionEntry.COLUMN_NAME_EXPENSE_TYPE))))
            {
                expenseType =ExpenseType.INCOME;
            }
            try {
                String dateString =c.getString(c.getColumnIndex(TransactionContract.TransactionEntry.COLUMN_NAME_DATE));
                Date date = SQLLiteHelper.dateFromString(dateString);
                Transaction tr = new Transaction(date,
                        c.getString(c.getColumnIndex(TransactionContract.TransactionEntry.COLUMN_NAME_ACCOUNT_NO)),
                        expenseType,
                        c.getFloat(c.getColumnIndex(TransactionContract.TransactionEntry.COLUMN_NAME_AMOUNT)));
                results.add(tr);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return results;
    }
}
