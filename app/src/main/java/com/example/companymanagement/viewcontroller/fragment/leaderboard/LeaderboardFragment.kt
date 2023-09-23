package com.example.companymanagement.viewcontroller.fragment.leaderboard

import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.example.companymanagement.R
import com.example.companymanagement.databinding.FragmentLeaderboardBinding
import com.example.companymanagement.model.info.UserInfoModel
import com.example.companymanagement.model.ranking.RankerModel
import com.example.companymanagement.utils.UtilsFuntion
import com.example.companymanagement.viewcontroller.adapter.LeaderBoardAdapter
import com.example.companymanagement.viewcontroller.adapter.LeaderBoardAdapter.RankerViewHolder
import com.example.companymanagement.viewcontroller.fragment.mainworkspace.ListUserParticipantViewModel
import com.google.android.material.imageview.ShapeableImageView
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.Picasso
import java.time.YearMonth

class LeaderboardFragment : Fragment() {
    private var _binding: FragmentLeaderboardBinding? = null

    private var leaderboardLayoutManager = LinearLayoutManager(activity)
    private lateinit var leaderboardAdapter: LeaderBoardAdapter

    lateinit var rankerViewModel: RankerViewModel
    private lateinit var userlistppviewmodel: ListUserParticipantViewModel

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        rankerViewModel = ViewModelProvider(requireActivity()).get(RankerViewModel::class.java)
        userlistppviewmodel =
            ViewModelProvider(this.requireActivity()).get(ListUserParticipantViewModel::class.java)

        _binding = FragmentLeaderboardBinding.inflate(inflater, container, false)

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val usersList = view.findViewById<RecyclerView>(R.id.rankers_list)


        val champ1 = view.findViewById<ShapeableImageView>(R.id.champ1_avatar)
        val champ2 = view.findViewById<ShapeableImageView>(R.id.champ2_avatar)
        val champ3 = view.findViewById<ShapeableImageView>(R.id.champ3_avatar)


        val monthDisplay = view.findViewById<EditText>(R.id.leaderboard_display_month)
        monthDisplay.setText(YearMonth.now().monthValue.toString())
        var month = monthDisplay.text.toString().toInt()
        val yearDisplay = view.findViewById<EditText>(R.id.leaderboard_display_year)
        yearDisplay.setText(YearMonth.now().year.toString())
        var year = yearDisplay.text.toString().toInt()

        val monthBack = view.findViewById<ImageButton>(R.id.leaderboard_button_month_back)
        val monthNext = view.findViewById<ImageButton>(R.id.leaderboard_button_month_next)

        val yearBack = view.findViewById<ImageButton>(R.id.leaderboard_button_year_back)
        val yearNext = view.findViewById<ImageButton>(R.id.leaderboard_button_year_next)

        leaderboardAdapter = LeaderBoardAdapter()

        monthBack.setOnClickListener {
            if (monthDisplay.text.isNotBlank() && month > 1) {
                month -= 1
                monthDisplay.setText(month.toString())
            }
        }
        monthNext.setOnClickListener {
            if (monthDisplay.text.isNotBlank() && month < 12) {
                month += 1
                monthDisplay.setText(month.toString())
            }
        }

        yearBack.setOnClickListener {
            if (yearDisplay.text.isNotBlank() && year > 2000) {
                year -= 1
                yearDisplay.setText(year.toString())
            }
        }
        yearNext.setOnClickListener {
            if (yearDisplay.text.isNotBlank() && year < YearMonth.now().year) {
                year += 1
                yearDisplay.setText(year.toString())
            }
        }


        yearDisplay.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (yearDisplay.text.isNotBlank()) {
                    if (yearDisplay.text.toString()
                            .toInt() > YearMonth.now().year || yearDisplay.text.toString()
                            .toInt() < 2000
                    ) {
                        alertInvalidInput(yearDisplay,
                            "Năm : 2000 - nay",
                            YearMonth.now().year.toString())
                    } else {
                        year = yearDisplay.text.toString().toInt()
                        rankerViewModel.retrieveLeaderBoardIn(yearDisplay.text.toString().toInt(),
                            monthDisplay.text.toString().toInt())
                    }
                }

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }
        })
        monthDisplay.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (monthDisplay.text.isNotBlank()) {
                    if (monthDisplay.text.toString().toInt() < 1 || monthDisplay.text.toString()
                            .toInt() > 12
                    ) {
                        alertInvalidInput(monthDisplay,
                            "Tháng : 1 - 12",
                            YearMonth.now().monthValue.toString())
                    } else {
                        month = monthDisplay.text.toString().toInt()
                        rankerViewModel.retrieveLeaderBoardIn(yearDisplay.text.toString().toInt(),
                            monthDisplay.text.toString().toInt())
                    }
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })

        fun showAvatar(url: String?, imageHolder: ShapeableImageView) {
            val dp =
                UtilsFuntion.convertDPToPX(32.0F, this.context).toInt()
            Glide.with(this).load(url)
                .placeholder(CircularProgressDrawable(requireContext()).apply { start() })
                .override(dp, dp)
                .centerCrop()
                .error(R.drawable.ic_default_avatar)
                .into(imageHolder)
        }

        rankerViewModel.retrieveLeaderBoardIn(yearDisplay.text.toString().toInt(),
            monthDisplay.text.toString().toInt())

        leaderboardAdapter.setOnBindOwner { uuid, vh: RecyclerView.ViewHolder ->
            if (vh is RankerViewHolder) {
                fun bind(user: UserInfoModel?, vh: RankerViewHolder) {
                    showAvatar(user?.AvatarURL, vh.avatar)
                    vh.name.text = user?.Name
                    vh.position.text = user?.Position
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

        rankerViewModel.rankList.observe(viewLifecycleOwner, { list ->
            leaderboardAdapter.addRankers(list as List<RankerModel>)

            usersList.apply {
                layoutManager = leaderboardLayoutManager
                adapter = leaderboardAdapter
            }

            fun setChamps(top: Int, champ: ShapeableImageView) {
                if (top <= list.size) {
                    if (userlistppviewmodel.UserList.value?.containsKey(list[top - 1].OwnerUUID) == true) {
                        showAvatar(userlistppviewmodel.UserList.value?.get(list[top - 1].OwnerUUID)?.AvatarURL,
                            champ)
                    } else {
                        userlistppviewmodel.appendUser(list[top - 1].OwnerUUID)
                            .observe(viewLifecycleOwner) {
                                if (it != null) {
                                    showAvatar(it.AvatarURL, champ)
                                }
                            }
                    }
                }
                champ.setImageResource(R.drawable.ic_default_avatar)
            }

            setChamps(1, champ1)
            setChamps(2, champ2)
            setChamps(3, champ3)

        })


    }

    private fun alertInvalidInput(edit: EditText, message: String, resetInput: String) {
        val builder = activity?.let { AlertDialog.Builder(it) }
        if (builder != null) {
            builder.setTitle("Invalid Input")
            builder.setMessage(message)
            builder.setPositiveButton("OK") { dialog, which ->
                edit.setText(resetInput)
            }
            builder.show()
        }

    }

}