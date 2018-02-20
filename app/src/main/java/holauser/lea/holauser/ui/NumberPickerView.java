package holauser.lea.holauser.ui;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import holauser.lea.holauser.R;

/**
 * Created by leandro on 20/2/18.
 */

public class NumberPickerView extends RelativeLayout {

    @BindView(R.id.tvCount)
    TextView tvCount;

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
        int count = Integer.valueOf(tvCount.getText().toString());
        if (count < 2)
            return;
        count--;
        tvCount.setText(String.valueOf(count));
    }

    @OnClick(R.id.ivMore)
    public void onMoreClick(View v) {
        int count = Integer.valueOf(tvCount.getText().toString());
        if (count > 59)
            return;
        count++;
        tvCount.setText(String.valueOf(count));
    }

    public int getValue() {
        return Integer.valueOf(tvCount.getText().toString());
    }
}
