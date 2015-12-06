package lk.ac.mrt.cse.dbs.simpleexpensemanager.control;

import android.content.Context;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.InMemoryTransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.persistent.PersistentAccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.persistent.PersistentTransactionDAO;


/**
 * Created by 130372T on 12/6/2015.
 */
public class PersistentExpenseManager extends ExpenseManager {

    private Context context = null;
    public PersistentExpenseManager( Context context) {
            this.context = context;
            setup();

    }

    @Override
    public void setup()  {
        //Setup DAO objects
        TransactionDAO persistentTransactionDAO = new PersistentTransactionDAO(context);
        setTransactionsDAO(persistentTransactionDAO);


        AccountDAO persistentAccountDAO = new PersistentAccountDAO(context);
        setAccountsDAO(persistentAccountDAO);


    }
}
