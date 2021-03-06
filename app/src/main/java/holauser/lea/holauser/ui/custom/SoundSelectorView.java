package holauser.lea.holauser.ui.custom;

import android.animation.Animator;
import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import holauser.lea.holauser.GlobalVars;
import holauser.lea.holauser.R;
import holauser.lea.holauser.util.Animations;

/**
 * Created by leandro on 20/2/18.
 */

public class SoundSelectorView extends RelativeLayout {

    @BindView(R.id.ivSelected)
    ImageView ivSelected;

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
        ButterKnife.bind(v);

    }

    @OnClick(R.id.ivBack)
    public void onBackClick(View v) {
        Animations.animateScale(v);

        final GlobalVars globalVariable = (GlobalVars) getContext().getApplicationContext();

        switch (soundSelected) {
            case 0:
                break;
            case 1:
                globalVariable.setSonido(R.raw.cuenco);
                soundSelected = 0;
                animateImageSelected(R.drawable.cuenco, true);
                break;
            case 2:
                globalVariable.setSonido(R.raw.triangle);
                soundSelected = 1;
                animateImageSelected(R.drawable.triangle, true);
                break;
            default: break;
        }
    }

    @OnClick(R.id.ivNext)
    public void onNextClick(View v) {
        Animations.animateScale(v);

        final GlobalVars globalVariable = (GlobalVars) getContext().getApplicationContext();

        switch (soundSelected) {
            case 0:
                soundSelected = 1;
                globalVariable.setSonido(R.raw.triangle);
                animateImageSelected(R.drawable.triangle, false);
                break;
            case 1:
                soundSelected = 2;
                globalVariable.setSonido(R.raw.koshi);
                animateImageSelected(R.drawable.koshi, false);
                break;
            case 2:
                break;
            default: break;
        }
    }

    private void animateImageSelected(final int image, final boolean leftPressed) {
        YoYo.with(leftPressed ? Techniques.SlideOutRight : Techniques.SlideOutLeft).duration(100).onEnd(new YoYo.AnimatorCallback() {
            @Override
            public void call(Animator animator) {
                ivSelected.setImageResource(image);
                YoYo.with(leftPressed ? Techniques.SlideInLeft : Techniques.SlideInRight).duration(100).playOn(ivSelected);
            }
        }).playOn(ivSelected);
    }
}
