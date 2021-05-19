package hu.bme.aut.android.srm.model

import java.io.Serializable

class BeerRecipe (
    var id : Int? = null,
    var name : String? = null,
    val tagline : String? = null,
    val date : String? = null,
    val description : String? = null,
    val abv : Double? = null,
    val ibu : Double? = null,
    val targetFg : Double? = null,
    val targetOg : Double? = null,
    val ebc : Double? = null,
    val volume : WaterVolume? = null,
    val boilVolume : WaterVolume? = null,
    val mashTemps : MutableList<TempStep>? = null,
    val fermentation : FermentTemp? = null,
    val ingredients : MutableList<Ingredient>? = null,
    val yeast : String? = null
) : Serializable
