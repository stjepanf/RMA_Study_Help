package com.mindorks.framework.myapplication.models

import com.mindorks.framework.myapplication.EMPTY_STRING
import com.mindorks.framework.myapplication.INIT_INT
import io.realm.RealmObject

open class College(
    var id: Int = INIT_INT,
    var name: String = EMPTY_STRING,
    var address: String = EMPTY_STRING,
    var telephone: String = EMPTY_STRING,
    var email: String = EMPTY_STRING,
    var url: String = EMPTY_STRING
) : RealmObject()