package hu.bme.aut.android.srm.model

import java.io.Serializable

data class FermentTemp(
    override val value : Double? = null,
    override val unit : String? = null
): Serializable, Temp