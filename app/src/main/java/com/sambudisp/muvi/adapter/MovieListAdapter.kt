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
import com.sambudisp.muvi.database.DatabaseContract.FavColumns.Companion.CATEGORY
import com.sambudisp.muvi.database.DatabaseContract.FavColumns.Companion.DATE
import com.sambudisp.muvi.database.DatabaseContract.FavColumns.Companion.DESC
import com.sambudisp.muvi.database.DatabaseContract.FavColumns.Companion.ID_FAV
import com.sambudisp.muvi.database.DatabaseContract.FavColumns.Companion.POSTER
import com.sambudisp.muvi.database.DatabaseContract.FavColumns.Companion.TITLE
import com.sambudisp.muvi.database.helper.FavHelper
import com.sambudisp.muvi.database.helper.MappingHelper
import com.sambudisp.muvi.model.localstorage.FavModel
import com.sambudisp.muvi.model.response.MovieResponseResult
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class MovieListAdapter(
    private val listener: MovieListListener,
    private val favListener: MovieFavListener
) : RecyclerView.Adapter<MovieListAdapter.Holder>() {

    private var fav: FavModel? = null
    private lateinit var favHelper: FavHelper
    private val data = ArrayList<MovieResponseResult>()

    fun setData(items: ArrayList<MovieResponseResult>) {
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

        internal fun bind(content: MovieResponseResult, listener: MovieListListener) {
            favHelper = FavHelper.getInstance(thisContext.context)
            if (!favHelper.isOpen()) favHelper.open()

            Picasso.get()
                .load("https://image.tmdb.org/t/p/w500" + content.poster_path)
                .error(R.drawable.ic_broken_image_black_24dp)
                .into(imgPoster)

            tvTitle.text = content.title
            tvRestriction.text = content.vote_average + "/10"
            tvCategory.text = content.release_date
            tvDescription.text = content.overview
            tvItemCategory.text = thisContext.context.getString(R.string.movie_cinema)
            tvItemCategory.setBackgroundResource(R.drawable.bg_label_red)

            btnDetail.setOnClickListener {
                listener.onClick(content)
            }

            thisContext.setOnClickListener {
                //listener.onClick(content)
            }

            btnSaveToFav.setOnClickListener {
                fav?.date = getCurrentDate()
                val values = ContentValues()
                values.put(ID_FAV, content.id)
                values.put(DATE, getCurrentDate())
                values.put(TITLE, content.title)
                values.put(DESC, content.overview)
                values.put(POSTER, content.poster_path)
                values.put(CATEGORY, "movie")

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

interface MovieListListener {
    fun onClick(movie: MovieResponseResult)
}

interface MovieFavListener {
    fun onFavClick(movie: MovieResponseResult)
}