package mein.wasser.adapters

import android.content.Context
import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import mein.wasser.R

class parentchatadapter(cs:Array<String>,con:Context) : RecyclerView.Adapter<parentchatadapter.holder>() {
    lateinit var chats:Array<String>;
    lateinit var c:Context
    init {
        this.c = con
        this.chats = cs;
    }
    class holder(v: View) : RecyclerView.ViewHolder(v)
    {
        lateinit var im:ImageView
        lateinit var name:TextView
        lateinit var lastchat:TextView
        init {
            this.im = v.findViewById(R.id.profilepic)
            this.name = v.findViewById(R.id.name);
            this.lastchat = v.findViewById(R.id.lastchat)
        }
    }
    override fun getItemCount(): Int {
        return chats.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): holder {
        val h = holder(LayoutInflater.from(c).inflate(R.layout.parentchats,parent,false))
        return h
    }

    override fun onBindViewHolder(holder: holder, position: Int) {

    }
}