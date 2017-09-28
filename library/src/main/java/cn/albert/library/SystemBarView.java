package cn.albert.library;

import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by albert on 2017/9/26.
 */

class SystemBarView extends View{

    public SystemBarView(Context context) {
        super(context);
    }

    public SystemBarView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }else {
            setMeasuredDimension(0, 0);
            setVisibility(GONE);
        }
    }
}
