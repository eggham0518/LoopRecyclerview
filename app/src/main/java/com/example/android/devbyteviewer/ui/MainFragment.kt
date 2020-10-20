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

package com.example.android.devbyteviewer.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.android.devbyteviewer.R
import com.example.android.devbyteviewer.databinding.FragmentDevByteBinding
import com.example.android.devbyteviewer.util.CoinSnapHelper
import com.example.android.devbyteviewer.util.sdf
import com.example.android.devbyteviewer.viewmodels.DatumViewModel
import timber.log.Timber
import java.util.*


/**d
 * Show a list of DevBytes on screen.
 */
class MainFragment : Fragment() {

    private val viewModel: DatumViewModel by lazy {
        val activity = requireNotNull(this.activity)
        ViewModelProvider(this, DatumViewModel.Factory(activity.application))
                .get(DatumViewModel::class.java)
    }

    lateinit var binding: FragmentDevByteBinding
    lateinit var layoutManager: RecyclerView.LayoutManager
    private var viewModelAdapter: CoinCardAdapter? = null
    val MIN: Long = 1000 * 60
    val LIMIT_ITEM_COUNT: Int = 40

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding: FragmentDevByteBinding = DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_dev_byte,
                container,
                false)

        init(binding)
        return binding.root
    }

    //뷰 모델과 바인딩 초기화
    private fun init(binding: FragmentDevByteBinding) {
        binding.setLifecycleOwner(viewLifecycleOwner)
        binding.viewModel = viewModel
        viewModel.eventNetworkError.observe(viewLifecycleOwner, { isNetworkError ->
            if (isNetworkError) onNetworkError()
        })
        this.binding = binding
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setRv()
        observeData()
    }

    private fun observeData() {
        viewModel.datumlist.observe(viewLifecycleOwner, { list ->
            list?.let {
                if (viewModelAdapter?.listItemCount != Int.MAX_VALUE) {
                    viewModelAdapter?.listItemCount = list.count()
                }
                viewModelAdapter?.submitList(it)
            }
        })
    }

    private fun setRv() {
        setRvConfig(binding)
        initRv(binding.recyclerView, layoutManager)
    }

    private fun setRvConfig(binding: FragmentDevByteBinding) {
        viewModelAdapter = CoinCardAdapter()
        layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)


        val snapHelper = CoinSnapHelper().apply { setFlingSpeed(1) }
        val recyclerView: RecyclerView = binding.recyclerView
        snapHelper.attachToRecyclerView(recyclerView)
//        layoutManager = LoopingLayoutManager(context!!, LoopingLayoutManager.HORIZONTAL, false)
    }

    private fun initRv(recyclerView: RecyclerView, layoutManager: RecyclerView.LayoutManager) {
        recyclerView.apply {
            setHasFixedSize(true)
            adapter = viewModelAdapter
            this.layoutManager = layoutManager
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    Timber.d("1스크롤")


                    if (recyclerView.scrollState == RecyclerView.SCROLL_STATE_IDLE) {
                        loadMoreData(recyclerView)
                        refreshVisibleItem(layoutManager)
                        Timber.d("2스크롤")
                    }

                }
            })
        }
    }

    private fun refreshVisibleItem(layoutManager: RecyclerView.LayoutManager) {
        var start = (layoutManager as LinearLayoutManager).findFirstVisibleItemPosition() + 1;
        if (start < 0) {
            return
        }

        start = (start % viewModel.datumlist.value!!.size)
        val datumlist = viewModel.datumlist.value
        val datum = datumlist?.get(start)
        val lastUpdateTime = datum?.last_updated
        val date: Date = sdf.parse(lastUpdateTime)
        var limit = 20

        if (start + 20 > LIMIT_ITEM_COUNT) {
            limit = LIMIT_ITEM_COUNT - (start)
        }

        Timber.d("lastUpdateTime1 $lastUpdateTime")
        Timber.d("lastUpdateTime2 ${date.time}")
        Timber.d("isNeedUpdate ${isNeedUpdate(date)}")

        if (isNeedUpdate(date)) {
//            start -= 10
            Timber.d("visibleRefresh")
            Timber.d("needupdate1 $start")
            Timber.d("needupdate2 limit $limit")

            viewModel.refreshDataFromRepository(start, limit)
        }
    }

    private fun isNeedUpdate(date: Date): Boolean {
        val currentTime = System.currentTimeMillis()
        val gap = currentTime - date.time
        Timber.d("gap $gap")
        Timber.d("데이트타임1 ${date.time}")
        Timber.d("데이트타임2 ${currentTime}")

        if (gap > MIN * 2) {
            return true
        }

        return false
    }

    var lastPos: Int = 0
    private fun loadMoreData(recyclerView: RecyclerView) {
        var start: Int
        try {
            start = viewModel.datumlist.value!!.size + 1
        } catch (e: Exception) {
            return
        }

        if (start <= 1) {
            return
        }

        if (!recyclerView.canScrollHorizontally(1)) {
//            if (!isMoreData(lastPos, start)) {
//                return;
//            }
            if (start >= LIMIT_ITEM_COUNT) {
                viewModelAdapter?.listItemCount = Int.MAX_VALUE
                viewModelAdapter?.notifyDataSetChanged()
                Timber.d("start1 $start")
                return
            }

            viewModel.refreshDataFromRepository(start)
            Timber.d("start2 $start")
            lastPos = start
        }
    }

    fun isMoreData(lastPos: Int, currentPos: Int): Boolean {
        if (lastPos >= 100) {
            viewModelAdapter?.listItemCount = Int.MAX_VALUE
            return false
        }
        return true
    }

    private fun onNetworkError() {
        if (!viewModel.isNetworkErrorShown.value!!) {
            Toast.makeText(activity, "Network Error", Toast.LENGTH_LONG).show()
            viewModel.onNetworkErrorShown()
        }
    }
}
