package mein.wasser.ui;

import android.annotation.SuppressLint;
import android.app.Service;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.safetynet.SafetyNet;
import com.google.android.gms.safetynet.SafetyNetApi;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

import mein.wasser.R;

public class phone extends Fragment {
    private String key = "";
    private int TYPE_PHONE = 1;
    private int TYPE_EMAIL = 2;
    private String code = "";
    private int lock = 0;
    public exit e;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        e.set_exit(0);
    }

    @Override
    public void onResume() {
        super.onResume();
        e.set_exit(0);
    }

    @Override
    public void onPause() {
        super.onPause();
        e.set_exit(1);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.phone, container, false);
        String[] countries = {"india"};
        EditText et = (EditText) v.findViewById(R.id.phone_edittext);
        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() == 5 && lock == 0) {
                    et.setText(charSequence.toString().trim() + "  ");
                    et.setSelection(charSequence.length() + 1);
                    lock = 1;
                }
                if (charSequence.length() == 4) {
                    lock = 0;
                }
                if (charSequence.length() == 12) {
                    InputMethodManager manager = (InputMethodManager) getActivity().getSystemService(Service.INPUT_METHOD_SERVICE);
                    manager.hideSoftInputFromWindow(et.getWindowToken(), 0);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        FloatingActionButton fab = v.findViewById(R.id.fab);
        ProgressBar pb = v.findViewById(R.id.signin_progress);
        ImageView im = v.findViewById(R.id.arrow);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String number = et.getText().toString().trim().replace(" ", "").trim();
                im.setVisibility(View.INVISIBLE);
                fab.setVisibility(View.INVISIBLE);
                pb.setVisibility(View.VISIBLE);
                //com.set_code(1);
                if (GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(requireContext()) == ConnectionResult.SUCCESS) {
                    SafetyNet.getClient(requireActivity()).attest("random".getBytes(), "AIzaSyAHFlvQWmss-iNunbYFb4FoSI5W1qzNngU")
                            .addOnSuccessListener(new OnSuccessListener<SafetyNetApi.AttestationResponse>() {
                                @Override
                                public void onSuccess(SafetyNetApi.AttestationResponse attestationResponse) {
                                    byte[] json = Base64.decode(attestationResponse.getJwsResult().split("[.]")[1], Base64.DEFAULT);
                                    String text = new String(json, StandardCharsets.UTF_8);
                                    verify_phone("+91" + number);
                                }
                            });
                }

            }
        });
        ListView s = v.findViewById(R.id.spinner);
        spinneradapter ad = new spinneradapter(getContext(), R.layout.country, countries);
        s.setAdapter(ad);
        return v;
    }

    private int existence_check(String user, int type) {
        return 0;
    }

    public void verify_phone(String phone) {
        PhoneAuthOptions options = PhoneAuthOptions.newBuilder()
                .setPhoneNumber(phone)
                .setActivity(requireActivity())
                .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                        code = phoneAuthCredential.getSmsCode();
                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {
                        Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        super.onCodeSent(s, forceResendingToken);
                        key = s;
                        RequestQueue q = Volley.newRequestQueue(getContext());
                        StringRequest sr = new StringRequest(Request.Method.POST, "https://identitytoolkit.googleapis.com/v1/accounts:lookup?key=" + key, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Toast.makeText(getContext(), response.toString(), Toast.LENGTH_LONG).show();
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(getContext(), error.toString(), Toast.LENGTH_LONG).show();
                            }
                        });
                        q.add(sr);
                    }
                })
                .setTimeout(60L, TimeUnit.SECONDS)
                .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private void signup() {
        screening_algorithm a = screening_algorithm.getInstance("");
    }

}