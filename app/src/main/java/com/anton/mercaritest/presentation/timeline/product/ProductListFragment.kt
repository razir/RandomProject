package com.anton.mercaritest.presentation.timeline.product

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.anton.mercaritest.R
import com.anton.mercaritest.data.entity.Category
import com.anton.mercaritest.data.entity.Product
import com.anton.mercaritest.presentation.base.BaseFragment
import com.anton.mercaritest.utils.GridItemDecorator
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_products.*
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

private const val SPAN_COUNT_LANDSCAPE = 3
private const val SPAN_COUNT_PORTRAIT = 2

class ProductListFragment : BaseFragment() {

    companion object {
        private const val ARGUMENT_CATEGORY = "category"

        fun newInstance(category: Category): ProductListFragment {
            return ProductListFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARGUMENT_CATEGORY, category)
                }
            }
        }
    }

    private val viewModel: ProductsListViewModel by viewModel(parameters = {
        parametersOf(arguments!!.getParcelable(ARGUMENT_CATEGORY))
    })

    private lateinit var productsAdapter: ProductsAdapter
    private var errorSnackbar: Snackbar? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_products, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        initViewModel()
        viewModel.handleLoadIfNeeded()
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser && isVisible) {
            viewModel.handleLoadIfNeeded()
        }
    }

    private fun initViews() {
        productsSwipe.setOnRefreshListener {
            viewModel.handleRefreshProducts()
        }
        productsList.apply {
            productsAdapter = ProductsAdapter(onDetailsClick = ::showProductDetails)

            adapter = productsAdapter
            addItemDecoration(GridItemDecorator(ContextCompat.getDrawable(context, R.drawable.product_divider)!!))
            val screenOrientation = resources.configuration.orientation
            if (screenOrientation == Configuration.ORIENTATION_LANDSCAPE) {
                layoutManager = GridLayoutManager(context, SPAN_COUNT_LANDSCAPE)
            } else {
                layoutManager = GridLayoutManager(context, SPAN_COUNT_PORTRAIT)
            }

        }
        productsErrorState.onActionBtnClick = {
            viewModel.handleRefreshError()
        }
    }

    private fun initViewModel() {
        viewModel.showProductsLoading.observe(viewLifecycleOwner, Observer {
            productsProgress.visibility = if (it) View.VISIBLE else View.GONE
        })

        viewModel.hideSwipeRefresh.observe(viewLifecycleOwner, Observer {
            productsSwipe.isRefreshing = false
        })

        viewModel.products.observe(viewLifecycleOwner, Observer {
            it?.let {
                productsSwipe.visibility = View.VISIBLE
                productsAdapter.products = it
            } ?: kotlin.run {
                productsSwipe.visibility = View.GONE
            }
        })

        viewModel.errorAlert.observe(viewLifecycleOwner, Observer { errorData ->
            context?.let {
                showSnackBar(errorData.getShortMsg(it))
            }
        })

        viewModel.errorState.observe(viewLifecycleOwner, Observer {
            it?.let {
                productsErrorState.setError(it)
                productsErrorState.visibility = View.VISIBLE
            } ?: kotlin.run {
                productsErrorState.visibility = View.GONE
            }
        })
    }

    private fun showSnackBar(message: String) {
        errorSnackbar = Snackbar.make(productsRoot, message, Snackbar.LENGTH_SHORT).apply {
            show()
        }
    }

    private fun showProductDetails(product: Product) {
        Toast.makeText(context!!, "Details for ${product.name}", Toast.LENGTH_SHORT).show()
    }


}