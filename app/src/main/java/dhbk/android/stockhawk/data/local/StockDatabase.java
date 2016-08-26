package dhbk.android.stockhawk.data.local;

import com.raizlabs.android.dbflow.annotation.Database;

/**
 * fixme - declare the database used in this project
 */
//Creating the database with anme and version
@Database(name = StockDatabase.NAME, version = StockDatabase.VERSION, foreignKeysSupported = true)
public class StockDatabase {

    // database name will be Stocks.db
    public static final String NAME = "Stocks";

    //Always Increase the Version Number
    public static final int VERSION = 1;
}
