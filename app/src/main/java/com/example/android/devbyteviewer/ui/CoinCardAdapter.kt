package com.example.android.devbyteviewer.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.android.devbyteviewer.R
import com.example.android.devbyteviewer.databinding.DevbyteItemBinding
import com.example.android.devbyteviewer.domain.Datum
import timber.log.Timber

class CoinCardAdapter() : ListAdapter<Datum, CoinCardViewHolder>(CoinCardViewHolder.DatumDiffCallback()) {

    var listItemCount : Int = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CoinCardViewHolder {
        val withDataBinding: DevbyteItemBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                CoinCardViewHolder.LAYOUT,
                parent,
                false)


        return CoinCardViewHolder(withDataBinding)
    }

    override fun getItemCount(): Int {
        return listItemCount
    }

    override fun onBindViewHolder(holderCard: CoinCardViewHolder, position: Int) {
        val datum = currentList.get(position % currentList.size)

        holderCard.viewDataBinding.also {
            it.datum = datum
        }

        val id_logo: String? = datum.id
        Timber.d("로고 $id_logo")
        Glide.with(holderCard.iv_logo.context)
                .load("https://s2.coinmarketcap.com/static/img/coins/64x64/$id_logo.png")
                .into(holderCard.iv_logo)
    }
}

class CoinCardViewHolder(val viewDataBinding: DevbyteItemBinding) :
        RecyclerView.ViewHolder(viewDataBinding.root) {
    val iv_logo = viewDataBinding.ivLogo;

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.devbyte_item
    }

    class DatumDiffCallback : DiffUtil.ItemCallback<Datum>() {
        override fun areItemsTheSame(oldItem: Datum, newItem: Datum): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Datum, newItem: Datum): Boolean {
            return oldItem == newItem
        }
    }
}