package hu.bme.aut.android.srm.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import hu.bme.aut.android.srm.databinding.RowRecipeBinding
import hu.bme.aut.android.srm.model.BeerRecipe


class FoundRecipiesAdapter(
    private val context: Context,
    private val beerRecipeList: MutableList<BeerRecipe> )
    : RecyclerView.Adapter<FoundRecipiesAdapter.ViewHolder>() {

    var itemClickListener: BeerFoundRecipeItemClickListener? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(RowRecipeBinding.inflate(
        LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val beerRecipe = beerRecipeList[position]

        holder.beerRecipe = beerRecipe

        holder.binding.tvName.text = beerRecipe.name
        holder.binding.tvTagline.text = beerRecipe.tagline

    }

    override fun getItemCount() = beerRecipeList.size


    inner class ViewHolder(val binding: RowRecipeBinding) : RecyclerView.ViewHolder(binding.root) {
        var beerRecipe: BeerRecipe? = null

        init {
            itemView.setOnClickListener {
                beerRecipe?.let { beerRecipe -> itemClickListener?.onItemClick(beerRecipe) }
            }

            itemView.setOnLongClickListener { view ->
                beerRecipe?.let { beerRecipe ->  itemClickListener?.onItemLongClick(view,beerRecipe) }
                true
            }
        }
    }

    interface BeerFoundRecipeItemClickListener {
        fun onItemClick(beerRecipe: BeerRecipe)
        fun onItemLongClick(view: View, recipe : BeerRecipe): Boolean
    }


}