package cn.albert.autosystembar;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.IntDef;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Handler;

/**
 * Created by albert on 2017/9/27.
 *
 */

@SuppressWarnings("unused")
public class SystemBarHelper {

    private static final String TAG = "SystemBarHelper";
    private Builder mBuilder;

    public static final int STATUS_BAR_DARK_FONT_STYLE = 2;
    public static final int STATUS_BAR_LIGHT_FONT_STYLE = 1;
    @IntDef({STATUS_BAR_DARK_FONT_STYLE, STATUS_BAR_LIGHT_FONT_STYLE})
    public @interface StatusBarFontStyle {}

    private static final List<IStatusBar> STATUS_BARS = new ArrayList<>();

    private static final List<INavigationBar> NAVIGATION_BARS = new ArrayList<>();

    private static final List<IStatusBarFontStyle> STATUS_BAR_FONT_STYLES = new ArrayList<>();

    static {
        STATUS_BARS.add(new IStatusBar.EMUI3_1());
        STATUS_BARS.add(new IStatusBar.Lollipop());
        STATUS_BARS.add(new IStatusBar.Kitkat());
        STATUS_BARS.add(new IStatusBar.Base());
        Collections.unmodifiableCollection(STATUS_BARS);

        NAVIGATION_BARS.add(new INavigationBar.Lollipop());
        NAVIGATION_BARS.add(new INavigationBar.Kitkat());
        NAVIGATION_BARS.add(new INavigationBar.Base());
        Collections.unmodifiableCollection(NAVIGATION_BARS);

        STATUS_BAR_FONT_STYLES.add(new IStatusBarFontStyle.Meizu());
        STATUS_BAR_FONT_STYLES.add(new IStatusBarFontStyle.MIUI());
        STATUS_BAR_FONT_STYLES.add(new IStatusBarFontStyle.M());
        STATUS_BAR_FONT_STYLES.add(new IStatusBarFontStyle.Base());
        Collections.unmodifiableCollection(STATUS_BAR_FONT_STYLES);
    }

    private SystemBarHelper(Builder builder){
        mBuilder = builder;
    }

    public void setStatusBarColor(@ColorInt int statusBarColor) {
        final InternalLayout internalLayout = mBuilder.mInternalLayout;
        if(internalLayout != null){
            internalLayout.setStatusBarColor(statusBarColor);
        }
    }

    public void setStatusBarDrawable(Drawable drawable){
        final InternalLayout internalLayout = mBuilder.mInternalLayout;
        if(internalLayout != null){
            internalLayout.setStatusBarDrawable(drawable);
        }
    }

    public void enableImmersedStatusBar(boolean immersed){
        final InternalLayout internalLayout = mBuilder.mInternalLayout;
        if(internalLayout != null){
            internalLayout.enableImmersedStatusBar(immersed);
        }
    }

    public void enableImmersedNavigationBar(boolean immersed){
        final InternalLayout internalLayout = mBuilder.mInternalLayout;
        if(internalLayout != null){
            internalLayout.enableImmersedNavigationBar(immersed);
        }
    }

    public void setNavigationBarDrawable(Drawable drawable){
        final InternalLayout internalLayout = mBuilder.mInternalLayout;
        if(internalLayout != null){
            internalLayout.setNavigationDrawable(drawable);
        }
    }

    public void setNavigationBarColor(@ColorInt int navigationBarColor) {
        final InternalLayout internalLayout = mBuilder.mInternalLayout;
        if(internalLayout != null){
            internalLayout.setNavigationBarColor(navigationBarColor);
        }
    }

    public void statusBarFontStyle(@StatusBarFontStyle int statusBarFontStyle) {
        boolean isDarkFont = statusBarFontStyle == STATUS_BAR_DARK_FONT_STYLE;
        for (IStatusBarFontStyle style: STATUS_BAR_FONT_STYLES){
            if(style.verify()){
                style.statusBarFontStyle(mBuilder.mActivity, isDarkFont);
                break;
            }
        }
    }

    public static class Builder{

        @ColorInt
        private int mStatusBarColor;
        @StatusBarFontStyle private int mStatusBarFontStyle = STATUS_BAR_LIGHT_FONT_STYLE;
        @ColorInt private int mNavigationBarColor;
        private Drawable mStatusBarDrawable;
        private Drawable mNavigationBarDrawable;
        private boolean mIsSetStatusBarColor;
        private boolean mIsSetNavigationBarColor;
        private InternalLayout mInternalLayout;
        private boolean mIsImmersedStatusBar;
        private boolean mIsImmersedNavigationBar;

        private Activity mActivity;
        private boolean mIsAuto = true;

        private static final int PADDING = 10;

        private static final int ACTION_BAR_DEFAULT_HEIGHT = 48; // dp

        public Builder enableAutoSystemBar(boolean isAuto){
            mIsAuto = isAuto;
            return this;
        }

        public Builder statusBarDrawable(Drawable drawable){
            mStatusBarDrawable = drawable;
            return this;
        }

