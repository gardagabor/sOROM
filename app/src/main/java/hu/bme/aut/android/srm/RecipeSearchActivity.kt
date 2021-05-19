package hu.bme.aut.android.srm

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import hu.bme.aut.android.srm.adapter.FoundRecipiesAdapter
import hu.bme.aut.android.srm.databinding.ActivityRecipeSearchBinding
import hu.bme.aut.android.srm.model.*
import hu.bme.aut.android.srm.model.json.JsonRecipe
import hu.bme.aut.android.srm.network.RecipeInteractor
import kotlinx.android.synthetic.main.activity_recipe_search.*

class RecipeSearchActivity : AppCompatActivity(),
    FoundRecipiesAdapter.BeerFoundRecipeItemClickListener{

    private lateinit var binding : ActivityRecipeSearchBinding

    private lateinit var adapter : FoundRecipiesAdapter

    private var addedBeers = arrayListOf<BeerRecipe>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecipeSearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSearch.setOnClickListener{
            loadRecipes()
        }

        binding.btnSearchCancel.setOnClickListener {
            finish()
        }

        setupRecyclerView()

    }

    override fun onResume() {
        super.onResume()
    }

    override fun finish() {
        val returnIntent = Intent()
        returnIntent.putExtra("new_added_recipes", addedBeers)
        setResult(
            RESULT_OK,
            returnIntent
        )

        super.finish()
    }

    private fun setupRecyclerView() {

        adapter = FoundRecipiesAdapter(this, mutableListOf())
        adapter.itemClickListener = this
        binding.root.findViewById<RecyclerView>(R.id.recipe_list).adapter = adapter
    }


    override fun onItemClick(beerRecipe: BeerRecipe) {
        val intent = Intent(this, RecipeDetailActivity::class.java)
        intent.putExtra("BeerRecipe", beerRecipe);
        startActivity(intent)
    }

    override fun onItemLongClick(view: View, recipe: BeerRecipe): Boolean {
        val popup = PopupMenu(this, view)
        popup.inflate(R.menu.menu_found_recipe)
        popup.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.add -> {
                    Toast.makeText(this, "Recipe has added to your recipies", Toast.LENGTH_SHORT).show()
                    addedBeers.add(recipe)
                }
            }
            false
        }
        popup.show()
        return false
    }


    private fun loadRecipes() {
        val recipeInteractor = RecipeInteractor()
        if(binding.etSearchName.text.isEmpty() && binding.etSearchAbv.text.isEmpty() && binding.etSearchEbc.text.isEmpty())
            recipeInteractor.getAllRecipies(onSuccess = this::showRecipes, onError = this::showError)

        else if(binding.etSearchAbv.text.isEmpty() && binding.etSearchEbc.text.isEmpty())
            recipeInteractor.getFilteredRecipiesNameOnly(binding.etSearchName.text.toString(),onSuccess = this::showRecipes, onError = this::showError)

        else if(binding.etSearchName.text.isEmpty() && binding.etSearchEbc.text.isEmpty())
            recipeInteractor.getFilteredRecipiesAbvOnly(binding.etSearchAbv.text.toString().toDouble(),onSuccess = this::showRecipes, onError = this::showError)

        else if(binding.etSearchName.text.isEmpty() && binding.etSearchAbv.text.isEmpty())
            recipeInteractor.getFilteredRecipiesEbcOnly(binding.etSearchEbc.text.toString().toDouble(),onSuccess = this::showRecipes, onError = this::showError)

        else if(binding.etSearchEbc.text.isEmpty())
            recipeInteractor.getFilteredRecipiesNameAndAbvOnly(binding.etSearchName.text.toString(),binding.etSearchAbv.text.toString().toDouble(),onSuccess = this::showRecipes, onError = this::showError)

        else if(binding.etSearchAbv.text.isEmpty())
            recipeInteractor.getFilteredRecipiesNameAndEbcOnly(binding.etSearchName.text.toString(),binding.etSearchEbc.text.toString().toDouble(),onSuccess = this::showRecipes, onError = this::showError)

        else if(binding.etSearchName.text.isEmpty())
            recipeInteractor.getFilteredRecipiesAbvAndEbcOnly(binding.etSearchAbv.text.toString().toDouble(),binding.etSearchEbc.text.toString().toDouble(),onSuccess = this::showRecipes, onError = this::showError)

        else
            recipeInteractor.getFilteredRecipies(binding.etSearchName.text.toString(),binding.etSearchAbv.text.toString().toDouble(),binding.etSearchEbc.text.toString().toDouble(),onSuccess = this::showRecipes, onError = this::showError)
    }

    private fun showRecipes(recipes: List<JsonRecipe>) {
        adapter = FoundRecipiesAdapter(applicationContext, convertRecipies(recipes as MutableList<JsonRecipe>))
        adapter.itemClickListener = this
        binding.root.findViewById<RecyclerView>(R.id.recipe_list).adapter = adapter
    }

    private fun showError(e: Throwable) {
        e.printStackTrace()
    }

    private fun convertRecipies(jsonRecipeList : MutableList<JsonRecipe>): MutableList<BeerRecipe> {
        var newIngredientlist = mutableListOf<BeerRecipe>()

        jsonRecipeList.forEach {
            var ingredientList = convertIngredientList(it)
            var tempStepsList = convertTempStepsList(it)
            newIngredientlist.add(
                BeerRecipe(
                    it.id,
                    it.name,
                    it.tagline,
                    it.first_brewed,
                    it.description?.substring(0,10),
                    it.abv,
                    it.ibu,
                    it.target_fg,
                    it.target_og,
                    it.ebc,
                    WaterVolume(it.volume?.value, it.volume?.unit),
                    WaterVolume(it.boil_volume?.value, it.boil_volume?.unit),
                    tempStepsList,
                    FermentTemp(it.method?.fermentation?.temp?.value,
                        it.method?.fermentation?.temp?.unit
                    ),
                    ingredientList,
                    it.ingredients?.yeast
            )
            )
        }
        return newIngredientlist
    }

    private fun convertTempStepsList(jsonRecipe: JsonRecipe): MutableList<TempStep> {
        var newTempStepList = mutableListOf<TempStep>()

        jsonRecipe.method?.mash_temp?.forEach {mashTemp ->
            newTempStepList.add(
                TempStep(
                    mashTemp.temp?.value,
                    mashTemp.temp?.unit,
                    mashTemp.duration
                )
            )
        }
        return newTempStepList
    }

    private fun convertIngredientList(jsonRecipe : JsonRecipe): MutableList<Ingredient> {

        var convertedIngredientList = mutableListOf<Ingredient>()

            jsonRecipe.ingredients?.hops?.forEach {hop ->
                convertedIngredientList.add(
                    Ingredient(
                        hop.name,
                        hop.amount?.value,
                        hop.amount?.unit
                    )
                )
            }

            jsonRecipe.ingredients?.malt?.forEach {malt->
                convertedIngredientList.add(
                    Ingredient(
                        malt.name,
                        malt.amount?.value,
                        malt.amount?.unit
                    )
                )
            }


        return convertedIngredientList
    }


}