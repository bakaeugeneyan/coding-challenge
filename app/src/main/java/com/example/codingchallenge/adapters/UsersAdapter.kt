package com.example.codingchallenge.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.codingchallenge.utils.UserDiffUtil
import com.example.codingchallenge.databinding.ItunesListPlaceholderRowLayoutBinding
import com.example.codingchallenge.models.Data
import com.example.codingchallenge.models.UserList

class UsersAdapter : RecyclerView.Adapter<UsersAdapter.MyViewHolder>() {

    private var users = emptyList<Data>()

    class MyViewHolder(private val binding: ItunesListPlaceholderRowLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(result: Data){
            binding.result = result
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): MyViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItunesListPlaceholderRowLayoutBinding.inflate(layoutInflater, parent, false)
                return  MyViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return  MyViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
       val currentItunes = users[position]
        holder.bind(currentItunes)
    }

    override fun getItemCount(): Int {
        return users.size
    }

    fun setData(newData: UserList) {
        val itunesDiffUtil = UserDiffUtil(users, newData.data)
        val diffUtilResult = DiffUtil.calculateDiff(itunesDiffUtil)
        users = newData.data
        diffUtilResult.dispatchUpdatesTo(this)
    }
}