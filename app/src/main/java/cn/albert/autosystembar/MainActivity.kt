package cn.albert.autosystembar

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.health.SystemHealthManager
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.widget.Toast
import cn.albert.library.SystemBarHelper
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import kotlinx.android.synthetic.main.activity_main.*

/**
 * Created by albert on 2017/9/26.
 *
 */
class MainActivity : AppCompatActivity() {

    object Static{

        val DATAS = listOf(Data(AutoStatusBarActivity::class.java,"auto status bar"),
                Data(NavigationBarActivity::class.java, "navigation bar"),
                Data(NavigationBarActivity::class.java, "navigation bar"))
    }

    data class Data(val clazz: Class<out Activity>, val title: String)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SystemBarHelper.Builder()
                .statusBarColor(ContextCompat.getColor(this, R.color.colorAccent))
                .into(this)
        setContentView(R.layout.activity_main)

        recycler_view.layoutManager = LinearLayoutManager(this)
        val adapter = object : BaseQuickAdapter<Data, BaseViewHolder>(R.layout.item, Static.DATAS) {

            override fun convert(helper: BaseViewHolder?, item: Data?) {
                helper?.setText(R.id.btn, item?.title)
                        ?.addOnClickListener(R.id.btn)
            }
        }

        recycler_view.adapter = adapter
        adapter.setOnItemChildClickListener {
            _, _, position ->
            startActivity(Intent(applicationContext, adapter.data[position].clazz))
        }
    }
}