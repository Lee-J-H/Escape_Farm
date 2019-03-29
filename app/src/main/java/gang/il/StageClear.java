package gang.il;

import static gang.il.Valiable.animalCount;
import static gang.il.Valiable.totalObj;

public class StageClear {

    public void clearCheck(int clearIndex) {
        boolean[] clearAnimal = new boolean[animalCount];
        int num =0; //0~animalCount 까지 증가 끝나는 순서대로?
        String checkType = totalObj[clearIndex].getType();
        Controller controller = new Controller();
        if(checkType.substring(checkType.length()-3,checkType.length()).equals("fin") && controller.IsMyFinish(checkType)){
            clearAnimal[num] = true;
            num ++;
        }
        if(num == animalCount-1){
            //clear
        }
    }
}
