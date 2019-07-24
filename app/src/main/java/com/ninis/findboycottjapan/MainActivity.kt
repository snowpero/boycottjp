package com.ninis.findboycottjapan

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.*
import com.ninis.findboycottjapan.adapter.BaseRecyclerView
import com.ninis.findboycottjapan.databinding.ActivityMainBinding
import com.ninis.findboycottjapan.databinding.LayoutItemRowBinding
import com.ninis.findboycottjapan.model.DBData
import com.ninis.findboycottjapan.model.ItemModel
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.activity_main.*
import org.angmarch.views.NiceSpinner
import org.angmarch.views.OnSpinnerItemSelectedListener

class MainActivity : AppCompatActivity() {

    private val viewModel: MainActivityViewModel by lazy {
        ViewModelProviders.of(this).get(MainActivityViewModel::class.java)
    }

    private val mainAdapter =
        BaseRecyclerView.Adapter<ItemModel, LayoutItemRowBinding>(
            layoutResId = R.layout.layout_item_row,
            bindingVariableId = BR.itemModel,
            callBack = Consumer { }
        )

    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
        binding.view = this
        binding.executePendingBindings()

        supportActionBar?.run {
            setIcon(R.mipmap.ic_launcher)
            setDisplayUseLogoEnabled(true)
            setDisplayShowHomeEnabled(true)
        }

        binding.rvItemList.run {
            this.adapter = mainAdapter
            layoutManager = GridLayoutManager(context, 2)
        }

        database = FirebaseDatabase.getInstance().reference

        viewModel.getData().observe(this, Observer {
            mainAdapter.replaceAll(it)
            mainAdapter.notifyDataSetChanged()
        })

        viewModel.categoryList.observe(this, Observer {
            spinner_category.attachDataSource(it)
        })

        viewModel.selectItems.observe(this, Observer {
            mainAdapter.replaceAll(it)
            mainAdapter.notifyDataSetChanged()
        })

        setFilter()
    }

    private fun setFilter() {
        spinner_category.setOnSpinnerItemSelectedListener { parent, view, position, id ->
            viewModel.selectFilter(position)
        }
    }
}
