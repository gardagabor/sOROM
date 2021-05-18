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

        adapter = FoundRecipiesAdapter(
            this, mutableListOf(
                BeerRecipe(
                    1,
                    "Milk Stout",
                    "Best stout",
                    "2020-02-20",
                    "Best stout, u never see before",
                    6.5,
                    30,
                    1010,
                    1060,
                    80,
                    WaterVolume(20, "litre"),
                    WaterVolume(25, "litre"),
                    mutableListOf(
                        TempStep(65, "Celsius", 60)
                    ),

                    FermentTemp(20, "Celsius"),
                    mutableListOf(
                        Ingredient("Oris matter", 5.0, "kg"),
                        Ingredient("fugglet", 50.0, "gm")
                    ),
                    "Safale us-5"
                ),
                BeerRecipe(
                    2,
                    "English Ale",
                    "Ale >> lager",
                    "2020-02-20",
                    "Ale power",
                    6.5,
                    30,
                    1010,
                    1060,
                    80,
                    WaterVolume(20, "litre"),
                    WaterVolume(25, "litre"),
                    mutableListOf(
                        TempStep(65, "Celsius", 20),
                        TempStep(70, "Celsius", 30)
                    ),

                    FermentTemp(20, "Celsius"),
                    mutableListOf(
                        Ingredient("Oris matter", 5.0, "kg"),
                        Ingredient("fugglet", 50.0, "gm")
                    ),
                    "Safale us-5"
                )
            )
        )
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


}