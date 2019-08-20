package gang.il;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import static gang.il.Valiable.gameMode;
import static gang.il.Valiable.min_count_ser;
import static gang.il.Valiable.stageCount;

public class StageList extends BaseAdapter {
    int succeedStage;


    public StageList() {
    }

    @Override
    public int getCount() {
        return 50;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = new ViewHolder();
        final Context context = parent.getContext();
        StageDBHelper StageDB = new StageDBHelper(context);
        StageDB.selectDB();
        succeedStage = StageDB.getClearedStage();
        convertView = null;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.stage_list, parent, false);
            for (int i = 0; i < 3; i++) {
                int textViewId = context.getResources().getIdentifier("num_" + (i + 1), "id", "gang.il");
                int layoutId = context.getResources().getIdentifier("stage_" + (i + 1), "id", "gang.il");
                int imageViewId = context.getResources().getIdentifier("lock_" + (i + 1), "id", "gang.il");
                int min_count_area_id = context.getResources().getIdentifier("min_lay_" + (i + 1), "id", "gang.il");
                int my_min_count_id = context.getResources().getIdentifier("my_min_" + (i + 1), "id", "gang.il");
                int min_count_id = context.getResources().getIdentifier("min_" + (i + 1), "id", "gang.il");
                viewHolder.stage_num_text[i] = (TextView) convertView.findViewById(textViewId);
                viewHolder.stage_area[i] = (LinearLayout) convertView.findViewById(layoutId);
                viewHolder.stage_lock_img[i] = (ImageView) convertView.findViewById(imageViewId);
                viewHolder.min_count_area[i] = (LinearLayout) convertView.findViewById(min_count_area_id);
                viewHolder.my_min_count[i] = (TextView) convertView.findViewById(my_min_count_id);
                viewHolder.min_count[i] = (TextView) convertView.findViewById(min_count_id);

            }
        }


        for (int i = 0; i < 3; i++) {
            viewHolder.stage_num_text[i].setText("" + (position * 3 + 1 + i));
            viewHolder.stage_area[i].setTag("" + (position * 3 + 1 + i));
            if (Integer.parseInt(viewHolder.stage_area[i].getTag().toString()) - 1 <= succeedStage) {
                viewHolder.stage_lock_img[i].setVisibility(View.GONE);
                viewHolder.stage_area[i].setBackgroundColor(Color.parseColor("#F4DFAC"));
                StageDB.selectDB();
                viewHolder.min_count_area[i].setVisibility(View.VISIBLE);
                viewHolder.min_count[i].setText("/"+min_count_ser.get(position*3+i));
                if (succeedStage >= (position * 3 + 1 + i)) {
                    viewHolder.my_min_count[i].setText(String.valueOf(StageDB.getMinimumCount(i)));
                }
            }
        }

        for (int i = 0; i < 3; i++)
            viewHolder.stage_area[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    stageCount = v.getTag().toString();
                    if (Integer.parseInt(stageCount) - 1 > succeedStage)
                        return;
                    LoadDB.GetDB Data = new LoadDB.GetDB();
                    Data.execute("http://106.10.57.117/EscapeFarm/getStage.php", stageCount, "game_start_min");  //스테이지DB 로딩
                }
            });

        return convertView;
    }

    private class ViewHolder {
        private TextView[] stage_num_text = new TextView[3];
        private LinearLayout[] stage_area = new LinearLayout[3];
        private ImageView[] stage_lock_img = new ImageView[3];
        private LinearLayout[] min_count_area = new LinearLayout[3];
        private TextView[] my_min_count = new TextView[3];
        private TextView[] min_count = new TextView[3];
    }

}