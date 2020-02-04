package com.sambudisp.muvi

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*

class MovieAdapter internal constructor(
        private val context: Context,
        private val listener: MovieListener
) : BaseAdapter() {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var itemView = convertView
        if (itemView == null){
            itemView = LayoutInflater.from(context).inflate(R.layout.item_content, parent, false)
        }

        val viewHolder = MovieHolder(itemView as View)
        val movie = getItem(position) as ContentModel
        viewHolder.bind(movie, listener, position)
        return itemView
    }

    override fun getItem(position: Int): Any = movie[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getCount(): Int = movie.size

    internal var movie = arrayListOf<ContentModel>()

    private inner class MovieHolder internal constructor(view: View){
        private val imgPoster : ImageView = view.findViewById(R.id.img_poster)
        private val tvTitle : TextView = view.findViewById(R.id.tv_title)
        private val tvRestriction : TextView = view.findViewById(R.id.tv_restriction)
        private val tvDescription : TextView = view.findViewById(R.id.tv_description)
        private val tvCategory : TextView = view.findViewById(R.id.tv_category)
        private val btnDetail : Button = view.findViewById(R.id.btn_detail)
        private val thisContext = view

        internal fun bind(content: ContentModel, listener: MovieListener, position: Int){
            imgPoster.setImageResource(content.poster)
            tvTitle.text = content.title
            tvRestriction.text = content.rate + " | ${content.restriction}"
            tvCategory.text = content.category
            tvDescription.text = content.description

            btnDetail.setOnClickListener {
                listener.onDetailClicked(content)
            }

            thisContext.setOnClickListener {
                listener.onDetailClicked(content)
            }
        }
    }
}

interface MovieListener {
    fun onDetailClicked(content: ContentModel)
}



