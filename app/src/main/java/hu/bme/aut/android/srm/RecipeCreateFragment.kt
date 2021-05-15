package hu.bme.aut.android.srm

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.get
import androidx.fragment.app.DialogFragment
import hu.bme.aut.android.srm.databinding.FragmentCreateBinding
import hu.bme.aut.android.srm.model.BeerRecipe
import kotlinx.android.synthetic.main.fragment_create.view.*
import kotlinx.android.synthetic.main.ingredient_row_deletable.view.*
import kotlinx.android.synthetic.main.temperature_step.view.*
import kotlinx.android.synthetic.main.temperature_step_deletable.view.*

class RecipeCreateFragment : DialogFragment() {
    private lateinit var listener: RecipeCreatedListener
    private lateinit var binding: FragmentCreateBinding

    override fun onAttach(context: Context) {
        super.onAttach(context)

        try{
            listener = if(targetFragment != null) {
                targetFragment as RecipeCreatedListener
            }
            else{
                activity as RecipeCreatedListener
            }
        }catch (e: ClassCastException) {
            throw RuntimeException(e)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentCreateBinding.inflate(inflater, container, false)
        dialog?.setTitle("Create")

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setAddTemperature()

    }

    private fun setAddTemperature(){
        binding.btnAddMashStep.setOnClickListener {
            val inflater = LayoutInflater.from(this.context).inflate(R.layout.temperature_step_deletable, null, false)

            inflater.tvTempValueDeletable.text = binding.etTempValue.text
            inflater.tvTempUnitDeletable.text = binding.etTempUnit.text
            inflater.tvTempDurationDeletable.text = binding.etTempDuration.text
            inflater.btnDeleteTemp.setOnClickListener {
                binding.MashTemps.removeView(it.parent as View)
            }

            if(binding.etTempValue.text.isNotEmpty() && binding.etTempUnit.text.isNotEmpty() && binding.etTempDuration.text.isNotEmpty()) {
                binding.MashTemps.addView(inflater, binding.MashTemps.childCount)
                }
         }
    }


    interface RecipeCreatedListener {
        fun onRecipeCreated(beerRecipe: BeerRecipe)
    }
}