package gang.il;

import android.content.Context;
import android.util.Log;

import static gang.il.Valiable.curObjNum;
import static gang.il.Valiable.finishObj;
import static gang.il.Valiable.minCount;
import static gang.il.Valiable.moveCount;
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
        if (finishObj.size() == 0) {  //모든 동물이 탈출했을 경우
            StageDBHelper StageDB = new StageDBHelper(mContext);
            StageDB.selectDB();
            if (StageDB.getClearedStage() < Integer.parseInt(stageCount))
                StageDB.insertDB(moveCount); // 클리어하지 않은 스테이지를 클리어한 경우 최소횟수 저장
            else {
                if (StageDB.getMinimumCount(Integer.valueOf(stageCount) - 1) > moveCount)
                    StageDB.recordMinCount(moveCount); //저장된 최소횟수보다 적은 횟수로 클리어한 경우 최소횟수 업데이트
            }
            ((GamePage) mContext).Dialog();
        }
    }

    private void removeAnimal() {
        objCount--;
        finishObj.remove(totalObj[curObjNum].getType());
        int caveNum = 0;
        for (int i = curObjNum; i < objCount; i++) {
            totalObj[i] = new TotalObject(totalObj[i + 1].getPosX(), totalObj[i + 1].getPosY(), totalObj[i + 1].getType(), totalObj[i + 1].isMoveAble()); //클리어한 동물 기준으로 객체 값을 하나씩 앞으로 땡기기
            if (totalObj[i].getType().equals("cave")) totalObj[i].setCaveNum(++caveNum);
        }
        totalObj[objCount] = null; //마지막 인덱스값 지우기
    }
}
