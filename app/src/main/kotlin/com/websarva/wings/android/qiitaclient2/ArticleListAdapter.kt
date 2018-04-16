package com.websarva.wings.android.qiitaclient2

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.websarva.wings.android.qiitaclient2.model.Article
import com.websarva.wings.android.qiitaclient2.model.User
import com.websarva.wings.android.qiitaclient2.view.ArticleView

class ArticleListAdapter(private val context: Context) : BaseAdapter() {

    var articles: List<Article> = emptyList()

    override fun getCount(): Int = articles.size

    override fun getItem(position: Int): Any? = articles[position]

    override fun getItemId(position: Int): Long = 0

    override fun getView(position: Int,
                         convertView: View?,
                         parent: ViewGroup?): View =
            /*
            convertViewを安全キャストas?を使い、ArticleViewにキャストする。
            キャストできなかった場合nullとなり、エルビス演算子によりArticleView(context)が適応される。

            選択されたArticleViewオブジェクトに対して、拡張関数applyを適応している。
            applyの行なっていることは、ArticleViewオブジェクトのsetArticleメソッドを呼び出し、記事をセット。
            最終的にArticleViewオブジェクトが返される。
            applyを使うことで、一時的な変数を導入せずに済む、メンバアクセスを短く記述できている。
              */
            ((convertView as? ArticleView) ?: ArticleView(context)).apply {
                setArticle(articles[position])
            }
}