package gang.il;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;

import static gang.il.Valiable.objCount;
import static gang.il.Valiable.stageSize;
import static gang.il.Valiable.totalObj;
import static gang.il.Valiable.direction;
import static gang.il.Valiable.curObjNum;

public class GameSurfaceView extends SurfaceView implements SurfaceHolder.Callback {
    Context mContext;
    SurfaceHolder mHolder;
    ImageThread mThread;
    Canvas mCanvas = null;
    float Width, Height;
    int spaceX, spaceY, blankX, blankY;
    boolean animal_clk = false;

    public GameSurfaceView(Context context) {
        super(context);
        mContext = context;
        mHolder = getHolder();
        mHolder.addCallback(this);
        mThread = new GameSurfaceView.ImageThread();
    }

    public GameSurfaceView(Context context, AttributeSet attr) {
        super(context, attr);
        mContext = context;
        mHolder = getHolder();
        mHolder.addCallback(this);
        mThread = new GameSurfaceView.ImageThread();
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        // Surface가 만들어질 때 호출됨
        mThread.start();
        ((GamePage) mContext).setMoveCount();
        ((GamePage) mContext).setStageCount();
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int format, int width, int height) {
        // Surface가 변경될 때 호출됨
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        // Surface가 종료될 때 호출됨
        try {
            mThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        PointF firstPoint=new PointF(0,0),lastPoint = new PointF(0,0);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                firstPoint.x = event.getX();
                firstPoint.y = event.getY();
                for (int i = 0; i < objCount; i++)
                    if (!totalObj[i].getType().equals("none") && (int)(firstPoint.x - blankX) / spaceX == totalObj[i].getPosX()/2 && (int) (firstPoint.y - blankY) / spaceY == totalObj[i].getPosY()/2) { //빈칸이 아니고 동물이 있는 땅을 눌렀을 때
                        if (!totalObj[i].getType().endsWith("fin") && !totalObj[i].getType().equals("trap") && !totalObj[i].getType().startsWith("warp")) {
                            animal_clk = true;
                            curObjNum = i;
                            break;
                        }
                    }
                break;

            case MotionEvent.ACTION_MOVE:
                return true;
            case MotionEvent.ACTION_UP:
                lastPoint.x=event.getX();
                lastPoint.y=event.getY();
                firstPoint = new PointF(spaceX * totalObj[curObjNum].getPosX()/2 + blankX, spaceY * totalObj[curObjNum].getPosY()/2 + blankY);
                double dx = lastPoint.x - firstPoint.x , dy = lastPoint.y - firstPoint.y;
                if (animal_clk && (Math.abs(dx) > spaceX/2 || Math.abs(dy) > spaceY/2)) {
                    setDirection(dx,dy);
                    Controller control = new Controller(mContext);
                    control.move();
                }
                animal_clk = false;
                return false;
        }
        return true;
    }

    public void setDirection(double dx, double dy) {
        if(-135 < Math.toDegrees(Math.atan2(dy,dx))  && Math.toDegrees(Math.atan2(dy,dx)) <= -45) //상 -135보다 크고 -45보다 작다
            direction = "up";
        else if(45 < Math.toDegrees(Math.atan2(dy,dx)) && Math.toDegrees(Math.atan2(dy,dx)) <=135 ) // 하 45보다 크고 135보단 작다.
            direction = "down";
        else if(135 < Math.toDegrees(Math.atan2(dy,dx)) && Math.toDegrees(Math.atan2(dy,dx)) <= 180 || -180 <= Math.toDegrees(Math.atan2(dy,dx)) && Math.toDegrees(Math.atan2(dy,dx)) < -135) //좌 135보단 크고 180보단 작으며 / -180보다 크고 -135보다 작다
            direction = "left";
        else if(-45 < Math.toDegrees(Math.atan2(dy,dx)) && Math.toDegrees(Math.atan2(dy,dx)) <= 45) //우 -45보다 크고 0보단 작으며 / 0보다 크며 45보단 작다
            direction = "right";
    }


