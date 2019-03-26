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

import static gang.il.Valiable.stageSize;
import static gang.il.Valiable.totalObj;
import static gang.il.Valiable.direction;

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
                /*for (int i = 0; i < animals.length; i++)
                    if (!animals[i].getKind().equals("none") && (int)(firstPoint.x - blankX) / spaceX == animals[i].getX() && (int) (firstPoint.y - blankY) / spaceY == animals[i].getY()) { //빈칸이 아니고 동물이 있는 땅을 눌렀을 때
                        if (!animals[i].getKind().endsWith("fin") && !animals[i].getKind().equals("trap") && !animals[i].getKind().startsWith("warp")) {
                            animal_clk = true;
                            animal = i;
                        }
                        break;
                    }*/
                break;

            case MotionEvent.ACTION_MOVE:
                return true;
            case MotionEvent.ACTION_UP:
                lastPoint.x=event.getX();
                lastPoint.y=event.getY();
                if (animal_clk) {
                    getAngle(firstPoint,lastPoint);
                }
                animal_clk = false;
                return false;
        }
        return true;
    }

    public void getAngle(PointF start, PointF end) {
        double dy = end.y-start.y;
        double dx = end.x-start.x;
        direction="";
        if(315 <= Math.atan2(dy, dx) * (180.0 / Math.PI) || Math.atan2(dy, dx) * (180.0 / Math.PI) < 45 ) //우
            direction = "right";
        if(45 <=Math.atan2(dy, dx) * (180.0 / Math.PI) && Math.atan2(dy, dx) * (180.0 / Math.PI) < 135) // 하
            direction = "down";
        if(135 <=Math.atan2(dy, dx) * (180.0 / Math.PI) && Math.atan2(dy, dx) * (180.0 / Math.PI) < 225) //좌
            direction = "right";
        if(225 <=Math.atan2(dy, dx) * (180.0 / Math.PI) && Math.atan2(dy, dx) * (180.0 / Math.PI) < 315) //상
            direction = "up";
    }


    class ImageThread extends Thread {
        Bitmap animal_1, animal_2, animal_3, animal_4, animal_5, ground, wall_down, wall_right, finish_1, finish_2, finish_3, finish_4, finish_5, trap, Back, warp;

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


            for(int i=0; i<totalObj.length; i++){
                if(totalObj[i].getType().equals("block")){
                    if(totalObj[i].getPosX()%2==0) //아래쪽 벽
                        mCanvas.drawBitmap(wall_down, spaceX * (totalObj[i].getPosX()/2) + blankX, spaceY * ((int)totalObj[i].getPosY()/2 + 0.95f) + blankY , null);
                    else //오른쪽 벽
                        mCanvas.drawBitmap(wall_right, spaceX * ((int)totalObj[i].getPosX()/2+0.95f) + blankX, (spaceY * totalObj[i].getPosY()/2) + blankY , null);
                }
                if(totalObj[i].getType().equals("dog"))
                    mCanvas.drawBitmap(animal_1, spaceX * totalObj[i].getPosX()/2 + blankX, spaceY * totalObj[i].getPosY()/2 + blankY, null);
            }





            /*for (int i = 0; i < animals.length; i++) {
                if (animals[i].getBlock().equals("right")) //오른쪽 벽
                    mCanvas.drawBitmap(wall_right, (spaceX * (animals[i].getX() + 0.9f)) + blankX, (spaceY * animals[i].getY()) + blankY, null);
                if (animals[i].getBlock().equals("down")) //아래쪽 벽
                    mCanvas.drawBitmap(wall_down, (spaceX * animals[i].getX()) + blankX, (spaceY * (animals[i].getY() + 0.9f)) + blankY, null);
                if (animals[i].getBlock().equals("twice")) {
                    mCanvas.drawBitmap(wall_right, (spaceX * (animals[i].getX() + 0.9f)) + blankX, (spaceY * animals[i].getY()) + blankY, null);
                    mCanvas.drawBitmap(wall_down, (spaceX * animals[i].getX()) + blankX, (spaceY * (animals[i].getY() + 0.9f)) + blankY, null);
                }
                if (animals[i].getKind().equals("dog"))
                    mCanvas.drawBitmap(animal_1, (spaceX * animals[i].getX()) + blankX, (spaceY * animals[i].getY()) + blankY, null);
                if (animals[i].getKind().equals("cat"))
                    mCanvas.drawBitmap(animal_2, (spaceX * animals[i].getX()) + blankX, (spaceY * animals[i].getY()) + blankY, null);
                if (animals[i].getKind().equals("rabbit"))
                    mCanvas.drawBitmap(animal_3, (spaceX * animals[i].getX()) + blankX, (spaceY * animals[i].getY()) + blankY, null);
                if (animals[i].getKind().equals("cow"))
                    mCanvas.drawBitmap(animal_4, (spaceX * animals[i].getX()) + blankX, (spaceY * animals[i].getY()) + blankY, null);
                if (animals[i].getKind().equals("lion"))
                    mCanvas.drawBitmap(animal_5, (spaceX * animals[i].getX()) + blankX, (spaceY * animals[i].getY()) + blankY, null);
                if (animals[i].getKind().equals("dog_fin"))
                    mCanvas.drawBitmap(finish_1, (spaceX * animals[i].getX()) + blankX, (spaceY * animals[i].getY()) + blankY, null);
                if (animals[i].getKind().equals("cat_fin"))
                    mCanvas.drawBitmap(finish_2, (spaceX * animals[i].getX()) + blankX, (spaceY * animals[i].getY()) + blankY, null);
                if (animals[i].getKind().equals("rabbit_fin"))
                    mCanvas.drawBitmap(finish_3, (spaceX * animals[i].getX()) + blankX, (spaceY * animals[i].getY()) + blankY, null);
                if (animals[i].getKind().equals("cow_fin"))
                    mCanvas.drawBitmap(finish_4, (spaceX * animals[i].getX()) + blankX, (spaceY * animals[i].getY()) + blankY, null);
                if (animals[i].getKind().equals("lion_fin"))
                    mCanvas.drawBitmap(finish_5, (spaceX * animals[i].getX()) + blankX, (spaceY * animals[i].getY()) + blankY, null);
                if (animals[i].getKind().equals("tarp"))
                    mCanvas.drawBitmap(trap, (spaceX * animals[i].getX()) + blankX, (spaceY * animals[i].getY()) + blankY, null);
                if (animals[i].getKind().equals("warp"))
                    mCanvas.drawBitmap(warp, (spaceX * animals[i].getX()) + blankX, (spaceY * animals[i].getY()) + blankY, null);
                if (animals[i].getKind().equals("warp_"))
                    mCanvas.drawBitmap(warp, (spaceX * animals[i].getX()) + blankX, (spaceY * animals[i].getY()) + blankY, null);
            }*/
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
