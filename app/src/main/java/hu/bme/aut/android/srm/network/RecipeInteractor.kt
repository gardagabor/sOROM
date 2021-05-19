package hu.bme.aut.android.srm.network

import android.os.Handler
import android.os.Looper
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import hu.bme.aut.android.srm.model.json.JsonRecipe
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import kotlin.concurrent.thread

class RecipeInteractor {
    private val recipeApi: RecipeAPI

    init {
        val moshi = Moshi.Builder()
            .addLast(KotlinJsonAdapterFactory())
            .build()
        val retrofit = Retrofit.Builder()
            .baseUrl(RecipeAPI.ENDPOINT_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()

        this.recipeApi = retrofit.create(RecipeAPI::class.java)
    }

    fun getAllRecipies(
        onSuccess: (List<JsonRecipe>) -> Unit,
        onError: (Throwable) -> Unit
    )
    {
        val getRecipesRequest = recipeApi.getAllRecipies()
        runCallOnBackgroundThread(getRecipesRequest,onSuccess,onError)
    }

    fun getFilteredRecipies(
        onSuccess: (List<JsonRecipe>) -> Unit,
        onError: (Throwable) -> Unit
    )
    {
        val getRecipesRequest = recipeApi.getFilteredRecipies()
        runCallOnBackgroundThread(getRecipesRequest,onSuccess,onError)
    }



    private fun <T> runCallOnBackgroundThread(
        call: Call<T>,
        onSuccess: (T) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        val handler = Handler(Looper.getMainLooper()!!)
        thread {
            try {
                val response = call.execute().body()!!
                handler.post { onSuccess(response) }

            } catch (e: Exception) {
                e.printStackTrace()
                handler.post { onError(e) }
            }
        }
    }
}