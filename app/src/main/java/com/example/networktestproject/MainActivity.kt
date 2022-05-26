package com.example.networktestproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.networktestproject.databinding.ActivityMainBinding
import retrofit2.HttpException
import java.io.IOException

private const val TAG = "MainActivity"
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var todoAdapter: TodoAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpRecyclerView()
        lifecycleScope.launchWhenCreated {
            binding.mainProgressBar.isVisible = true

            val response= try {
                RetrofitInstance.api.getTodos()
            }catch (e: IOException){
                Log.d(TAG, "IOException")
                return@launchWhenCreated
            } catch (e: HttpException){
                Log.d(TAG, "Http exception")
                return@launchWhenCreated
            }
            if (response.isSuccessful && response.body() != null){
                todoAdapter.todos = response.body()!!
            }else{
                Log.d(TAG, "Response not successful")
            }
            binding.mainProgressBar.isVisible = false
        }


    }

    private fun setUpRecyclerView() = binding.rvTodos.apply {
        todoAdapter = TodoAdapter()
        adapter= todoAdapter
        layoutManager = LinearLayoutManager(this@MainActivity)
    }
}