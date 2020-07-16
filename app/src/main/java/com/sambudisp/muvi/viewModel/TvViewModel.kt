package com.sambudisp.muvi.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sambudisp.muvi.BuildConfig
import com.sambudisp.muvi.MuviApp
import com.sambudisp.muvi.model.response.TVResponseResult
import com.sambudisp.muvi.model.response.TvResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class TvViewModel : ViewModel() {
    companion object {
        private const val API_KEY = BuildConfig.API_KEY
    }

    val listTv = MutableLiveData<ArrayList<TVResponseResult>>()
    private var language = "id-ID"

    internal fun setListTv(keyword: String?) {
        if (Locale.getDefault().displayLanguage.toString() != "Indonesia") language = "en-US"

        if (keyword == null) {
            MuviApp.apiService
                .tv(API_KEY, language)
                .enqueue(object : Callback<TvResponse> {
                    override fun onFailure(call: Call<TvResponse>, t: Throwable) {
                        Log.d("onFailure", t.message.toString())
                    }

                    override fun onResponse(
                        call: Call<TvResponse>,
                        response: Response<TvResponse>
                    ) {
                        response.code().let {
                            try {
                                if (it == 200) {
                                    response.body()?.let {
                                        listTv.postValue(it.results)
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
                .tvSearch(API_KEY, language, keyword)
                .enqueue(object : Callback<TvResponse> {
                    override fun onFailure(call: Call<TvResponse>, t: Throwable) {
                        Log.d("onFailure", t.message.toString())
                    }

                    override fun onResponse(
                        call: Call<TvResponse>,
                        response: Response<TvResponse>
                    ) {
                        response.code().let {
                            try {
                                if (it == 200) {
                                    response.body()?.let {
                                        listTv.postValue(it.results)
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

    internal fun getListTv(): LiveData<ArrayList<TVResponseResult>> {
        return listTv
    }
}