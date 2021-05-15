package hu.bme.aut.android.srm.model

import java.io.Serializable

data class Ingredient(
        val name : String,
        val value : Double,
        val unit : String
    ) : Serializable