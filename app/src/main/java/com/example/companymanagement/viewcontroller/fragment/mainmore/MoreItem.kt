package com.example.companymanagement.viewcontroller.fragment.mainmore

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.View
import androidx.core.content.res.ResourcesCompat

class MoreItem() {
    var name: String = "default"
    var drawable: Drawable? = null
    var ClickListener: View.OnClickListener? = null

    class Builder(var ctx: Context?) {
        var item: MoreItem = MoreItem();
        fun setName(name: String): Builder {
            item.name = name;
            return this;
        }

        fun setOnClickListener(ClickListener: View.OnClickListener): Builder {
            item.ClickListener = ClickListener;
            return this;
        }

        fun setDrawableFromID(drawableid: Int): Builder {
            ResourcesCompat.getDrawable(ctx!!.resources, drawableid, null);
            return this;
        }

        fun create(): MoreItem {
            return item;
        }
    }

}

