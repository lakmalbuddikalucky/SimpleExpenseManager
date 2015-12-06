package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.persistent.contracts;

import android.provider.BaseColumns;

/**
 * Created by 130372T on 12/6/2015.
 */
public final class AccountContract {
    public AccountContract() {}

    public static abstract class AccountEntry implements BaseColumns {
        public static final String TABLE_NAME = "account";
        public static final String COLUMN_NAME_ACCOUNT_NO = "account_number";
        public static final String COLUMN_NAME_BANK_NAME = "bank_name";
        public static final String COLUMN_NAME_ACCOUNT_HOLDER_NAME = "account_holder_name";
        public static final String COLUMN_NAME_BALANCE = "balance";
    }
}