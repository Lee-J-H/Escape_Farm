package gang.il;

import android.content.Context;
import android.graphics.PointF;
import android.util.Log;

import static gang.il.Valiable.curObjNum;
import static gang.il.Valiable.direction;
import static gang.il.Valiable.moveCount;
import static gang.il.Valiable.objCount;
import static gang.il.Valiable.stageSize;
import static gang.il.Valiable.totalObj;

public class Controller {
    Context mContext;

    public Controller(Context mContext) {
        this.mContext = mContext;
    }

    public int check(int moveIndex) { // 전역변수로 지정된 현재 객체의 이동여부를 검사

        String structure = totalObj[moveIndex].getType();
        if (structure.endsWith("fin")) structure = "finish";
        if (structure.startsWith("food")) structure= "food";
        switch (structure) {
            case "trap":
                totalObj[curObjNum].setMoveAble(false);
                return 0;
            case "finish":
            case "food":
                return 0;
            case "wall":
            case "boundary":
                return 1;
            default:
                return 2;
        }
    }

    public void move() {

        int moveIndex, movePoint; //가장 가까운 장애물의 인덱스
        float curPosX = totalObj[curObjNum].getPosX(), curPosY = totalObj[curObjNum].getPosY(); // 현재 위치에 대한 x,y 좌표의 값을 저장
        float oriPosX = curPosX, oriPosY = curPosY;//기존 좌표 저장

        if (!totalObj[curObjNum].isMoveAble()) return; //트랩에 걸렸을때

        if (direction.equals("left") || direction.equals("up")) // 방향성에 따른 증가 감소 변화
            moveIndex = 0;
        else
            moveIndex = 1;

        for (int i = 2; i < objCount; i++) {
            // 모든 객체를 검사하면서 현재 선택된 객체와의 절대 길이를 비교
            if (totalObj[i].getType().equals(totalObj[curObjNum].getType()))
                continue; //현재 선택된 객체는 제외
            if (totalObj[i].getLength(curPosX, curPosY) < totalObj[moveIndex].getLength(curPosX, curPosY)) {
                if (totalObj[i].getType().endsWith("fin") && !totalObj[i].getType().equals(totalObj[curObjNum].getType() + "_fin"))//선택된 객체의 피니시가 아닐 경우 제외
                    continue;
                if (totalObj[i].getType().startsWith("food") && !totalObj[curObjNum].foods.contains(totalObj[i].getType())) //선택된 객체의 음식이 아닐 경우 제외
                    continue;
                switch (direction) {
                    case "left":
                        if (curPosY == totalObj[i].getPosY() && curPosX > totalObj[i].getPosX())
                            moveIndex = i; //동일한 y축에 x값이 선택된 동물보다 작은 경우
                        break;
                    case "right":
                        if (curPosY == totalObj[i].getPosY() && curPosX < totalObj[i].getPosX())
                            moveIndex = i;
                        break;
                    case "up":
                        if (curPosX == totalObj[i].getPosX() && curPosY > totalObj[i].getPosY())
                            moveIndex = i;
                        break;
                    case "down":
                        if (curPosX == totalObj[i].getPosX() && curPosY < totalObj[i].getPosY())
                            moveIndex = i;
                        break;
                }
            }
        }

        movePoint = check(moveIndex);

        switch (direction) {
            case "left":
                while ((totalObj[moveIndex].getPosX() + movePoint) != curPosX)
                    totalObj[curObjNum].setPosX(curPosX -= 0.000001f);
                break;
            case "right":
                while ((totalObj[moveIndex].getPosX() - movePoint) != curPosX)
                    totalObj[curObjNum].setPosX(curPosX += 0.000001f);
                break;
            case "up":
                while ((totalObj[moveIndex].getPosY() + movePoint) != curPosY)
                    totalObj[curObjNum].setPosY(curPosY -= 0.000001f);
                break;
            case "down":
                while ((totalObj[moveIndex].getPosY() - movePoint) != curPosY)
                    totalObj[curObjNum].setPosY(curPosY += 0.000001f);
                break;
        }

        if (totalObj[moveIndex].type.startsWith("food")) { //이동지가 음식일 경우
            objCount--;
            for (int i = moveIndex; i < objCount; i++) {
                totalObj[i] = new TotalObject(totalObj[i + 1].posX, totalObj[i + 1].posY, totalObj[i + 1].getType(), totalObj[i + 1].isMoveAble()); //해당 음식 기준으로 객체 값을 하나씩 앞으로 땡기기
            }
            totalObj[objCount] = null; //마지막 인덱스값 지우기
        }

        if (oriPosX != curPosX || oriPosY != curPosY) { //객체가 이동을 한 경우
            moveCount++; //이동 횟수 증가
            ((GamePage) mContext).setMoveCount(); //뷰의 이동횟수 갱신
        }
        if (totalObj[moveIndex].getType().endsWith("fin")) { //이동지가 피니시인 경우
            StageClear StageClear = new StageClear(mContext);
            StageClear.clearCheck();
        }
    }
}
