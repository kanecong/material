package com.example.materialdesigntest

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_fruit.*

class FruitActivity : AppCompatActivity() {

    companion object{
        const val FRUIT_NAME = "fruit_name"
        const val FRUIT_IAMGE_ID = "fruit_image_id"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fruit)
        val fruitName = intent.getStringExtra(FRUIT_NAME) ?: ""
        val fruitImageId = intent.getIntExtra(FRUIT_IAMGE_ID, 0)
        setSupportActionBar(toolBarBar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        collapsingToolbarbar.title = fruitName
        Glide.with(this).load(fruitImageId).into(fruitImageAl)
        fruitContentText.text = generateFruitContent(fruitName)
    }

    private fun generateFruitContent(fruitName: String): CharSequence? {
        return fruitName.repeat(100)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}