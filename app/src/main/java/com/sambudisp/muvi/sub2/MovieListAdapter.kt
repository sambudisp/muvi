package com.sambudisp.muvi.sub2

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sambudisp.muvi.ContentModel
import com.sambudisp.muvi.R

class MovieListAdapter(
    private val contentList: List<ContentModel>,
    private val listener: OnMovieListClick
) : RecyclerView.Adapter<MovieListAdapter.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.adapter_movie_list,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = contentList.size

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(contentList[position], listener)
    }

    inner class Holder(view: View) : RecyclerView.ViewHolder(view) {
        private val imgPoster: ImageView = view.findViewById(R.id.img_poster)
        private val tvTitle: TextView = view.findViewById(R.id.tv_title)
        private val tvRestriction: TextView = view.findViewById(R.id.tv_restriction)
        private val tvDescription: TextView = view.findViewById(R.id.tv_description)
        private val tvCategory: TextView = view.findViewById(R.id.tv_category)
        private val btnDetail: Button = view.findViewById(R.id.btn_detail)
        private val tvItemCategory: TextView = view.findViewById(R.id.tv_item_category)
        private val thisContext = view

        internal fun bind(content: ContentModel, listener: OnMovieListClick) {
            imgPoster.setImageResource(content.poster)
            tvTitle.text = content.title
            tvRestriction.text = content.rate + " | ${content.restriction}"
            tvCategory.text = content.category
            tvDescription.text = content.description
            tvItemCategory.text = content.itemCategory
            tvItemCategory.setBackgroundResource(R.drawable.bg_label_red)

            btnDetail.setOnClickListener {
                listener.onClick(content)
            }

            thisContext.setOnClickListener {
                listener.onClick(content)
            }
        }
    }
}

interface OnMovieListClick {
    fun onClick(movie: ContentModel)
}