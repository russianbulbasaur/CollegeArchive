package mein.wasser.ui

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.RequestFuture
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.material.button.MaterialButton
import mein.wasser.R
import mein.wasser.adapters.data
import mein.wasser.extras.json_decode
import org.json.JSONObject
import java.util.*
import kotlin.collections.HashMap

public class google : Fragment() {
    private var userdata = Bundle();
    lateinit var pd:ProgressDialog;
    lateinit var gclient:GoogleSignInClient;
    companion object
    {
        var login_lock:Boolean = false;
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.google, container, false)
        val gbutton: MaterialButton = v.findViewById(R.id.gbutton)
        pd = ProgressDialog(context)
        pd.setMessage("Signing In.....")
        pd.setCancelable(false)
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.key))
            .requestEmail()
            .requestId()
            .build();
        gclient = GoogleSignIn.getClient(requireActivity(),gso);
        gbutton.setOnClickListener(object:View.OnClickListener
        {
            override fun onClick(p0: View?) {
                pd.show()
                val intent = gclient.signInIntent;
                startActivityForResult(intent,2)
            }
        })
        return v
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==2 && !login_lock)
        {
            login_lock = true;
            val queue = Volley.newRequestQueue(context);
            val ob:fragment_commands by activityViewModels()
            val task = GoogleSignIn.getSignedInAccountFromIntent(data);
            task.addOnSuccessListener(OnSuccessListener {
                val array = arrayListOf<String>(it.displayName as String,it.id as String,it.idToken as String,it.email as String,it.photoUrl!!.toString() as String)
                val sr = object:StringRequest(Request.Method.POST,"https://thearchiveee.000webhostapp.com/existence_check.php", Response.Listener {
                    Log.e("ankitdata",it.toString())
                    Log.e("ankit",array[1])
                    val jsonObject = JSONObject(it.toString())
                    val EBIT = jsonObject.getInt("EBIT")
                    val CBIT = jsonObject.getInt("CBIT")
                    val d = jsonObject.getString("data")
                    val job = jsonObject.getJSONObject("data")
                    val hash = HashMap<Int, Bundle>()
                    if(EBIT==0 && CBIT==1) {
                        userdata.putString("name", array[0])
                        userdata.putString("email", array[3])
                        userdata.putString("photo", array[4])
                        userdata.putString("id", array[1])
                        userdata.putString("idtoken", array[2])
                        userdata.putInt("code", 1)
                        hash[0] = userdata
                        ob.set_code(hash)
                    }
                    else if(EBIT==1 && CBIT==1)
                    {
                        val json = json_decode()
                        val dat = json.decode(d)
                        val parcel = data(dat)
                        userdata.putString("chats",job.getString("chats"))
                        userdata.putString("idtoken",array[2])
                        userdata.putParcelable("parcel",parcel)
                        userdata.putInt("code",2)
                        hash[0] = userdata
                        ob.set_code(hash)
                    }
                    else if(EBIT==0 &&CBIT==0)
                    {
                        Toast.makeText(context,"Error-101",Toast.LENGTH_LONG).show()
                    }
                    else if(EBIT==1 && CBIT==0)
                    {
                        Toast.makeText(context,"Error-101",Toast.LENGTH_LONG).show();
                    }
                    login_lock = false
                    pd.cancel()
                    pd.dismiss()
                }, Response.ErrorListener {
                    login_lock = false
                    pd.cancel()
                    pd.dismiss()
                    Toast.makeText(context,it.toString(),Toast.LENGTH_LONG).show()
                })
                {
                    override fun getParams(): MutableMap<String, String> {
                        val hash = HashMap<String,String>();
                        hash["name"] = array[0]
                        hash["email"] = array[3]
                        hash["photo"] = array[4]
                        hash["id"] = array[1]
                        hash["idtoken"] = array[2]
                        return hash;
                    }
                }
                queue.add(sr)
            })
            task.addOnFailureListener {
                Toast.makeText(requireContext(),"Connection Error",Toast.LENGTH_LONG).show()
            }
        }
    }
}