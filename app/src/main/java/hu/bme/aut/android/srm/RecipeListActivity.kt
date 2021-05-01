package hu.bme.aut.android.srm

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import hu.bme.aut.android.srm.adapter.SimpleItemRecyclerViewAdapter
import hu.bme.aut.android.srm.databinding.ActivityMainBinding
import hu.bme.aut.android.srm.model.*

class RecipeListActivity : AppCompatActivity(),
    SimpleItemRecyclerViewAdapter.BeerRecipeItemClickListener {

    private lateinit var binding: ActivityMainBinding

    private lateinit var simpleItemRecyclerViewAdapter: SimpleItemRecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
        // intent.putExtra(RecipeDetailActivity.KEY_DESC, beerRecipe.description)
        intent.putExtra("BeerRecipe", beerRecipe);
        startActivity(intent)
    }

    override fun onItemLongClick(position: Int, view: View): Boolean {
        TODO("Not yet implemented")
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
                MutableList(1) {
                    TempStep(65,"Celsius",60)
                },

                FermentTemp(20,"Celsius"),
                MutableList(2) {
                    Malt("Oris matter",5.0,"kg");
                    Hop("fugglet",50.0,"gm","10min")
                },
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
                MutableList(1) {
                    TempStep(65,"Celsius",60)
                },

                FermentTemp(20,"Celsius"),
                MutableList(2) {
                    Malt("Oris matter",5.0,"kg");
                    Hop("fugglet",50.0,"gm","10min")
                },
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
                MutableList(1) {
                    TempStep(65,"Celsius",60)
                },

                FermentTemp(20,"Celsius"),
                MutableList(2) {
                    Malt("Oris matter",5.0,"kg");
                    Hop("fugglet",50.0,"gm","10min")
                },
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
                MutableList(1) {
                    TempStep(65,"Celsius",60)
                },

                FermentTemp(20,"Celsius"),
                MutableList(2) {
                    Malt("Oris matter",5.0,"kg");
                    Hop("fugglet",50.0,"gm","10min")
                },
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
                MutableList(1) {
                    TempStep(65,"Celsius",60)
                },

                FermentTemp(20,"Celsius"),
                MutableList(2) {
                    Malt("Oris matter",5.0,"kg");
                    Hop("fugglet",50.0,"gm","10min")
                },
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
                MutableList(1) {
                    TempStep(65,"Celsius",60)
                },

                FermentTemp(20,"Celsius"),
                MutableList(2) {
                    Malt("Oris matter",5.0,"kg");
                    Hop("fugglet",50.0,"gm","10min")
                },
                "Safale us-5"
            )
        )

        simpleItemRecyclerViewAdapter.addAll(demoData)
    }
}