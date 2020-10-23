/*
 * Copyright (C) 2019 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.loopRecyclerview.ui


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.android.loopRecyclerview.R
import com.example.android.loopRecyclerview.databinding.FragmentMainBinding

import com.example.android.loopRecyclerview.util.CoinSnapHelper
import com.example.android.loopRecyclerview.viewmodels.DatumViewModel
import com.example.android.loopRecyclerview.viewmodels.DatumViewModelFactory
import timber.log.Timber

class MainFragment : Fragment() {
    private lateinit var binding: FragmentMainBinding
    private lateinit var layoutManager: RecyclerView.LayoutManager
    private val viewModelAdapter: CoinCardAdapter = CoinCardAdapter()

    private var lastStartPos: Int = 0
    private var lastLimitPos: Int = 20

    companion object {
        const val MIN: Long = 1000 * 60
        const val LIMIT_ITEM_COUNT: Int = 40
    }

    private val viewModel: DatumViewModel by lazy {
        val activity = requireNotNull(this.activity)
        ViewModelProvider(this, DatumViewModelFactory(activity.application))
                .get(DatumViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding: FragmentMainBinding = DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_main,
                container,
                false)

        init(binding)
        return binding.root
    }

    private fun init(binding: FragmentMainBinding) {
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        this.binding = binding
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setRv()
        observeLiveData()
    }

    private fun observeLiveData() {
        viewModel.eventNetworkError.observe(viewLifecycleOwner, { isNetworkError ->
            if (isNetworkError) onNetworkError()
        })

        viewModel.datumlist.observe(viewLifecycleOwner, { list ->
            list?.let {
                if (viewModelAdapter.listItemCount != Int.MAX_VALUE) {
                    viewModelAdapter.listItemCount = list.count()
                }
                viewModelAdapter.submitList(it)
                Timber.d("list.count  ${list.count()}")
            }
        })

        viewModel.isTimerFinished.observe(viewLifecycleOwner, {
            if (it) viewModel.refreshDataFromRepository(lastStartPos, lastLimitPos)
        })

    }

    private fun setRv() {
        setRvConfig(binding)
        initRv(binding.recyclerView, layoutManager)
    }

    private fun setRvConfig(binding: FragmentMainBinding) {
        layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        val snapHelper = CoinSnapHelper().apply { setFlingSpeed(1) }
        val recyclerView: RecyclerView = binding.recyclerView
        snapHelper.attachToRecyclerView(recyclerView)
    }

    private fun initRv(recyclerView: RecyclerView, layoutManager: RecyclerView.LayoutManager) {
        recyclerView.apply {
            animation = null
            viewModelAdapter.viewModel = viewModel
            adapter = viewModelAdapter
            this.layoutManager = layoutManager
            addOnscrollListener(layoutManager)
        }
    }

    private fun RecyclerView.addOnscrollListener(layoutManager: RecyclerView.LayoutManager) {
        addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (recyclerView.scrollState == RecyclerView.SCROLL_STATE_IDLE) {
                    loadMoreData(recyclerView)
                    refreshVisibleItem(layoutManager)
                }
            }
        })
    }

    private fun refreshVisibleItem(layoutManager: RecyclerView.LayoutManager) {
        var start = (layoutManager as LinearLayoutManager).findFirstVisibleItemPosition() + 1

        if (start < 0) {
            return
        }

        start = (start % viewModel.datumlist.value!!.size)
        var limit = 20
        if (start + 20 > LIMIT_ITEM_COUNT) {
            limit = LIMIT_ITEM_COUNT - (start)
        }
        setLastQueryParam(start, limit)
        viewModel.refreshDataFromRepository(start, limit)
    }

    fun setLastQueryParam(start: Int, limit: Int) {
        lastStartPos = start
        lastLimitPos = limit
    }

    private var lastPos: Int = 0
    private fun loadMoreData(recyclerView: RecyclerView) {
        val start: Int
        try {
            start = viewModel.datumlist.value!!.size + 1
        } catch (e: Exception) {
            return
        }

        if (start <= 1) {
            return
        }

        if (!recyclerView.canScrollHorizontally(1)) {
            if (start >= LIMIT_ITEM_COUNT) {
                viewModelAdapter.listItemCount = Int.MAX_VALUE
                viewModelAdapter.notifyDataSetChanged()
                return
            }

            viewModel.refreshDataFromRepository(start)
            lastPos = start
        }
    }

    private fun onNetworkError() {
        if (!viewModel.isNetworkErrorShown.value!!) {
           Timber.d("error")
            viewModel.onNetworkErrorShown()
        }
    }
}
