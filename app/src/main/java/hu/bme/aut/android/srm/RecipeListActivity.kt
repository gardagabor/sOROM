package hu.bme.aut.android.srm

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import hu.bme.aut.android.srm.adapter.MyRecipiesAdapter
import hu.bme.aut.android.srm.databinding.ActivityMainBinding
import hu.bme.aut.android.srm.model.*


class RecipeListActivity : AppCompatActivity(),
    MyRecipiesAdapter.BeerRecipeItemClickListener,
RecipeCreateFragment.RecipeCreatedListener,
RecipeUpdateFragment.RecipeUpdatedListener{

    private lateinit var binding: ActivityMainBinding

    private lateinit var myRecipiesAdapter: MyRecipiesAdapter

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

        myRecipiesAdapter = MyRecipiesAdapter()
        myRecipiesAdapter.itemClickListener = this
      //fillListWithDefValues()
        binding.root.findViewById<RecyclerView>(R.id.recipe_list).adapter = myRecipiesAdapter
    }

    override fun onItemClick(beerRecipe: BeerRecipe) {
        val intent = Intent(this, RecipeDetailActivity::class.java)
        intent.putExtra("BeerRecipe", beerRecipe);
        startActivity(intent)
    }

    override fun onItemLongClick(position: Int, view: View, recipe: BeerRecipe): Boolean {
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
                    todoUpdateFragment.show(supportFragmentManager, "TAG")
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
                startActivityForResult(intent, 0)
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onRecipeCreated(beerRecipe: BeerRecipe) {
        val db = Firebase.firestore

        db.collection("recipes")
            .add(beerRecipe)
            .addOnSuccessListener {
                toast("Recipe created")
            }
            .addOnFailureListener { e -> toast(e.toString()) }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 0 && resultCode == RESULT_OK) {
            val passedItem: ArrayList<BeerRecipe>? = data?.extras!!["new_added_recipes"] as ArrayList<BeerRecipe>?
            val db = Firebase.firestore
            passedItem?.forEach {
                db.collection("recipes")
                    .add(it)
                    .addOnFailureListener { e -> toast(e.toString()) }
            }
        }
    }

    override fun onRecipeUpdated(oldRecipe: BeerRecipe, newRecipe: BeerRecipe){
        myRecipiesAdapter.deleteElement(oldRecipe)
        myRecipiesAdapter.addItem(newRecipe)

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
                        DocumentChange.Type.ADDED -> myRecipiesAdapter.addItem(dc.document.toObject<BeerRecipe>())
                        DocumentChange.Type.REMOVED -> myRecipiesAdapter.deleteElement(dc.document.toObject<BeerRecipe>())
                    }
                }
            }
    }

}