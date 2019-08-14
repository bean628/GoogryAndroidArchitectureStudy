package com.test.androidarchitecture

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.test.androidarchitecture.adpter.ViewpagerAdapter
import com.test.androidarchitecture.data.Market
import com.test.androidarchitecture.network.RetrofitClient
import com.test.androidarchitecture.network.RetrofitService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {


    private var viewpagerAdapter: ViewpagerAdapter? = null
    private val titleList = arrayListOf("KRW", "BTC", "ETH", "USDT")
    private val fragmentList: ArrayList<Fragment> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        main_tabLayout.setupWithViewPager(main_viewPager)
        loadMarketData()
    }

    private fun loadMarketData() {
        val retrofitService = RetrofitClient().getClient().create(RetrofitService::class.java)
        retrofitService.loadMarketData().subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(this::marketResponse, this::marketError)
    }

    private fun marketResponse(marketList: ArrayList<Market>) {
        val krwList: ArrayList<String> = ArrayList()
        val btcList: ArrayList<String> = ArrayList()
        val ethList: ArrayList<String> = ArrayList()
        val usdtList: ArrayList<String> = ArrayList()
        fragmentList.run {
            for (market in marketList) {
                if (market.market.startsWith("KRW"))
                    krwList.add(market.market)
                if (market.market.startsWith("BTC"))
                    btcList.add(market.market)
                if (market.market.startsWith("ETH"))
                    ethList.add(market.market)
                if (market.market.startsWith("USDT"))
                    usdtList.add(market.market)
            }
            add(CoinFragment.getInstance(krwList, "KRW"))
            add(CoinFragment.getInstance(btcList, "BTC"))
            add(CoinFragment.getInstance(ethList, "ETH"))
            add(CoinFragment.getInstance(usdtList, "USDT"))
        }
        viewpagerAdapter = ViewpagerAdapter(supportFragmentManager, fragmentList, titleList)
        main_viewPager.offscreenPageLimit = 3
        main_viewPager.adapter = viewpagerAdapter
    }

    private fun marketError(error: Throwable) {
        Toast.makeText(this, error.message, Toast.LENGTH_SHORT).show()
    }

}
