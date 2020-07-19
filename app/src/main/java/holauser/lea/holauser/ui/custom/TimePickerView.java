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
    private int maxValue = 60;
    private int minValue = 1;
    private int value = 3;

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
        count--;
        value = count;
        updateTimeText();
    }

    public void onMoreClick(View v) {
        Animations.animateScale(v);

        int count = value;
        if (count == maxValue)
            return;
        count++;
        value = count;
        updateTimeText();
    }

    public void setMaxValue(int maxValue) {
        this.maxValue = maxValue;
    }

    public void setMinValue(int minValue) {
        this.minValue = minValue;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int time) {
        if (time < minValue || time > maxValue) return;
        value = time;
        updateTimeText();
    }

    private void updateTimeText() {
        tvCount.setText(String.format(Locale.getDefault(), "%d' min", value));
    }
}
