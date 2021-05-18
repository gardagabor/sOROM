package hu.bme.aut.android.srm.model

import java.io.Serializable

data class TempStep(
    override val value : Int? = null,
    override val unit : String? = null,
    val duration : Int? = null
): Serializable, Temp