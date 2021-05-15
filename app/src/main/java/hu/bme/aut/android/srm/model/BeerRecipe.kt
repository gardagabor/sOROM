package hu.bme.aut.android.srm.model

import java.io.Serializable
import java.util.*

data class BeerRecipe (
    val id : Int,
    val name : String,
    val tagline : String,
    val date : String,
    val description : String,
    val abv : Double,
    val ibu : Int,
    val targetFg : Int,
    val targetOg : Int,
    val ebc : Int,
    val volume : WaterVolume,
    val boilVolume : WaterVolume,
    val mashTemps : MutableList<TempStep>,
    val fermentation : FermentTemp,
    val ingredients : MutableList<Ingredient>,
    val yeast : String
) : Serializable
