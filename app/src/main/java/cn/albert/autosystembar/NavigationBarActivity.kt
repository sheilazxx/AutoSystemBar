package cn.albert.autosystembar

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat
import cn.albert.library.SystemBarHelper

class NavigationBarActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_navigation_bar)
        SystemBarHelper.Builder()
                .navigationBarColor(ContextCompat.getColor(this, R.color.colorAccent))
                .into(this)
    }
}
