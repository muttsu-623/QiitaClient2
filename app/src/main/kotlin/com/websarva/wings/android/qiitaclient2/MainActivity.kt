package com.websarva.wings.android.qiitaclient2

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.ProgressBar
import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import com.trello.rxlifecycle.components.support.RxAppCompatActivity
import com.trello.rxlifecycle.kotlin.bindToLifecycle
import com.websarva.wings.android.qiitaclient2.client.ArticleClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import org.jetbrains.anko.setContentView

class MainActivity : RxAppCompatActivity() {

    val ui: MainActivityUI by lazy {
        MainActivityUI().apply { setContentView(this@MainActivity) }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val listAdapter = ArticleListAdapter(applicationContext)
        ui.listView.adapter = listAdapter
        ui.listView.setOnItemClickListener { adapterView, view, position, id ->
            val intent = ArticleActivity.intent(this, listAdapter.articles[position])
            startActivity(intent)
        }

        val gson = GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create()

        val retrofit = Retrofit.Builder()
                .baseUrl("https://qiita.com")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build()
        val articleClient = retrofit.create(ArticleClient::class.java)

        ui.searchButton.setOnClickListener {
            ui.progressBar.visibility = View.VISIBLE

            articleClient.search(ui.queryEditText.text.toString())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doAfterTerminate {
                        ui.progressBar.visibility = View.GONE
                    }
                    .bindToLifecycle(this)
                    .subscribe({
                        ui.queryEditText.text.clear()
                        listAdapter.articles = it
                        listAdapter.notifyDataSetChanged()
                    } , {
                        toast("エラー：$it")
                    })
        }
    }
}