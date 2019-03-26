package gang.il;

import static gang.il.Valiable.curObjNum;
import static gang.il.Valiable.direction;
import static gang.il.Valiable.objCount;
import static gang.il.Valiable.stageSize;
import static gang.il.Valiable.totalObj;

public class Controller {

    public void check() { // 전역변수로 지정된 현재 객체의 이동여부를 검사

        //임시 변수 지정
        totalObj[0] = new AnimalObject(3, 4, "animal1", true);
        totalObj[1] = new TotalObject(2,4,"wall");
        totalObj[2] = new TotalObject(5,4,"wall");
        objCount = 2;
        curObjNum = 0;
        direction = "left";

        // 클래스에서 필요한 변수
        float min=0, max=stageSize;
        int moveIndex=999; //하나도 찾지 못했을 경우
        float desX,desY;

        //가로 이동
        for(int i=0 ; i< objCount ; i++){
            if(totalObj[curObjNum].posY == totalObj[i].posY){ //Y좌표가 같을경우
                if (direction.equals("left")){ // 왼쪽으로 이동일경우
                    if(totalObj[i].posX >= min && totalObj[i].posX < totalObj[curObjNum].posX){ // 가장 가까운 말 구하기
                        min = totalObj[i].posX;
                        moveIndex = i;
                    }
                    if(moveIndex == 999){
                        totalObj[curObjNum].posX = 0;
                    }else{
                        if(totalObj[moveIndex].type.equals("animal")){
                            while(totalObj[curObjNum].posX != totalObj[moveIndex].posX + 1){
                                totalObj[curObjNum].posX -= 0.00001f ;
                            }
                        } else if(totalObj[moveIndex].type.equals("wall")){
                            while(totalObj[curObjNum].posX != totalObj[moveIndex].posX + 1){
                                totalObj[curObjNum].posX -= 0.00001f ;
                            }
                        } else if(totalObj[moveIndex].type.equals("trap")){
                            while(totalObj[curObjNum].posX != totalObj[moveIndex].posX){
                                totalObj[curObjNum].posX -= 0.00001f ;
                            }
                        } else if(totalObj[moveIndex].type.equals("fin")){
                            while(totalObj[curObjNum].posX != totalObj[moveIndex].posX){
                                totalObj[curObjNum].posX -= 0.00001f ;
                            }
                        }
                    }
                } else {
                    if(totalObj[i].posX <= max && totalObj[i].posX > totalObj[curObjNum].posX){ // 가장 가까운 말 구하기
                        max = totalObj[i].posX;
                        moveIndex = i;
                    }
                    if(moveIndex == 999){
                        totalObj[curObjNum].posX = stageSize;
                    }else{
                        if(totalObj[moveIndex].type.equals("animal")){
                            while(totalObj[curObjNum].posX != totalObj[moveIndex].posX + 1){
                                totalObj[curObjNum].posX -= 0.00001f ;
                            }
                        } else if(totalObj[moveIndex].type.equals("wall")){
                            while(totalObj[curObjNum].posX != totalObj[moveIndex].posX + 1){
                                totalObj[curObjNum].posX -= 0.00001f ;
                            }
                        } else if(totalObj[moveIndex].type.equals("trap")){
                            while(totalObj[curObjNum].posX != totalObj[moveIndex].posX){
                                totalObj[curObjNum].posX -= 0.00001f ;
                            }
                        } else if(totalObj[moveIndex].type.equals("fin")){
                            while(totalObj[curObjNum].posX != totalObj[moveIndex].posX){
                                totalObj[curObjNum].posX -= 0.00001f ;
                            }
                        }
                    }
                }
            }
        }
    }
}
