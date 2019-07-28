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
        int opponentCaveObjNum = 0, caveCount = 0;
        int opponentCaveNum[] = new int[4];
        for (int i = 0; i < objCount; i++) {
            if (totalObj[i].getType().equals("cave"))
                opponentCaveNum[caveCount++] = i;
        }
        switch (caveNum) {  //상반되는 caveNum(1-2/3-4) 찾아서 좌표 변환
            case 1:
                opponentCaveObjNum = opponentCaveNum[1];
                break;
            case 2:
                opponentCaveObjNum = opponentCaveNum[0];
                break;
            case 3:
                opponentCaveObjNum = opponentCaveNum[3];
                break;
            case 4:
                opponentCaveObjNum = opponentCaveNum[2];
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
