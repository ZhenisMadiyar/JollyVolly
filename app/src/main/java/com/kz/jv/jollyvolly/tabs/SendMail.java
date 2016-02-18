package com.kz.jv.jollyvolly.tabs;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.kz.jv.jollyvolly.R;
import com.google.gson.Gson;
import com.parse.FunctionCallback;
import com.parse.ParseCloud;
import com.parse.ParseException;

import java.util.HashMap;

/**
 * Created by madiyarzhenis on 31.08.15.
 */
public class SendMail extends Fragment {
    EditText editTextName, editTextEmail, editTextPhone, editTextSubjectText;
    Button sendBtn;
    HashMap<String, Object> parameter;
    Gson gson;
    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.send_mail_fragment, container, false);
        parameter = new HashMap<>();
        editTextEmail = (EditText) view.findViewById(R.id.editTextEmail);
        editTextName = (EditText) view.findViewById(R.id.editTextName);
        editTextPhone = (EditText) view.findViewById(R.id.editTextTelephon);
        editTextSubjectText = (EditText) view.findViewById(R.id.editTextText);
        sendBtn = (Button) view.findViewById(R.id.buttonSend);

        gson = new Gson();
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editTextEmail.getText().toString().equals("")) {
                    Toast.makeText(getActivity(), "Пожалуйста введите Email", Toast.LENGTH_SHORT).show();
                } else {
                    parameter.put("email", editTextEmail.getText().toString());
                    parameter.put("subject_name", editTextName.getText().toString());
                    parameter.put("subject_text", editTextSubjectText.getText().toString()+"\n"+editTextPhone.getText().toString());
                    ParseCloud.callFunctionInBackground("send_email", parameter, new FunctionCallback<Object>() {
                        @Override
                        public void done(Object o, ParseException e) {
                            if (e == null) {
                                String response = gson.toJson(o);
                                Log.i("response_email", response);
                                Toast.makeText(getActivity(), "Ваше сообщение отправлено", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getActivity(), "Пожалуйста введите правильный Email", Toast.LENGTH_SHORT).show();
                                Log.e("error_email", e.getMessage());
                            }
                        }
                    });
                }
            }
        });

        return view;
    }
}