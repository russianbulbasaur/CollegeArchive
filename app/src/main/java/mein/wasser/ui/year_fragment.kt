package mein.wasser.ui

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ListView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.material.card.MaterialCardView
import mein.wasser.R
import mein.wasser.adapters.years_adapter
import org.w3c.dom.Text


class year_fragment : Fragment() {
    lateinit var done:year_done
    lateinit var change:change_course;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.fragment_year_fragment, container, false)
        val list = v.findViewById<ListView>(R.id.yearlist)
        var previousview : View? = null
        val change_button = v.findViewById<TextView>(R.id.change_course);
        change_button.setOnClickListener(object:View.OnClickListener
        {
            override fun onClick(p0: View?) {
                change.clicked(true)
            }
        })
        val yearlist = arrayListOf<String>("1st Year","2nd Year","3rd Year")
        list.setOnItemClickListener(object:AdapterView.OnItemClickListener{
            override fun onItemClick(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                val attrib = p1!!.findViewById<TextView>(R.id.attrib);
                val card = p1!!.findViewById<MaterialCardView>(R.id.yearcardview)
                if(attrib.text.toString().trim().isEmpty())
                {
                    if(previousview!=null)
                    {
                        previousview!!.findViewById<MaterialCardView>(R.id.yearcardview).setStrokeColor(
                            ColorStateList.valueOf(Color.WHITE))
                        previousview!!.findViewById<TextView>(R.id.attrib).setText("")
                    }
                    card.setStrokeColor(ContextCompat.getColor(context as Context,R.color.main))
                    attrib.setText("1")
                    previousview = p1 as View
                    done.done(p2, yearlist);
                }
            }
        })
        val adapter = years_adapter(context as Context,R.layout.year_listview,yearlist,1)
        list.adapter = adapter
        return v
    }
}
interface change_course
{
    fun clicked(clicked:Boolean)
}