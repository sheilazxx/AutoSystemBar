package cn.albert.library;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created by albert on 2017/9/26.
 *
 */

interface StatusBarFontStyle {

    boolean statusBarFontStyle(Activity activity, boolean darkFont);

    class Base implements StatusBarFontStyle {

        @Override
        public boolean statusBarFontStyle(Activity activity, boolean darkFont) {
            return false;
        }
    }


    class Meizu implements StatusBarFontStyle{

        @Override
        public boolean statusBarFontStyle(Activity activity, boolean darkFont) {
            try {
                WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
                Field darkFlag = WindowManager.LayoutParams.class.getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
                Field meizuFlags = WindowManager.LayoutParams.class.getDeclaredField("meizuFlags");
                darkFlag.setAccessible(true);
                meizuFlags.setAccessible(true);
                int bit = darkFlag.getInt(null);
                int value = meizuFlags.getInt(lp);
                if (darkFont) {
                    value |= bit;
                } else {
                    value &= ~bit;
                }
                meizuFlags.setInt(lp, value);
                activity.getWindow().setAttributes(lp);
                return true;
            } catch (Exception ignored) {
            }
            return false;
        }
    }

    class MIUI implements StatusBarFontStyle{

        @Override
        public boolean statusBarFontStyle(Activity activity, boolean darkFont) {
            Window window = activity.getWindow();
            Class<?> clazz = window.getClass();
            try {
                Class layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
                Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
                int darkModeFlag = field.getInt(layoutParams);
                Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
                if (darkFont) {
                    extraFlagField.invoke(window, darkModeFlag, darkModeFlag);
                } else {
                    extraFlagField.invoke(window, 0, darkModeFlag);
                }
                return true;
            } catch (Exception ignored) {
            }
            return false;
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    class M extends Base {

        @Override
        public boolean statusBarFontStyle(Activity activity, boolean darkFont) {
            Window window = activity.getWindow();
            View decorView = window.getDecorView();
            if(darkFont){
                decorView.setSystemUiVisibility(decorView.getSystemUiVisibility() | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            }else {
                decorView.setSystemUiVisibility(decorView.getSystemUiVisibility() & ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            }
            return true;
        }
    }
}
