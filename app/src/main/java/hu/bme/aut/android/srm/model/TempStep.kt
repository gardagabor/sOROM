package hu.bme.aut.android.srm.model

import java.io.Serializable

data class TempStep(
    override val value : Int,
    override val unit : String,
    val duration : Int
): Serializable, Temp