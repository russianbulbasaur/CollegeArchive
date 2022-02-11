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
import android.widget.GridView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.google.android.material.card.MaterialCardView
import mein.wasser.R
import mein.wasser.adapters.courses_adapter

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [course_fragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class course_fragment : Fragment() {
    lateinit var courseDone : course_done
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.fragment_course_fragment,container,false)
        val gridView : GridView = v.findViewById(R.id.course_grid)
        var previouslyselected: View? = null;
       /* gridView.setOnItemClickListener(object:AdapterView.OnItemClickListener
        {
            override fun onItemClick(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                val card = p1!!.findViewById<MaterialCardView>(R.id.coursecardview)
                val attrib = card.findViewById<TextView>(R.id.attrib)
                if(attrib.text.toString().trim().isEmpty())
                {
                    card.setStrokeColor(ContextCompat.getColor(context as Context,R.color.main))
                    if(previouslyselected!=null)
                    {
                        previouslyselected!!.findViewById<MaterialCardView>(R.id.coursecardview).setStrokeColor(ColorStateList.valueOf(Color.WHITE))
                        previouslyselected!!.findViewById<TextView>(R.id.attrib).setText("")
                    }
                    previouslyselected = p1 as View;
                    attrib.setText("1")
                }
                else
                {
                    card.setStrokeColor(ColorStateList.valueOf(Color.WHITE))
                    attrib.setText("")
                }
            }
        }) */
        val blist = arrayListOf<Int>(R.drawable.bmaths,R.drawable.bphysics,R.drawable.bchemistry,R.drawable.bbotany,R.drawable.bzoology,R.drawable.beco,R.drawable.bgeo,R.drawable.bgeology,R.drawable.bhis,R.drawable.bbusiness,R.drawable.beng,R.drawable.bhindi,R.drawable.bbusiness,R.drawable.bsankrit,R.drawable.bcs,R.drawable.bmca,
                                                                                                                                                                                                                                                                  R.drawable.bpol,R.drawable.bedu,R.drawable.bsoc,R.drawable.bpsy,R.drawable.bphysical,
                                                                                                                                                                                                                                                                  R.drawable.bmusic,R.drawable.bpubad,R.drawable.bjournal,R.drawable.bsculp,R.drawable.btour,R.drawable.bphil);
        val courses = arrayListOf<String>("Mathematics","Physics","Chemistry","Botany","Zoology","Economics","Geography","Geology","History","BBA","English","Hindi","MBA","Sanskrit","BCA","MCA","Pol Science","Education","Sociology","Psychology","Physical Education","Music","Public Administration","Journalism","Sculpture","Tourism","Philosophy")
        val adapter = courses_adapter(context as Context,R.layout.course_card,courses,blist)
        gridView.adapter = adapter
        gridView.setOnItemClickListener(object :AdapterView.OnItemClickListener{
            override fun onItemClick(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                courseDone.done(p2,courses)
            }
        })
        return v
    }
}