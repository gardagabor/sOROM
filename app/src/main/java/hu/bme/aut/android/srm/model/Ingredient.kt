package hu.bme.aut.android.srm.model

import java.io.Serializable

data class Ingredient(
        val name : String? = null,
        val value : Double? = null,
        val unit : String? = null
    ) : Serializable