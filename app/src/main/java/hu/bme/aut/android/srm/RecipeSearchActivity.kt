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
import com.google.android.gms.common.FirstPartyScopes
import hu.bme.aut.android.srm.adapter.FoundRecipiesAdapter
import hu.bme.aut.android.srm.databinding.ActivityRecipeSearchBinding
import hu.bme.aut.android.srm.model.*
import hu.bme.aut.android.srm.model.json.JsonRecipe
import hu.bme.aut.android.srm.network.RecipeInteractor
import kotlinx.android.synthetic.main.activity_recipe_search.*

class RecipeSearchActivity : AppCompatActivity(),AdapterView.OnItemSelectedListener,
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
        setSpinners()
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

    private fun setSpinners(){
        val list = mutableListOf("=", "<", ">")

        binding.spinnerAbv.adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            list
        )
        spinnerAbv.onItemSelectedListener = this

        binding.spinnerEbc.adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            list
        )
        spinnerEbc.onItemSelectedListener = this



    }

    override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {

        when (parent.id) {
            binding.spinnerAbv.id -> {
                binding.spinnerAbv.setSelection(pos)
            }
            binding.spinnerEbc.id -> {
                binding.spinnerEbc.setSelection(pos)
            }
        }
    }


    override fun onNothingSelected(parent: AdapterView<*>) {
        // Another interface callback
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
        recipeInteractor.getRecipes(onSuccess = this::showRecipes, onError = this::showError)
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
        var newlist = mutableListOf<BeerRecipe>()

        jsonRecipeList.forEach {
            newlist.add(
                BeerRecipe(
                    it.id,
                    it.name,
                    it.tagline
            )
            )
        }
        return newlist
    }

}