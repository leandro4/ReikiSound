package holauser.lea.holauser.ui.custom;

import android.content.Context;
import android.os.Build;
import androidx.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import holauser.lea.holauser.ReikiSound;
import holauser.lea.holauser.R;
import holauser.lea.holauser.util.Animations;
import holauser.lea.holauser.util.DataManager;

/**
 * Created by leandro on 20/2/18.
 */

public class SoundSelectorView extends RelativeLayout {

    private int soundSelected = 0;

    public SoundSelectorView(Context context) {
        super(context);
        init(null);
    }

    public SoundSelectorView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public SoundSelectorView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public SoundSelectorView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    public void init(AttributeSet attributeSet) {
        View v = LayoutInflater.from(getContext()).inflate(R.layout.view_sound_selector, this);
        v.findViewById(R.id.ivCuenco).setOnClickListener(this::onCuencoClick);
        v.findViewById(R.id.ivKoshi).setOnClickListener(this::onKoshiClick);
        v.findViewById(R.id.ivTriangle).setOnClickListener(this::onTriangleClick);
    }

    private void onCuencoClick(View v) {
        DataManager.INSTANCE.setBell(getContext(), R.raw.cuenco);
        findViewById(R.id.ivCuenco).setSelected(true);
        findViewById(R.id.ivKoshi).setSelected(false);
        findViewById(R.id.ivTriangle).setSelected(false);
    }

    private void onKoshiClick(View v) {
        DataManager.INSTANCE.setBell(getContext(), R.raw.koshi);
        findViewById(R.id.ivCuenco).setSelected(false);
        findViewById(R.id.ivKoshi).setSelected(true);
        findViewById(R.id.ivTriangle).setSelected(false);
    }

    private void onTriangleClick(View v) {
        DataManager.INSTANCE.setBell(getContext(), R.raw.triangle);
        findViewById(R.id.ivCuenco).setSelected(false);
        findViewById(R.id.ivKoshi).setSelected(false);
        findViewById(R.id.ivTriangle).setSelected(true);
    }
}
