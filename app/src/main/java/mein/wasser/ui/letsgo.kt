package mein.wasser.ui

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.transition.TransitionInflater
import android.view.MenuItem
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.*
import androidx.viewpager2.widget.ViewPager2
import com.android.volley.toolbox.NetworkImageView
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.card.MaterialCardView
import com.google.android.material.navigation.NavigationBarView
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.annotations.SerializedName
import com.mindorks.placeholderview.SwipePlaceHolderView
import mein.wasser.R;
import mein.wasser.adapters.data
import mein.wasser.adapters.personalAdapters
import mein.wasser.extras.OutBound
import mein.wasser.extras.userdataa
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.lang.StringBuilder

class letsgo : AppCompatActivity() {
    lateinit var gclient:GoogleSignInClient;
    lateinit var OutBoundInter:OutBound;
    lateinit var conAdapter:Retrofit;
    lateinit var userdata: data
    lateinit var idtoken:String
    lateinit var fragmanager:FragmentManager
    lateinit var people:ArrayList<userdataa>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_letsgo)
        window.statusBarColor = resources.getColor(R.color.main)
        supportActionBar!!.hide()
        fragmanager = supportFragmentManager
        val data = intent.extras!!.getBundle("data");
        userdata = (data!!.getParcelable("parcel") as data?)!!
        idtoken = data.getString("idtoken") as String
        val cob = convertor();
        val chats = cob.initialize(Gson(),data.getString("chats"));
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.key))
            .requestEmail()
            .requestId()
            .build();
        conAdapter = Retrofit.Builder().baseUrl(getString(R.string.url)).addConverterFactory(ScalarsConverterFactory.create()).build()
        OutBoundInter = conAdapter.create(OutBound::class.java)
        gclient = GoogleSignIn.getClient(this,gso);
        getData(userdata as data,idtoken as String,object:resp_people
        {
            override fun people(arr: ArrayList<userdataa>) {
                people = arr;
                show()
            }
        })
        val bottom_nav = findViewById<BottomNavigationView>(R.id.bottom_nav)
        bottom_nav.setOnItemSelectedListener(object:NavigationBarView.OnItemSelectedListener
        {
            override fun onNavigationItemSelected(item: MenuItem): Boolean {
                if(item.itemId==R.id.chat)
                    chat(chats)
                return true
            }
        })
    }
    fun chat(ch:Array<String>)
    {
        fragmanager.beginTransaction().addToBackStack("cards").add(R.id.frag_container,chats(ch)).commit()
    }
    fun getData(data: data,idtoken:String,resp_people:resp_people)
    {
        OutBoundInter.people(data.id,idtoken,data.email).enqueue(object:Callback<String>
        {
            override fun onFailure(call: Call<String>, t: Throwable) {
                Toast.makeText(applicationContext,t.toString(),Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<String>, response: Response<String>) {
                val resp = response.body().toString().trim()
                val jsonob = JSONObject(resp)
                val jsonarr = jsonob.getJSONArray("people")
                var people = ArrayList<userdataa>()
                for(i in 0 until jsonarr.length())
                {
                    val gson = Gson()
                    people.add(gson.fromJson(jsonarr[i].toString(),userdataa::class.java))
                }
                resp_people.people(people)
            }
        })
    }
    fun show()
    {
        val card_frag = card_fragment(people,userdata,idtoken)
        gclient.signOut();
        val ob :photo_clicked by viewModels()
        ob.d.observe(this, Observer {
            val frag = photu_frag(people.get(it), FirebaseStorage.getInstance())
            fragmanager.beginTransaction()
                .addToBackStack("cards")
                .add(R.id.frag_container,frag)
                .commit()
        })
        fragmanager.beginTransaction().addToBackStack("cards").add(R.id.frag_container,card_frag).commit()
    }
}
internal class photo_clicked : ViewModel() {
    var d: MutableLiveData<Int> = MutableLiveData()
    fun clicked(itemNumber:Integer) {
        d.value = itemNumber.toInt()
    }
}
interface resp_people
{
    fun people(arr:ArrayList<userdataa>)
}