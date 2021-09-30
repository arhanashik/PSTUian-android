package com.workfort.pstuian.app.data.local.appconst

interface AppConst {
    interface Key{
        companion object {
            val FACULTY: String
                get() = "FACULTY"
            val BATCH: String
                get() = "BATCH"
        }
    }
}