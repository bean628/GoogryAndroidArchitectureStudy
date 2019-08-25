package study.architecture.ui.coinjob

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_coin.*
import kotlinx.android.synthetic.main.fragment_coin.view.*
import study.architecture.R
import study.architecture.data.local.LocalDataSourceImpl
import study.architecture.data.remote.RemoteDataSourceImpl
import study.architecture.data.repository.RepositoryImpl
import study.architecture.presentation.CoinContract
import study.architecture.presentation.CoinPresenter


@SuppressLint("ValidFragment", "WrongConstant")
class CoinFragment : Fragment(), CoinContract.View {

    private val presenter by lazy {
        CoinPresenter(
            this@CoinFragment,
            arguments!!.getSerializable("idx") as FragIndex,
            RepositoryImpl.getInstance(
                RemoteDataSourceImpl,
                LocalDataSourceImpl.getInstance(context!!),
                context!!.getSystemService(
                    Context.CONNECTIVITY_SERVICE
                ) as ConnectivityManager
            )
        ).also {
            it.setAdapterModel(adapter)
            it.setAdapterView(adapter)
        }
    }
    private val adapter = CoinDataAdapter()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.fragment_coin, container, false).apply {
            recyclerView.adapter = adapter
            loading.animation = AnimationUtils.loadAnimation(context, R.anim.loading)
        }


    override fun showProgress() {
        loading?.visibility = View.VISIBLE
        loading?.animation?.start()
    }

    override fun hideProgress() {
        loading?.visibility = View.INVISIBLE
        loading?.animation?.cancel()
    }

    override fun onPause() {
        presenter.onPause()
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        presenter.onResume()
    }

    override fun showError(e: String?) {
        Toast.makeText(context, e, Toast.LENGTH_SHORT).show()
    }

    enum class FragIndex {
        KRW, BTC, ETH, USDT
    }
}