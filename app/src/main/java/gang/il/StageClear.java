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

    public void clearCheck(/*int moveIndex*/) {

        /*boolean[] clearAnimal = new boolean[animalCount];
        int num =0; //0~animalCount 까지 증가 끝나는 순서대로?
        String checkType = totalObj[clearIndex].getType();
        Controller controller = new Controller(mContext);
        if(checkType.substring(checkType.length()-3,checkType.length()).equals("fin") && controller.IsMyFinish(checkType)){
            clearAnimal[num] = true;
            num ++;
        }
        if(num == animalCount){
            //clear
            onClearStage(mContext, getStage);
            ((GamePage) mContext).Dialog();
        } !@#$% */

        removeAnimal();
        if (finishCount == 0) { //모든 동물이 탈출했을 경우
            if (Integer.parseInt(stageCount) > Integer.parseInt(onCheckStage(mContext))) //클리어한 스테이지가 가장 마지막으로 클리어한 스테이지인 경우
                onClearStage(mContext, stageCount);
            ((GamePage) mContext).Dialog();
        }
    }

    private void removeAnimal() {
        objCount--;
        finishCount--;
        //totalObj[curObjNum] = new TotalObject(totalObj[objCount].posX, totalObj[objCount].posY, totalObj[objCount].getType(), totalObj[objCount].isMoveAble()); //클리어된 동물 객체에 객체배열의 맨 마지막 인덱스값 저장
        //totalObj[objCount] = null; //마지막 인덱스값 지우기

        /*!@#$ 위에 걸로 하면 trap에 다른게 걸린 상태에서 finish라인에 도착한 동물이 삭제되고 나서 제3의 동물로 trap쪽으로 이동하면 trap에 동물이 있는건데 앞에 안멈추고 안에 멈춤 이건 이동 알고리즘에 문제가 있는거
        trap과 다른동물이 겹쳐있는 상황을 고려해야되는데 현재동물과의 거리만 고려하다보니 trap과 그 trap안의 동물의 거리값이 동일해서 둘중 먼저check되는 값만 벽으로 세워져서 trap이 먼저면 trap안에 동물2개가 겹치고 다른 동물이 먼저면 trap(동물)전에 멈춤
        이부분은 이동 알고리즘을 고쳐야 할듯 싶은데 사실 생각해보면 동물이 덫에 걸린상황에서 동물 위에 덫이 그려져야함으로 동물보다 trap이 뒤에 check 되어야 이미지가 자연스럽게 되기는 한데 어쨋든 알고리즘에 문제이기는 함*/

        for (int i = curObjNum; i < objCount; i++) {
            totalObj[i] = new TotalObject(totalObj[i + 1].posX, totalObj[i + 1].posY, totalObj[i + 1].getType(), totalObj[i + 1].isMoveAble()); //클리어한 동물 기준으로 객체 값을 하나씩 앞으로 땡기기
        }
        totalObj[objCount] = null; //마지막 인덱스값 지우기
    }
}
