package com.app.demos.sqlite;

import com.app.demos.base.C;
import com.app.demos.model.Zhangben;

/**
 * Created by tom on 15-12-16.
 */
public class CreateSql {
    public static String createZhanben() {
        return "CREATE TABLE IF NOT EXISTS " + C.string.zhanBen + " (" +
                Zhangben.COL_ID + " INTEGER PRIMARY KEY, " +
                Zhangben.COL_TIME + " TEXT, " +
                Zhangben.COL_ONE + " TEXT, " +
                Zhangben.COL_TWO + " TEXT, " +
                Zhangben.COL_THREE + " TEXT, " +
                Zhangben.COL_LOCATION + " TEXT, " +
                Zhangben.COL_MONEY + " TEXT " +
                ");";
    }
}
