package com.workfort.pstuian.model

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.BINARY)
expect annotation class Parcelize()

expect interface Parcelable
