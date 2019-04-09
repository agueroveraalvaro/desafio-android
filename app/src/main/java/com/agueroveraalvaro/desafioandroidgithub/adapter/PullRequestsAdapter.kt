package com.agueroveraalvaro.desafioandroidgithub.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.agueroveraalvaro.desafioandroidgithub.R
import com.agueroveraalvaro.desafioandroidgithub.interfaces.PullRequestItemClick
import com.agueroveraalvaro.desafioandroidgithub.model.PullRequest
import com.agueroveraalvaro.desafioandroidgithub.picasso.CropCircleTransformation
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.pull_request_item.view.*

import kotlinx.android.synthetic.main.repository_item.view.*

class PullRequestsAdapter(
    private var data: MutableList<PullRequest>,
    private val onPullRequestItemClick: PullRequestItemClick?
) : RecyclerView.Adapter<PullRequestsAdapter.ViewHolder>()
{
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.pull_request_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int)
    {
        val item = data.get(position)

        holder.txtTitle.text = item.title
        holder.txtBody.text = item.body
        holder.txtUsername.text = item.user.login
        holder.txtType.text = item.user.type

        Picasso
            .get()
            .load(item.user.avatarUrl)
            .transform(CropCircleTransformation())
            .error(R.mipmap.ic_git_user)
            .fit()
            .into(holder.imgOwner)

        holder.mView.setOnClickListener()
        {
            onPullRequestItemClick?.onPullRequestItemClick(item)
        }
    }

    fun add(pullRequests: List<PullRequest>?)
    {
        if (pullRequests != null)
        {
            for(pullRequest in pullRequests)
            {
                pullRequest.let { data.add(pullRequest) }
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

    override fun getItemCount(): Int = data.size

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView)
    {
        val txtTitle: TextView = mView.txtTitle
        val txtBody: TextView = mView.txtBody
        var txtUsername: TextView = mView.txtUsernamePullRequest
        val txtType: TextView = mView.txtTypePullRequest
        val imgOwner: ImageView = mView.imgOwnerPullRequest
    }
}
