package com.agueroveraalvaro.desafioandroidgithub

import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import br.com.concretesolutions.kappuccino.custom.recyclerView.RecyclerViewInteractions.recyclerView
import com.agueroveraalvaro.desafioandroidgithub.main.MainActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RepositoriesTest
{
    @Rule
    @JvmField
    var mActivityRule = ActivityTestRule<MainActivity>(MainActivity::class.java, false, true)

    @Test
    fun checkSizeRecyclerView() {
        recyclerView(R.id.recyclerViewRepositories) {
            sizeIs(30)
        }
        //onView(withId(R.id.recyclerViewRepositories))
    }

    @Test
    fun displayedAtZero()
    {
        recyclerView(R.id.recyclerViewRepositories)
        {
            atPosition(0)
            {
                displayed{
                    text("Position 0")
                    //text("ANDROID")
                }
            }
        }
    }
}