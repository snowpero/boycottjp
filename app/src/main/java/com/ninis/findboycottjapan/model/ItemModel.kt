package com.ninis.findboycottjapan.model

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class ItemModel(
    var category: String? = "",
    var name: String? = "",
    var desc: String? = "",
    var logo: String? = "",
    var categoryKor: String? = ""
)