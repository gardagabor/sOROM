package hu.bme.aut.android.srm.model

import java.io.Serializable

data class WaterVolume(
    val value : Double? = null,
    val unit : String? = null
): Serializable