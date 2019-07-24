package com.ninis.findboycottjapan

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.ninis.findboycottjapan.base.BaseViewModel
import com.ninis.findboycottjapan.model.DBData
import com.ninis.findboycottjapan.model.ItemModel
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.collections.HashSet

class MainActivityViewModel : BaseViewModel() {
    val itemModels = MutableLiveData<ArrayList<ItemModel>>()
    val selectItems = MutableLiveData<ArrayList<ItemModel>>()
    val categoryList = MutableLiveData<List<String>>()

    val categoryItems = HashMap<String, ArrayList<ItemModel>>()

    fun getData(): LiveData<ArrayList<ItemModel>> {
        if (itemModels.value == null) {
            FirebaseDatabase.getInstance().reference
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onCancelled(databaseError: DatabaseError) {

                    }

                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        val data = dataSnapshot.getValue(DBData::class.java)
                        data?.run {
                            itemModels.value = items
                            setCategoryFilter(items)
                        }
                    }
                })
        }

        return itemModels
    }

    private fun setCategoryFilter(items: ArrayList<ItemModel>) {
        val categorySet = HashSet<String>()
        for (itemModel in items) {
            if( !categorySet.contains(itemModel.category) ) {
                categorySet.add(itemModel.categoryKor!!)
            }

            if( categoryItems[itemModel.categoryKor!!] == null )
                categoryItems[itemModel.categoryKor!!] = ArrayList<ItemModel>()

            categoryItems[itemModel.categoryKor!!]?.add(itemModel)
        }

        val tmpList = LinkedList<String>(categorySet)
        tmpList.add(0, "전체")

        categoryList.value = tmpList
    }

    fun selectFilter(index: Int) {
        if( index > 0 ) {
            val category = categoryList.value?.get(index)

            if (categoryItems.containsKey(category)) {
                selectItems.value = categoryItems[category]
            }
        } else {
            selectItems.value = itemModels.value
        }
    }
}