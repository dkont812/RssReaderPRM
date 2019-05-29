package com.example.rssreaderprm.Adapter


import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.example.rssreaderprm.Activities.NewsActivity
import com.example.rssreaderprm.R
import com.example.rssreaderprm.RssModel.ItemClickListener
import com.example.rssreaderprm.RssModel.RSSObject
import com.squareup.picasso.Picasso
import org.jsoup.Jsoup
import org.jsoup.select.Elements


class FeedViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
    View.OnClickListener, View.OnLongClickListener {

    var txtTitle: TextView
    var txtPubdate: TextView
    var imageOfNews: ImageView

    private var itemClickListener: ItemClickListener? = null

    init {
        txtTitle = itemView.findViewById(R.id.txtTitle) as TextView
        txtPubdate = itemView.findViewById(R.id.txtPubdate) as TextView
        imageOfNews = itemView.findViewById(R.id.imageOfNews) as ImageView

        itemView.setOnClickListener(this)
        itemView.setOnLongClickListener(this)

    }


    fun setItemClickListener(itemClickListener: ItemClickListener) {
        this.itemClickListener = itemClickListener

    }

    override fun onClick(v: View?) {
        itemClickListener!!.onClick(v, adapterPosition, false)

    }

    override fun onLongClick(v: View?): Boolean {
        itemClickListener!!.onClick(v, adapterPosition, true)
        return true
    }


}


class FeedAdapter(private val rssObject: RSSObject, public val mContext: Context) :
    RecyclerView.Adapter<FeedViewHolder>() {

    private val inflater: LayoutInflater

    init {
        inflater = LayoutInflater.from(mContext)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedViewHolder {
        val itemView = inflater.inflate(com.example.rssreaderprm.R.layout.row1, parent, false)
        return FeedViewHolder(itemView)
    }


    override fun getItemCount(): Int {
        return rssObject.items.size
    }


    override fun onBindViewHolder(holder: FeedViewHolder, position: Int) {

        holder.txtTitle.text = rssObject.items[position].title
        holder.txtPubdate.text = rssObject.items[position].pubDate
        var document: org.jsoup.nodes.Document? = Jsoup.parse(rssObject.items[position].description)

        var element: Elements = document!!.select("img")

        val pic = element.get(0).attr("src")
        Picasso
            .get()
            .load(pic)
            .fit()
            .centerInside()
            .into(holder.imageOfNews)
        holder.setItemClickListener(ItemClickListener { view, position, isLongClick ->
            if (!isLongClick) {
                val bo = Intent(mContext, NewsActivity::class.java)
                bo.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                bo.putExtra("title", rssObject.items[position].title)
                bo.putExtra("desc", rssObject.items[position].description.replace("<.*>[^A-Z]".toRegex(), ""))
                bo.putExtra("link", rssObject.items[position].link)
                bo.putExtra("img", pic)
                bo.putExtra("pubDate", rssObject.items[position].pubDate)
                mContext.startActivity(bo)
            }


        })
    }


}
