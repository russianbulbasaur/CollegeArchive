package mein.wasser.ui

import androidx.appcompat.app.AppCompatActivity
import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.telecom.Call
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import mein.wasser.databinding.ActivityFullscreenBinding
import mein.wasser.R
import androidx.activity.viewModels;
import androidx.fragment.app.activityViewModels
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.safetynet.SafetyNet
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import mein.wasser.adapters.data
import mein.wasser.extras.OutBound
import mein.wasser.extras.json_decode
import org.json.JSONObject
import java.util.ArrayList

class FullscreenActivity : AppCompatActivity() {
    companion object {
        var EXIT: Int = 1;
        var start: Int = 1;
    }

    private lateinit var binding: ActivityFullscreenBinding
    private lateinit var fullscreenContent: TextView
    private lateinit var fullscreenContentControls: LinearLayout
    private val hideHandler = Handler()
    private lateinit var auth: FirebaseAuth;

    @SuppressLint("InlinedApi")
    private val hidePart2Runnable = Runnable {
        // Delayed removal of status and navigation bar
        if (Build.VERSION.SDK_INT >= 30) {
            fullscreenContent.windowInsetsController?.hide(WindowInsets.Type.statusBars() or WindowInsets.Type.navigationBars())
        } else {
            // Note that some of these constants are new as of API 16 (Jelly Bean)
            // and API 19 (KitKat). It is safe to use them, as they are inlined
            // at compile-time and do nothing on earlier devices.
            fullscreenContent.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LOW_PROFILE or
                        View.SYSTEM_UI_FLAG_FULLSCREEN or
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
                        View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        }
    }
    private val showPart2Runnable = Runnable {
        // Delayed display of UI elements
        supportActionBar?.show()
        fullscreenContentControls.visibility = View.VISIBLE
    }
    private var isFullscreen: Boolean = false

    override fun onBackPressed() {
        super.onBackPressed()
        if (EXIT.toInt() == 1) {
            finish();
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = resources.getColor(R.color.main)
        binding = ActivityFullscreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        FirebaseApp.initializeApp(this)
        auth = FirebaseAuth.getInstance()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.hide()
        val frag_manager = supportFragmentManager;
        frag_manager.beginTransaction().addToBackStack("login").add(R.id.frag_main, first_time())
            .commit()
        val command_listener: fragment_commands by viewModels()
        command_listener.code.observe(this, Observer {
            if (it[0]!!.getInt("code") == 0) {
                var phoneLogin = phone_login(applicationContext)
                frag_manager.beginTransaction().setCustomAnimations(R.anim.enter, R.anim.exitt)
                    .addToBackStack("login").add(R.id.frag_main, phoneLogin).commit()
            } else if (it[0]!!.getInt("code") == 1) {
                frag_manager.beginTransaction().setCustomAnimations(R.anim.enter, R.anim.exitt)
                    .replace(R.id.frag_main, details(it[0]!!)).commit()
                val observer: signup_done by viewModels()
                observer.done.observe(this, Observer {
                    if (it.pop() as Boolean) {
                        val pd = ProgressDialog(this)
                        pd.setMessage("Please Wait...")
                        pd.setCancelable(false)
                        pd.show()
                        val arr = it.pop() as java.util.HashMap<*, *>
                        val q = Volley.newRequestQueue(applicationContext)
                        val sr = object:StringRequest(Method.POST,getString(R.string.url)+"/signin.php",
                            com.android.volley.Response.Listener {
                                try
                                {
                                    val res = it.trim().toInt();
                                    if(res==1)
                                        login(arr,pd,arr["idtoken"].toString().trim())
                                }
                                catch (e:Exception)
                                {
                                    Toast.makeText(applicationContext,"Erorr 400",Toast.LENGTH_LONG).show()
                                }
                            }, com.android.volley.Response.ErrorListener {
                                Toast.makeText(applicationContext,it.toString(),Toast.LENGTH_LONG).show()
                            })
                        {
                            override fun getParams(): MutableMap<String, String> {
                                val hash = HashMap<String,String>()
                                hash["id"] = arr["id"].toString().toString()
                                hash["idtoken"] = arr["idtoken"].toString().toString()
                                hash["name"] = arr["name"].toString().toString()
                                hash["email"] = arr["email"].toString().toString()
                                hash["gender"] = arr["gender"].toString().toString()
                                hash["dob"] = arr["dob"].toString().toString()
                                hash["course"] = arr["course"].toString().toString()
                                hash["year"] = arr["year"].toString().toString()
                                hash["describe"] = arr["describe"].toString().toString()
                                hash["drink"] = arr["drink"].toString().toString()
                                hash["hobbies"] = arr["hobbies"].toString().toString()
                                hash["photos"] = arr["photos"].toString().toString()
                                return hash
                            }
                        }
                        q.add(sr)
                    }
                })
            }
            else if (it[0]!!.getInt("code") == 2) {
                EXIT=1;
                val intent = Intent(this,letsgo::class.java)
                intent.putExtra("data",it[0])
                startActivity(intent)
                overridePendingTransition(R.anim.enter,R.anim.exitt)
            }
        })
    }
    private fun login(arr: HashMap<*, *>, pd: ProgressDialog,idtoken:String) {
        val ob:fragment_commands by viewModels()
        val queue = Volley.newRequestQueue(this);
        val userdata = Bundle()
        val sr = object : StringRequest(
            Request.Method.POST,
            "https://thearchiveee.000webhostapp.com/existence_check.php",
            com.android.volley.Response.Listener {
                Log.e("ankitdata", it.toString())
                val jsonObject = JSONObject(it.toString())
                val EBIT = jsonObject.getInt("EBIT")
                val CBIT = jsonObject.getInt("CBIT")
                val d = jsonObject.getString("data")
                val hash = HashMap<Int, Bundle>()
                if (EBIT == 1 && CBIT == 1) {
                        val json = json_decode()
                        val dat = json.decode(d)
                        val parcel = data(dat)
                        userdata.putString("idtoken", idtoken)
                        userdata.putParcelable("parcel", parcel)
                        userdata.putInt("code", 2)
                        hash[0] = userdata
                        ob.set_code(hash)
                    } else {
                        Toast.makeText(applicationContext, "Error 303", Toast.LENGTH_LONG).show()
                    }
                pd.cancel()
                pd.dismiss()
            },
            com.android.volley.Response.ErrorListener {
                pd.cancel()
                pd.dismiss()
                Toast.makeText(applicationContext, it.toString(), Toast.LENGTH_LONG).show()
            }) {
            override fun getParams(): MutableMap<String, String> {
                val hash = HashMap<String, String>();
                hash["email"] = arr["email"].toString().trim()
                hash["id"] = arr["id"].toString().trim()
                hash["idtoken"] = arr["idtoken"].toString().trim()
                return hash;
            }
        }
        queue.add(sr)
    }
}
class fragment_commands : ViewModel()
{
    var code = MutableLiveData<HashMap<Int,Bundle>>()
    fun set_code(i:HashMap<Int,Bundle>)
    {
        code.value = i
    }
}
interface exit
{
    fun set_exit(i:Int)
}