package mein.wasser.adapters

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.google.android.material.card.MaterialCardView
import mein.wasser.R
import java.util.ArrayList

class photos_adapter(con: Context, res:Int, photonumbers: ArrayList<String>,photo:ArrayList<Bitmap?>) : ArrayAdapter<String>(con,res)
{
    lateinit var ob:photoclick;
    lateinit var photos:ArrayList<Bitmap?>
    lateinit var photos_text: ArrayList<String>;
    init {
        this.photos = photo
        this.photos_text = photonumbers;
    }
    override fun getCount(): Int {
        return photos_text.size
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        lateinit var holder:hobbies_holder;
        var v: View? = convertView
        if(v==null)
        {
            v = LayoutInflater.from(context).inflate(R.layout.photos_card,parent,false)
            holder = hobbies_holder()
            holder.im = v.findViewById(R.id.image)
            if(photos[position]!=null)
                holder.im.setImageBitmap(photos[position])
            v.setTag(holder)
        }
        else{
            holder = v.tag as hobbies_holder
            if(photos[position]!=null)
                holder.im.setImageBitmap(photos[position])
        }
        v!!.setOnClickListener(object:View.OnClickListener
        {
            override fun onClick(p0: View?) {
                ob.clicked(position+1)
            }
        })
        return v as View
    }
    inner class hobbies_holder()
    {
        lateinit var im : ImageView
    }
}
interface photoclick
{
    fun clicked(i:Int)
}