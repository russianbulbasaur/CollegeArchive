package mein.wasser.adapters

import android.content.Context
import android.database.DataSetObserver
import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListAdapter
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import mein.wasser.R

class chatadapter(number:Int,c:Context,texts:ArrayList<String>): ArrayAdapter<String>(c,R.layout.outchat) {
    lateinit var count:Integer;
    lateinit var chats:ArrayList<String>;
    lateinit var contxt:Context;
    inner class chat_state()
    {
        lateinit var chatview : TextView;
    }
    init {
        this.chats = texts;
        this.contxt = c;
        this.count = Integer(number)
    }

    override fun getCount(): Int {
        return count.toInt()
    }
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var chatState = chat_state();
        var v:View? = null;
        if(convertView==null)
        {
            v = LayoutInflater.from(context).inflate(R.layout.outchat,parent,false);
            chatState = chat_state();
            chatState.chatview = v.findViewById(R.id.chatview);
            v.tag = chatState
            return v
        }
        else
        {
            chatState = convertView.tag!!  as chat_state
        }
        return convertView
    }

}