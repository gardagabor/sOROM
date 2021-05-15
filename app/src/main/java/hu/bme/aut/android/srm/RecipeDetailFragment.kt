package hu.bme.aut.android.srm

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import hu.bme.aut.android.srm.databinding.RecipeDetailBinding
import hu.bme.aut.android.srm.model.*
import kotlinx.android.synthetic.main.ingredient_row.view.*
import kotlinx.android.synthetic.main.temperature_step.view.*


class RecipeDetailFragment : Fragment() {
    private var selectedRecipe: BeerRecipe? = null
    private lateinit var binding: RecipeDetailBinding

    companion object {

        private const val KEY_RECIPE = "KEY_RECIPE"

        fun newInstance(recipe : BeerRecipe): RecipeDetailFragment{
            val result = RecipeDetailFragment()

            val args = Bundle()
            args.putSerializable(KEY_RECIPE, recipe)

            result.arguments = args
            return result
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        selectedRecipe = arguments?.getSerializable(KEY_RECIPE) as BeerRecipe?
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = RecipeDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvId.text = selectedRecipe?.id.toString()
        binding.tvBeerName.text = selectedRecipe?.name
        binding.tvBeerTagline.text = selectedRecipe?.tagline
        binding.tvDate.text = selectedRecipe?.date
        binding.tvDescription.text = selectedRecipe?.description
        binding.tvAbv.text = selectedRecipe?.abv.toString()
        binding.tvIbu.text = selectedRecipe?.ibu.toString()
        binding.tvTargetFg.text = selectedRecipe?.targetFg.toString()
        binding.tvTargetOg.text = selectedRecipe?.targetOg.toString()
        binding.tvEbc.text = selectedRecipe?.ebc.toString()
        binding.tvVolumeValue.text = selectedRecipe?.volume?.value.toString()
        binding.tvVolumeUnit.text = selectedRecipe?.volume?.unit
        binding.tvVolumeValue.text = selectedRecipe?.volume?.value.toString()
        binding.tvVolumeUnit.text = selectedRecipe?.volume?.unit
        binding.tvBoilVolumeValue.text = selectedRecipe?.boilVolume?.value.toString()
        binding.tvBoilVolumeUnit.text = selectedRecipe?.boilVolume?.unit

        for(temp : TempStep  in selectedRecipe?.mashTemps!!){
            addIngredientToView(temp)
        }

        binding.tvFermentValue.text = selectedRecipe?.fermentation?.value.toString()
        binding.tvFermentUnit.text = selectedRecipe?.fermentation?.unit

        for(ing : Ingredient  in selectedRecipe?.ingredients!!){
            addIngredientToView(ing)
        }

        binding.tvYeast.text = selectedRecipe?.yeast;

    }

    private fun addIngredientToView(temp : TempStep){
        val inflater = LayoutInflater.from(this.context).inflate(R.layout.temperature_step, null,false)

        inflater.tvTempValue.text = temp.value.toString()
        inflater.tvTempUnit.text = temp.unit
        inflater.tvTempDuration.text = temp.duration.toString()

        binding.DetailMashTempLayout.addView(inflater,binding.DetailMashTempLayout.childCount)
    }

    private fun addIngredientToView(ing : Ingredient){
        val inflater = LayoutInflater.from(this.context).inflate(R.layout.ingredient_row, null,false)

        inflater.tvIngredientName.text = ing.name
        inflater.tvIngredientValue.text = ing.value.toString()
        inflater.tvIngredientUnit.text = ing.unit

        binding.DetailIngredientsLayout.addView(inflater,binding.DetailIngredientsLayout.childCount)
    }

}