package com.example.shoppinglisttest.ui

import android.content.Context
import androidx.navigation.fragment.NavHostFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainNavHostFragment:NavHostFragment() {

    @Inject
    lateinit var fragmentFactory: ShoppingFragmentFactory

    override fun onAttach(context: Context) {
        super.onAttach(context)
        childFragmentManager.fragmentFactory=fragmentFactory
    }
}