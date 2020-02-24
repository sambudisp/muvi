package com.sambudisp.muvi.adapter

import android.content.ContentValues
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.sambudisp.muvi.R
import com.sambudisp.muvi.database.DatabaseContract
import com.sambudisp.muvi.database.helper.FavHelper
import com.sambudisp.muvi.database.helper.MappingHelper
import com.sambudisp.muvi.model.localStorage.FavModel
import com.sambudisp.muvi.model.response.MovieResponseResult
import com.sambudisp.muvi.model.response.TVResponseResult
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class TvShowListAdapter(
    private val listener: TvListListener,
    private val favListener: TvFavListener
) : RecyclerView.Adapter<TvShowListAdapter.Holder>() {

    private var fav: FavModel? = null
    private lateinit var favHelper: FavHelper

    private val data = ArrayList<TVResponseResult>()
    fun setData(items: ArrayList<TVResponseResult>) {
        data.clear()
        data.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.adapter_list,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(data[position], listener)
    }

    inner class Holder(view: View) : RecyclerView.ViewHolder(view) {
        private val imgPoster: ImageView = view.findViewById(R.id.img_poster)
        private val tvTitle: TextView = view.findViewById(R.id.tv_title)
        private val tvRestriction: TextView = view.findViewById(R.id.tv_restriction)
        private val tvDescription: TextView = view.findViewById(R.id.tv_description)
        private val tvCategory: TextView = view.findViewById(R.id.tv_category)
        private val btnDetail: Button = view.findViewById(R.id.btn_detail)
        private val tvItemCategory: TextView = view.findViewById(R.id.tv_item_category)
        private val btnSaveToFav: Button = view.findViewById(R.id.btn_save_to_fav)
        private val thisContext = view

        internal fun bind(content: TVResponseResult, listener: TvListListener) {
            favHelper = FavHelper.getInstance(thisContext.context)
            if (!favHelper.isOpen()) favHelper.open()

            Picasso.get()
                .load("https://image.tmdb.org/t/p/w500" + content.poster_path)
                .error(R.drawable.ic_broken_image_black_24dp)
                .into(imgPoster)

            tvTitle.text = content.title
            tvRestriction.text = content.vote_average + "/10"
            tvCategory.text = content.release_date
            tvDescription.text =
                if (content.overview == "" || content.overview == "") thisContext.context.getString(
                    R.string.null_desc
                ) else content.overview
            tvItemCategory.text = thisContext.context.getString(R.string.tv_show)
            tvItemCategory.setBackgroundResource(R.drawable.bg_label_blue)

            btnDetail.setOnClickListener {
                listener.onClick(content)
            }

            thisContext.setOnClickListener {
                listener.onClick(content)
            }

            btnSaveToFav.setOnClickListener {
                fav?.date = getCurrentDate()
                val values = ContentValues()
                values.put(DatabaseContract.FavColumns.ID_FAV, content.id)
                values.put(DatabaseContract.FavColumns.DATE, getCurrentDate())
                values.put(DatabaseContract.FavColumns.TITLE, content.title)
                values.put(DatabaseContract.FavColumns.DESC, content.overview)
                values.put(DatabaseContract.FavColumns.POSTER, content.poster_path)
                values.put(DatabaseContract.FavColumns.CATEGORY, "tv")

                try {
                    val cursor = favHelper.quertByFavId(content.id.toString())
                    val resultCursor = MappingHelper.mapCursorToArrayList(cursor)
                    if (resultCursor.size > 0) {
                        Toast.makeText(
                            thisContext.context,
                            thisContext.context.getString(R.string.movie_has_been_saved_before),
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    } else {
                        val result = favHelper.insert(values)
                        if (result > 0) {
                            fav?.id = result.toInt()
                            Toast.makeText(
                                thisContext.context,
                                thisContext.context.getString(R.string.item_saved),
                                Toast.LENGTH_SHORT
                            )
                                .show()
                        } else {
                            Toast.makeText(
                                thisContext.context,
                                thisContext.context.getString(R.string.item_save_failed),
                                Toast.LENGTH_SHORT
                            )
                                .show()
                        }
                    }
                } catch (e: IllegalStateException) {
                    Toast.makeText(
                        thisContext.context,
                        thisContext.context.getString(R.string.item_save_catch),
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
            }
        }

        private fun getCurrentDate(): String {
            val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val date = Date()
            return dateFormat.format(date)
        }
    }
}

interface TvListListener {
    fun onClick(tv: TVResponseResult)
}


interface TvFavListener {
    fun onFavClick(movie: MovieResponseResult)
}