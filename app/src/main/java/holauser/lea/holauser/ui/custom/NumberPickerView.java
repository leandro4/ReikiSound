package holauser.lea.holauser.ui.custom;

import android.content.Context;
import android.os.Build;
import androidx.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import holauser.lea.holauser.R;
import holauser.lea.holauser.util.Animations;

/**
 * Created by leandro on 20/2/18.
 */

public class NumberPickerView extends RelativeLayout {

    @BindView(R.id.tvCount)
    TextView tvCount;

    private int maxValue = 60;
    private int minValue = 1;

    public NumberPickerView(Context context) {
        super(context);
        init(null);
    }

    public NumberPickerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public NumberPickerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public NumberPickerView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    public void init (AttributeSet attributeSet) {

        View v = LayoutInflater.from(getContext()).inflate(R.layout.view_number_picker, this);
        ButterKnife.bind(v);
    }

    @OnClick(R.id.ivLess)
    public void onLessClick(View v) {
        Animations.animateScale(v);

        int count = Integer.valueOf(tvCount.getText().toString());
        if (count == minValue)
            return;
        count--;
        tvCount.setText(String.valueOf(count));
    }

    @OnClick(R.id.ivMore)
    public void onMoreClick(View v) {
        Animations.animateScale(v);

        int count = Integer.valueOf(tvCount.getText().toString());
        if (count == maxValue)
            return;
        count++;
        tvCount.setText(String.valueOf(count));
    }

    public void setMaxValue(int maxValue) {
        this.maxValue = maxValue;
    }

    public void setMinValue(int minValue) {
        this.minValue = minValue;
    }

    public int getValue() {
        return Integer.valueOf(tvCount.getText().toString());
    }

    public void setValue(int time) {
        if (time < minValue || time > maxValue) return;
        tvCount.setText(String.valueOf(time));
    }
}
