package hu.bme.aut.android.srm

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import hu.bme.aut.android.srm.adapter.SimpleItemRecyclerViewAdapter
import hu.bme.aut.android.srm.databinding.ActivityMainBinding
import hu.bme.aut.android.srm.model.*

class RecipeListActivity : AppCompatActivity(),
    SimpleItemRecyclerViewAdapter.BeerRecipeItemClickListener,
RecipeCreateFragment.RecipeCreatedListener,
RecipeUpdateFragment.RecipeUpdatedListener{

    private lateinit var binding: ActivityMainBinding

    private lateinit var simpleItemRecyclerViewAdapter: SimpleItemRecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        binding.toolbar.title = title

        setupRecyclerView()

    }

    private fun setupRecyclerView() {

        simpleItemRecyclerViewAdapter = SimpleItemRecyclerViewAdapter()
        simpleItemRecyclerViewAdapter.itemClickListener = this
        fillListWithDefValues()
        binding.root.findViewById<RecyclerView>(R.id.recipe_list).adapter = simpleItemRecyclerViewAdapter
    }

    override fun onItemClick(beerRecipe: BeerRecipe) {
        val intent = Intent(this, RecipeDetailActivity::class.java)
        intent.putExtra("BeerRecipe", beerRecipe);
        startActivity(intent)
    }

    override fun onItemLongClick(position: Int, view: View, recipe : BeerRecipe): Boolean {
        val popup = PopupMenu(this, view)
        popup.inflate(R.menu.menu_recipe)
        popup.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.delete -> {
                    simpleItemRecyclerViewAdapter.deleteRow(position)
                }
                R.id.modify -> {
                    val todoUpdateFragment = RecipeUpdateFragment(recipe)
                    todoUpdateFragment.show(supportFragmentManager,"TAG")
            }
        }
            false
        }
        popup.show()
        return false
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_list, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.itemCreateRecipe) {
            val recipeCreateFragment = RecipeCreateFragment()
            recipeCreateFragment.show(supportFragmentManager, "TAG")
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onRecipeCreated(beerRecipe: BeerRecipe) {
        simpleItemRecyclerViewAdapter.addItem(beerRecipe)
    }

    override fun onRecipeUpdated(oldRecipe: BeerRecipe, newRecipe : BeerRecipe){
        simpleItemRecyclerViewAdapter.deleteElement(oldRecipe)
        simpleItemRecyclerViewAdapter.addItem(newRecipe)
    }

    fun fillListWithDefValues(){
        val demoData = mutableListOf(
            BeerRecipe(1,
                "Milk Stout",
                "Best stout",
                "2020-02-20",
                "Best stout, u never see before",
                6.5,
                30,
                1010,
                1060,
                80,
                WaterVolume(20,"litre"),
                WaterVolume(25,"litre"),
                mutableListOf(
                    TempStep(65,"Celsius",60)
                ),

                FermentTemp(20,"Celsius"),
                mutableListOf(
                    Ingredient("Oris matter",5.0,"kg"),
                    Ingredient("fugglet",50.0,"gm")
                ),
                "Safale us-5"
            ),
            BeerRecipe(2,
                "English Ale",
                "Ale >> lager",
                "2020-02-20",
                "Ale power",
                6.5,
                30,
                1010,
                1060,
                80,
                WaterVolume(20,"litre"),
                WaterVolume(25,"litre"),
                mutableListOf(
                    TempStep(65,"Celsius",20),
                    TempStep(70,"Celsius",30)
                ),

                FermentTemp(20,"Celsius"),
                mutableListOf(
                    Ingredient("Oris matter",5.0,"kg"),
                    Ingredient("fugglet",50.0,"gm")
                ),
                "Safale us-5"
            ),
            BeerRecipe(3,
                "English pride",
                "better than my coding skills",
                "2020-02-20",
                "pride on my tonge",
                6.5,
                30,
                1010,
                1060,
                80,
                WaterVolume(20,"litre"),
                WaterVolume(25,"litre"),
                mutableListOf(
                    TempStep(65,"Celsius",60)
                ),

                FermentTemp(20,"Celsius"),
                mutableListOf(
                    Ingredient("Oris matter",5.0,"kg"),
                    Ingredient("fugglet",50.0,"gm")
                ),
                "Safale us-5"
            ),
            BeerRecipe(4,
                "London porter",
                "Hey porter, heeeey porter",
                "2020-02-20",
                "harry poteeer",
                6.5,
                30,
                1010,
                1060,
                80,
                WaterVolume(20,"litre"),
                WaterVolume(25,"litre"),
                mutableListOf(
                    TempStep(65,"Celsius",60)
                ),

                FermentTemp(20,"Celsius"),
                mutableListOf(
                    Ingredient("Oris matter",5.0,"kg"),
                    Ingredient("fugglet",50.0,"gm")
                ),
                "Safale us-5"
            ),BeerRecipe(5,
                "I wanna be ",
                "beerstro man",
                "2020-02-20",
                "ortsreeb",
                6.5,
                30,
                1010,
                1060,
                80,
                WaterVolume(20,"litre"),
                WaterVolume(25,"litre"),
                mutableListOf(
                    TempStep(65,"Celsius",60)
                ),

                FermentTemp(20,"Celsius"),
                mutableListOf(
                    Ingredient("Oris matter",5.0,"kg"),
                    Ingredient("fugglet",50.0,"gm"),
                ),
                "Safale us-5"
            ),
            BeerRecipe(6,
                "Running out ale",
                "of ideas hops",
                "2020-02-20",
                "yess",
                6.5,
                30,
                1010,
                1060,
                80,
                WaterVolume(20,"litre"),
                WaterVolume(25,"litre"),
                mutableListOf(
                    TempStep(65,"Celsius",60)
                ),

                FermentTemp(20,"Celsius"),
                mutableListOf(
                    Ingredient("Oris matter",5.0,"kg"),
                    Ingredient("fugglet",50.0,"gm")
                ),
                "Safale us-5"
            )
        )

        simpleItemRecyclerViewAdapter.addAll(demoData)
    }

}