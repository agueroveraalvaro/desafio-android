package com.agueroveraalvaro.desafioandroidgithub.main

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.agueroveraalvaro.desafioandroidgithub.R
import com.agueroveraalvaro.desafioandroidgithub.adapter.RepositoriesAdapter
import com.agueroveraalvaro.desafioandroidgithub.api.GitHubAPI
import com.agueroveraalvaro.desafioandroidgithub.interfaces.InterfaceAPI
import com.agueroveraalvaro.desafioandroidgithub.model.ApiGitHubJSON
import com.agueroveraalvaro.desafioandroidgithub.model.Repository

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * A fragment representing a list of Items.
 * Activities containing this fragment MUST implement the
 * [RepositoriesFragment.OnListFragmentInteractionListener] interface.
 */
class RepositoriesFragment : Fragment()
{
    private lateinit var recyclerViewRepositories: RecyclerView
    private lateinit var adapterRepositories: RepositoriesAdapter
    private var columnCount = 1
    private var listener: OnListFragmentInteractionListener? = null
    private  var interfaceApi: InterfaceAPI? = null

    val PARAM_LANGUAGE = "language:Java"
    val PARAM_SORT = "stars"
    val PARAM_PAGE = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            columnCount = it.getInt(ARG_COLUMN_COUNT)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?
    {
        val view = inflater.inflate(R.layout.repositories_layout, container, false)

        loadElements(view)

        return view
    }

    fun loadElements(view: View)
    {
        interfaceApi = GitHubAPI.getClient()?.create(InterfaceAPI::class.java)
        recyclerViewRepositories = view.findViewById(R.id.recyclerViewRepositories) as RecyclerView

        if(columnCount <= 1)
            recyclerViewRepositories.layoutManager = LinearLayoutManager(context)
        else
            recyclerViewRepositories.layoutManager = GridLayoutManager(context, 1)

        adapterRepositories = RepositoriesAdapter(mutableListOf(), listener)
        recyclerViewRepositories.adapter = adapterRepositories

        fetchRepositories()
    }

    fun fetchRepositories()
    {
        callRepositoryService()?.enqueue(object: Callback<ApiGitHubJSON>
        {
            override fun onFailure(call: Call<ApiGitHubJSON>,t: Throwable)
            {

            }

            override fun onResponse(call: Call<ApiGitHubJSON>,
                                    response: Response<ApiGitHubJSON>)
            {
                if(response.code() == 200)
                {
                    adapterRepositories.add(response.body()?.repositories)
                    //progressBar.visibility = View.GONE
                }
            }
        })
    }

    private fun  callRepositoryService(): Call<ApiGitHubJSON>?
    {
        return interfaceApi?.getRepositories(PARAM_LANGUAGE,PARAM_SORT,PARAM_PAGE)
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson
     * [Communicating with Other Fragments](http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
     */
    interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onListFragmentInteraction(item: Repository?)
    }

    companion object {

        // TODO: Customize parameter argument names
        const val ARG_COLUMN_COUNT = "column-count"

        // TODO: Customize parameter initialization
        @JvmStatic
        fun newInstance(columnCount: Int) =
            RepositoriesFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_COLUMN_COUNT, columnCount)
                }
            }
    }
}
