package hu.bme.aut.android.srm

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import hu.bme.aut.android.srm.databinding.ActivityMainBinding
import hu.bme.aut.android.srm.databinding.ActivityRecipeSearchBinding
import kotlinx.android.synthetic.main.activity_recipe_search.*

class RecipeSearchActivity : AppCompatActivity(),AdapterView.OnItemSelectedListener{

    private lateinit var binding : ActivityRecipeSearchBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecipeSearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }

    override fun onResume() {
        super.onResume()
        setSpinners()
    }

    private fun setSpinners(){
        val list = mutableListOf("=","<",">")

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

}