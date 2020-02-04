package com.sambudisp.muvi.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sambudisp.muvi.MuviApp
import com.sambudisp.muvi.model.response.MovieDetailResponse
import com.sambudisp.muvi.model.response.TvDetailResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class ContentDetailViewModel : ViewModel() {
    companion object {
        private const val API_KEY = "79adf9839ac69fcd9a49792c1d0bafc7"
    }

    val detailTv = MutableLiveData<TvDetailResponse>()
    val detailMovie = MutableLiveData<MovieDetailResponse>()
    var type: String? = null
    private var language = "id-ID"

    internal fun setDetail(id: String?, type: String?) {
        if (Locale.getDefault().displayLanguage.toString() != "Indonesia") language = "en-US"
        this.type = type
        if (type == "tv") {
            MuviApp.apiService
                .tvDetail(id, API_KEY, language)
                .enqueue(object : Callback<TvDetailResponse> {
                    override fun onFailure(call: Call<TvDetailResponse>, t: Throwable) {
                        Log.d("onFailure", t.message.toString())
                    }

                    override fun onResponse(
                        call: Call<TvDetailResponse>,
                        response: Response<TvDetailResponse>
                    ) {
                        response.code().let {
                            try {
                                if (it == 200) {
                                    response.body()?.let {
                                        detailTv.postValue(it)
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
        } else {
            MuviApp.apiService
                .movieDetail(id, API_KEY, language)
                .enqueue(object : Callback<MovieDetailResponse> {
                    override fun onFailure(call: Call<MovieDetailResponse>, t: Throwable) {
                        Log.d("onFailure", t.message.toString())
                    }

                    override fun onResponse(
                        call: Call<MovieDetailResponse>,
                        response: Response<MovieDetailResponse>
                    ) {
                        response.code().let {
                            try {
                                if (it == 200) {
                                    response.body()?.let {
                                        detailMovie.postValue(it)
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
    }

    internal fun getDetailTv(): LiveData<TvDetailResponse> {
        return detailTv
    }

    internal fun getDetailMovie(): LiveData<MovieDetailResponse> {
        return detailMovie
    }
}