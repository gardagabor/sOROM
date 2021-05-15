package hu.bme.aut.android.srm

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.forEach
import androidx.core.view.get
import androidx.fragment.app.DialogFragment
import hu.bme.aut.android.srm.databinding.FragmentCreateBinding
import hu.bme.aut.android.srm.model.*
import kotlinx.android.synthetic.main.fragment_create.view.*
import kotlinx.android.synthetic.main.ingredient_row_deletable.view.*
import kotlinx.android.synthetic.main.temperature_step.view.*
import kotlinx.android.synthetic.main.temperature_step_deletable.view.*
import kotlin.random.Random

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

        setAddIngredient()

        binding.btnCreateRecipe.setOnClickListener{

            var steps: MutableList<TempStep> = mutableListOf()
            binding.MashTemps.forEach {
                steps.add(
                        TempStep(
                                it.tvTempValueDeletable.text.toString().toInt(),
                                it.tvTempUnitDeletable.text.toString(),
                                it.tvTempDurationDeletable.text.toString().toInt()
                        )
                )

            }

            var ingredients: MutableList<Ingredient> = mutableListOf()
            binding.Ingredients.forEach {
                ingredients.add(
                        Ingredient(
                                it.tvIngredientNameDeletable.text.toString(),
                                it.tvIngredientUnitDeletable.text.toString(),
                                it.tvIngredientValueDeletable.text.toString().toInt()
                        )
                )

            }


            listener.onRecipeCreated(
                    BeerRecipe(100,
                            binding.etCreateName.text.toString(),
                            binding.etCreateTagline.text.toString(),
                            binding.etCreateDate.text.toString(),
                            binding.etCreateDescription.text.toString(),
                            binding.etCreateAbv.text.toString().toDouble(),
                            binding.etCreateIbu.text.toString().toInt(),
                            binding.etCreateTargetFg.text.toString().toInt(),
                            binding.etCreateTargetOg.text.toString().toInt(),
                            binding.etCreateEbc.text.toString().toInt(),
                            WaterVolume(binding.etCreateVolume.text.toString().toInt(),"liter"),
                            WaterVolume(binding.etCreateBoilVolume.text.toString().toInt(),"litre"),
                            steps,
                            FermentTemp(binding.etCreateFermentation.text.toString().toInt(),"Celsius"),
                            ingredients,
                            binding.etCreateYeast.text.toString()
                    )
            )
            dismiss()
        }

        binding.btnCancel.setOnClickListener {
            dismiss()
        }


    }

    private fun setAddIngredient() {
        binding.btnAddIngredient.setOnClickListener {
            val inflater = LayoutInflater.from(this.context).inflate(R.layout.ingredient_row_deletable, null, false)

            inflater.tvIngredientNameDeletable.text = binding.etIngredientName.text
            inflater.tvIngredientValueDeletable.text = binding.etIngredientValue.text
            inflater.tvIngredientUnitDeletable.text = binding.etIngredientUnit.text

            inflater.btnDeleteIngredient.setOnClickListener {
                binding.Ingredients.removeView(it.parent as View)
            }

            if(binding.etIngredientName.text.isNotEmpty() && binding.etIngredientValue.text.isNotEmpty() && binding.etIngredientUnit.text.isNotEmpty()) {
                binding.Ingredients.addView(inflater, binding.Ingredients.childCount)
            }
        }
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