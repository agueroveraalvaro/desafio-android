package com.agueroveraalvaro.desafioandroidgithub.main

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import com.agueroveraalvaro.desafioandroidgithub.R
import com.agueroveraalvaro.desafioandroidgithub.adapter.PullRequestsAdapter
import com.agueroveraalvaro.desafioandroidgithub.adapter.RepositoriesAdapter
import com.agueroveraalvaro.desafioandroidgithub.api.GitHubAPI
import com.agueroveraalvaro.desafioandroidgithub.interfaces.InterfaceAPI
import com.agueroveraalvaro.desafioandroidgithub.interfaces.PullRequestItemClick
import com.agueroveraalvaro.desafioandroidgithub.interfaces.RepositoryItemClick
import com.agueroveraalvaro.desafioandroidgithub.model.PullRequest
import com.agueroveraalvaro.desafioandroidgithub.model.RepositoriesAPI
import com.agueroveraalvaro.desafioandroidgithub.model.Repository
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener
import com.scwang.smartrefresh.layout.listener.OnRefreshListener

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.content.Intent
import android.net.Uri
import java.util.ArrayList

class RepositoriesFragment : Fragment(),RepositoryItemClick,PullRequestItemClick
{
    private lateinit var recyclerViewRepositories: RecyclerView
    private lateinit var adapterRepositories: RepositoriesAdapter
    private var interfaceApi: InterfaceAPI? = null
    private lateinit var refreshLayout: RefreshLayout
    var adapterPullRequests: PullRequestsAdapter? = null
    var progressBarPullRequests: ProgressBar? = null
    lateinit var dialogPullRequests: AlertDialog

    val PARAM_LANGUAGE = "language:Java"
    val PARAM_SORT = "stars"
    var PARAM_PAGE = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?
    {
        val view = inflater.inflate(R.layout.repositories_layout, container, false)

        loadElements(view)

        return view
    }

    override fun onSaveInstanceState(outState: Bundle)
    {
        super.onSaveInstanceState(outState)
        MyStaticClass.lastPositionScroll =  (recyclerViewRepositories.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
    }

    fun loadElements(view: View)
    {
        interfaceApi = GitHubAPI.getClient()?.create(InterfaceAPI::class.java)
        recyclerViewRepositories = view.findViewById(R.id.recyclerViewRepositories) as RecyclerView

        //if(columnCount <= 1)
            recyclerViewRepositories.layoutManager = LinearLayoutManager(context)
        //else
        //    recyclerViewRepositories.layoutManager = GridLayoutManager(context, 1)
        adapterRepositories = RepositoriesAdapter(MyStaticClass.staticRepositories,this)
        recyclerViewRepositories.adapter = adapterRepositories

        refreshLayout = view.findViewById<View>(R.id.refreshLayout) as RefreshLayout
        refreshLayout.setPrimaryColors(resources.getColor(R.color.colorBlack))
        refreshLayout.setOnRefreshListener(OnRefreshListener {
            PARAM_PAGE=1
            adapterRepositories.clearData(false)
            fetchRepositories()
        })
        refreshLayout.setOnLoadMoreListener(OnLoadMoreListener { refreshLayout ->
            //refreshLayout.finishLoadMore(1000/*,false*/)
            PARAM_PAGE++
            fetchRepositories()
        })

        if(MyStaticClass.staticRepositories.size==0)
            refreshLayout.autoRefresh()
        else
            recyclerViewRepositories.scrollToPosition(MyStaticClass.lastPositionScroll)

        if (MyStaticClass.isShowingRepository)
            showPullRequests(MyStaticClass.currentRepository!!)
    }


    fun fetchRepositories()
    {
        callRepositoryService()?.enqueue(object: Callback<RepositoriesAPI>
        {
            override fun onFailure(call: Call<RepositoriesAPI>, t: Throwable)
            {

            }

            override fun onResponse(call: Call<RepositoriesAPI>,
                                    response: Response<RepositoriesAPI>)
            {
                if(response.code() == 200)
                {
                    adapterRepositories.add(response.body()?.repositories)

                    if(PARAM_PAGE>1)
                        refreshLayout.finishLoadMore()
                    else
                        refreshLayout.finishRefresh()

                    MyStaticClass.staticRepositories = adapterRepositories.getData()
                }
            }
        })
    }

    private fun  callRepositoryService(): Call<RepositoriesAPI>?
    {
        return interfaceApi?.getRepositories(PARAM_LANGUAGE,PARAM_SORT,PARAM_PAGE)
    }

    override fun onRepositoryItemClick(repository: Repository)
    {
        showPullRequests(repository)
    }

    override fun onPullRequestItemClick(pullRequest: PullRequest)
    {
        val intent = Intent(Intent.ACTION_VIEW,Uri.parse(pullRequest.htmlUrl))
        startActivity(intent)
    }

    private fun showPullRequests(repository: Repository)
    {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_pull_requests,null)

        progressBarPullRequests = dialogView.findViewById(R.id.progressBar) as ProgressBar
        val txtName = dialogView.findViewById(R.id.txtName) as TextView
        val txtOpen = dialogView.findViewById(R.id.txtOpen) as TextView
        val txtWatchers = dialogView.findViewById(R.id.txtWatchers) as TextView
        val recyclerViewPullRequests = dialogView.findViewById(R.id.recyclerViewPullRequests) as RecyclerView
        recyclerViewPullRequests.layoutManager = LinearLayoutManager(context)
        adapterPullRequests = PullRequestsAdapter(mutableListOf(),this)
        recyclerViewPullRequests.adapter = adapterPullRequests

        txtName.text = repository.name
        txtOpen.text = repository.openIssuesCount.toString()
        txtWatchers.text = repository.watchersCount.toString()
        fetchPullRequests(repository.owner.login,repository.name)

        val builder = AlertDialog.Builder(context)
            .setView(dialogView)
            //.setTitle(title)
            .setPositiveButton("Aceptar", null)
            //.setNegativeButton("Cancel", null)
        dialogPullRequests = builder.show()

        MyStaticClass.isShowingRepository = true
        MyStaticClass.currentRepository = repository

        dialogPullRequests.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener{
            dialogPullRequests.dismiss()
        }
        dialogPullRequests.setOnDismissListener(DialogInterface.OnDismissListener {
            MyStaticClass.isShowingRepository = false
            MyStaticClass.currentRepository = null
        })
    }

    fun fetchPullRequests(login:String,name:String)
    {
        callPullRequestsService(login,name)?.enqueue(object: Callback<List<PullRequest>>
        {
            override fun onFailure(call: Call<List<PullRequest>>,t: Throwable)
            {
                println("Error : "+t.localizedMessage)
            }

            override fun onResponse(call: Call<List<PullRequest>>,
                                    response: Response<List<PullRequest>>
            ) {
                if(response.code() == 200)
                {
                    adapterPullRequests?.add(response.body())
                    progressBarPullRequests?.visibility = View.GONE
                }
            }
        })
    }

    private fun callPullRequestsService(login:String,name:String): Call<List<PullRequest>>?
    {
        return interfaceApi?.getPullRequestsRepository(login,name)
    }
}

class MyStaticClass {
    companion object {
        var staticRepositories = ArrayList<Repository>()
        var lastPositionScroll: Int = 0
        var isShowingRepository: Boolean = false
        var currentRepository: Repository? = null
    }
}
