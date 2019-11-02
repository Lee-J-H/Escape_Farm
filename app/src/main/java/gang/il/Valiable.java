package gang.il;

import android.app.Activity;
import android.content.Context;

import java.util.ArrayList;

public class Valiable {

    public static int curObjNum, objCount, stageSize_x, stageSize_y, minCount, moveCount, tutorialNum, spaceX, spaceY, blankX, blankY;; // 선택된 객체의 번호, 전체 객체의 수, 판의 크기,  동물의 수
    public static TotalObject[] totalObj; //모든 객체 변수
    public static String direction, stageCount, gameMode="day"; //이동 방향
    public static final int LOAD_FINISH = 1;
    public static Activity StagePage, MainPage, StartPage;
    public static ArrayList finishObj = new ArrayList();
    public static StageClearDialog dialog;
    public static Context mContext;
    public static boolean drawInit;
    public static float width, height;
}
