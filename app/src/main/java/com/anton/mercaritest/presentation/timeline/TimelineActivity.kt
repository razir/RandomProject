package com.anton.mercaritest.presentation.timeline

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import androidx.lifecycle.Observer
import com.anton.mercaritest.R
import com.anton.mercaritest.data.entity.Category
import com.anton.mercaritest.di.DI_SCOPE_TIMELINE
import com.anton.mercaritest.presentation.base.BaseActivity
import com.anton.mercaritest.presentation.timeline.product.ProductListFragment
import kotlinx.android.synthetic.main.activity_timeline.*
import org.koin.android.scope.ext.android.bindScope
import org.koin.android.scope.ext.android.getOrCreateScope
import org.koin.android.viewmodel.ext.android.viewModel
import kotlin.properties.Delegates

class TimelineActivity : BaseActivity() {

    private val viewModel: TimelineViewModel by viewModel()

    private lateinit var categoriesAdapter: ProductCategoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_timeline)
        bindScope(getOrCreateScope(DI_SCOPE_TIMELINE))
        initViews()
        initViewModel()
    }

    private fun initViewModel() {
        viewModel.categories.observe(this, Observer {
            it?.let {
                setContentVisibility(visible = true)
                setupTabs(it)
            } ?: kotlin.run {
                setContentVisibility(visible = false)
            }
        })

        viewModel.errorState.observe(this, Observer { errorData ->
            timelineErrorState.apply {
                errorData?.let {
                    visibility = View.VISIBLE
                    setError(it)
                } ?: kotlin.run {
                    visibility = View.GONE
                }
            }
        })

        viewModel.showCategoriesLoading.observe(this, Observer {
            timelineProgress.visibility = if (it) View.VISIBLE else View.GONE
        })
    }

    private fun initViews() {
        timelineErrorState.onActionBtnClick = {
            viewModel.handleErrorRefresh()
        }
        categoriesAdapter = ProductCategoryAdapter()
        timelineCategoriesPager.adapter = categoriesAdapter
        timelineTabs.setupWithViewPager(timelineCategoriesPager)
    }

    private fun setContentVisibility(visible: Boolean) {
        val visibility = if (visible) View.VISIBLE else View.GONE
        timelineTabs.visibility = visibility
        timelineCategoriesPager.visibility = visibility
        timelineSellBtn.visibility = visibility
    }

    private fun setupTabs(categories: List<Category>) {
        categoriesAdapter.categories = categories
    }

    inner class ProductCategoryAdapter : FragmentPagerAdapter(supportFragmentManager) {
        var categories: List<Category> by Delegates.observable(emptyList()) { _, _, _ ->
            notifyDataSetChanged()
        }

        override fun getItem(position: Int): Fragment {
            return ProductListFragment.newInstance(categories[position])
        }

        override fun getCount() = categories.size

        override fun getPageTitle(position: Int) = categories[position].name
    }
}