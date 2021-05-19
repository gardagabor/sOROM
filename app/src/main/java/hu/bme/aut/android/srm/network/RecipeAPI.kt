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

    @GET("beers?page=1&per_page=80")
    fun getAllRecipies(): Call<List<JsonRecipe>>

    @GET("beers/")
    fun getFilteredRecipiesNameOnly(@Query(value = "beer_name", encoded = true) name : String) : Call<List<JsonRecipe>>

    @GET("beers/")
    fun getFilteredRecipiesAbvOnly(@Query(value = "abv_lt", encoded = true) abv_lt : Double,
                                   @Query(value = "abv_gt", encoded = true) abv_gt : Double) : Call<List<JsonRecipe>>

    @GET("beers/")
    fun getFilteredRecipiesEbcOnly(@Query(value = "ebc_lt", encoded = true) ebc_lt : Double,
                                   @Query(value = "ebc_gt", encoded = true) ebc_gt : Double) : Call<List<JsonRecipe>>


    @GET("beers/")
    fun getFilteredRecipiesNameAndAbvOnly(@Query(value = "beer_name", encoded = true) name : String,
                                          @Query(value = "abv_lt", encoded = true) abv_lt : Double,
                                          @Query(value = "abv_gt", encoded = true) abv_gt : Double) : Call<List<JsonRecipe>>

    @GET("beers/")
    fun getFilteredRecipiesNameAndEbcOnly(@Query(value = "beer_name", encoded = true) name : String,
                                          @Query(value = "ebc_lt", encoded = true) ebc_lt : Double ,
                                          @Query(value = "ebc_gt", encoded = true) ebc_gt : Double) : Call<List<JsonRecipe>>

    @GET("beers/")
    fun getFilteredRecipiesAbvAndEbcOny(@Query(value = "abv_lt", encoded = true) abv_lt : Double,
                                        @Query(value = "abv_gt", encoded = true) abv_gt : Double,
                                        @Query(value = "ebc_lt", encoded = true) ebc_lt : Double ,
                                        @Query(value = "ebc_gt", encoded = true) ebc_gt : Double) : Call<List<JsonRecipe>>

    @GET("beers/")
    fun getFilteredRecipies(@Query(value = "beer_name", encoded = true) name : String,
                            @Query(value = "abv_lt", encoded = true) abv_lt : Double,
                            @Query(value = "abv_gt", encoded = true) abv_gt : Double,
                            @Query(value = "ebc_lt", encoded = true) ebc_lt : Double ,
                            @Query(value = "ebc_gt", encoded = true) ebc_gt : Double) : Call<List<JsonRecipe>>



}