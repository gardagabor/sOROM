package hu.bme.aut.android.srm.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import hu.bme.aut.android.srm.databinding.RowRecipeBinding
import hu.bme.aut.android.srm.model.*

class MyRecipiesAdapter : RecyclerView.Adapter<MyRecipiesAdapter.ViewHolder>() {

    private val beerRecipeList = mutableListOf<BeerRecipe>()

    var itemClickListener: BeerRecipeItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(RowRecipeBinding.inflate(
            LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val beerRecipe = beerRecipeList[position]

        holder.beerRecipe = beerRecipe

        holder.binding.tvName.text = beerRecipe.name
        holder.binding.tvTagline.text = beerRecipe.tagline

    }

    fun getBeerRecipeList() : MutableList<BeerRecipe> = beerRecipeList

    fun addItem(beerRecipe: BeerRecipe) {
        val size = beerRecipeList.size
        beerRecipeList.add(beerRecipe)
        notifyItemInserted(size)
    }

    fun addAll(beerRecipes: List<BeerRecipe>) {
        val size = beerRecipes.size
        beerRecipeList += beerRecipes
        notifyItemRangeInserted(size, beerRecipes.size)
    }


    fun deleteRow(position: Int) {
        beerRecipeList.removeAt(position)
        notifyItemRemoved(position)
    }

    fun deleteElement(beerRecipe: BeerRecipe){
        var index = beerRecipeList.indexOf(beerRecipe)
        deleteRow(index)

    }



    override fun getItemCount() = beerRecipeList.size

    inner class ViewHolder(val binding: RowRecipeBinding) : RecyclerView.ViewHolder(binding.root) {
        var beerRecipe: BeerRecipe? = null

        init {
            itemView.setOnClickListener {
                beerRecipe?.let { beerRecipe -> itemClickListener?.onItemClick(beerRecipe) }
            }

            itemView.setOnLongClickListener { view ->
                 beerRecipe?.let { beerRecipe ->  itemClickListener?.onItemLongClick(adapterPosition,view,beerRecipe) }
                true
            }
        }
    }

    interface BeerRecipeItemClickListener {
        fun onItemClick(beerRecipe: BeerRecipe)
        fun onItemLongClick(position: Int, view: View, recipe : BeerRecipe): Boolean
    }


}