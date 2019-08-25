package study.architecture.data

import io.reactivex.Single
import study.architecture.data.entity.Market
import study.architecture.data.entity.Ticker

interface UpbitLocalDataSource {
    fun getMarkets(): Single<List<Market>>
    fun getTickers(markets: String): Single<MutableList<Ticker>>
    fun insertMarket(market: Market)
    fun insertTicker(ticker: Ticker)

}