    class ImageThread extends Thread {
        Bitmap animal_1, animal_2, animal_3, animal_4, animal_5, ground, wall_down, wall_right, finish_1, finish_2, finish_3, finish_4, finish_5, trap, Back, warp, carrot, bone;

        private ImageThread() {
            WindowManager manager = (WindowManager)
                    mContext.getSystemService((Context.WINDOW_SERVICE));
            Display display = manager.getDefaultDisplay();
            Point sizePoint = new Point();
            display.getSize(sizePoint);
            Width = sizePoint.x;
            Height = sizePoint.y;
            spaceX = (int) Width / 10;
            spaceY = (int) Height / 20;
            animal_1 = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.animal_1);
            animal_1 = Bitmap.createScaledBitmap(animal_1, (int) spaceX, (int) spaceY, true);
            animal_2 = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.animal_2);
            animal_2 = Bitmap.createScaledBitmap(animal_2, (int) spaceX, (int) spaceY, true);
            animal_3 = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.animal_3);
            animal_3 = Bitmap.createScaledBitmap(animal_3, (int) spaceX, (int) spaceY, true);
            animal_4 = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.animal_4);
            animal_4 = Bitmap.createScaledBitmap(animal_4, (int) spaceX, (int) spaceY, true);
            animal_5 = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.animal_5);
            animal_5 = Bitmap.createScaledBitmap(animal_5, (int) spaceX, (int) spaceY, true);
            ground = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.ground);
            ground = Bitmap.createScaledBitmap(ground, (int) spaceX, (int) spaceY, true);
            wall_right = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.wall);
            wall_right = Bitmap.createScaledBitmap(wall_right, (int) spaceX / 10, (int) spaceY, true);
            wall_down = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.wall);
            wall_down = Bitmap.createScaledBitmap(wall_down, (int) spaceX, (int) spaceY / 10, true);
            finish_1 = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.finish_1);
            finish_1 = Bitmap.createScaledBitmap(finish_1, (int) spaceX, (int) spaceY, true);
            finish_2 = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.finish_2);
            finish_2 = Bitmap.createScaledBitmap(finish_2, (int) spaceX, (int) spaceY, true);
            finish_3 = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.finish_3);
            finish_3 = Bitmap.createScaledBitmap(finish_3, (int) spaceX, (int) spaceY, true);
            finish_4 = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.finish_4);
            finish_4 = Bitmap.createScaledBitmap(finish_4, (int) spaceX, (int) spaceY, true);
            finish_5 = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.finish_5);
            finish_5 = Bitmap.createScaledBitmap(finish_5, (int) spaceX, (int) spaceY, true);
            trap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.trap);
            trap = Bitmap.createScaledBitmap(trap, (int) spaceX, (int) spaceY, true);
            Back = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.backimg);
            Back = Bitmap.createScaledBitmap(Back, (int) Width, (int) Height, true);
            warp = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.warp_1);
            warp = Bitmap.createScaledBitmap(warp, (int) spaceX, (int) spaceY, true);
            carrot = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.carrot);
            carrot = Bitmap.createScaledBitmap(carrot, (int) spaceX, (int) spaceY, true);
            bone = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.bone);
            bone = Bitmap.createScaledBitmap(bone, (int) spaceX, (int) spaceY, true);



        }

        private void doDraw() {
            switch (stageSize) {
                case 4:
                    blankX = spaceX * 2;
                    blankY = spaceY * 6;
                    break;
                case 6:
                    blankX = spaceX * 1;
                    blankY = spaceY * 5;
                    break;
                case 8:
                    blankX = spaceX * 0;
                    blankY = spaceY * 4;
                    break;
            }
                for (int i = 0; i < stageSize; i++)
                    for (int j = 0; j < stageSize; j++) {
                        mCanvas.drawBitmap(ground, (spaceX * (i+1)) + blankX, (spaceY * (j+1)) + blankY, null);
                    }
            //stage.get(0)[0] 종류 ()[1] x, ()[2] y, ()[3] 우측벽, ()[4] 아래벽


            for(int i=0; i<objCount; i++){
                if(totalObj[i].getType().equals("wall")){
                    if(totalObj[i].getPosX()%2==0) //아래쪽 벽
                        mCanvas.drawBitmap(wall_down, spaceX * (totalObj[i].getPosX()/2) + blankX, spaceY * ((int)totalObj[i].getPosY()/2 + 0.95f) + blankY , null);
                    else //오른쪽 벽
                        mCanvas.drawBitmap(wall_right, spaceX * ((int)totalObj[i].getPosX()/2+0.95f) + blankX, (spaceY * totalObj[i].getPosY()/2) + blankY , null);
                }
                if(totalObj[i].getType().equals("dog"))
                    mCanvas.drawBitmap(animal_1, spaceX * totalObj[i].getPosX()/2 + blankX, spaceY * totalObj[i].getPosY()/2 + blankY, null);
                if(totalObj[i].getType().equals("cat"))
                    mCanvas.drawBitmap(animal_2, spaceX * totalObj[i].getPosX()/2 + blankX, spaceY * totalObj[i].getPosY()/2 + blankY, null);
                if(totalObj[i].getType().equals("rabbit"))
                    mCanvas.drawBitmap(animal_3, spaceX * totalObj[i].getPosX()/2 + blankX, spaceY * totalObj[i].getPosY()/2 + blankY, null);
                if(totalObj[i].getType().equals("horse"))
                    mCanvas.drawBitmap(animal_4, spaceX * totalObj[i].getPosX()/2 + blankX, spaceY * totalObj[i].getPosY()/2 + blankY, null);
                if(totalObj[i].getType().equals("mouse"))
                    mCanvas.drawBitmap(animal_5, spaceX * totalObj[i].getPosX()/2 + blankX, spaceY * totalObj[i].getPosY()/2 + blankY, null);
                if(totalObj[i].getType().equals("dog_fin"))
                    mCanvas.drawBitmap(finish_1, spaceX * totalObj[i].getPosX()/2 + blankX, spaceY * totalObj[i].getPosY()/2 + blankY, null);
                if(totalObj[i].getType().equals("cat_fin"))
                    mCanvas.drawBitmap(finish_2, spaceX * totalObj[i].getPosX()/2 + blankX, spaceY * totalObj[i].getPosY()/2 + blankY, null);
                if(totalObj[i].getType().equals("rabbit_fin"))
                    mCanvas.drawBitmap(finish_3, spaceX * totalObj[i].getPosX()/2 + blankX, spaceY * totalObj[i].getPosY()/2 + blankY, null);
                if(totalObj[i].getType().equals("horse_fin"))
                    mCanvas.drawBitmap(finish_4, spaceX * totalObj[i].getPosX()/2 + blankX, spaceY * totalObj[i].getPosY()/2 + blankY, null);
                if(totalObj[i].getType().equals("mouse_fin"))
                    mCanvas.drawBitmap(animal_5, spaceX * totalObj[i].getPosX()/2 + blankX, spaceY * totalObj[i].getPosY()/2 + blankY, null);
                if(totalObj[i].getType().equals("trap"))
                    mCanvas.drawBitmap(trap, spaceX * totalObj[i].getPosX()/2 + blankX, spaceY * totalObj[i].getPosY()/2 + blankY, null);
                if(totalObj[i].getType().equals("food_carrot"))
                    mCanvas.drawBitmap(carrot, spaceX * totalObj[i].getPosX()/2 + blankX, spaceY * totalObj[i].getPosY()/2 + blankY, null);
                if(totalObj[i].getType().equals("food_bone"))
                    mCanvas.drawBitmap(bone, spaceX * totalObj[i].getPosX()/2 + blankX, spaceY * totalObj[i].getPosY()/2 + blankY, null);
            }
        }

        public void run() {
            while (true) {
                mCanvas = mHolder.lockCanvas();
                try {
                    synchronized (mHolder) {
                        mCanvas.drawBitmap(Back, 0, 0, null);
                        doDraw();
                    }
                } finally {
                    if (mCanvas == null) return;
                    mHolder.unlockCanvasAndPost(mCanvas);
                }
            }
        }
    }
}
