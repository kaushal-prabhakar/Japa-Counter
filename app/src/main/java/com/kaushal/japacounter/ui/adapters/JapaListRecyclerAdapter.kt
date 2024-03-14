
package com.kaushal.japacounter.ui.adapters

import android.view.ViewGroup
import android.widget.AdapterView.OnItemSelectedListener
import androidx.recyclerview.widget.RecyclerView
import com.kaushal.japacounter.data.Entities
import com.kaushal.japacounter.databinding.ItemJapaListBinding
import com.kaushal.japacounter.ext.layoutInflator

class JapaListRecyclerAdapter(private val myJapaList: MutableList<Entities>, private val onItemSelectedListener: (String) -> Unit) : RecyclerView.Adapter<JapaListRecyclerAdapter.ViewHolder>() {

    class ViewHolder(private val binding: ItemJapaListBinding, private val onItemSelectedListener: (String) -> Unit): RecyclerView.ViewHolder(binding.root) {
        fun bind(japasList: MutableList<Entities>, position: Int) {
            val item = japasList[position]
            binding.txtName.text = item.name
            binding.txtGoal.text = item.goal.toString()
            binding.txtCurrentCount.text = item.currentValue.toString()
            binding.txtLastUpdatedTime.text = item.lastUpdatedTime.toString()
            binding.txtStatus.text = item.status.name

            binding.japaInfoCard.setOnClickListener {
                onItemSelectedListener.invoke(item.name)
            }
        }

        companion object {
            fun create(parent: ViewGroup, onItemSelectedListener: (String) -> Unit): ViewHolder {
                return ViewHolder(ItemJapaListBinding.inflate(parent.layoutInflator, parent, false), onItemSelectedListener)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.create(parent = parent, onItemSelectedListener)
    }

    override fun getItemCount(): Int = myJapaList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(myJapaList, position)
    }
}
