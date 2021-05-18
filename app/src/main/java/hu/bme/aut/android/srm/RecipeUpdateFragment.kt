package hu.bme.aut.android.srm

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.forEach
import androidx.fragment.app.DialogFragment
import hu.bme.aut.android.srm.databinding.FragmentCreateBinding
import hu.bme.aut.android.srm.model.*
import kotlinx.android.synthetic.main.ingredient_row_deletable.view.*
import kotlinx.android.synthetic.main.temperature_step_deletable.view.*

class RecipeUpdateFragment(val recipe : BeerRecipe) : DialogFragment() {
    private lateinit var listener: RecipeUpdatedListener
    private lateinit var binding: FragmentCreateBinding

    override fun onAttach(context: Context) {
        super.onAttach(context)

        try {
            listener = if (targetFragment != null) {
                targetFragment as RecipeUpdatedListener
            } else {
                activity as RecipeUpdatedListener
            }
        } catch (e: ClassCastException) {
            throw RuntimeException(e)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentCreateBinding.inflate(inflater, container, false)
        dialog?.setTitle("Update")

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setAddTemperature()

        setAddIngredient()

        initRecipe()

        binding.btnCreateRecipe.setOnClickListener {

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
                                it.tvIngredientValueDeletable.text.toString().toDouble(),
                                it.tvIngredientUnitDeletable.text.toString()
                        )
                )

            }


            listener.onRecipeUpdated(recipe,
                    BeerRecipe(100,
                            binding.etCreateName?.text!!.toString(),
                            binding.etCreateTagline?.text!!.toString(),
                            binding.etCreateDate?.text!!.toString(),
                            binding.etCreateDescription?.text!!.toString(),
                            binding.etCreateAbv?.text!!.toString().toDouble(),
                            binding.etCreateIbu?.text!!.toString().toInt(),
                            binding.etCreateTargetFg?.text!!.toString().toInt(),
                            binding.etCreateTargetOg?.text!!.toString().toInt(),
                            binding.etCreateEbc?.text!!.toString().toInt(),
                            WaterVolume(binding.etCreateVolume?.text!!.toString().toInt(), "liter"),
                            WaterVolume(binding.etCreateBoilVolume?.text!!.toString().toInt(), "litre"),
                            steps,
                            FermentTemp(binding.etCreateFermentation?.text!!.toString().toInt(), "Celsius"),
                            ingredients,
                            binding.etCreateYeast?.text!!.toString()
                    )
            )
            dismiss()
        }

        binding.btnCancel.setOnClickListener {
            dismiss()
        }


    }

    private fun initRecipe() {
        binding.etCreateName.setText(recipe.name)
        binding.etCreateTagline.setText(recipe.tagline)
        binding.etCreateDate.setText(recipe.date)
        binding.etCreateDescription.setText(recipe.description)
        binding.etCreateAbv.setText(recipe.abv.toString())
        binding.etCreateIbu.setText(recipe.ibu.toString())
        binding.etCreateTargetFg.setText(recipe.targetFg.toString())
        binding.etCreateTargetOg.setText(recipe.targetOg.toString())
        binding.etCreateEbc.setText(recipe.ebc.toString())
        binding.etCreateVolume.setText(recipe.volume.value.toString())
        binding.etCreateBoilVolume.setText(recipe.boilVolume.value.toString())

        fillBindingWithMashSteps()

        binding.etCreateFermentation.setText(recipe.fermentation.value.toString())

        fillBindingWithIngredients()

        binding.etCreateYeast.setText(recipe.yeast)

        binding.btnCreateRecipe.text = "Modify"
    }

    private fun fillBindingWithIngredients() {
        recipe.ingredients.forEach{
            val inflater = LayoutInflater.from(this.context).inflate(R.layout.ingredient_row_deletable, null, false)

            inflater.tvIngredientNameDeletable.text = it.name
            inflater.tvIngredientValueDeletable.text = it.value.toString()
            inflater.tvIngredientUnitDeletable.text = it.unit

            inflater.btnDeleteIngredient.setOnClickListener {
                binding.Ingredients.removeView(it.parent as View)
            }

            binding.Ingredients.addView(inflater, binding.Ingredients.childCount)
        }
    }

    private fun fillBindingWithMashSteps() {
        recipe.mashTemps.forEach{
            val inflater = LayoutInflater.from(this.context).inflate(R.layout.temperature_step_deletable, null, false)

            inflater.tvTempValueDeletable.text = it.value.toString()
            inflater.tvTempUnitDeletable.text = it.unit
            inflater.tvTempDurationDeletable.text = it.duration.toString()
            inflater.btnDeleteTemp.setOnClickListener {
                binding.MashTemps.removeView(it.parent as View)
            }

            binding.MashTemps.addView(inflater, binding.MashTemps.childCount)
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

            if (binding.etIngredientName.text.isNotEmpty() && binding.etIngredientValue.text.isNotEmpty() && binding.etIngredientUnit.text.isNotEmpty()) {
                binding.Ingredients.addView(inflater, binding.Ingredients.childCount)
            }
        }
    }

    private fun setAddTemperature() {
        binding.btnAddMashStep.setOnClickListener {
            val inflater = LayoutInflater.from(this.context).inflate(R.layout.temperature_step_deletable, null, false)

            inflater.tvTempValueDeletable.text = binding.etTempValue.text
            inflater.tvTempUnitDeletable.text = binding.etTempUnit.text
            inflater.tvTempDurationDeletable.text = binding.etTempDuration.text
            inflater.btnDeleteTemp.setOnClickListener {
                binding.MashTemps.removeView(it.parent as View)
            }

            if (binding.etTempValue.text.isNotEmpty() && binding.etTempUnit.text.isNotEmpty() && binding.etTempDuration.text.isNotEmpty()) {
                binding.MashTemps.addView(inflater, binding.MashTemps.childCount)
            }
        }
    }


    interface RecipeUpdatedListener {
        fun onRecipeUpdated(oldRecipe: BeerRecipe,newRecipe : BeerRecipe)
    }
}
