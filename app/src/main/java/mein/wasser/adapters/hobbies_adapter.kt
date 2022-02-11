package mein.wasser.adapters

import android.content.Context
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.google.android.material.card.MaterialCardView
import mein.wasser.R
import java.util.ArrayList

class hobbies_adapter(con: Context, res:Int, hobbies: ArrayList<String>,code:Int) : ArrayAdapter<String>(con,res)
{
    lateinit var code:Integer;
    lateinit var hobby_list: ArrayList<String>;
    init {
        this.hobby_list = hobbies
        this.code = Integer(code);
    }
    override fun getCount(): Int {
        return hobby_list.size
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        lateinit var holder:hobbies_holder;
        var v: View? = convertView
        if(v==null)
        {
            v = LayoutInflater.from(context).inflate(R.layout.hobbies_card,parent,false)
            holder = hobbies_holder()
            holder.hobby_name = v.findViewById<TextView>(R.id.hobby_name)
            holder.hobby_card = v.findViewById<MaterialCardView>(R.id.hobby_card)
            holder.attrib = v.findViewById<TextView>(R.id.attrib)
            holder.attrib.setText("0")
            if(code.toInt()==1)
            {
                holder.hobby_name.setTextColor(ContextCompat.getColor(context, R.color.white))
                holder.hobby_card.setStrokeColor(
                    ColorStateList.valueOf(
                        ContextCompat.getColor(
                            context,
                            R.color.main
                        )
                    )
                )
                holder.hobby_card.setCardBackgroundColor(
                    ContextCompat.getColor(
                        context,
                        R.color.main
                    )
                )
            }
            else {
                holder.hobby_name.setTextColor(ContextCompat.getColor(context, R.color.black))
                holder.hobby_card.setStrokeColor(
                    ColorStateList.valueOf(
                        ContextCompat.getColor(
                            context,
                            R.color.black
                        )
                    )
                )
                holder.hobby_card.setCardBackgroundColor(
                    ContextCompat.getColor(
                        context,
                        R.color.white
                    )
                )
            }
            holder.hobby_name.setText("      "+hobby_list.get(position)+"      ")
            v.setTag(holder)
        }
        else{
            holder = v.tag as hobbies_holder
        }
        return v as View
    }
    inner class hobbies_holder()
    {
        lateinit var attrib: TextView;
        lateinit var hobby_name: TextView;
        lateinit var hobby_card : MaterialCardView;
    }
}