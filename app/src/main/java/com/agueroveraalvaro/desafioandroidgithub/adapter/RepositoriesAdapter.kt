package com.agueroveraalvaro.desafioandroidgithub.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.agueroveraalvaro.desafioandroidgithub.R
import com.agueroveraalvaro.desafioandroidgithub.interfaces.RepositoryItemClick
import com.agueroveraalvaro.desafioandroidgithub.model.Repository
import com.agueroveraalvaro.desafioandroidgithub.picasso.CropCircleTransformation
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso

import kotlinx.android.synthetic.main.repository_item.view.*

class RepositoriesAdapter(
    private var data: ArrayList<Repository>,
    private val onRepositoryItemClick: RepositoryItemClick?
) : RecyclerView.Adapter<RepositoriesAdapter.ViewHolder>()
{
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.repository_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int)
    {
        val item = data.get(position)

        holder.txtName.text = item.name
        holder.txtDescription.text = item.description
        holder.txtForks.text = item.forks.toString()
        holder.txtStars.text = item.stargazersCount.toString()
        holder.txtUsername.text = item.owner.login
        holder.txtType.text = item.owner.type

        Picasso
            .get()
            .load(item.owner.avatarUrl)
            .transform(CropCircleTransformation())
            .error(R.mipmap.ic_git_user)
            .fit()
            .into(holder.imgOwner)

        holder.mView.setOnClickListener()
        {
            onRepositoryItemClick?.onRepositoryItemClick(item)
        }
    }

    fun add(repositories: List<Repository>?)
    {
        if (repositories != null)
        {
            for(repository in repositories)
            {
                repository.let { data.add(repository) }
                //notifyItemInserted(data.size - 1)
            }
        }
        notifyDataSetChanged()
    }

    fun clearData(notifyDataSetChanged_:Boolean)
    {
        data.removeAll { true }
        if (notifyDataSetChanged_)
            notifyDataSetChanged()
    }

    fun getData(): ArrayList<Repository> = data

    override fun getItemCount(): Int = data.size

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView)
    {
        val txtName: TextView = mView.txtName
        val txtDescription: TextView = mView.txtDescription
        val txtForks: TextView = mView.txtForks
        val txtStars: TextView = mView.txtStars
        val txtUsername: TextView = mView.txtUsername
        val txtType: TextView = mView.txtType
        val imgOwner: ImageView = mView.imgOwner

        /*override fun toString(): String
        {
            return super.toString() + " '" + txtName.text + "'"
        }*/
    }
}
