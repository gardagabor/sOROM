package hu.bme.aut.android.srm

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import hu.bme.aut.android.srm.databinding.RecipeDetailBinding
import hu.bme.aut.android.srm.model.*

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

    }
}