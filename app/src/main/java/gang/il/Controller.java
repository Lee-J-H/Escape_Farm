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

        int moveIndex, movePoint;

        if(direction.equals("left") || direction.equals("up"))
            moveIndex=0;
        else
            moveIndex=1;

        for(int i=2 ; i< totalObj.length ; i++)
            if(totalObj[i].getLength(totalObj[curObjNum].getPosX(),totalObj[curObjNum].getPosY(),direction) < totalObj[moveIndex].getLength(totalObj[curObjNum].getPosX(),totalObj[curObjNum].getPosY(),direction))
                moveIndex = i;

        movePoint = check(moveIndex);

        switch (direction){
            case "left":
                while((totalObj[moveIndex].getPosX() + movePoint) !=totalObj[curObjNum].getPosX())
                    totalObj[curObjNum].setPosX(totalObj[curObjNum].getPosX()-0.0000f);
            case "right":
                while((totalObj[moveIndex].getPosX() - movePoint) !=totalObj[curObjNum].getPosX())
                    totalObj[curObjNum].setPosX(totalObj[curObjNum].getPosX()+0.0000f);
            case "up":
                while((totalObj[moveIndex].getPosY() + movePoint) !=totalObj[curObjNum].getPosY())
                    totalObj[curObjNum].setPosY(totalObj[curObjNum].getPosY()-0.0000f);
            case "down":
                while((totalObj[moveIndex].getPosY() - movePoint) !=totalObj[curObjNum].getPosY())
                    totalObj[curObjNum].setPosY(totalObj[curObjNum].getPosY()+0.0000f);
        }
    }
}
