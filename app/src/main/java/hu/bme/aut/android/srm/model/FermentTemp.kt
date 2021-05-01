package hu.bme.aut.android.srm.model

import java.io.Serializable

data class FermentTemp(
    override val value : Int,
    override val unit : String
): Serializable, Temp