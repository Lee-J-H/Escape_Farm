package gang.il;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import static gang.il.Valiable.gameMode;
import static gang.il.Valiable.stageCount;

public class StageDBHelper extends SQLiteOpenHelper {
    private SQLiteDatabase db;
    private static final int DATABASE_VERSION = 1;
    private static final String Database_Name = "StageCountDB";
    private String Table_Name = gameMode;
    public int clearedStage;
    public int minimum_count[];

    public StageDBHelper(Context context) {
        super(context, Database_Name, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String classic_table =
                "CREATE TABLE " + "Classic" + "(" +
                        "Stage INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        //"Rating INTEGER NOT NULL," +
                        "Min_Count INTEGER NOT NULL" +
                        ");";
        String blind_table =
                "CREATE TABLE " + "Blind" + "(" +
                        "Stage INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        //"Rating INTEGER NOT NULL," +
                        "Min_Count INTEGER NOT NULL" +
                        ");";
        db.execSQL(classic_table);
        db.execSQL(blind_table);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String DROP_TABLE =
                "DROP TABLE IF EXISTS " + Table_Name;
        db.execSQL(DROP_TABLE);
        onCreate(db);
    }

    public void selectDB() {
        db = getReadableDatabase();
        if (gameMode.equals("classic"))
            Table_Name = "Classic";
        else
            Table_Name = "Blind";
        Table_Name="Classic";

        int countNum = 0;
        Cursor c = db.rawQuery("select Min_Count from " + Table_Name, null);
        if (c.getCount() == 0) {
            clearedStage=0;
            return;
        }
        clearedStage = c.getCount();
        minimum_count = new int[clearedStage];
        while (c.moveToNext()) {
            minimum_count[countNum++] = c.getInt(0);
            //Log.d(tag,"id:"+id+",name:"+name);
        }
        countNum = 0;
        db.close();
    }

    public int getClearedStage() {
        return clearedStage;
    }

    public int getMinimumCount(int index) {
        return minimum_count[index];
    }

    public void recordMinCount(int min) {
        db = getWritableDatabase();
        if (gameMode.equals("classic"))
            Table_Name = "Classic";
        else
            Table_Name = "Blind";
        db.execSQL("update " + Table_Name + " set min_Count="+min+" where Stage="+ stageCount + ";");
        db.close();
        //Log.d(tag, "insert 성공~!");
    }


    public void insertDB(int Min_Count) {
        db = getWritableDatabase();
        if (gameMode.equals("classic"))
            Table_Name = "Classic";
        else
            Table_Name = "Blind";

        db.execSQL("insert into " + Table_Name + " (Stage, Min_Count) values('" + stageCount + "', " + Min_Count + ");");
        db.close();
        //Log.d(tag, "insert 성공~!");
    } //출처: https://bitsoul.tistory.com/118?category=623707 [Happy Programmer~]
    // 출처: https://sisiblog.tistory.com/175 [고슴이]
}
