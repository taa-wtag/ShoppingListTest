package com.example.shoppinglisttest.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.filters.MediumTest
import com.example.shoppinglisttest.R
import com.example.shoppinglisttest.data.local.ShoppingItem
import com.example.shoppinglisttest.getOrAwaitValue
import com.example.shoppinglisttest.launchFragmentInHiltContainer
import com.example.shoppinglisttest.repositories.FakeShoppingRepository
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.kotlin.verify
import javax.inject.Inject

@MediumTest
@HiltAndroidTest
@ExperimentalCoroutinesApi
class AddShoppingItemFragmentTest{

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var hiltRule = HiltAndroidRule(this)


    @Inject
    lateinit var fragmentFactory: TestShoppingFragmentFactory


    @Before
    fun setup(){
        hiltRule.inject()
    }

    @After
    fun teardown(){

    }

    @Test
    fun clickInsertIntoDb_shoppingItemInsertIntoDb(){
        val testViewModel = ShoppingViewModel(FakeShoppingRepository())
        launchFragmentInHiltContainer<AddShoppingItemFragment>(fragmentFactory=fragmentFactory){
            viewModel=testViewModel
        }
        onView(withId(R.id.etShoppingItemName)).perform(replaceText("shopping item"))
        onView(withId(R.id.etShoppingItemAmount)).perform(replaceText("5"))
        onView(withId(R.id.etShoppingItemPrice)).perform(replaceText("5.5"))
        onView(withId(R.id.btnAddShoppingItem)).perform(click())

        assertThat(testViewModel.shoppingItems.getOrAwaitValue()).contains(ShoppingItem("shopping item", 5, 5.5f,""))

    }

    @Test
    fun pressBackButton_popBackStack(){
        val navController = mock(NavController::class.java)
        launchFragmentInHiltContainer<AddShoppingItemFragment>(fragmentFactory=fragmentFactory) {
            Navigation.setViewNavController(requireView(), navController)
        }

        pressBack()

        verify(navController).popBackStack()
    }

    @Test
    fun pressBackButton_currURLEmptyString(){
        val navController = mock(NavController::class.java)
        val testViewModel = ShoppingViewModel(FakeShoppingRepository())
        testViewModel.setCurImageUrl("TEST")
        launchFragmentInHiltContainer<AddShoppingItemFragment>(fragmentFactory=fragmentFactory) {
            Navigation.setViewNavController(requireView(), navController)
            viewModel = testViewModel
        }

        pressBack()
        assertThat(testViewModel.curImageUrl.getOrAwaitValue()).isEqualTo("")

    }

    @Test
    fun clickAddShoppingItemPickImageView_navigateToImagePickFragment(){
        val navController = mock(NavController::class.java)
        launchFragmentInHiltContainer<AddShoppingItemFragment>(fragmentFactory=fragmentFactory) {
            Navigation.setViewNavController(requireView(), navController)
        }

        Espresso.onView(ViewMatchers.withId(R.id.ivShoppingImage)).perform(ViewActions.click())

        Mockito.verify(navController).navigate(
            AddShoppingItemFragmentDirections.actionAddShoppingItemFragmentToImagePickFragment()
        )
    }
}