package com.example.companymanagement.viewcontroller.adapter

import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.FragmentActivity
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.companymanagement.R
import com.example.companymanagement.viewcontroller.fragment.mainhome.HomeGridViewViewModel
import com.google.android.material.imageview.ShapeableImageView


class HomeGridViewApdapter(
    private val ctx: FragmentActivity,
    private val items: MutableList<HomeGridViewViewModel>,
) :
    RecyclerView.Adapter<HomeGridViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): HomeGridViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_cardview_button, viewGroup, false)
        return HomeGridViewHolder(view)

    }

    override fun onBindViewHolder(holder: HomeGridViewHolder, position: Int) {
        holder.setText(items[position].text)
        holder.setImageDrawable(ctx.resources.getDrawable(items[position].imageDrawable))
        holder.setColorBackground(items[position].backgroundColor)
        holder.setForegroundTint(items[position].foregroundTint)
        holder.setOnclickListener{ v ->
            ctx.findNavController(R.id.activity_container_fragment)
                .navigate(items[position].linkID!!)
        }
    }

    override fun getItemCount() = items.size

}


class HomeGridViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val img: ShapeableImageView = itemView.findViewById(R.id.button_image)
    val background: CardView = itemView.findViewById(R.id.layout_image)
    val text: TextView = itemView.findViewById(R.id.cardview_text)
    val layout: LinearLayout = itemView.findViewById(R.id.cardview_layout)

    init {
        text.isSelected = true;
    }

    fun setImageDrawable(imageid: Drawable) {
        this.img.foreground = imageid

    }

    fun setForegroundTint(color: Color?) {
        if (color != null) {
            this.img.foregroundTintList = ColorStateList.valueOf(color.toArgb())
        }
    }

    fun setColorBackground(color: Color) {
        this.background.setCardBackgroundColor(color.toArgb())
    }

    fun setOnclickListener(e: View.OnClickListener) {
        this.layout.setOnClickListener(e);
    }

    fun setText(text: String) {
        this.text.setText(text);
    }
}
