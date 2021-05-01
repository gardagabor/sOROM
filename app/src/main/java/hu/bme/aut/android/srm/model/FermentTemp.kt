package hu.bme.aut.android.srm.model

import java.io.Serializable

data class FermentTemp(
    val value : Int,
    val unit : String
): Serializable