package com.example.companymanagement.viewcontroller.fragment.mainworkspace

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.example.companymanagement.R
import com.example.companymanagement.model.info.UserInfoModel
import com.example.companymanagement.model.tweet.TweetModel
import com.example.companymanagement.utils.UtilsFuntion
import com.example.companymanagement.utils.customize.EndlessScrollRecyclListener
import com.example.companymanagement.viewcontroller.adapter.TweetHolder
import com.example.companymanagement.viewcontroller.adapter.TweetRecyclerViewAdapter
import com.example.companymanagement.viewcontroller.fragment.shareviewmodel.UserInfoViewModel
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.textfield.TextInputEditText

class MainTweet : Fragment() {

    private var tweetviewmodel: TweetViewModel = TweetViewModel();
    private lateinit var userlistppviewmodel: ListUserParticipantViewModel;
    private lateinit var userinfoviewmodel: UserInfoViewModel;

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        userlistppviewmodel =
            ViewModelProvider(this.requireActivity()).get(ListUserParticipantViewModel::class.java)
        userinfoviewmodel =
            ViewModelProvider(this.requireActivity()).get(UserInfoViewModel::class.java)
        return inflater.inflate(R.layout.fragment_main_tweet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recyclerView = view.findViewById<RecyclerView>(R.id.workspace_tweet_container)
        val postbtn = view.findViewById<Button>(R.id.tweet_post_btn)
        val content = view.findViewById<TextInputEditText>(R.id.tweet_content)
        val postavatar = view.findViewById<ShapeableImageView>(R.id.tweet_post_avatar)
        val layout = LinearLayoutManager(context);
        val adapter = TweetRecyclerViewAdapter()
        //
        userinfoviewmodel.info.observe(viewLifecycleOwner) {
            if (it != null) {
                val dp = UtilsFuntion.convertDPToPX(32.0F, context).toInt()
                Glide.with(this).load(it.AvatarURL)
                    .placeholder(CircularProgressDrawable(requireContext()).apply { start() })
                    .override(dp, dp)
                    .centerCrop()
                    .error(R.drawable.ic_default_avatar)
                    .into(postavatar)
            }
        }

        adapter.setOnCommentClick {
            showComment(it);
        }
        adapter.setOnLikeClick {
            tweetviewmodel.CountLikeUp(it);
        }
        adapter.setOnBindOwner { uuid, vh: RecyclerView.ViewHolder ->
            if (vh is TweetHolder) {
                fun bind(user: UserInfoModel?, vh: TweetHolder) {
                    val dp = UtilsFuntion.convertDPToPX(32.0F, context).toInt()
                    Glide.with(this).load(user?.AvatarURL)
                        .placeholder(CircularProgressDrawable(requireContext()).apply { start() })
                        .override(dp, dp)
                        .centerCrop()
                        .error(R.drawable.ic_default_avatar)
                        .into(vh.avatar)
                    vh.name.text = user?.Name
                }
                if (userlistppviewmodel.UserList.value?.containsKey(uuid) == true) {
                    bind(userlistppviewmodel.UserList.value?.get(uuid), vh);
                } else {
                    userlistppviewmodel.appendUser(uuid).observe(viewLifecycleOwner) {
                        bind(it, vh);
                    }
                }
            }
        }
        tweetviewmodel.TweetList.observe(viewLifecycleOwner) {
            adapter.setData(it);
        }

        recyclerView.addOnScrollListener(object : EndlessScrollRecyclListener() {
            override fun onLoadMore(page: Int, totalItemsCount: Int) {
                tweetviewmodel.lazyLoadTweet().observe(viewLifecycleOwner) {
                    adapter.notifyItemRangeInserted(adapter.list?.lastIndex!!, it)
                }
                Log.d("Load nore", "load More")
            }
        })

        postbtn.setOnClickListener {
            try {
                if (content.text.toString().isNotBlank())
                    tweetviewmodel.addTweet(TweetModel(content.text.toString(),
                        userinfoviewmodel.info.value?.uid!!))
                content.text?.clear()
                adapter.notifyItemInserted(0)
            } catch (err: Exception) {
                toastEditfaild(err.message.toString())
            }
        }
        layout.orientation = RecyclerView.VERTICAL;
        recyclerView.layoutManager = layout;
        recyclerView.adapter = adapter;

    }

    fun showComment(tweetid: String) {
        Log.d("tweet", tweetid)
        val comment = Comment()
        val bundle = Bundle();
        bundle.putString("tweet_id", tweetid)
        comment.arguments = bundle
        comment.show(childFragmentManager, tweetid)
    }

    fun toastEditfaild(err: String) {
        Toast.makeText(context, err, Toast.LENGTH_SHORT).show()
    }
}
