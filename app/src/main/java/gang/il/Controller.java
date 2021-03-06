package gang.il;

import android.content.Context;

import static gang.il.StageDBHelper.putInFood;
import static gang.il.Valiable.curObjNum;
import static gang.il.Valiable.direction;
import static gang.il.Valiable.eatSound;
import static gang.il.Valiable.holeSound;
import static gang.il.Valiable.moveCount;
import static gang.il.Valiable.objCount;
import static gang.il.Valiable.passSound;
import static gang.il.Valiable.soundPlay;
import static gang.il.Valiable.soundPool;
import static gang.il.Valiable.stageCount;
import static gang.il.Valiable.totalObj;
import static gang.il.Valiable.trapSound;
import static gang.il.Valiable.tutorialNum;
import static gang.il.Valiable.wallSound;

public class Controller {
    Context mContext;

    public Controller(Context mContext) {
        this.mContext = mContext;
    }

    public int check(int moveIndex) { // 전역변수로 지정된 현재 객체의 이동여부를 검사

        String structure = totalObj[moveIndex].getType();
        if (structure.endsWith("fin")) structure = "finish";
        if (structure.startsWith("food")) structure = "food";
        switch (structure) {
            case "trap":
                totalObj[curObjNum].setMoveAble(false);
                return 0;
            case "finish":
            case "food":
            case "cave":
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
        if (!totalObj[curObjNum].isMoveAble()) {
            if(soundPlay) soundPool.play(trapSound, 1f, 1f, 0, 0, 1f); //덫 사운드 재생
            return; //트랩에 걸렸을때
        }
        if (direction.equals("left") || direction.equals("up")) // 방향성에 따른 증가 감소 변화
            moveIndex = 0;
        else
            moveIndex = 1;

        for (int i = 2; i < objCount; i++) {
            // 모든 객체를 검사하면서 현재 선택된 객체와의 절대 길이를 비교
            if (totalObj[i].getType().equals(totalObj[curObjNum].getType()))
                continue; //현재 선택된 객체는 제외
            if (totalObj[i].getLength(curPosX, curPosY) <= totalObj[moveIndex].getLength(curPosX, curPosY)) {
                if (totalObj[i].getType().endsWith("fin") && !totalObj[i].getType().equals(totalObj[curObjNum].getType() + "_fin"))//선택된 객체의 피니시가 아닐 경우 제외
                    continue;
                if (totalObj[i].getType().startsWith("food") && !totalObj[curObjNum].foods.contains(totalObj[i].getType())) //선택된 객체의 음식이 아닐 경우 제외
                    continue;
                if (totalObj[i].getType().equals("trap") && totalObj[i].getPosX() == totalObj[moveIndex].getPosX() && totalObj[i].getPosY() == totalObj[moveIndex].getPosY()) // 함정과 동물이 겹쳐있는 경우 동물로 목적지 설정
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
                if (totalObj[moveIndex].getPosX() + movePoint == curPosX) //이동할 좌표와 현재 좌표가 같으면 객체 이동 x
                    return;
                else {
                    while ((totalObj[moveIndex].getPosX() + movePoint) != curPosX)
                        totalObj[curObjNum].setPosX(curPosX -= 0.000001f);
                    break;
                }
            case "right":
                if (totalObj[moveIndex].getPosX() - movePoint == curPosX)
                    return;
                else {
                    while ((totalObj[moveIndex].getPosX() - movePoint) != curPosX)
                        totalObj[curObjNum].setPosX(curPosX += 0.000001f);
                    break;
                }
            case "up":
                if (totalObj[moveIndex].getPosY() + movePoint == curPosY)
                    return;
                else {
                    while ((totalObj[moveIndex].getPosY() + movePoint) != curPosY)
                        totalObj[curObjNum].setPosY(curPosY -= 0.000001f);
                    break;
                }
            case "down":
                if (totalObj[moveIndex].getPosY() - movePoint == curPosY)
                    return;
                else {
                    while ((totalObj[moveIndex].getPosY() - movePoint) != curPosY)
                        totalObj[curObjNum].setPosY(curPosY += 0.000001f);
                    break;
                }
        }

        moveCount++; //이동 횟수 증가
        ((GamePage) mContext).setMoveCount();
        if (stageCount.equals("1")) { //튜토리얼 진행
            if (tutorialNum < 3) tutorialNum++;
        }

        if (totalObj[moveIndex].type.startsWith("food")) { //이동지가 음식일 경우
            if(soundPlay) soundPool.play(eatSound, 1f, 1f, 0, 0, 1f); //음식 사운드 재생
            objCount--;
            int caveNum = 0;
            for (int i = moveIndex; i < objCount; i++) {
                totalObj[i] = new TotalObject(totalObj[i + 1].posX, totalObj[i + 1].posY, totalObj[i + 1].getType(), totalObj[i + 1].isMoveAble()); //해당 음식 기준으로 객체 값을 하나씩 앞으로 땡기기
                if (totalObj[i].getType().equals("cave")) totalObj[i].setCaveNum(++caveNum);
                else if(totalObj[i].getType().equals("dog") ||totalObj[i].getType().equals("squirrel") || totalObj[i].getType().equals("rabbit") || totalObj[i].getType().equals("panda") || totalObj[i].getType().equals("tiger"))
                    putInFood(totalObj[i].getType(),i);
            }
            totalObj[objCount] = null; //마지막 인덱스값 지우기
        } else if (totalObj[moveIndex].getType().equals("cave")) { //이동지가 동굴인 경우
            if(soundPlay) soundPool.play(holeSound, 1f, 1f, 0, 0, 1f); //동굴(구멍) 사운드 재생
            Cave Cave = new Cave();
            if (Cave.onCave(moveIndex)) {
                move();
                moveCount--;
            }
        } else if (totalObj[moveIndex].getType().endsWith("fin")) { //이동지가 피니시인 경우
            if(soundPlay) soundPool.play(passSound, 1f, 1f, 0, 0, 1f); //동물 통과 사운드 재생
            StageClear StageClear = new StageClear(mContext);
            StageClear.clearCheck();
        }
        else {
            if(soundPlay) soundPool.play(wallSound, 1f, 1f, 0, 0, 1f); //그 외 이동지일 경우 부딪힘 사운드 재생
        }
    }
}
