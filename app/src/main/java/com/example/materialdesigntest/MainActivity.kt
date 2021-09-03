package com.example.materialdesigntest

import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {

    var context: Context? =null

    val fruits = mutableListOf(
        Fruit("apple", R.drawable.apple_pic),
        Fruit("banana", R.drawable.banana_pic),
        Fruit("cherry", R.drawable.cherry_pic),
        Fruit("pear", R.drawable.pear_pic) ,
        Fruit("grape", R.drawable.grape_pic),
        Fruit("mango", R.drawable.mango_pic)
    )

    val fruitList = ArrayList<Fruit>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //另外一种 占用状态栏的方法(配合 xml中 android:fitsSystemWindows(为状态栏留出空间，避免向上偏移) 属性使用更加)
//        val decorView = window.decorView
//        decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//        window.statusBarColor = Color.TRANSPARENT

        setContentView(R.layout.activity_main)

        setSupportActionBar(toolBar)

        context = this.applicationContext

        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)     // 让导航按钮显示出来
            it.setHomeAsUpIndicator(R.drawable.watermelon_pic)   //设置导航按钮图标
        }

        navView.setCheckedItem(R.id.navCall)     //将 Call 菜单项 默认选中
        navView.setNavigationItemSelectedListener {
            drawerLayout.closeDrawer()  //关闭菜单   // drawerLayout.closeDrawers()
            true  //表示事件被处理
        }

        fab.setOnClickListener {
//            Toast.makeText(this, " fab ckicked !", Toast.LENGTH_SHORT).show()

            //在弹出的 Toast 中增加按钮
            Snackbar.make(it, " Data deleted", Snackbar.LENGTH_SHORT)
                .setAction("Undo"){
                    Toast.makeText(this, " Data restored", Toast.LENGTH_SHORT).show()
                }
                .show()
        }

        initFruits()
        val layoutManager = GridLayoutManager(this, 2)   //使用表格布局
        recyclerview.layoutManager = layoutManager
        val adapter = FruitAdapter(this, fruitList)
        recyclerview.adapter = adapter

        swipeRefresh.setColorSchemeResources(R.color.design_default_color_primary)  //刷新UI的颜色
        swipeRefresh.setOnRefreshListener {      //刷新响应事件
            refreshFruits(adapter)
        }

        recyclerview.setOnItemClickListener(object : RecyclerViewExt.OnItemClickListener{
            override fun onItemClick(parent: RecyclerView.Adapter<*>?, vh: RecyclerView.ViewHolder?, position: Int) {
                Log.d("gaorui", "setOnItemClickListener - position = " + position)
                val intent = Intent(context, FruitActivity::class.java).apply {
                    putExtra(FruitActivity.FRUIT_NAME, fruitList[position].name)
                    putExtra(FruitActivity.FRUIT_IAMGE_ID, fruitList[position].imageId)
                }
                startActivity(intent)

            }

            override fun onItemLongClick(vh: RecyclerView.ViewHolder?, position: Int) {
                Toast.makeText(context, " onItemLongClick", Toast.LENGTH_SHORT).show()
            }

        })

        recyclerview.addOnItemTouchListener(object : RecyclerView.OnItemTouchListener{
            override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
                when(e.action){
                    MotionEvent.ACTION_DOWN -> {
                        Log.d("gaorui", "onInterceptTouchEvent - ACTION_DOWN")
                    }
                    MotionEvent.ACTION_UP -> Log.d("gaorui", "onInterceptTouchEvent - ACTION_UP")
                    MotionEvent.ACTION_CANCEL -> Log.d("gaorui", "onInterceptTouchEvent - ACTION_CANCEL")
                    MotionEvent.ACTION_MOVE -> Log.d("gaorui", "onInterceptTouchEvent - ACTION_MOVE")
                }
                return false
            }


            override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {

            }

            override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {

            }

        })

    }

    private fun refreshFruits(adapter: FruitAdapter) {
        thread {
            Thread.sleep(2000)
            runOnUiThread {
                initFruits()
                adapter.notifyDataSetChanged()
                swipeRefresh.isRefreshing = false
            }
        }
    }

    private fun initFruits() {
        fruitList.clear()
        repeat(20){
            val index = (0 until fruits.size).random()
            fruitList.add(fruits[index])
        }
    }

    //加载菜单布局
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toobar, menu)
        return true
    }

    //处理菜单点击情况
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        //home 按钮的 ID 永远都是 android.R.id.home。
        when(item.itemId){
            android.R.id.home -> drawerLayout.openDrawerView()   //drawerLayout.openDrawer(GravityCompat.START)
            R.id.backup -> Toast.makeText(this, " Backup !", Toast.LENGTH_SHORT).show()
            R.id.delete -> Toast.makeText(this, " Delete !", Toast.LENGTH_SHORT).show()
            R.id.settings -> Toast.makeText(this, " Settings !", Toast.LENGTH_SHORT).show()
        }
        return true
    }
}