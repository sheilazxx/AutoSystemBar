package cn.albert.library;

import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by albert on 2017/9/26.
 *
 */

interface StatusBarInsert {

    boolean expandLayoutToStatusBar(Activity activity);

    class Base implements StatusBarInsert {

        @Override
        public boolean expandLayoutToStatusBar(Activity activity) {
            return false;
        }
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    class Kitkat extends Base{

        @Override
        public boolean expandLayoutToStatusBar(Activity activity) {
            Window window = activity.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            return true;
        }
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    class EMUI3_1 extends Kitkat{

    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    class Lollipop extends Kitkat{

        @Override
        public boolean expandLayoutToStatusBar(Activity activity) {
            Window window = activity.getWindow();
            View decorView = window.getDecorView();
            decorView.setSystemUiVisibility(decorView.getSystemUiVisibility()
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
            return true;
        }
    }
}
