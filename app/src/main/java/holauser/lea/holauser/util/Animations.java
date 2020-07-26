package holauser.lea.holauser.util;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import java.util.ArrayList;
import java.util.List;

import static android.view.View.INVISIBLE;

public class Animations {

    public static void animateScale(final View view) {
        AnimatorSet animationSet = new AnimatorSet();
        animationSet.setInterpolator(new AccelerateDecelerateInterpolator());

        view.setLayerType(View.LAYER_TYPE_HARDWARE, null);

        animationSet.playSequentially(scaleAnimationList(view));
        animationSet.setDuration(100);
        animationSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                view.setLayerType(View.LAYER_TYPE_NONE, null);
            }
        });

        animationSet.start();
    }

    private static List<Animator> scaleAnimationList(View view) {
        List<Animator> animatorList = new ArrayList<>();

        final PropertyValuesHolder scaleX = PropertyValuesHolder.ofFloat(View.SCALE_X, 1.2f);
        final PropertyValuesHolder scaleY = PropertyValuesHolder.ofFloat(View.SCALE_Y, 1.2f);
        animatorList.add(ObjectAnimator.ofPropertyValuesHolder(view, scaleX, scaleY));

        final PropertyValuesHolder scaleXNormal = PropertyValuesHolder.ofFloat(View.SCALE_X, 1);
        final PropertyValuesHolder scaleYNormal = PropertyValuesHolder.ofFloat(View.SCALE_Y, 1);
        animatorList.add(ObjectAnimator.ofPropertyValuesHolder(view, scaleXNormal, scaleYNormal));

        return animatorList;
    }
}
