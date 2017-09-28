package cn.albert.library;

import android.graphics.Bitmap;
import android.graphics.Rect;
import android.support.v4.graphics.ColorUtils;
import android.support.v7.graphics.Palette;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by albert on 2017/9/27.
 */

public class PaletteHelper {

    private Palette.Builder mBuilder;
    private boolean mIsCanceled;
    private float[] mTemp = new float[3];
    private static final float BLACK_MAX_LIGHTNESS = 0.05f;
    private static final float WHITE_MIN_LIGHTNESS = 0.95f;
    private static final float MIDDLE_LIGHTNESS = 0.50f;
    private Rect mRect;

    private static final Palette.Filter FILTER = new Palette.Filter() {

        @Override
        public boolean isAllowed(int rgb, float[] hsl) {
            return true;
        }
    };


    public PaletteHelper(Bitmap bitmap, Rect rect){
        mRect=rect;
        mBuilder = new Palette.Builder(bitmap)
                .clearFilters()
                .addFilter(FILTER)
                .setRegion(rect.left, rect.top, rect.right, rect.bottom);
    }

    public void getPaletteSwatch(final Consumer<Palette> callback){
        mBuilder.generate(new Palette.PaletteAsyncListener() {
            @Override
            public void onGenerated(Palette palette) {
                if(!mIsCanceled && callback != null){
                    callback.accept(palette);
                }

            }
        });
    }

    public void getDarkPalette(final Bitmap bitmap, final OnPaletteCallback onPaletteCallback){
        getPaletteSwatch(new Consumer<Palette>() {
            @Override
            public void accept(Palette palette) {

                List<Palette.Swatch> swatches = new ArrayList<>(palette.getSwatches());
                Palette.Swatch populationSwatch = null;
                if(!swatches.isEmpty()) {
                    // get the max population Palette.Swatch
                    Collections.sort(swatches, new Comparator<Palette.Swatch>() {
                        @Override
                        public int compare(Palette.Swatch lhs, Palette.Swatch rhs) {
                            if (lhs == null && rhs != null) {
                                return 1;
                            } else if (lhs != null && rhs == null) {
                                return -1;
                            } else if (lhs == null && rhs == null) {
                                return 0;
                            } else {
                                return rhs.getPopulation() - lhs.getPopulation();
                            }
                        }
                    });
                    populationSwatch = swatches.get(0);
                }

                boolean blackFlag = true;
                if(populationSwatch == null) {
                    try {
                        // bitmap is get close to full black or white
                        int c = bitmap.getPixel(mRect.right / 2, mRect.bottom / 2);
                        ColorUtils.colorToHSL(c, mTemp);
                        if (mTemp[2] <= BLACK_MAX_LIGHTNESS) {
                            blackFlag = false;
                        } else if (mTemp[2] >= WHITE_MIN_LIGHTNESS) {
                            blackFlag = true;
                        }
                    } catch (Exception ignore) {
                    }
                }else {
                    ColorUtils.colorToHSL(populationSwatch.getTitleTextColor(), mTemp);
                    float distance = mTemp[2] - MIDDLE_LIGHTNESS;
                    blackFlag = distance <= 0;
                }
                if(!mIsCanceled && onPaletteCallback != null) {
                    onPaletteCallback.onPaletteSwatchDone(blackFlag);
                }
            }
        });
    }

    public interface OnPaletteCallback{
        void onPaletteSwatchDone(boolean isDarkPalette);
    }

    public void cancel(){
        mIsCanceled = true;
    }
}
