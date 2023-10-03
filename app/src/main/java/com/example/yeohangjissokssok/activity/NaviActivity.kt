package com.example.yeohangjissokssok.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.yeohangjissokssok.R
import com.example.yeohangjissokssok.databinding.ActivityNaviBinding
import com.example.yeohangjissokssok.fragment.HomeFragment
import com.example.yeohangjissokssok.fragment.RecommendFragment
import com.example.yeohangjissokssok.fragment.SearchFragment

private const val TAG_HOME = "home_fragment"
private const val TAG_SEARCH = "search_fragment"
private const val TAG_RECOMMEND = "recommend_fragment"

class NaviActivity : AppCompatActivity() {

    private lateinit var binding : ActivityNaviBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNaviBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //setContentView(R.layout.activity_navi)

        setFragment(TAG_HOME, HomeFragment())

        binding.navigationView.setOnItemSelectedListener { item ->
            when(item.itemId){
                R.id.homeFragment -> setFragment(TAG_HOME, HomeFragment())
                //R.id.searchFragment -> setFragment(TAG_SEARCH, SearchResultFragment())
                R.id.searchFragment -> setFragment(TAG_SEARCH, SearchFragment())
                R.id.recommendFragment -> setFragment(TAG_RECOMMEND, RecommendFragment())
            }
            true
        }
    }

    private fun setFragment(tag: String, fragment: Fragment){
        val manager: FragmentManager = supportFragmentManager
        val fragTransaction = manager.beginTransaction()

        if (manager.findFragmentByTag(tag) == null){
            fragTransaction.add(R.id.mainFrameLayout, fragment, tag)
        }

        val home = manager.findFragmentByTag(TAG_HOME)
        val search = manager.findFragmentByTag(TAG_SEARCH)
        val recommend = manager.findFragmentByTag(TAG_RECOMMEND)

        if(home != null){
            fragTransaction.hide(home)
        }
        if(search != null){
            fragTransaction.hide(search)
        }
        if(recommend != null){
            fragTransaction.hide(recommend)
        }

        if(tag == TAG_HOME){
            if (home!=null){
                fragTransaction.show(home)
            }
        }
        else if(tag == TAG_SEARCH){
            if (search!=null){
                fragTransaction.show(search)
            }
        }
        else if(tag == TAG_RECOMMEND){
            if (recommend!=null){
                fragTransaction.show(recommend)
            }
        }
        fragTransaction.commitAllowingStateLoss()
    }
}