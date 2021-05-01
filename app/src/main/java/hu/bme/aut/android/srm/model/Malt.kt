package hu.bme.aut.android.srm.model

import java.io.Serializable

data class Malt(
    override val name: String,
    override val value: Double,
    override val unit: String
    ) : Ingredient,Serializable