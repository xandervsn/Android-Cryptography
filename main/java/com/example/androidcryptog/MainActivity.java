package com.example.androidcryptog;

import static java.lang.Integer.parseInt;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button encrypt;
    Button decrypt;
    TextView output;
    EditText input;
    EditText key;
    RadioButton scytale;
    RadioButton caeser;
    RadioButton vigenere;
    boolean checked;
    boolean yFormat = true;
    String cypher = "";
    String uInput;
    int cKey;
    String vKey;
    String keyOut;
    String sOut = "";
    Boolean encrypting;
    int current;
    int mod;
    int vWrap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        encrypt = (Button)this.findViewById(R.id.button1);
        encrypt.setOnClickListener(this);
        decrypt = (Button)this.findViewById(R.id.button2);
        decrypt.setOnClickListener(this);
        scytale = (RadioButton)this.findViewById(R.id.radio1);
        caeser = (RadioButton)this.findViewById(R.id.radio2);
        vigenere = (RadioButton)this.findViewById(R.id.radio3);
        output = (TextView)this.findViewById(R.id.textView1);
        input = (EditText)this.findViewById(R.id.editText1);
        key = (EditText)this.findViewById(R.id.editText2);
        key.setVisibility(View.GONE);
    }

    public void onRadioButtonClicked(View v){
        checked = ((RadioButton) v).isChecked();
        if(v.getId() == R.id.radio1){
            cypher = "scytale";
            key.setVisibility(View.GONE);
        }else if(v.getId() == R.id.radio2){
            cypher = "caeser";
            key.setVisibility(View.VISIBLE);
        }else if(v.getId() == R.id.radio3){
            cypher = "vigenere";
            key.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        if(checked) {
            if(v.getId() == R.id.button1){
                encrypting = true;
                encrypt(input, key, encrypting);
            }else if(v.getId() == R.id.button2){
                encrypting = false;
                decrypt(input, key, encrypting);
            }
        }else{
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setTitle("No Cypher Selected");
            alertDialogBuilder
                    .setMessage("Please select a cypher")
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                        }
                    });
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }
    }

    public void encrypt(EditText input, EditText key, Boolean easy){
        sOut = "";
        keyOut = "";
        uInput = input.getText().toString();
        if(cypher.equals("caeser")){
            try {
                cKey = parseInt(key.getText().toString());
            }
            catch(Exception e) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                alertDialogBuilder.setTitle("Invalid Key");
                alertDialogBuilder
                        .setMessage("Please select a valid key (Caeser Cyphers require only integers)")
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }

            if(cKey > 25){ cKey %= 25; }

            for (int i = 0; i < uInput.length(); i++) {
                current = (int)uInput.charAt(i);

                if(encrypting) {
                    mod = current + cKey;
                    //loops around the alphabet if we reach z
                    if(((current >= 65 && current <= 90) && mod > 96) || (mod > 90 && mod < 97)){
                        mod -= 26;
                    }else if(mod > 122){
                        mod -= 26;
                    }
                }else if(!encrypting){
                    mod = current - cKey;
                    //loops around the alphabet if we reach a
                    if(((current >= 97 && current <= 122) && mod < 91) || (mod > 90 && mod < 97)){
                        mod += 26;
                    }else if(mod < 65){
                        mod += 26;
                    }
                }

                if((current > 122 || current < 64) || (current > 90 && current < 97)){
                    mod = current;
                }
                sOut += (char)(mod);
                output.setText(sOut);
            }
        }else if(cypher.equals("vigenere")){
            vKey = key.getText().toString();
            int[] keyNums = new int[vKey.length()];
            for (int i = 0; i < vKey.length(); i++) {
                if(vKey.charAt(i) >= 65 && vKey.charAt(i) <= 90){
                    keyNums[i] = (int)vKey.charAt(i)-64;
                }else if(vKey.charAt(i) >= 97 && vKey.charAt(i) <= 122){
                    keyNums[i] = (int)vKey.charAt(i)-96;
                }else {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                    alertDialogBuilder.setTitle("Invalid Key");
                    alertDialogBuilder
                            .setMessage("Please select a valid key (Vigenere cyphers require only letters)")
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.dismiss();
                                }
                            });
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                    yFormat = false;

                }
            }
            if(yFormat) {
                for(int i = 0; i < uInput.length(); i++){
                    vWrap = i;
                    if(vWrap > keyNums.length - 1){
                        vWrap = (i % keyNums.length);
                    }

                    current = (int)uInput.charAt(i);

                    if(encrypting) {
                        mod = current + cKey;
                        //loops around the alphabet if we reach z
                        if(((current >= 65 && current <= 90) && mod > 96) || (mod > 90 && mod < 97)){
                            mod -= 26;
                        }else if(mod > 122){
                            mod -= 26;
                        }
                    }else if(!encrypting){
                        mod = current - cKey;
                        //loops around the alphabet if we reach a
                        if(((current >= 97 && current <= 122) && mod < 91) || (mod > 90 && mod < 97)){
                            mod += 26;
                        }else if(mod < 65){
                            mod += 26;
                        }
                    }

                    sOut += (char)mod;
                }
                output.setText(sOut + ", " + encrypting);
            }
        }

        if (cypher.equals("scytale")) {
            output.setText("scytale");
        }
    }

    public void decrypt(EditText input, EditText key, Boolean encrypting){
        if(cypher.equals("caeser")){
            encrypting = false;
            encrypt(input, key, encrypting);
        }else if(cypher.equals("vigenere")){
            encrypting = false;
            encrypt(input, key, encrypting);
        }
    }
}