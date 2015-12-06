package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.persistent;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.persistent.contracts.AccountContract;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;

/**
 * Created by 130372T on 12/6/2015.
 */
public class PersistentAccountDAO implements AccountDAO {

    private  Context context;
    public PersistentAccountDAO(Context context)
    {
        this.context = context;
    }
    @Override
    public List<String> getAccountNumbersList() {
        SQLLiteHelper helper = SQLLiteHelper.getInstance(context);
        SQLiteDatabase db = helper.getReadableDatabase();

        String[] projection = {
                AccountContract.AccountEntry.COLUMN_NAME_ACCOUNT_NO
        };
        String sortOrder = AccountContract.AccountEntry.COLUMN_NAME_ACCOUNT_NO;
        Cursor c = db.query(AccountContract.AccountEntry.TABLE_NAME, projection,null,null,null,null,sortOrder,null);

        ArrayList<String> results = new ArrayList<>();
        while (c.moveToNext())
        {
            results.add(c.getString(c.getColumnIndex(AccountContract.AccountEntry.COLUMN_NAME_ACCOUNT_NO)));
        }
        return results;
    }

    @Override
    public List<Account> getAccountsList() {
        SQLLiteHelper helper = SQLLiteHelper.getInstance(context);
        SQLiteDatabase db = helper.getReadableDatabase();

        String[] projection = {
                AccountContract.AccountEntry.COLUMN_NAME_ACCOUNT_NO,
                AccountContract.AccountEntry.COLUMN_NAME_ACCOUNT_HOLDER_NAME,
                AccountContract.AccountEntry.COLUMN_NAME_BANK_NAME,
                AccountContract.AccountEntry.COLUMN_NAME_BALANCE
        };
        String sortOrder = AccountContract.AccountEntry.COLUMN_NAME_ACCOUNT_NO;
        Cursor c = db.query(AccountContract.AccountEntry.TABLE_NAME, projection,null,null,null,null,sortOrder,null);

        ArrayList<Account> results = new ArrayList<>();
        while (c.moveToNext())
        {
            Account ac = new Account(c.getString(c.getColumnIndex(AccountContract.AccountEntry.COLUMN_NAME_ACCOUNT_NO)),
                    c.getString(c.getColumnIndex(AccountContract.AccountEntry.COLUMN_NAME_BANK_NAME)),
                    c.getString(c.getColumnIndex(AccountContract.AccountEntry.COLUMN_NAME_ACCOUNT_HOLDER_NAME)),
                    c.getFloat(c.getColumnIndex(AccountContract.AccountEntry.COLUMN_NAME_BALANCE)));

            results.add(ac);
        }
        return results;
    }

    @Override
    public Account getAccount(String accountNo) throws InvalidAccountException {
        SQLLiteHelper helper = SQLLiteHelper.getInstance(context);
        SQLiteDatabase db = helper.getReadableDatabase();

        String[] projection = {
                AccountContract.AccountEntry.COLUMN_NAME_ACCOUNT_NO,
                AccountContract.AccountEntry.COLUMN_NAME_ACCOUNT_HOLDER_NAME,
                AccountContract.AccountEntry.COLUMN_NAME_BANK_NAME,
                AccountContract.AccountEntry.COLUMN_NAME_BALANCE
        };
        String sortOrder = AccountContract.AccountEntry.COLUMN_NAME_ACCOUNT_NO + " DESC";
        Cursor c = db.query(AccountContract.AccountEntry.TABLE_NAME, projection,AccountContract.AccountEntry.COLUMN_NAME_ACCOUNT_NO + "='" + accountNo +"'",null,null,null,sortOrder,null);

        if(c.moveToNext()) {
            Account ac = new Account(c.getString(c.getColumnIndex(AccountContract.AccountEntry.COLUMN_NAME_ACCOUNT_NO)),
                    c.getString(c.getColumnIndex(AccountContract.AccountEntry.COLUMN_NAME_BANK_NAME)),
                    c.getString(c.getColumnIndex(AccountContract.AccountEntry.COLUMN_NAME_ACCOUNT_HOLDER_NAME)),
                    c.getFloat(c.getColumnIndex(AccountContract.AccountEntry.COLUMN_NAME_BALANCE)));

            return  ac;
        }
        else {
            throw new InvalidAccountException("Account number invalid");
        }


    }

    @Override
    public void addAccount(Account account) {
        SQLLiteHelper helper = SQLLiteHelper.getInstance(context);
        SQLiteDatabase db = helper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(AccountContract.AccountEntry.COLUMN_NAME_ACCOUNT_NO,account.getAccountNo());
        values.put(AccountContract.AccountEntry.COLUMN_NAME_ACCOUNT_HOLDER_NAME,account.getAccountHolderName());
        values.put(AccountContract.AccountEntry.COLUMN_NAME_BANK_NAME,account.getBankName());
        values.put(AccountContract.AccountEntry.COLUMN_NAME_BALANCE,account.getBalance());

        db.insert(AccountContract.AccountEntry.TABLE_NAME,null,values);

    }

    @Override
    public void removeAccount(String accountNo) throws InvalidAccountException {
        SQLLiteHelper helper = SQLLiteHelper.getInstance(context);
        SQLiteDatabase db = helper.getWritableDatabase();

        String selection = AccountContract.AccountEntry.COLUMN_NAME_ACCOUNT_NO + "= ?'";
        String[] selectionArgs = { accountNo };
        db.delete(AccountContract.AccountEntry.TABLE_NAME,selection,selectionArgs);
    }

    @Override
    public void updateBalance(String accountNo, ExpenseType expenseType, double amount) throws InvalidAccountException {

        Account ac = getAccount(accountNo);

        SQLLiteHelper helper = SQLLiteHelper.getInstance(context);
        SQLiteDatabase db = helper.getWritableDatabase();

        ContentValues values = new ContentValues();

        switch (expenseType)
        {
            case EXPENSE:
                values.put(AccountContract.AccountEntry.COLUMN_NAME_BALANCE,ac.getBalance() - amount);
                break;
            case INCOME:
                values.put(AccountContract.AccountEntry.COLUMN_NAME_BALANCE,ac.getBalance() + amount);
                break;
        }

        String selection = AccountContract.AccountEntry.COLUMN_NAME_ACCOUNT_NO + " = ?";
        String[] selectionArgs = { accountNo };

        db.update(AccountContract.AccountEntry.TABLE_NAME,values, selection,selectionArgs);

    }
}
