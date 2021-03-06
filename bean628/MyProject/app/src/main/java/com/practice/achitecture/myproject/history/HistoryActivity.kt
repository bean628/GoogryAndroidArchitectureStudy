package com.practice.achitecture.myproject.history

import android.os.Bundle
import com.practice.achitecture.myproject.R
import com.practice.achitecture.myproject.base.BaseNaverSearchActivity
import com.practice.achitecture.myproject.data.source.NaverRepository
import com.practice.achitecture.myproject.data.source.local.NaverDatabase
import com.practice.achitecture.myproject.data.source.local.NaverLocalDataSourceImpl
import com.practice.achitecture.myproject.data.source.remote.NaverRemoteDataSourceImpl
import com.practice.achitecture.myproject.databinding.ActivityHistoryBinding
import com.practice.achitecture.myproject.makeToast
import com.practice.achitecture.myproject.network.RetrofitClient
import com.practice.achitecture.myproject.util.AppExecutors
import common.NAVER_API_BASE_URL

class HistoryActivity : BaseNaverSearchActivity<ActivityHistoryBinding>(R.layout.activity_history) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var historyViewModel = HistoryViewModel(
            NaverRepository.getInstance(
                NaverRemoteDataSourceImpl(RetrofitClient(NAVER_API_BASE_URL).makeRetrofitServiceForNaver()),
                NaverLocalDataSourceImpl.getInstance(
                    AppExecutors(),
                    NaverDatabase.getInstance(this.applicationContext).naverDao(),
                    this.cacheDir.absolutePath
                )
            )
        ).apply {
            movieOrBookItemsObserver = {
                searchMovieAndBookAdapter?.notifyDataSetChanged(it)
                binding.rvSearchedList.adapter = searchMovieAndBookAdapter
            }

            blogOrNewsItemsObserver = {
                searchBlogAndNewsAdapter?.notifyDataSetChanged(it)
                binding.rvSearchedList.adapter = searchBlogAndNewsAdapter
            }

            stringMessageIdObserver = {
                makeToast(it)
            }
        }
        historyViewModel.loadHistory()
        binding.viewModel = historyViewModel

    }

}