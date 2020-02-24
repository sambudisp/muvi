package com.sambudisp.muvi.adapter

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.sambudisp.muvi.R
import com.sambudisp.muvi.activity.ContentDetailActivity
import com.sambudisp.muvi.database.helper.FavHelper
import com.sambudisp.muvi.listener.CustomOnItemClickListener
import com.sambudisp.muvi.model.localStorage.FavModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_fav.view.*

class FavAdapter(
    private val listener: DeletedListener) :
    RecyclerView.Adapter<FavAdapter.NoteViewHolder>() {

    private lateinit var favHelper: FavHelper

    var listFav = ArrayList<FavModel>()
        set(listNotes) {
            if (listNotes.size > 0) {
                this.listFav.clear()
            }
            this.listFav.addAll(listNotes)
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_fav, parent, false)
        return NoteViewHolder(view)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        holder.bind(listFav[position], listener)
    }

    override fun getItemCount(): Int = this.listFav.size

    inner class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(fav: FavModel, listener: DeletedListener) {
            favHelper = FavHelper.getInstance(itemView.context)
            if (!favHelper.isOpen()) favHelper.open()

            with(itemView) {
                val savedAt = context.getString(R.string.saved_at) + " " + fav.date
                tv_saved_id.text = fav.savedId
                tv_date.text = savedAt
                tv_saved_title.text = fav.title
                tv_saved_desc.text = fav.desc

                Picasso.get()
                    .load("https://image.tmdb.org/t/p/w342" + fav.poster)
                    .error(R.drawable.ic_broken_image_black_24dp)
                    .into(img_saved_poster)


                cv_item_fav.setOnClickListener(
                    CustomOnItemClickListener(
                        adapterPosition,
                        object : CustomOnItemClickListener.OnItemClickCallback {
                            override fun onItemClicked(view: View, position: Int) {
                                val bundle = Bundle()
                                bundle.putString(ContentDetailActivity.EXTRA_DATA, fav.savedId)
                                bundle.putString(ContentDetailActivity.EXTRA_TYPE, fav.category)
                                val intent = Intent(context, ContentDetailActivity::class.java)
                                intent.putExtras(bundle)
                                context.startActivity(intent)
                            }
                        })
                )

                btn_delete_saved_item.setOnClickListener {
                    val result = favHelper.deleteById(fav.id.toString()).toLong()
                    if (result > 0) {
                        Toast.makeText(
                            context,
                            "${fav.title} ${context.getString(R.string.item_deleted)}",
                            Toast.LENGTH_SHORT
                        ).show()
                        listener.onDeleted()
                    } else {
                        Toast.makeText(
                            context,
                            context.getString(R.string.item_delete_failed),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }
}

interface DeletedListener {
    fun onDeleted()
}