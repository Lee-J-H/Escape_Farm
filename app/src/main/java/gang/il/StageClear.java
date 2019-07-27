package gang.il;

import android.content.Context;
import android.util.Log;

import static gang.il.CheckedStage.onCheckStage;
import static gang.il.CheckedStage.onClearStage;
import static gang.il.Valiable.finishCount;
import static gang.il.Valiable.curObjNum;
import static gang.il.Valiable.objCount;
import static gang.il.Valiable.stageCount;
import static gang.il.Valiable.totalObj;

public class StageClear {
    Context mContext;

    public StageClear(Context mContext) {
        this.mContext = mContext;
    }

    public void clearCheck() {
        removeAnimal();
        if (finishCount == 0) { //모든 동물이 탈출했을 경우
            //if (Integer.parseInt(stageCount) > Integer.parseInt(onCheckStage(mContext))) //클리어한 스테이지가 가장 마지막으로 클리어한 스테이지인 경우
            //    onClearStage(mContext, stageCount);
            onClearStage(mContext, Integer.parseInt(stageCount), ((GamePage) mContext).checkMinimumMove());
            ((GamePage) mContext).Dialog();
        }
    }

    private void removeAnimal() {
        objCount--;
        finishCount--;
        for (int i = curObjNum; i < objCount; i++) {
            totalObj[i] = new TotalObject(totalObj[i + 1].posX, totalObj[i + 1].posY, totalObj[i + 1].getType(), totalObj[i + 1].isMoveAble()); //클리어한 동물 기준으로 객체 값을 하나씩 앞으로 땡기기
        }
        totalObj[objCount] = null; //마지막 인덱스값 지우기
    }
}
