package gang.il;

import android.app.Activity;

public class Valiable {

    public static int curObjNum, objCount, stageSize, finishCount, minCount, moveCount; // 선택된 객체의 번호, 전체 객체의 수, 판의 크기,  동물의 수
    public static TotalObject[] totalObj;// = new TotalObject[objCount]; //모든 객체 변수
    public static String direction, stageCount; //이동 방향
    public static final int CLEAR_STAGE = 1;
    public static final int LOAD_FINISH = 2;
    public static final int STAGE_RESET = 3;
    public static Activity StagePage;
    public static boolean onReset=false;
}
