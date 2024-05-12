package com.example.shoppinglisttest.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.shoppinglisttest.getOrAwaitValue
import com.example.shoppinglisttest.other.Constants
import com.example.shoppinglisttest.other.Status
import com.example.shoppinglisttest.repositories.FakeShoppingRepository
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class ShoppingViewModelTest{

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val dispatcher = StandardTestDispatcher()

    private lateinit var viewModel: ShoppingViewModel

    @Before
    fun setUp() {
        viewModel = ShoppingViewModel(FakeShoppingRepository())
        Dispatchers.setMain(dispatcher)
    }

    @After
    fun tearDown() {
    }

    @Test
    fun `insert shopping item with empty field returns error`(){
        viewModel.insertShoppingItem("name","","3.0")
        val value = viewModel.insertShoppingItemStatus.getOrAwaitValue()
        dispatcher.scheduler.advanceUntilIdle()
        assertThat(value.getContentIfNotHandled()?.status).isEqualTo(Status.ERROR)
    }

    @Test
    fun `insert shopping item with too long name returns error`(){
        val string = buildString { for(i in 1..Constants.MAX_NAME_LENGTH + 1) append(1) }
        viewModel.insertShoppingItem(string,"5","3.0")
        val value = viewModel.insertShoppingItemStatus.getOrAwaitValue()
        dispatcher.scheduler.advanceUntilIdle()
        assertThat(value.getContentIfNotHandled()?.status).isEqualTo(Status.ERROR)
    }

    @Test
    fun `insert shopping item with too long price returns error`(){
        val string = buildString { for(i in 1..Constants.MAX_PRICE_LENGTH + 1) append(1) }
        viewModel.insertShoppingItem("name","5",string)
        val value = viewModel.insertShoppingItemStatus.getOrAwaitValue()
        dispatcher.scheduler.advanceUntilIdle()
        assertThat(value.getContentIfNotHandled()?.status).isEqualTo(Status.ERROR)
    }

    @Test
    fun `insert shopping item with too high amount returns error`(){
        viewModel.insertShoppingItem("name","999999999999999999999999999999999","3.0")
        val value = viewModel.insertShoppingItemStatus.getOrAwaitValue()
        dispatcher.scheduler.advanceUntilIdle()
        assertThat(value.getContentIfNotHandled()?.status).isEqualTo(Status.ERROR)
    }

    @Test
    fun `insert shopping item with valid input returns success`(){
        viewModel.insertShoppingItem("name","5","3.0")
        val value = viewModel.insertShoppingItemStatus.getOrAwaitValue()
        dispatcher.scheduler.advanceUntilIdle()
        assertThat(value.getContentIfNotHandled()?.status).isEqualTo(Status.SUCCESS)
    }

}