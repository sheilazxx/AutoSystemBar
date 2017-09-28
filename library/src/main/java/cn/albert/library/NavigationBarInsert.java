package cn.albert.library;

import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by albert on 2017/9/27.
 *
 */

interface NavigationBarInsert {

    boolean expandLayoutToNavigationBar(Activity activity);


    class Base implements NavigationBarInsert{

        @Override
        public boolean expandLayoutToNavigationBar(Activity activity) {
            return false;
        }
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    class Kitkat extends Base{

        @Override
        public boolean expandLayoutToNavigationBar(Activity activity) {
            Window window = activity.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            return true;
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    class Lollipop extends Kitkat {

        @Override
        public boolean expandLayoutToNavigationBar(Activity activity) {
            Window window = activity.getWindow();
            View decorView = window.getDecorView();
            decorView.setSystemUiVisibility(decorView.getSystemUiVisibility() | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setNavigationBarColor(Color.TRANSPARENT);
            return true;
        }
    }
}
