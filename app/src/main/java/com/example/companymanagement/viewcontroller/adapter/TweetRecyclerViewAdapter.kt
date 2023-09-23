package com.example.companymanagement.viewcontroller.adapter

import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.companymanagement.R
import com.example.companymanagement.model.tweet.TweetModel
import com.example.companymanagement.utils.customize.OnBindOwnerLisener
import com.example.companymanagement.utils.customize.OnButtonClickListener
import com.google.android.material.imageview.ShapeableImageView



class TweetRecyclerViewAdapter() :
    RecyclerView.Adapter<TweetHolder>() {
    private var cmtclicklisener: OnButtonClickListener? = null;
    private var likeclicklisener: OnButtonClickListener? = null;
    private var ownerbindinglisener: OnBindOwnerLisener? = null;
    var list: MutableList<TweetModel>? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): TweetHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_tweet, parent, false)
        return TweetHolder(view)
    }

    fun setData(data: MutableList<TweetModel>) {
        this.list = data;
        this.notifyDataSetChanged()
    }

    override fun getItemCount(): Int = list?.size ?: 0;
    override fun onBindViewHolder(holder: TweetHolder, position: Int) {
        holder.bind(list!![position])
        holder.cmtbtn.setOnClickListener {
            cmtclicklisener?.onClick(list!![position].postuid!!)
        };
        holder.likebtn.setOnClickListener {
            likeclicklisener?.onClick(list!![position].postuid!!)
            holder.countLikeUp(list!![position].apply {
                this.LikeCount++;
            })
        }
        ownerbindinglisener?.onBind(list!![position].OwnerUUID!!, holder)
    }

    fun setOnCommentClick(e: OnButtonClickListener) {
        cmtclicklisener = e;
    }

    fun setOnBindOwner(e: OnBindOwnerLisener) {
        ownerbindinglisener = e;
    }

    fun setOnLikeClick(e: OnButtonClickListener) {
        likeclicklisener = e;
    }
}

class TweetHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val likebtn: Button = itemView.findViewById(R.id.tweet_item_like_btn)
    val cmtbtn: Button = itemView.findViewById(R.id.tweet_item_comment_btn)
    val content: TextView = itemView.findViewById(R.id.tweet_item_content)
    val avatar: ShapeableImageView = itemView.findViewById(R.id.tweet_item_owner_avatar)
    val date: TextView = itemView.findViewById(R.id.tweet_item_owner_create_date)
    val name: TextView = itemView.findViewById(R.id.tweet_item_owner_name)
    fun bind(tweet: TweetModel) {
        content.text = tweet.Content;
        date.text = DateUtils.getRelativeTimeSpanString(tweet.CreateTime?.time!!);
        likebtn.text = tweet.LikeCount.toString() + " Like"
    }

    fun countLikeUp(tweet: TweetModel) {
        likebtn.text = tweet.LikeCount.toString() + " Like"
    }
};