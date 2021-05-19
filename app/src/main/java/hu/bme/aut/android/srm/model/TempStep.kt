package hu.bme.aut.android.srm.model

import java.io.Serializable

data class TempStep(
    override val value : Double? = null,
    override val unit : String? = null,
    val duration : Double? = null
): Serializable, Temp