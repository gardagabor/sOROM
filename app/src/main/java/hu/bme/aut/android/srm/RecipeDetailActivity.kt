package hu.bme.aut.android.srm

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import hu.bme.aut.android.srm.model.BeerRecipe

class RecipeDetailActivity : AppCompatActivity() {
    companion object {
        const val KEY_DESC = "KEY_DESC"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe_detail)


        if (savedInstanceState == null) {
            val fragment = RecipeDetailFragment.newInstance(intent.getSerializableExtra("BeerRecipe")!! as BeerRecipe)

            supportFragmentManager.beginTransaction()
                .add(R.id.recipe_detail_container, fragment)
                .commit()
        }
    }

}