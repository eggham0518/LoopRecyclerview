package com.example.android.loopRecyclerview.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.android.loopRecyclerview.R
import com.example.android.loopRecyclerview.database.getDatabase
import com.example.android.loopRecyclerview.databinding.ItemCoinBinding
import com.example.android.loopRecyclerview.domain.Datum
import com.example.android.loopRecyclerview.repository.DatumsRepository
import com.example.android.loopRecyclerview.viewmodels.DatumViewModel
import timber.log.Timber

class CoinCardAdapter : ListAdapter<Datum, CoinCardViewHolder>(CoinCardViewHolder.DatumDiffCallback()) {
    var listItemCount: Int = 0
    lateinit var viewModel: DatumViewModel

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CoinCardViewHolder {
        val withDataBinding: ItemCoinBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                CoinCardViewHolder.LAYOUT,
                parent,
                false)
        return CoinCardViewHolder(withDataBinding)
    }

    override fun getItemCount(): Int {
        Timber.d("adapter_listItemCount $listItemCount")
        return listItemCount
    }

    override fun onBindViewHolder(holderCard: CoinCardViewHolder, position: Int) {
        val datum = currentList.get(position % currentList.size)
        holderCard.viewDataBinding.also {
            it.datum = datum
        }

        val id_logo: Int = datum.id

//        Glide.with(holderCard.ivLogo.context)
//                .load("https://s2.coinmarketcap.com/static/img/coins/64x64/$id_logo.png")
//                .into(holderCard.ivLogo)

        loadLogoBitmap(id_logo, holderCard.ivLogo)

    }

    fun loadLogoBitmap(id_logo: Int, ivLogo: ImageView) {
        viewModel.refreshLogo(id_logo, ivLogo)
    }

}

class CoinCardViewHolder(val viewDataBinding: ItemCoinBinding) :
        RecyclerView.ViewHolder(viewDataBinding.root) {
    val ivLogo = viewDataBinding.ivLogo

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_coin
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