package com.agueroveraalvaro.desafioandroidgithub.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.agueroveraalvaro.desafioandroidgithub.R


import com.agueroveraalvaro.desafioandroidgithub.main.RepositoriesFragment.OnListFragmentInteractionListener
import com.agueroveraalvaro.desafioandroidgithub.model.Repository

import kotlinx.android.synthetic.main.repository_item.view.*

/**
 * [RecyclerView.Adapter] that can display a [DummyItem] and makes a call to the
 * specified [OnListFragmentInteractionListener].
 * TODO: Replace the implementation with code for your data type.
 */
class RepositoriesAdapter(
    private var data: MutableList<Repository>,
    private val mListener: OnListFragmentInteractionListener?
) : RecyclerView.Adapter<RepositoriesAdapter.ViewHolder>()
{
    private val mOnClickListener: View.OnClickListener

    init {
        mOnClickListener = View.OnClickListener { v ->
            val item = v.tag as Repository
            // Notify the active callbacks interface (the activity, if the fragment is attached to
            // one) that an item has been selected.
            mListener?.onListFragmentInteraction(item)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.repository_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data.get(position)
        holder.mIdView.text = item.id.toString()
        holder.mContentView.text = item.name

        with(holder.mView) {
            tag = item
            setOnClickListener(mOnClickListener)
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

    override fun getItemCount(): Int = data.size

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val mIdView: TextView = mView.item_number
        val mContentView: TextView = mView.content

        override fun toString(): String {
            return super.toString() + " '" + mContentView.text + "'"
        }
    }
}
