package gang.il;

import android.util.Log;

import static gang.il.Valiable.curObjNum;
import static gang.il.Valiable.objCount;
import static gang.il.Valiable.totalObj;

public class Cave {
    public Cave() {
    }

    public boolean onCave(int moveIndex) {
        int caveNum = totalObj[moveIndex].getCaveNum();
        int opponentCaveObjNum = 0;
        switch (caveNum) {  //상반되는 caveNum(1-2/3-4) 찾아서 좌표 변환
            case 1:
                for (int i = 0; i < objCount; i++) {
                    if (totalObj[i].getCaveNum() == 2) {
                        opponentCaveObjNum = i;
                    }
                }
                break;
            case 2:
                for (int i = 0; i < objCount; i++) {
                    if (totalObj[i].getCaveNum() == 1) {
                        opponentCaveObjNum = i;
                    }
                }
                break;
            case 3:
                for (int i = 0; i < objCount; i++) {
                    if (totalObj[i].getCaveNum() == 4) {
                        opponentCaveObjNum = i;
                    }
                }
                break;
            case 4:
                for (int i = 0; i < objCount; i++) {
                    if (totalObj[i].getCaveNum() == 3) {
                        opponentCaveObjNum = i;
                    }
                }
                break;
        }
        for (int i = 0; i < objCount; i++) {
            if (!totalObj[i].getType().equals("cave") && totalObj[i].getPosX() == totalObj[opponentCaveObjNum].getPosX() && totalObj[i].getPosY() == totalObj[opponentCaveObjNum].getPosY()) {
                //상대 동굴에 동물이 위치해 있는 경우 좌표 변환X
                return false;
            }
        }
        totalObj[curObjNum].setPosX(totalObj[opponentCaveObjNum].getPosX());
        totalObj[curObjNum].setPosY(totalObj[opponentCaveObjNum].getPosY());
        return true;
    }
}
