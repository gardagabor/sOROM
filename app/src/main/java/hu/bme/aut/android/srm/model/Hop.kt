package hu.bme.aut.android.srm.model

import java.io.Serializable

data class Hop(
    override val name: String,
    override val value: Double,
    override val unit: String,
    val time : String
    ) : Ingredient,Serializable