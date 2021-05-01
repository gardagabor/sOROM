package hu.bme.aut.android.srm.model

import java.io.Serializable

data class TempStep(
    val value : Int,
    val unit : String,
    val duration : Int
): Serializable