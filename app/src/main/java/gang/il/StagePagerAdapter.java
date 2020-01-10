package gang.il;

import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import static gang.il.Valiable.StagePage;
import static gang.il.Valiable.main_btnSound;
import static gang.il.Valiable.soundPool;
import static gang.il.Valiable.stageCount;

public class StagePagerAdapter extends PagerAdapter {
    int succeedStage;
    // LayoutInflater 서비스 사용을 위한 Context 참조 저장.
    Context stagePagerContext = null ;
    StageDBHelper StageDB;
    private long mLastClickTime = 0;

    public StagePagerAdapter() {

    }

    // Context를 전달받아 mContext에 저장하는 생성자 추가.
    public StagePagerAdapter(Context context) {
        stagePagerContext = context ;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = null ;
        StagePagerAdapter.ViewHolder viewHolder = new StagePagerAdapter.ViewHolder();
        StageDB = new StageDBHelper(stagePagerContext);
        succeedStage = StageDB.clearStageNum();

        if (stagePagerContext != null) {
            // LayoutInflater를 통해 "/res/layout/page.xml"을 뷰로 생성.
            LayoutInflater inflater = (LayoutInflater) stagePagerContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.stage_list, container, false);

            for (int i = 0; i < 20; i++) {
                int stageCountId = stagePagerContext.getResources().getIdentifier("num_" + (i+1), "id", "gang.il");
                int minCountId = stagePagerContext.getResources().getIdentifier("min_" + (i+1), "id", "gang.il");
                int buttonImgId = stagePagerContext.getResources().getIdentifier("btn_" + (i+1), "id", "gang.il");
                viewHolder.stageCount[i] = (TextView) view.findViewById(stageCountId);
                viewHolder.minCount[i] = (TextView) view.findViewById(minCountId);
                viewHolder.buttonImg[i] = (ImageView) view.findViewById(buttonImgId);
            }
            viewHolder.pageCount = (TextView) view.findViewById(R.id.page_num);
        }
        viewHolder.pageCount.setText((position+1)+"/3");

            for(int i=0; i<20; i++){
                viewHolder.stageCount[i].setText("" + (position * 20 + 1 + i));
                viewHolder.buttonImg[i].setTag("" + (position * 20 + 1 + i));
                if(position*20+1+i>60) break; //마지막 스테이지 이후 막기
                if (Integer.parseInt(viewHolder.buttonImg[i].getTag().toString()) - 1 <= succeedStage) {
                    viewHolder.buttonImg[i].setImageDrawable(stagePagerContext.getResources().getDrawable(R.drawable.button));
                    viewHolder.stageCount[i].setVisibility(View.VISIBLE);
                    viewHolder.minCount[i].setVisibility(View.VISIBLE);
                    viewHolder.minCount[i].setText("0/"+StageDB.getMinCount(position*20+1+i));
                    if (succeedStage >= (position * 20 + 1 + i)) {
                        viewHolder.minCount[i].setText(StageDB.getMyMinCount(position*20+i+1) + "/" + StageDB.getMinCount(position*20+i+1));
                    }
                }
            }
        for (int i = 0; i < 20; i++)
            viewHolder.buttonImg[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (SystemClock.elapsedRealtime() - mLastClickTime < 100){
                        return;
                    }
                    mLastClickTime = SystemClock.elapsedRealtime();
                    stageCount = v.getTag().toString();
                    if (Integer.parseInt(stageCount) - 1 > succeedStage)
                        return;
                    StageDB.getStageObj();
                    soundPool.play(main_btnSound, 1f, 1f, 0, 0, 1f); //버튼 사운드 재생
                    Intent intent = new Intent(StagePage, GamePage.class);
                    StagePage.startActivity(intent);
                }
            });

        // 뷰페이저에 추가.
        container.addView(view) ;

        return view ;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        // 뷰페이저에서 삭제.
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        // 전체 페이지 수는 3개로 고정.
        return 3;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return (view == (View)object);
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
        //return super.getItemPosition(object);
    }

    private class ViewHolder {
        private TextView[] minCount = new TextView[20];
        private TextView[] stageCount = new TextView[20];
        private ImageView[] buttonImg = new ImageView[20];
        private TextView pageCount;

    }
}