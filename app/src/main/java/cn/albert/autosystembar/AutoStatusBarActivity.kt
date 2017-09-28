package cn.albert.autosystembar

import android.annotation.TargetApi
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import cn.albert.library.PaletteHelper
import kotlinx.android.synthetic.main.activity_auto_status_bar.*
import android.graphics.Rect
import android.os.Build
import android.util.Log
import android.view.ViewTreeObserver
import cn.albert.library.SystemBarHelper


class AutoStatusBarActivity : AppCompatActivity() {

    var b : Boolean = false

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auto_status_bar)


        toolbar.viewTreeObserver.addOnPreDrawListener(
        object : ViewTreeObserver.OnPreDrawListener {
            override fun onPreDraw(): Boolean {
                if(b) {
                    toolbar.isDrawingCacheEnabled = true
                    val b = toolbar.drawingCache

                    val rect = Rect(0, 0, toolbar.width, toolbar.height)

                    val helper = PaletteHelper(b, rect)
                    helper.getPaletteSwatch {

                        its ->
                        Log.d("c", its.lightVibrantSwatch?.rgb.toString())

                        its.lightVibrantSwatch?.rgb?.let {
                            SystemBarHelper.Builder().statusBarColor(it)
                                    .into(this@AutoStatusBarActivity)
                        }

                    }
                }

                b = true
                return true
            }
        })


    }
}
