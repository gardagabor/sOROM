package hu.bme.aut.android.srm

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
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

        initPostsListener()

    }

    private fun setupRecyclerView() {

        simpleItemRecyclerViewAdapter = SimpleItemRecyclerViewAdapter()
        simpleItemRecyclerViewAdapter.itemClickListener = this
      //fillListWithDefValues()
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
                    val db = Firebase.firestore

                    ///Todo: Url-jét megszerezni az elemnek, amivel utána ki lehet törölni
//                    db.collection("recipes").document()
//                        .delete()
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
        when (item.itemId) {
            R.id.itemCreateRecipe -> {
                val recipeCreateFragment = RecipeCreateFragment()
                recipeCreateFragment.show(supportFragmentManager, "TAG")
            }
            R.id.itemSearchRecipe -> {
                val intent = Intent(this, RecipeSearchActivity::class.java)
                startActivity(intent)
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onRecipeCreated(beerRecipe: BeerRecipe) {
      // simpleItemRecyclerViewAdapter.addItem(beerRecipe)
        val db = Firebase.firestore

        db.collection("recipes")
            .add(beerRecipe)
            .addOnSuccessListener {
                toast("Recipe created")
            }
            .addOnFailureListener { e -> toast(e.toString()) }



    }

    override fun onRecipeUpdated(oldRecipe: BeerRecipe, newRecipe : BeerRecipe){
        simpleItemRecyclerViewAdapter.deleteElement(oldRecipe)
        simpleItemRecyclerViewAdapter.addItem(newRecipe)

    }

    private fun toast(message: String?) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun initPostsListener() {
        val db = Firebase.firestore
        db.collection("recipes")
            .addSnapshotListener { snapshots, e ->
                if (e != null) {
                    Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show()
                    return@addSnapshotListener
                }

                for (dc in snapshots!!.documentChanges) {
                    when (dc.type) {
                        DocumentChange.Type.ADDED -> simpleItemRecyclerViewAdapter.addItem(dc.document.toObject<BeerRecipe>())
                        DocumentChange.Type.REMOVED -> simpleItemRecyclerViewAdapter.deleteElement(dc.document.toObject<BeerRecipe>())
                    }
                }
            }
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