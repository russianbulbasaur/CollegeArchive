package mein.wasser.ui

import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.ImageDecoder
import android.os.Build
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.text.Editable
import android.text.Html
import android.text.TextWatcher
import android.util.Log
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.android.material.card.MaterialCardView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import mein.wasser.R
import mein.wasser.adapters.*
import kotlin.collections.HashMap
import java.util.*
import kotlin.collections.ArrayList
import kotlin.properties.Delegates

class details(bundle: Bundle) : Fragment() {
    lateinit var data:Bundle;
    companion object {
        val photos: HashMap<Int, String> = HashMap();
    }
    val gotphotoob:got_photo by activityViewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
init {
    this.data = bundle;
}
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.details_layout,container,false)
        val viewpager = v.findViewById<ViewPager>(R.id.details_viewpager)
        viewpager.setOnTouchListener(object:View.OnTouchListener
        {
            override fun onTouch(p0: View?, p1: MotionEvent?): Boolean {
                return true
            }
        })
        val photo_requsted_ob : photo_requested by activityViewModels()
        photo_requsted_ob.photonumber.observe(this, Observer {
            val intent = Intent()
            intent.setType("image/*")
            intent.setAction(Intent.ACTION_GET_CONTENT)
            startActivityForResult(intent,it)
        })
        for(i in 1..6)
        {
            photos[i] = "emputy";
        }
        val progressbar : ProgressBar = v.findViewById(R.id.page_progress)
        val page_number = v.findViewById<TextView>(R.id.pagenumber)

        v.findViewById<ImageView>(R.id.back).setOnClickListener(object:View.OnClickListener
        {
            override fun onClick(p0: View?) {
                if(viewpager.currentItem!=0) {
                    val progress = (viewpager.currentItem / 8.0) * 100.0;
                    if (Build.VERSION.SDK_INT > 24)
                        progressbar.setProgress(progress.toInt(), true)
                    else
                        progressbar.setProgress(progress.toInt())
                    page_number.setText(viewpager.currentItem.toString() + "/8")
                    viewpager.currentItem = viewpager.currentItem - 1;
                }
            }
        })
        val adapter = details_viewpager_adapter(context as Context,requireActivity().supportFragmentManager,photo_requsted_ob,gotphotoob,this,data)
        adapter.ob = object:continu
        {
            override fun continu() {
                val pagenumber = viewpager.currentItem+1
                val progress : Double = ((pagenumber+1.0)/8.0)*100.0
                if(Build.VERSION.SDK_INT>24)
                    progressbar.setProgress(progress.toInt(),true)
                else
                    progressbar.setProgress(progress.toInt())
                viewpager.currentItem = viewpager.currentItem+1;
                if(pagenumber!=8)
                    page_number.setText((pagenumber+1).toString()+"/8")
                if(pagenumber==7)
                {
                    val inputMethodManager : InputMethodManager = context!!.getSystemService(Service.INPUT_METHOD_SERVICE) as InputMethodManager
                    inputMethodManager.hideSoftInputFromWindow(progressbar.windowToken,0)
                }
                if(pagenumber==8)
                {
                    val ob:signup_done by activityViewModels()
                    ob.set_done(true,details_viewpager_adapter.data_array)
                }
            }
        }
        viewpager.offscreenPageLimit = 8
        viewpager.adapter = adapter
        return v
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(data!=null)
        {
            val uri = data.data
            val photo:Bitmap = ImageDecoder.decodeBitmap(ImageDecoder.createSource(requireContext().contentResolver,uri!!));
            val firebase_storage = FirebaseStorage.getInstance();
            val storage = firebase_storage.getReference();
            val uid = UUID.randomUUID().toString();
            val ref = storage.child("images/"+uid);
            val pd = ProgressDialog(context)
            pd.setCancelable(false)
            pd.setMessage("Please Wait a moment....")
            pd.show()
            ref.putFile(uri).addOnSuccessListener {
                pd.cancel()
                pd.dismiss()
                photos[requestCode] = uid;
                gotphotoob.set_photo(requestCode,photo)
                Toast.makeText(context,uid,Toast.LENGTH_LONG).show()
            }.addOnFailureListener {
                pd.cancel()
                pd.dismiss()
                Toast.makeText(context,it.toString(),Toast.LENGTH_LONG).show()
            }
        }
    }
}
class details_viewpager_adapter(c: Context,fragmentManager: FragmentManager,photorequestedob:photo_requested,gotphotoob:got_photo,ow:Fragment,bundle:Bundle) : PagerAdapter()
{
    companion object
    {
        val data_array = HashMap<Any,Any>()
    }
    lateinit var con:Context;
    lateinit var bundle: Bundle;
    lateinit var owner:Fragment;
    lateinit var gotphotoob: got_photo
    lateinit var photo_ob:photo_requested;
    lateinit var fragmentManager: FragmentManager
    public lateinit var ob : continu;
    init {
        this.fragmentManager = fragmentManager
        this.con = c;
        this.owner = ow;
        this.bundle = bundle;
        this.gotphotoob = gotphotoob
        this.photo_ob = photorequestedob
    }
    override fun getCount(): Int {
        return 8
    }
    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val inflater:LayoutInflater = LayoutInflater.from(con)
        var v :ViewGroup = name(inflater,container,bundle.getString("name"))
        when(position)
        {
            1 ->
            {
                v = gender(inflater,container)
            }
            2 ->
            {
                v = dob(inflater,container)
            }
            3 ->
            {
                v = course(inflater,container,fragmentManager)
            }
            4 ->
            {
                v = hobbies(inflater,container)
            }
            5 ->
            {
                v = smoke_drink(inflater,container)
            }
            6 ->
            {
                v = describe(inflater,container)
            }
            7->
            {
                v = photos(inflater,container,photo_ob,gotphotoob,owner)
            }
        }
        container.addView(v)
        return v
    }
    private fun name(inflater:LayoutInflater,container: ViewGroup,name:String?):ViewGroup
    {
        val v = inflater.inflate(R.layout.name,container,false) as ViewGroup
        val nameet = v.findViewById<EditText>(R.id.nameet);
        nameet.setText(name)
        val but = v.findViewById<Button>(R.id.continu)
        but.setBackgroundColor(ContextCompat.getColor(con,R.color.main))
        but.setOnClickListener(object:View.OnClickListener
                      {
                          override fun onClick(p0: View?) {
                              if(nameet.text.toString().trim().isNotEmpty()) {
                                  val serv: InputMethodManager =
                                      con.getSystemService(Service.INPUT_METHOD_SERVICE) as InputMethodManager;
                                  serv.hideSoftInputFromWindow(but.windowToken, 0)
                                  data_array["name"] = nameet.text.toString().trim()
                                  ob.continu()
                              }
                          }
                      })
        nameet.addTextChangedListener(object:TextWatcher
        {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {

            }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if(p0.toString().trim().isEmpty()) {
                    but.setBackgroundColor(Color.LTGRAY)
                    but.setOnClickListener(object:View.OnClickListener
                    {
                        override fun onClick(p0: View?) {
                            Log.e("hello","fuck off")
                        }
                    })
                }
                else {
                      but.setOnClickListener(object:View.OnClickListener
                      {
                          override fun onClick(p0: View?) {
                              if(nameet.text.toString().trim().isNotEmpty()) {
                                  val serv: InputMethodManager =
                                      con.getSystemService(Service.INPUT_METHOD_SERVICE) as InputMethodManager;
                                  serv.hideSoftInputFromWindow(but.windowToken, 0)
                                  data_array["name"] = nameet.text.toString().trim()
                                  ob.continu()
                              }
                          }
                      })
                    but.setBackgroundColor(con.resources.getColor(R.color.main))
                }
            }
        })
        return v
    }
    private fun gender(inflater:LayoutInflater,container: ViewGroup):ViewGroup
    {
        val v = inflater.inflate(R.layout.gender,container,false) as ViewGroup
        val mantouch = v.findViewById<FloatingActionButton>(R.id.mantouch)
        val womantouch = v.findViewById<FloatingActionButton>(R.id.womantouch)
        val man_color = v.findViewById<FloatingActionButton>(R.id.colorchange_man)
        val woman_color = v.findViewById<FloatingActionButton>(R.id.colorchange_woman)
        val but = v.findViewById<Button>(R.id.continu)
        but.setBackgroundColor(Color.LTGRAY)
        mantouch.setOnClickListener(object:View.OnClickListener
        {
            override fun onClick(p0: View?) {
                man_color.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(con,R.color.main))
                woman_color.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(con,R.color.gray))
                but.setBackgroundColor(con.resources.getColor(R.color.main))
                but.setOnClickListener(object:View.OnClickListener
                {
                    override fun onClick(p0: View?) {
                        data_array["gender"] = 'M'
                        ob.continu()
                    }
                })
            }
        })
        womantouch.setOnClickListener(object:View.OnClickListener
        {
            override fun onClick(p0: View?) {
                woman_color.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(con,R.color.main))
                man_color.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(con,R.color.gray))
                but.setBackgroundColor(con.resources.getColor(R.color.main))
                but.setOnClickListener(object:View.OnClickListener
                {
                    override fun onClick(p0: View?) {
                        data_array["gender"] = 'F'
                        ob.continu()
                    }
                })
            }
        })
        return v
    }
    private fun dob(inflater:LayoutInflater,container: ViewGroup):ViewGroup
    {
        val v = inflater.inflate(R.layout.dob,container,false) as ViewGroup
        val rel = v.findViewById<RelativeLayout>(R.id.clickable)
        val dateet = v.findViewById<TextView>(R.id.date)
        val but = v.findViewById<Button>(R.id.continu)
        but.setBackgroundColor(Color.LTGRAY)
        rel.setOnClickListener(object:View.OnClickListener
        {
            override fun onClick(p0: View?) {
                if (Build.VERSION.SDK_INT>24) {
                    val date = DatePickerDialog(con);
                    date.setOnDateSetListener(object:DatePickerDialog.OnDateSetListener
                    {
                        override fun onDateSet(p0: DatePicker?, p1: Int, p2: Int, p3: Int) {
                            dateet.setText(p3.toString()+"/"+(p2+1).toString()+"/"+p1.toString());
                            but.setBackgroundColor(con.resources.getColor(R.color.main))
                            but.setOnClickListener(object:View.OnClickListener
                            {
                                override fun onClick(p0: View?) {
                                    data_array["dob"] = p3.toString().trim()+"/"+(p2+1).toString().trim()+"/"+p1.toString().trim()
                                    ob.continu()
                                }
                            })
                        }
                    })
                    date.show()
                }
            }
        })
        return v
    }
    private fun course(inflater: LayoutInflater,container: ViewGroup,fragmentManager: FragmentManager):ViewGroup
    {
        val v = inflater.inflate(R.layout.course,container,false) as ViewGroup
        val fragmentcontainer : FragmentContainerView = v.findViewById(R.id.courses_year_frag)
        val frag = course_fragment()
        val but = v.findViewById<Button>(R.id.continu)
        val titleet = v.findViewById<TextView>(R.id.title);
        but.setBackgroundColor(Color.LTGRAY)
        var selectedcourse:String = "";
        var selectedyear:Int = 0;
        val temp_txt = titleet.text.toString();
        frag.courseDone = object:course_done
        {
            override fun done(i: Int, courses: ArrayList<String>) {
                selectedcourse = courses.get(i)
                but.setBackgroundColor(ContextCompat.getColor(con,R.color.main))
                but.setOnClickListener(object:View.OnClickListener{
                    override fun onClick(p0: View?) {
                        val yearFragment = year_fragment()
                        fragmentManager.beginTransaction().setCustomAnimations(R.anim.enter,R.anim.exitt).add(R.id.courses_year_frag,yearFragment).addToBackStack("year").commit()
                        titleet.setText(Html.fromHtml("<b>"+selectedcourse+"</b> - Year of course"))
                        but.setBackgroundColor(Color.LTGRAY)
                        but.setOnClickListener(object:View.OnClickListener{
                            override fun onClick(p0: View?) {

                            }
                        })
                        yearFragment.change = object:change_course
                        {
                            override fun clicked(clicked: Boolean) {
                                fragmentManager.popBackStack("year",FragmentManager.POP_BACK_STACK_INCLUSIVE)
                                titleet.setText(temp_txt)
                            }
                        }
                        yearFragment.done = object:year_done
                        {
                            override fun done(i: Int, years: ArrayList<String>) {
                                selectedyear = i+1
                                but.setBackgroundColor(ContextCompat.getColor(con,R.color.main))
                                but.setOnClickListener(object:View.OnClickListener{
                                    override fun onClick(p0: View?) {
                                        data_array["course"]  = selectedcourse
                                        data_array["year"] = selectedyear.toInt()
                                        ob.continu();
                                    }
                                })
                            }
                        }
                    }
                })
            }
        }
        fragmentManager.beginTransaction().add(R.id.courses_year_frag,frag).addToBackStack("course").commit()
        return v
    }
    private fun hobbies(inflater:LayoutInflater,container: ViewGroup):ViewGroup
    {
        val v = inflater.inflate(R.layout.hobbies,container,false) as ViewGroup
        val gridview = v.findViewById<GridView>(R.id.gridview)
        val stack = HashMap<String,Int>()
        val hobbies = arrayListOf<String>("Reading","Music","Cooking","Movies","Lofi-beats","Dancing",
        "Singing","Bollywood","Hollywood","Foodie","Biking","Gyming","Writing","Camping","Chess","Drawing","Aesthetics","Gardening","Art","Hiking","Coffee","Chai","Meditation","Photography","Fitness","Stand-Up",
        "Sports","Yoga")
        for(i in hobbies)
        {
            stack[i] = 0;
        }
        val but = v.findViewById<Button>(R.id.continu)
        val adapter = hobbies_adapter(con,R.layout.hobbies_card,hobbies,0)
        gridview.adapter = adapter
        but.setBackgroundColor(Color.LTGRAY)
        gridview.setOnItemClickListener(object :AdapterView.OnItemClickListener
        {
            override fun onItemClick(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                val hobbyname = p1!!.findViewById<TextView>(R.id.hobby_name)
                val hobbycard = p1!!.findViewById<MaterialCardView>(R.id.hobby_card)
                val attrib = p1!!.findViewById<TextView>(R.id.attrib)
                if(attrib.text.toString().toInt()==0) {
                    hobbyname.setTextColor(ContextCompat.getColor(con, R.color.white))
                    hobbycard.setStrokeColor(ContextCompat.getColor(con, R.color.sec3))
                    hobbycard.setCardBackgroundColor(ContextCompat.getColor(con, R.color.main))
                    stack[hobbies.get(p2)] = 1;
                    attrib.setText("1")
                }
                else
                {
                    stack[hobbies.get(p2)] = 0;
                    hobbyname.setTextColor(ContextCompat.getColor(con, R.color.black))
                    hobbycard.setStrokeColor(ContextCompat.getColor(con, R.color.black))
                    hobbycard.setCardBackgroundColor(ContextCompat.getColor(con, R.color.white))
                    attrib.setText("0")
                }
                var number = 0
                val hobbie_list = ArrayList<String>()
                for(i in hobbies)
                {
                    if(stack[i]==1) {
                        number++
                        hobbie_list.add(i)
                    }
                }
                if(number>=3)
                {
                    but.setBackgroundColor(ContextCompat.getColor(con,R.color.main))
                    but.setOnClickListener(object:View.OnClickListener
                    {
                        override fun onClick(p0: View?) {
                            data_array["hobbies"] = hobbie_list
                            ob.continu()
                        }
                    })
                }
                else
                {
                    but.setBackgroundColor(Color.LTGRAY)
                    but.setOnClickListener(object:View.OnClickListener
                    {
                        override fun onClick(p0: View?) {

                        }
                    })
                }
            }
        })
        return v
    }
    private fun smoke_drink(inflater:LayoutInflater,container: ViewGroup):ViewGroup
    {
        val v = inflater.inflate(R.layout.smoke_drink,container,false) as ViewGroup
        val list = v.findViewById<ListView>(R.id.smoke_drink_list);
        val but = v.findViewById<Button>(R.id.continu)
        but.setBackgroundColor(Color.LTGRAY)
        val options = arrayListOf<String>("Frequent Drinker & Smoker","Occasionally Drink","Occasionally Smoke","No" +
                " tolerance")
        val adapter : years_adapter = years_adapter(con,R.layout.year_listview,options,2)
        var previousview:View? = null
        list.setOnItemClickListener(object:AdapterView.OnItemClickListener{
            override fun onItemClick(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                val attrib = p1!!.findViewById<TextView>(R.id.attrib);
                val card = p1.findViewById<MaterialCardView>(R.id.yearcardview)
                if(attrib.text.toString().trim().isEmpty())
                {
                    if(previousview!=null)
                    {
                        previousview!!.findViewById<MaterialCardView>(R.id.yearcardview).setStrokeColor(
                            ColorStateList.valueOf(Color.WHITE))
                        previousview!!.findViewById<TextView>(R.id.attrib).setText("")
                    }
                    card.setStrokeColor(ContextCompat.getColor(con,R.color.main))
                    attrib.setText("1")
                    previousview = p1 as View
                    but.setBackgroundColor(ContextCompat.getColor(con,R.color.main));
                    but.setOnClickListener(object:View.OnClickListener
                    {
                        override fun onClick(p0: View?) {
                            data_array["drink"] = options[p2]
                            ob.continu()
                        }
                    })
                }
            }
        })
        list.adapter = adapter
        return v
    }
    private fun describe(inflater:LayoutInflater,container: ViewGroup):ViewGroup
    {
        val v = inflater.inflate(R.layout.describe,container,false) as ViewGroup
        val detailet = v.findViewById<EditText>(R.id.detail);
        val but = v.findViewById<Button>(R.id.continu)
        but.setBackgroundColor(Color.LTGRAY)
        detailet.addTextChangedListener(object :TextWatcher
        {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {

            }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if(p0.toString().trim().isNotEmpty()&&p0.toString().length>5)
                {
                    but.setBackgroundColor(ContextCompat.getColor(con,R.color.main))
                    but.setOnClickListener(object:View.OnClickListener
                    {
                        override fun onClick(p0: View?) {
                            if(detailet.text.toString().trim().isNotEmpty()) {
                                data_array["describe"] = detailet.text.toString().trim()
                                ob.continu()
                            }
                        }
                    })
                }
                else{
                    but.setBackgroundColor(Color.LTGRAY)
                    but.setOnClickListener(object:View.OnClickListener
                    {
                        override fun onClick(p0: View?) {

                        }
                    })
                }
            }
        })
        v.findViewById<RelativeLayout>(R.id.rel).setOnClickListener(object:View.OnClickListener
        {
            override fun onClick(p0: View?) {
                detailet.requestFocus()
                val inputmanager : InputMethodManager = con.getSystemService(Service.INPUT_METHOD_SERVICE) as InputMethodManager
                inputmanager.toggleSoftInputFromWindow(detailet.windowToken,InputMethodManager.SHOW_FORCED,0)
            }
        })
        return v
    }
    private fun photos(inflater: LayoutInflater,container: ViewGroup,photoob:photo_requested,gotphotoob:got_photo,owner:Fragment):ViewGroup
    {
        val v = inflater.inflate(R.layout.photos,container,false) as ViewGroup
        val grid:GridView = v.findViewById<GridView>(R.id.photos_grid)
        val but = v.findViewById<Button>(R.id.continu)
        but.setBackgroundColor(Color.LTGRAY)
        val images:ArrayList<Bitmap?> = arrayListOf<Bitmap?>(null,null,null,null,null,null)
        val adapter = photos_adapter(con,R.layout.photos_card, arrayListOf("1","2","3","4","5","6"),images)
        adapter.ob = object:photoclick
        {
            override fun clicked(i: Int) {
                gotphotoob.photonumber.observe(owner, Observer {
                    for(i in 1..6)
                    {
                        images[i-1] = it[i]
                    }
                    adapter.notifyDataSetChanged()
                    var check = 0;
                    for(s in images)
                    {
                        if(s!=null)
                            check++
                    }
                    if(check>0)
                    {
                        but.setBackgroundColor(ContextCompat.getColor(con,R.color.main))
                        but.setOnClickListener(object:View.OnClickListener
                        {
                            override fun onClick(p0: View?) {
                                val arr = ArrayList<String>()
                                for(i in 1..6)
                                {
                                    if(!details.photos[i].equals("emputy",true))
                                        arr.add(details.photos[i]!!)
                                }
                                data_array["photos"] = arr
                                data_array["email"] = bundle.getString("email") as Any
                                data_array["idtoken"] = bundle.getString("idtoken") as Any
                                data_array["id"] = bundle.getString("id") as Any
                                ob.continu()
                            }
                        })
                    }
                })
                photoob.set_photonumber(i)
            }
        }
        grid.adapter = adapter
        return v
    }
    override fun isViewFromObject(view: View, `object`: Any): Boolean {
     return view==`object`
    }
}
interface continu
{
    fun continu();
}
interface course_done
{
    fun done(i:Int,courses:ArrayList<String>)
}
interface year_done
{
    fun done(i:Int,years:ArrayList<String>)
}
class photo_requested : ViewModel()
{
    var photonumber:MutableLiveData<Int> = MutableLiveData();
    fun set_photonumber(i:Int)
    {
        photonumber.value = i;
    }
}
class got_photo:ViewModel()
{
    var photonumber:MutableLiveData<HashMap<Int,Bitmap>> = MutableLiveData()
    fun set_photo(i:Int,b:Bitmap)
    {
        val h = HashMap<Int,Bitmap>()
        h[i] = b;
        photonumber.value = h;
    }
}
class signup_done:ViewModel()
{
    var done:MutableLiveData<Stack<Any>> = MutableLiveData()
    fun set_done(b:Boolean,ob:HashMap<*,*>)
    {
        val s  = Stack<Any>()
        s.push(ob as Any)
        s.push(b as Any)
        done.value = s
    }
}
