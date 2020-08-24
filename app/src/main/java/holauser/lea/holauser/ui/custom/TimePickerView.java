package holauser.lea.holauser.ui.custom;

import android.content.Context;
import android.os.Build;
import androidx.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Locale;

import holauser.lea.holauser.R;
import holauser.lea.holauser.util.Animations;

/**
 * Created by leandro on 20/2/18.
 */

public class TimePickerView extends RelativeLayout {

    TextView tvCount;
    private int minValue = 15;
    private int value = 180;

    public TimePickerView(Context context) {
        super(context);
        init(null);
    }

    public TimePickerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public TimePickerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public TimePickerView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    public void init (AttributeSet attributeSet) {
        View v = LayoutInflater.from(getContext()).inflate(R.layout.view_number_picker, this);
        tvCount = v.findViewById(R.id.tvCount);
        v.findViewById(R.id.ivLess).setOnClickListener(this::onLessClick);
        v.findViewById(R.id.ivMore).setOnClickListener(this::onMoreClick);
    }

    public void onLessClick(View v) {
        Animations.animateScale(v);

        int count = value;
        if (count == minValue)
            return;
        count -= 15;
        value = count;
        updateTimeText();
    }

    public void onMoreClick(View v) {
        Animations.animateScale(v);

        int count = value;
        count += 15;
        value = count;
        updateTimeText();
    }

    public int getValue() {
        return value;
    }

    public void setValue(int time) {
        if (time < minValue) return;
        value = time;
        updateTimeText();
    }

    private void updateTimeText() {
        int min = value / 60;
        int seg = value % 60;
        tvCount.setText(String.format(Locale.getDefault(), "%d' %d\"", min, seg));
    }
}
