package com.sambudisp.muvi.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sambudisp.muvi.R
import com.sambudisp.muvi.model.response.TVResponseResult
import com.squareup.picasso.Picasso

class TvShowListAdapter(
    private val listener: TvListListener
) : RecyclerView.Adapter<TvShowListAdapter.Holder>() {

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
        private val thisContext = view

        internal fun bind(content: TVResponseResult, listener: TvListListener) {
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
        }
    }
}

interface TvListListener {
    fun onClick(tv: TVResponseResult)
}