package com.azhar.newsapp.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.azhar.newsapp.R
import com.azhar.newsapp.adapter.NewsAdapter
import com.azhar.newsapp.model.ModelArticle
import com.azhar.newsapp.model.ModelNews
import com.azhar.newsapp.networking.ApiEndpoint.getApiClient
import com.azhar.newsapp.networking.ApiInterface
import com.azhar.newsapp.util.Utils.getCountry
import kotlinx.android.synthetic.main.fragement_news.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*



class FragmentTechnology : Fragment() {

    companion object {
        const val API_KEY = "API KEY ada di Video YT"
    }

    var strCategory = "technology"
    var strCountry: String? = null
    var modelArticle: MutableList<ModelArticle> = ArrayList()
    var newsAdapter: NewsAdapter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragement_news, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tvTitle.setText("Berita Teknologi")

        rvListNews.setLayoutManager(LinearLayoutManager(context))
        rvListNews.setHasFixedSize(true)
        rvListNews.showShimmerAdapter()

        //reload news
        imageRefresh.setOnClickListener {
            rvListNews.showShimmerAdapter()
            getListNews()
        }

        //get news
        getListNews()
    }

    //set api
    private fun getListNews() {

            //get country/
            strCountry = getCountry()

            //set api
            val apiInterface = getApiClient().create(ApiInterface::class.java)
            val call = apiInterface.getTechnology(strCountry, strCategory, API_KEY)
            call.enqueue(object : Callback<ModelNews> {
                override fun onResponse(call: Call<ModelNews>, response: Response<ModelNews>) {
                    if (response.isSuccessful && response.body() != null) {
                        modelArticle = response.body()?.modelArticle as MutableList<ModelArticle>
                        newsAdapter = NewsAdapter(modelArticle, context!!)
                        rvListNews.adapter = newsAdapter
                        newsAdapter?.notifyDataSetChanged()
                        rvListNews.hideShimmerAdapter()
                    }
                }

                override fun onFailure(call: Call<ModelNews>, t: Throwable) {
                    Toast.makeText(context, "Oops, jaringan kamu bermasalah.", Toast.LENGTH_SHORT).show()
                }
            })
        }

}