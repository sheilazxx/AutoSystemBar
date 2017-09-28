package cn.albert.library;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;

/**
 * Created by albert on 2017/9/27.
 */

public class SystemBarHelper {

    private static final boolean UP_KITKAT = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

    private Builder mBuilder;

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

    public static class Builder{

        @ColorInt
        private int mStatusBarColor;
        @ColorInt private int mNavigationBarColor;
        private Drawable mStatusBarDrawable;
        private Drawable mNavigationBarDrawable;
        private boolean mIsSetStatusBarColor;
        private boolean mIsSetNavigationBarColor;
        private InternalLayout mInternalLayout;
        private boolean mIsImmersedStatusBar;
        private boolean mIsImmersedNavigationBar;

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

        public SystemBarHelper into(Activity activity){
            SystemBarHelper systemBarHelper = new SystemBarHelper(this);
            StatusBarInsert insert;
            if(Utils.isEMUI3_1()){
                insert = new StatusBarInsert.EMUI3_1();
            }else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                insert = new StatusBarInsert.Lollipop();
            }else if(Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT){
                insert = new StatusBarInsert.Kitkat();
            }else{
                insert = new StatusBarInsert.Base();
            }

            boolean isExpandedLayout2StatusBar = insert.expandLayoutToStatusBar(activity);
            boolean isExpandedLayout2NavigationBar = false;
            if(mIsSetNavigationBarColor || (mNavigationBarDrawable != null) ){
                NavigationBarInsert navigationBarInsert;
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                    navigationBarInsert = new NavigationBarInsert.Lollipop();
                }else if(Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT){
                    navigationBarInsert = new NavigationBarInsert.Kitkat();
                }else {
                    navigationBarInsert = new NavigationBarInsert.Base();
                }
                isExpandedLayout2NavigationBar = navigationBarInsert.expandLayoutToNavigationBar(activity);
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
                        mInternalLayout.setStatusBarVisibility(true);
                        if(mIsSetStatusBarColor){
                            helper.setStatusBarColor(mStatusBarColor);
                        }
                        if(mStatusBarDrawable != null){
                            helper.setStatusBarDrawable(mStatusBarDrawable);
                        }
                        if(isExpandedLayout2NavigationBar){
                            mInternalLayout.setNavigationBarVisibility(true);
                        }
                        if(mIsSetNavigationBarColor){
                            helper.setNavigationBarColor(mNavigationBarColor);
                        }
                        if(mNavigationBarDrawable != null){
                            helper.setNavigationBarDrawable(mNavigationBarDrawable);
                        }
                        helper.enableImmersedStatusBar(mIsImmersedStatusBar);
                        helper.enableImmersedNavigationBar(mIsImmersedNavigationBar);

                        androidContent.getViewTreeObserver().removeOnPreDrawListener(this);
                        return true;
                    }
                });
            }
        }
    }
}
