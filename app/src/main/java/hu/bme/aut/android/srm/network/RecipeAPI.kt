package hu.bme.aut.android.srm.network

import hu.bme.aut.android.srm.model.json.JsonRecipe

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query


interface RecipeAPI {
    companion object {
        const val ENDPOINT_URL = "https://api.punkapi.com/v2/"
        const val IMAGE_PREFIX_URL = "https://punkapi.com/"
    }

    @GET("beers")
    fun getAllRecipies(): Call<List<JsonRecipe>>

    @GET("beers/")
    fun getFilteredRecipies(@Query(value = "beer_name", encoded = true) name : String, ) : Call<List<JsonRecipe>>



}