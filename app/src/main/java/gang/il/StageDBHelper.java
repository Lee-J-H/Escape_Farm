package gang.il;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import static gang.il.Valiable.finishObj;
import static gang.il.Valiable.gameMode;
import static gang.il.Valiable.objCount;
import static gang.il.Valiable.stageCount;
import static gang.il.Valiable.stageSize_x;
import static gang.il.Valiable.stageSize_y;
import static gang.il.Valiable.totalObj;

public class StageDBHelper extends SQLiteOpenHelper {
    private SQLiteDatabase db;
    private static final int DATABASE_VERSION = 1;
    private static final String Database_Name = "StageDB";

    public StageDBHelper(Context context) {
        super(context, Database_Name, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String nightCount =
                "CREATE TABLE " + "NightCount" + "(" +
                        "Stage INTEGER PRIMARY key, " +
                        "Count INTEGER NOT NULL," +
                        "myCount INTEGER" +
                        ");";
        String nightStage =
                "CREATE TABLE " + "NightStage" + "(" +
                        "Stage INTEGER, " +
                        "Structure varchar(255) not null, " +
                        "posX INTEGER not null, " +
                        "posY INTEGER not null, " +
                        "sort_num INTEGER NOT NULL," +
                        "foreign key(Stage) references NightCount(Stage) on update cascade on delete cascade" +
                        ");";
        String dayCount =
                "CREATE TABLE " + "DayCount" + "(" +
                        "Stage INTEGER PRIMARY key, " +
                        "Count INTEGER NOT NULL," +
                        "myCount INTEGER" +
                        ");";
        String dayStage =
                "CREATE TABLE " + "DayStage" + "(" +
                        "Stage INTEGER, " +
                        "Structure varchar(255) not null, " +
                        "posX INTEGER not null, " +
                        "posY INTEGER not null, " +
                        "sort_num INTEGER NOT NULL," +
                        "foreign key(Stage) references DayCount(Stage) on update cascade on delete cascade" +
                        ");";
        db.execSQL(nightCount);
        db.execSQL(nightStage);
        db.execSQL(dayCount);
        db.execSQL(dayStage);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String dropDayStage =
                "DROP TABLE IF EXISTS dayStage";
        String dropNightStage =
                "DROP TABLE IF EXISTS nightStage";
        String dropDayCount =
                "DROP TABLE IF EXISTS dayCount";
        String dropNightCount =
                "DROP TABLE IF EXISTS nightCount";
        db.execSQL(dropDayCount);
        db.execSQL(dropNightCount);
        db.execSQL(dropDayStage);
        db.execSQL(dropNightStage);
        onCreate(db);
    }
    public void init(){
        db = getWritableDatabase();
        String dropDayStage =
                "DROP TABLE IF EXISTS DayStage";
        String dropNightStage =
                "DROP TABLE IF EXISTS NightStage";
        String dropDayCount =
                "DROP TABLE IF EXISTS DayCount";
        String dropNightCount =
                "DROP TABLE IF EXISTS NightCount";
        db.execSQL(dropDayCount);
        db.execSQL(dropNightCount);
        db.execSQL(dropDayStage);
        db.execSQL(dropNightStage);
        onCreate(db);
    }

    public void getStageObj() {
        db = getReadableDatabase();
        Cursor c = db.rawQuery("select Structure, posX, posY, sort_num from " + gameMode + "Stage where Stage="+stageCount +" order by sort_num asc;", null);
        if (c.getCount() == 0) {
            return;
        }
        finishObj.clear();
        objCount = c.getCount();
        totalObj = new TotalObject[objCount];
        int caveNum = 0;
        int count=0;
        while (c.moveToNext()) {
            String structure = c.getString(0);
            int x = c.getInt(1);
            int y = c.getInt(2);
            int sort = c.getInt(3);
            if (count == 1) {
                stageSize_x = x / 2;
                stageSize_y = y / 2;
            }
            switch (sort) {
                case 10:
                    totalObj[count] = new TotalObject(x, y, structure, true);
                    putInFood(structure, count); //해당 동물의 음식 속성 부여
                    break;
                case 3:
                    totalObj[count] = new TotalObject(x, y, structure, false);
                    totalObj[count].caveNum = ++caveNum;
                    break;
                default:
                    totalObj[count] = new TotalObject(x, y, structure, false);
                    break;
            }
            if (totalObj[count].getType().endsWith("_fin"))
                finishObj.add(totalObj[count].getType().substring(0, totalObj[count].getType().indexOf("_"))); //finish 있는 동물 저장
            count++;
        }
        db.close();
    }
    public int getMyMinCount(int stageNum){
        int myMinCount = 0;
        db = getReadableDatabase();
        Cursor c = db.rawQuery("select myCount from "+gameMode+"Count where Stage ="+stageNum, null);
        if(c != null){
            c.moveToFirst();
            myMinCount = c.getInt(0);
        }
        db.close();
        return myMinCount;
    }
    public int getMinCount(int stageNum){
        int minCount = 0;
        db = getReadableDatabase();
        Cursor c = db.rawQuery("select Count from "+gameMode+ "Count where Stage ="+stageNum, null);
        c.moveToFirst();
        minCount = c.getInt(0);
        db.close();
        return minCount;
    }
    public void recordCount(int minCount){
        db = getWritableDatabase();
        db.execSQL("update "+gameMode+"Count set myCount="+minCount+" where Stage="+stageCount+";");
        db.close();
    }

    public int clearStageNum(){
        int count =0;
        db = getReadableDatabase();
        Cursor c = db.rawQuery("select Stage from "+gameMode+"Count where myCount is not null;", null);
        //Cursor c = db.rawQuery("select Stage from dayCount where myCount is not null;", null);
        if(c!=null)
            count = c.getCount();
        db.close();
        return count;
    }

    public void initObjDB(String mode,int stage, int sortNum,TotalObject... obj){
        db = getWritableDatabase();
        db.execSQL("insert into "+mode+"Stage(Stage, Structure, posX, posY, sort_num) values("+stage+",'"+obj[0].getType()+"',"+obj[0].getPosX()+","+obj[0].getPosY()+","+sortNum+");");
        db.close();
    }
    public void initCountDB(String mode, int minCount, int stage){
        db = getWritableDatabase();
        db.execSQL("insert into "+mode+"Count(Stage,Count, myCount) values("+stage+","+minCount+",null);");
        db.close();
    }

    private static void putInFood(String animalName, int animalIndex) {
        switch (animalName) {
            case "dog":
                totalObj[animalIndex].foods.add("food_bone");
                break;
            case "squirrel":
                totalObj[animalIndex].foods.add("food_acorn");
                break;
            case "panda":
                totalObj[animalIndex].foods.add("food_bamboo");
                break;
            case "rabbit":
                totalObj[animalIndex].foods.add("food_carrot");
                break;
            case "tiger":
                totalObj[animalIndex].foods.add("food_meat");
                break;
        }
    }
}
