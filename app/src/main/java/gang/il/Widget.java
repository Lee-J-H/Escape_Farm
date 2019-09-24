package gang.il;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import static gang.il.Valiable.blankX;
import static gang.il.Valiable.finishObj;
import static gang.il.Valiable.spaceX;
import static gang.il.Valiable.spaceY;

public class Widget {
    Canvas mCanvas;
    Context mContext;
    Bitmap dogWidget, squirrelWidget, rabbitWidget, pandaWidget, tigerWidget;
    int finCount=0;

    public Widget(Context context, Canvas canvas) {
        mContext = context;
        mCanvas = canvas;
        dogWidget = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.dog);
        dogWidget = Bitmap.createScaledBitmap(dogWidget, (int) spaceX, (int) spaceY, true);
        squirrelWidget = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.squirrel);
        squirrelWidget = Bitmap.createScaledBitmap(squirrelWidget, (int) spaceX, (int) spaceY, true);
        rabbitWidget = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.rabbit);
        rabbitWidget = Bitmap.createScaledBitmap(rabbitWidget, (int) spaceX, (int) spaceY, true);
        pandaWidget = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.panda);
        pandaWidget = Bitmap.createScaledBitmap(pandaWidget, (int) spaceX, (int) spaceY, true);
        tigerWidget = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.tiger);
        tigerWidget = Bitmap.createScaledBitmap(tigerWidget, (int) spaceX, (int) spaceY, true);
    }

    public void drawAnimalWidget() {
        int finishWidgetNum = 0;
        for (int i = 0; i < finishObj.size(); i++) {
            if(finishObj.get(i).equals("dog_fin")) {
                dogWidget = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.dog_widget);
                dogWidget = Bitmap.createScaledBitmap(dogWidget, (int) spaceX, (int) spaceY, true);
            }
            else if(finishObj.get(i).equals("squirrel_fin")) {
                squirrelWidget = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.squirrel_widget);
                squirrelWidget = Bitmap.createScaledBitmap(squirrelWidget, (int) spaceX, (int) spaceY, true);
            }
            else if(finishObj.get(i).equals("rabbit_fin")) {
                rabbitWidget = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.rabbit_widget);
                rabbitWidget = Bitmap.createScaledBitmap(rabbitWidget, (int) spaceX, (int) spaceY, true);
            }
            else if(finishObj.get(i).equals("panda_fin")) {
                pandaWidget = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.panda_widget);
                pandaWidget = Bitmap.createScaledBitmap(pandaWidget, (int) spaceX, (int) spaceY, true);
            }
            else if(finishObj.get(i).equals("tiger_fin")) {
                tigerWidget = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.tiger_widget);
                tigerWidget = Bitmap.createScaledBitmap(tigerWidget, (int) spaceX, (int) spaceY, true);
            } //탈출한 동물 확인


            if(finishObj.get(i).toString().startsWith("dog"))
                mCanvas.drawBitmap(dogWidget, (finishWidgetNum++) * blankX, spaceY*14/3, null);
            else if(finishObj.get(i).toString().startsWith("squirrel"))
                mCanvas.drawBitmap(squirrelWidget, (finishWidgetNum++) * blankX, spaceY*14/3, null);
            else if(finishObj.get(i).toString().startsWith("rabbit"))
                mCanvas.drawBitmap(rabbitWidget, (finishWidgetNum++) * blankX, spaceY*14/3, null);
            else if(finishObj.get(i).toString().startsWith("panda"))
                mCanvas.drawBitmap(pandaWidget, (finishWidgetNum++) * blankX, spaceY*14/3, null);
            else if(finishObj.get(i).toString().startsWith("tiger"))
                mCanvas.drawBitmap(tigerWidget, (finishWidgetNum++) * blankX, spaceY*14/3, null);
        }
    }

    public void finWidget(String animal) {
        switch (animal) {
            case "dog":
                dogWidget = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.dog_widget);
                dogWidget = Bitmap.createScaledBitmap(dogWidget, (int) spaceX, (int) spaceY, true);
                break;
            case "squirrel":
                squirrelWidget = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.squirrel_widget);
                squirrelWidget = Bitmap.createScaledBitmap(squirrelWidget, (int) spaceX, (int) spaceY, true);
                break;
            case "rabbit":
                rabbitWidget = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.rabbit_widget);
                rabbitWidget = Bitmap.createScaledBitmap(rabbitWidget, (int) spaceX, (int) spaceY, true);
                break;
            case "panda":
                pandaWidget = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.panda_widget);
                pandaWidget = Bitmap.createScaledBitmap(pandaWidget, (int) spaceX, (int) spaceY, true);
                break;
            case "tiger":
                tigerWidget = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.tiger_widget);
                tigerWidget = Bitmap.createScaledBitmap(tigerWidget, (int) spaceX, (int) spaceY, true);
                break;
        }
        for(int i=0; i<finCount; i++){

        }
    }
}
