package org.lox.kotlindemo.model

import android.content.Context
import android.content.Intent
import android.support.v4.content.ContextCompat.startActivity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView



class AdapterList constructor (private var items: List<Data?>,
              private val mContext: Context): RecyclerView.Adapter<AdapterList.ViewHolder>(){
    var mOnItemClickListener: OnItemClickListener? = null

    override fun onCreateViewHolder(viewGroup: ViewGroup, position: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(mContext).inflate(
            org.lox.kotlindemo.R.layout.list_item, viewGroup, false
        ))
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(p0: ViewHolder, position: Int) {
        p0.bind(items[position])
        p0.itemView.setOnClickListener {
            //p0.dataStr(items[position])
            mOnItemClickListener?.onItemClick(items[position])
//            startActivity(Intent(mContext, DetailActivity::class.java))
//            Toast.makeText(mContext,"$position : ${p0.dataStr(items[position])} ", Toast.LENGTH_SHORT).show()
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var nameView: TextView = itemView.findViewById(org.lox.kotlindemo.R.id.text_name)
        private var typeView: TextView = itemView.findViewById(org.lox.kotlindemo.R.id.text_type)
        private var sumView: TextView = itemView.findViewById(org.lox.kotlindemo.R.id.text_sum)

        fun bind(data: Data?){
            itemView.apply {
                nameView.text = data?.Name
                typeView.text = if(data?.Type != null) ": ${data.Type}" else ""
                sumView.text = "${data?.Sum}"
            }
        }

        fun dataStr(data: Data?): String?{
            return data?.print()
        }
    }

    fun addItem(item: Data?, position: Int) {
        this.items = this.items + (item)
        notifyItemInserted(position)
    }
}

