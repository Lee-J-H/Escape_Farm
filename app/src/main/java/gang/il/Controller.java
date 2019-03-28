package gang.il;

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
            case "wall":
                return 1;
            case "finish":
                return 0;
                default:
                    return 1;
        }
    }

    public void move(){

        int moveIndex, movePoint; //가장 가까운 장애물의 인덱스
        float curPosX = totalObj[curObjNum].getPosX(), curPosY = totalObj[curObjNum].getPosY(); // 현재 위치에 대한 x,y 좌표의 값을 저장

        if(direction.equals("left") || direction.equals("up")) // 방향성에 따른 증가 감소 변화
            moveIndex=0;
        else
            moveIndex=1;

        for(int i=2 ; i< totalObj.length ; i++)
            // 모든 객체를 검사하면서 현재 선택된 객체와의 절대 길이를 비교
            if(totalObj[i].getLength(curPosX,curPosY,direction) < totalObj[moveIndex].getLength(curPosX,curPosY,direction)){
                if((direction.equals("left") || direction.equals("right")) && curPosY == totalObj[i].getPosY())
                    moveIndex = i; //값이 작으면 더 가까운 것이므로 인덱스 값을 변경
                else if((direction.equals("up") || direction.equals("down")) && curPosX == totalObj[i].getPosX())
                    moveIndex = i; //값이 작으면 더 가까운 것이므로 인덱스 값을 변경
            }


        movePoint = check(moveIndex);

        switch (direction){
            case "left":
                while((totalObj[moveIndex].getPosX() + movePoint) != curPosX)
                    totalObj[curObjNum].setPosX(curPosX-0.0000f);
            case "right":
                while((totalObj[moveIndex].getPosX() - movePoint) != curPosX)
                    totalObj[curObjNum].setPosX(curPosX+0.0000f);
            case "up":
                while((totalObj[moveIndex].getPosY() + movePoint) != curPosY)
                    totalObj[curObjNum].setPosY(curPosY-0.0000f);
            case "down":
                while((totalObj[moveIndex].getPosY() - movePoint) != curPosY)
                    totalObj[curObjNum].setPosY(curPosY+0.0000f);
        }
    }
}
