package gang.il;

import android.util.Log;

import static gang.il.Valiable.curObjNum;
import static gang.il.Valiable.direction;
import static gang.il.Valiable.stageSize;
import static gang.il.Valiable.totalObj;

public class Controller {

    public int check(int moveIndex) { // 전역변수로 지정된 현재 객체의 이동여부를 검사

        String structure= totalObj[moveIndex].getType();
        if (structure.endsWith("_fin")) structure = "finish";
        switch (structure){
            case "trap":
                totalObj[curObjNum].setMoveAble(false);
                return 0;
            case "finish":
                return 0;
                default:
                    return 1;
        }
    }

    public boolean IsMyFinish(String checkType) {
        String curType = totalObj[curObjNum].getType();
        //Log.d("testFinishCheck","cur : " + curType + ", check : "+checkType.substring(0,checkType.length()-4));
        if(curType.equals(checkType.substring(0,checkType.length()-4))){
            return true;
        }
        return false;
    }

    public void move(){

        int moveIndex, movePoint; //가장 가까운 장애물의 인덱스
        float curPosX = totalObj[curObjNum].getPosX(), curPosY = totalObj[curObjNum].getPosY(); // 현재 위치에 대한 x,y 좌표의 값을 저장

        if(totalObj[curObjNum].isMoveAble() == false) return; //트랩에 걸렸을때

        if(direction.equals("left") || direction.equals("up")) // 방향성에 따른 증가 감소 변화
            moveIndex=0;
        else
            moveIndex=1;

        for(int i=2 ; i< totalObj.length ; i++) {
            // 모든 객체를 검사하면서 현재 선택된 객체와의 절대 길이를 비교
            if (totalObj[i].getType().equals(totalObj[curObjNum].getType())) continue; //현재 선택된 객체는 제외
            if (totalObj[i].getLength(curPosX, curPosY) < totalObj[moveIndex].getLength(curPosX, curPosY)) {
                String checkType = totalObj[i].getType();
                switch (direction) {
                    case "left":
                        if (curPosY == totalObj[i].getPosY()  && curPosX > totalObj[i].getPosX())
                            if(checkType.substring(checkType.length()-3,checkType.length()).equals("fin") && !IsMyFinish(checkType))    break; // 선택된것이 피니시이고 자기것이 아닌경우는 패스
                            else    moveIndex = i; //동일한 y축에 x값이 선택된 동물보다 작은 경우
                        break;
                    case "right":
                        if (curPosY == totalObj[i].getPosY()  && curPosX < totalObj[i].getPosX())
                            if(checkType.substring(checkType.length()-3,checkType.length()).equals("fin") && !IsMyFinish(checkType))    break;
                            else    moveIndex = i;
                        break;
                    case "up":
                        if (curPosX == totalObj[i].getPosX() && curPosY > totalObj[i].getPosY())
                            if(checkType.substring(checkType.length()-3,checkType.length()).equals("fin") && !IsMyFinish(checkType))    break;
                            else    moveIndex = i;
                        break;
                    case "down":
                        if (curPosX == totalObj[i].getPosX() && curPosY < totalObj[i].getPosY())
                            if(checkType.substring(checkType.length()-3,checkType.length()).equals("fin") && !IsMyFinish(checkType))    break;
                            else    moveIndex = i;
                        break;
                }
            }
        }

        movePoint = check(moveIndex);

        Log.d("testMoveIndex","result:index"+moveIndex+"/Point"+movePoint);

        switch (direction){
            case "left":
                while((totalObj[moveIndex].getPosX() + movePoint) != curPosX)
                    totalObj[curObjNum].setPosX(curPosX -= 0.000001f);
                break;
            case "right":
                while((totalObj[moveIndex].getPosX() - movePoint) != curPosX)
                    totalObj[curObjNum].setPosX(curPosX += 0.000001f);
                break;
            case "up":
                while((totalObj[moveIndex].getPosY() + movePoint) != curPosY)
                    totalObj[curObjNum].setPosY(curPosY -= 0.000001f);
                break;
            case "down":
                while((totalObj[moveIndex].getPosY() - movePoint) != curPosY)
                    totalObj[curObjNum].setPosY(curPosY += 0.000001f);
                break;
        }


    }
}
