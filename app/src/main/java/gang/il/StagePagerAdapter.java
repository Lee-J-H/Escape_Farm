package gang.il;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import static gang.il.LoadingImg.progressON;
import static gang.il.Valiable.StagePage;
import static gang.il.Valiable.gameMode;
import static gang.il.Valiable.min_count_ser;
import static gang.il.Valiable.stageCount;

public class StagePagerAdapter extends PagerAdapter {
    int succeedStage;
    // LayoutInflater 서비스 사용을 위한 Context 참조 저장.
    private Context mContext = null ;

    public StagePagerAdapter() {

    }

    // Context를 전달받아 mContext에 저장하는 생성자 추가.
    public StagePagerAdapter(Context context) {
        mContext = context ;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = null ;
        StagePagerAdapter.ViewHolder viewHolder = new StagePagerAdapter.ViewHolder();
        StageDBHelper StageDB = new StageDBHelper(mContext);
        StageDB.selectDB();
        succeedStage = StageDB.getClearedStage();

        if (mContext != null) {
            // LayoutInflater를 통해 "/res/layout/page.xml"을 뷰로 생성.
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.stage_list, container, false);

            for (int i = 0; i < 20; i++) {
                int stageCountId = mContext.getResources().getIdentifier("num_" + (i+1), "id", "gang.il");
                int minCountId = mContext.getResources().getIdentifier("min_" + (i+1), "id", "gang.il");
                int buttonImgId = mContext.getResources().getIdentifier("btn_" + (i+1), "id", "gang.il");
                viewHolder.stageCount[i] = (TextView) view.findViewById(stageCountId);
                viewHolder.minCount[i] = (TextView) view.findViewById(minCountId);
                viewHolder.buttonImg[i] = (ImageView) view.findViewById(buttonImgId);
            }
            viewHolder.pageCount = (TextView) view.findViewById(R.id.page_num);
        }
        viewHolder.pageCount.setText((position+1)+"/5");

            for(int i=0; i<20; i++){
                viewHolder.stageCount[i].setText("" + (position * 20 + 1 + i));
                viewHolder.buttonImg[i].setTag("" + (position * 20 + 1 + i));
                if (Integer.parseInt(viewHolder.buttonImg[i].getTag().toString()) - 1 <= succeedStage) {
                    viewHolder.buttonImg[i].setImageDrawable(mContext.getResources().getDrawable(R.drawable.button));
                    StageDB.selectDB();
                    viewHolder.stageCount[i].setVisibility(View.VISIBLE);
                    viewHolder.minCount[i].setVisibility(View.VISIBLE);
                    viewHolder.minCount[i].setText("0/"+min_count_ser.get(position*20+i));
                    if (succeedStage >= (position * 20 + 1 + i)) {
                        viewHolder.minCount[i].setText(String.valueOf(StageDB.getMinimumCount(i)) + "/" + min_count_ser.get(position*20+i));
                    }
                }
            }
            for (int i = 0; i < 20; i++)
                viewHolder.buttonImg[i].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        stageCount = v.getTag().toString();
                        if (Integer.parseInt(stageCount) - 1 > succeedStage)
                            return;
                        progressON(StagePage, null);
                        LoadDB.GetDB Data = new LoadDB.GetDB();
                        Data.execute("http://106.10.57.117/EscapeFarm/getStage.php", stageCount, "game_start_min", gameMode);  //스테이지DB 로딩
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
        // 전체 페이지 수는 10개로 고정.
        return 5;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return (view == (View)object);
    }

    private class ViewHolder {
        private TextView[] minCount = new TextView[20];
        private TextView[] stageCount = new TextView[20];
        private ImageView[] buttonImg = new ImageView[20];
        private TextView pageCount;

    }
}