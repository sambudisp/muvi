package com.sambudisp.muvi.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sambudisp.muvi.MuviApp
import com.sambudisp.muvi.model.response.MovieResponse
import com.sambudisp.muvi.model.response.MovieResponseResult
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MovieViewModel : ViewModel() {
    companion object {
        private const val API_KEY = "79adf9839ac69fcd9a49792c1d0bafc7"
        private const val LANGUAGE_ID = "id-ID"
        private const val LANGUAGE_EN = "en-US"
    }

    val listMovie = MutableLiveData<ArrayList<MovieResponseResult>>()

    internal fun setListMovie() {
        MuviApp.apiService
            .movie(API_KEY, LANGUAGE_ID)
            .enqueue(object : Callback<MovieResponse> {
                override fun onFailure(call: Call<MovieResponse>, t: Throwable) {
                    Log.d("onFailure", t.message.toString())
                }

                override fun onResponse(
                    call: Call<MovieResponse>,
                    response: Response<MovieResponse>
                ) {
                    response.code().let {
                        try {
                            if (it == 200) {
                                response.body()?.let {
                                    listMovie.postValue(it.results)
                                }
                            } else {
                                Log.d(
                                    "onSuccessErr",
                                    "Code : ${it} | Msg : ${response.errorBody()?.string()}"
                                )
                            }
                        } catch (e: Exception) {
                            Log.d("Exception", e.message.toString())
                        }
                    }
                }
            })
    }

    internal fun getListMovie(): LiveData<ArrayList<MovieResponseResult>> {
        return listMovie
    }
}