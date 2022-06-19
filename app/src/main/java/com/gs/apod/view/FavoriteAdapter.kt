package com.gs.apod.view

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.VideoView
import androidx.recyclerview.widget.RecyclerView
import com.gs.apod.R
import com.gs.apod.utils.Utils
import com.gs.apod.db.NasaApodEntity

class FavoriteAdapter (private val mList: List<NasaApodEntity>) : RecyclerView.Adapter<FavoriteAdapter.ViewHolder>() {

    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_view_design, parent, false)
        context = parent.getContext();
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val itemView = mList[position]

        // sets the image to the imageview from our itemHolder class
        setThumbnail(holder, itemView)

        // sets the text to the textview from our itemHolder class
        holder.textView.text = itemView.title
    }

    private fun setThumbnail(holder: ViewHolder, itemView: NasaApodEntity){
        holder.videoView.setOnErrorListener(MediaPlayer.OnErrorListener(
            fun(mp: MediaPlayer, what: Int, extra: Int): Boolean {
                return true
            }
        ))
        if(itemView.mediaType.contentEquals("video")) {
            holder.imageView.visibility = View.GONE
            holder.videoView.visibility = View.VISIBLE
            val uri: Uri = Uri.parse(itemView.url)
            holder.videoView.setVideoURI(uri);
        } else {
            holder.imageView.visibility = View.VISIBLE
            holder.videoView.visibility = View.GONE
            Utils.bindImageFromUrl(context, holder.imageView, itemView.url)
        }
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imgview_astro)
        val textView: TextView = itemView.findViewById(R.id.tv_title)
        val videoView: VideoView = itemView.findViewById(R.id.videoview_astro)
    }
}