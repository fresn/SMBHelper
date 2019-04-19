package net.yimingma.smbhelper.util;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.EditText;

public class Utils {
    static public final String[] months={"Jan.","Feb.","Mar.","Apr.","May.","Jun.","Jul.","Aug.","Sept.","Oct.","Nov.","Dec."};
    static public void startShakeByView(View view, float scaleSmall, float scaleLarge, float shakeDegrees, long duration) {
        //由小变大
        Animation scaleAnim = new ScaleAnimation(scaleSmall, scaleLarge, scaleSmall, scaleLarge);
        //从左向右
        Animation rotateAnim = new RotateAnimation(-shakeDegrees, shakeDegrees, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);

        scaleAnim.setDuration(duration);
        rotateAnim.setDuration(duration / 10);
        rotateAnim.setRepeatMode(Animation.REVERSE);
        rotateAnim.setRepeatCount(10);

        AnimationSet smallAnimationSet = new AnimationSet(false);
        smallAnimationSet.addAnimation(scaleAnim);
        smallAnimationSet.addAnimation(rotateAnim);

        view.startAnimation(smallAnimationSet);

    }

    static public void startShakeByView(View view){
        startShakeByView(view,0.8f,1,0,50);
    }

    static public void rollbackEditTextChangAndShake(EditText view, String beforeChanged){
        Utils.startShakeByView(view);
        view.setText(beforeChanged);
        view.setSelection(beforeChanged.length() );

    }


}
