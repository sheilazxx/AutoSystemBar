package cn.albert.demo

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import cn.albert.autosystembar.SystemBarHelper


class AutoStatusBarActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auto_status_bar)

        SystemBarHelper.Builder().into(this)
    }
}