        public Builder statusBarColor(@ColorInt int statusBarColor){
            mIsSetStatusBarColor = true;
            mStatusBarColor = statusBarColor;
            return this;
        }

        public Builder enableImmersedStatusBar(boolean enable){
            mIsImmersedStatusBar = enable;
            return this;
        }

        public Builder enableImmersedNavigationBar(boolean enable){
            mIsImmersedNavigationBar = enable;
            return this;
        }

        public Builder navigationBarColor(@ColorInt int navigationBarColor){
            mIsSetNavigationBarColor = true;
            mNavigationBarColor = navigationBarColor;
            return this;
        }

        public Builder navigationBarDrawable(Drawable drawable){
            mNavigationBarDrawable = drawable;
            return this;
        }

        public Builder statusBarFontStyle(@StatusBarFontStyle int style){
            mStatusBarFontStyle = style;
            return this;
        }

        public SystemBarHelper into(Activity activity){
            mActivity = activity;
            SystemBarHelper systemBarHelper = new SystemBarHelper(this);
            boolean isExpandedLayout2StatusBar = false;
            for (IStatusBar statusBar: STATUS_BARS){
                if(statusBar.verify()){
                    isExpandedLayout2StatusBar = statusBar.expandLayoutToStatusBar(activity);
                    break;
                }
            }

            boolean isExpandedLayout2NavigationBar = false;
            if(mIsSetNavigationBarColor || (mNavigationBarDrawable != null) ){
                for (INavigationBar navigationBar: NAVIGATION_BARS){
                    if(navigationBar.verify()){
                        isExpandedLayout2NavigationBar = navigationBar.expandLayoutToNavigationBar(activity);
                        break;
                    }
                }
            }
            initialize(activity, systemBarHelper, isExpandedLayout2StatusBar, isExpandedLayout2NavigationBar);
            return systemBarHelper;
        }

        private void initialize(final Activity activity, final SystemBarHelper helper,
                                boolean isExpandedLayout2StatusBar, final boolean isExpandedLayout2NavigationBar) {
            if(!isExpandedLayout2StatusBar && !isExpandedLayout2NavigationBar){
                return;
            }

            Window window = activity.getWindow();
            final View decorView = window.getDecorView();
            final View androidContent = window.getDecorView().findViewById(Window.ID_ANDROID_CONTENT);
            if(androidContent instanceof ViewGroup){
                androidContent.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                    @Override
                    public boolean onPreDraw() {
                        final ViewGroup realContent = ((ViewGroup) androidContent);
                        View content = realContent.getChildAt(0);
                        if(content != null){
                            realContent.removeView(content);
                            InternalLayout layout = new InternalLayout(activity);
                            layout.setContentView(content);
                            realContent.addView(layout);
                            mInternalLayout = layout;
                        }

                        if(mInternalLayout != null){
                            mInternalLayout.setStatusBarVisibility(true);
                            if(isExpandedLayout2NavigationBar){
                                mInternalLayout.setNavigationBarVisibility(true);
                            }
                        }

                        helper.enableImmersedStatusBar(mIsImmersedStatusBar);
                        helper.enableImmersedNavigationBar(mIsImmersedNavigationBar);

                        if(mIsSetNavigationBarColor){
                            helper.setNavigationBarColor(mNavigationBarColor);
                        }
                        if(mNavigationBarDrawable != null){
                            helper.setNavigationBarDrawable(mNavigationBarDrawable);
                        }

                        if(mIsAuto){
                            new android.os.Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    decorView.setDrawingCacheEnabled(true);
                                    final Bitmap bitmap = Bitmap.createBitmap(decorView.getDrawingCache());
                                    decorView.setDrawingCacheEnabled(false);
                                    if(bitmap.getWidth() > 0 && bitmap.getHeight() > 0) {
                                        int top = Utils.sStatusBarHeight + PADDING;
                                        int bottom = (int) (top + ACTION_BAR_DEFAULT_HEIGHT * decorView.getResources().getDisplayMetrics().density);
                                        Rect rect = new Rect(0, top, bitmap.getWidth(), bottom);
                                        PaletteHelper paletteHelper = new PaletteHelper(bitmap, rect);
                                        paletteHelper.findCloseColor(new PaletteHelper.OnPaletteCallback() {
                                            @Override
                                            public void onSuccess(PaletteHelper.Model model) {
                                                helper.setStatusBarColor(model.color);
                                                helper.statusBarFontStyle(model.isDarkStyle ? STATUS_BAR_DARK_FONT_STYLE : STATUS_BAR_LIGHT_FONT_STYLE);
                                            }
                                        });
                                    }
                                }
                            }, 300);
                        }else  {
                            if (mIsSetStatusBarColor) {
                                helper.setStatusBarColor(mStatusBarColor);
                            }
                            if (mStatusBarDrawable != null) {
                                helper.setStatusBarDrawable(mStatusBarDrawable);
                            }
                            helper.statusBarFontStyle(mStatusBarFontStyle);
                        }

                        androidContent.getViewTreeObserver().removeOnPreDrawListener(this);
                        return true;
                    }
                });
            }
        }
    }


}
