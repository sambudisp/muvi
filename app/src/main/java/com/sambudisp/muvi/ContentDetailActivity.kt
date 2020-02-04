package com.sambudisp.muvi

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.activity_movie_detail.*

class ContentDetailActivity : AppCompatActivity() {

    private var fabPlay: FloatingActionButton? = null
    private var btnOrder: TextView? = null
    private var data: ContentModel? = null

    companion object {
        const val EXTRA_DATA = "extra_data"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setContentView(R.layout.activity_movie_detail)

        initComponent()
        setupButton()
        setupParsingData()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == android.R.id.home) {
            finish()
            return super.onOptionsItemSelected(item)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setupParsingData() {
        //content = intent.getParcelableExtra(EXTRA_DATA) as ContentModel
        data = intent.getParcelableExtra(EXTRA_DATA) as ContentModel
        setupContentData()
    }

    private fun setupContentData() {
        /*this.title = content?.title.toString()
        img_poster.setImageResource(content!!.poster)
        tv_title.text = content?.title
        tv_rate.text = content?.rate
        tv_category.text = content?.restriction + " | " + content?.category
        tv_description.text = content?.title + " | " + content?.description
        btnOrder?.text = "Pesan ${content?.price}"*/

        this.title = data?.title.toString()
        img_poster.setImageResource(data!!.poster)
        tv_title.text = data?.title
        tv_rate.text = data?.rate
        tv_category.text = data?.restriction + " | " + data?.category
        tv_description.text = data?.title + " | " + data?.description
        btnOrder?.text = getString(R.string.order) + " ${data?.price}"
    }

    private fun initComponent() {
        fabPlay = findViewById(R.id.fab_play)
        btnOrder = findViewById(R.id.btn_order)
    }

    private fun setupButton() {
        fabPlay?.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(data?.video))
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.setPackage("com.google.android.youtube")
            startActivity(intent)
        }

        btnOrder?.setOnClickListener {
            Toast.makeText(this, getString(R.string.order_isb_processing), Toast.LENGTH_LONG).show()
        }

    }
}
