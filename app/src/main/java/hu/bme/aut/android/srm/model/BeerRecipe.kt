package hu.bme.aut.android.srm.model

import java.io.Serializable

class BeerRecipe (
    var id : Int? = null,
    var name : String? = null,
    val tagline : String? = null,
    val date : String? = null,
    val description : String? = null,
    val abv : Double? = null,
    val ibu : Int? = null,
    val targetFg : Int? = null,
    val targetOg : Int? = null,
    val ebc : Int? = null,
    val volume : WaterVolume? = null,
    val boilVolume : WaterVolume? = null,
    val mashTemps : MutableList<TempStep>? = null,
    val fermentation : FermentTemp? = null,
    val ingredients : MutableList<Ingredient>? = null,
    val yeast : String? = null
) : Serializable
