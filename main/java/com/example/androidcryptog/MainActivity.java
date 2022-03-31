/* Xander Siruno-Nebel
   Java Programming Sem 2, Galbraith
   March 30, 2022
   Android Cryptography -- an app for Android that decrypts, and ecrypts, any message in SCYTALE, CAESER, or VIGENERE
 */

package com.example.androidcryptog;

//imports
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

import java.util.Locale;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    //vars
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
    String newInput;
    int cKey;
    int rows;
    int columns;
    int sKey;
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

        //initialize buttons
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
        //checks to see which cipher is selected
        checked = ((RadioButton) v).isChecked();
        if(v.getId() == R.id.radio1){
            cypher = "scytale";
            key.setVisibility(View.VISIBLE);
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
                //if a cipher is selected & we're encrypting, run it through function encrypt
                encrypting = true;
                encrypt(input, key, encrypting);
            }else if(v.getId() == R.id.button2){
                //if we're decrypting, run it through function decrypt
                encrypting = false;
                decrypt(input, key, encrypting);
            }
        }else{
            //if no cipher is selected, return error dialog box
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setTitle("No Cipher Selected");
            alertDialogBuilder
                    .setMessage("Please select a cipher")
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                        }
                    });
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
            return;
        }
    }

    public void encrypt(EditText input, EditText key, Boolean encrypting){
        //init vars
        sOut = "";
        keyOut = "";
        newInput = input.getText().toString().toUpperCase();
        uInput = "";

        //converts input to all uppercase, only letters
        for (int i = 0; i < newInput.length(); i++) {
            if(newInput.charAt(i) >= 'A' && newInput.charAt(i) <= 'Z'){
                uInput = uInput + newInput.charAt(i);
            }
        }
        uInput = uInput.replaceAll("\\s", "");
        uInput = uInput.replaceAll("[^A-Z]", "");

        //CAESER:
        if(cypher.equals("caeser")){
            //if an invalid key is used, return error dialog box
            try {
                cKey = parseInt(key.getText().toString());
            }
            catch(Exception e) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                alertDialogBuilder.setTitle("Invalid Key");
                alertDialogBuilder
                        .setMessage("Please select a valid key (Caeser Ciphers require only integers)")
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
                return;
            }

            if(cKey > 25){
                //if the key is >25, loop around
                cKey %= 25;
            }

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
                    //if within the alphabet, add the given var
                    mod = current;
                }
                sOut += (char)(mod);
            }
            output.setText(sOut);
            sOut = "";
        //VIGENERE
        }else if(cypher.equals("vigenere")){
            //init vars
            vKey = key.getText().toString().toUpperCase();
            int[] keyNums = new int[vKey.length()];

            for (int i = 0; i < vKey.length(); i++) {
                //if an invalid key is used, return error dialog box
                if(vKey.charAt(i) >= 65 && vKey.charAt(i) <= 90){
                    keyNums[i] = (int)vKey.charAt(i)-65;
                }else {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                    alertDialogBuilder.setTitle("Invalid Key");
                    alertDialogBuilder
                            .setMessage("Please select a valid key (Vigenere Ciphers require only letters)")
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.dismiss();
                                }
                            });
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                    return;
                }
            }
                for(int i = 0; i < uInput.length(); i++){
                    //variable that loops through the key
                    vWrap = i;
                    if(vWrap > keyNums.length - 1){
                        vWrap = (i % keyNums.length);
                    }

                    current = (int)uInput.charAt(i);

                    if(encrypting) {
                        mod = current + keyNums[vWrap];
                        //loops around the alphabet if we reach z
                        if(((current >= 65 && current <= 90) && mod > 96) || (mod > 90 && mod < 97)){
                            mod -= 26;
                        }else if(mod > 122){
                            mod -= 26;
                        }
                    }else if(!encrypting){
                        mod = current - keyNums[vWrap];
                        //loops around the alphabet if we reach a
                        if(((current >= 97 && current <= 122) && mod < 91) || (mod > 90 && mod < 97)){
                            mod += 26;
                        }else if(mod < 65){
                            mod += 26;
                        }
                    }
                    sOut += (char)mod;
                }
                output.setText(sOut);
                sOut = "";
        }
        //SCYTALE
        if (cypher.equals("scytale")) {
            //if invalid key is used, return error dialog box
            try{
                rows = parseInt(key.getText().toString());
            }
            catch (Exception e) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                alertDialogBuilder.setTitle("Invalid Key");
                alertDialogBuilder
                        .setMessage("Please select a valid key (Scytale ciphers require any integer)")
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
                return;
            }

            columns = uInput.length()/rows;
            int equals = uInput.length() % rows;

            if(equals != 0){
                //if the word is not evenly divisible, add a column
                columns++;
            }

            char[][] cyl = new char[columns][rows];

            if(equals != 0){
                //replaces any future space with '@'
                for (int i = equals; i < rows; i++) {
                    cyl[columns-1][i] = '@';
                }
            }

            current = 0;
            if(encrypting) {
                for (int y = 0; y < rows; y++) {
                    for (int x = 0; x < columns; x++) {
                        //goes through 3d array, replacing all blank spaces with the correct letter
                        if (current < uInput.length()) {
                            if (cyl[x][y] != '@') {
                                cyl[x][y] = uInput.charAt(current);
                                current++;
                            }
                        }
                    }
                }

                for (int x = 0; x < columns; x++) {
                    for (int y = 0; y < rows; y++) {
                        //outputs interpretation of 3d array that is encoded
                        sOut += cyl[x][y];
                    }
                }
            }else if(!encrypting){
                //same as above, only the columns and rows are swapped
                for (int x = 0; x < columns; x++) {
                    for (int y = 0; y < rows; y++) {
                        if (current < uInput.length()) {
                            if (cyl[x][y] != '@') {
                                cyl[x][y] = uInput.charAt(current);
                                current++;
                            }
                        }
                    }
                }

                for (int y = 0; y < rows; y++) {
                    for (int x = 0; x < columns; x++) {
                        if(cyl[x][y] != '@'){
                            sOut += cyl[x][y];
                        }
                    }
                }
            }
            output.setText(sOut);
            sOut = "";
        }
    }

    public void decrypt(EditText input, EditText key, Boolean encrypting){
        //calls encrypt() function with boolean encrypting in mind, which makes small changes when necessary
        if(cypher.equals("caeser")){
            encrypting = false;
            encrypt(input, key, encrypting);
        }else if(cypher.equals("vigenere")){
            encrypting = false;
            encrypt(input, key, encrypting);
        }else if(cypher.equals("scytale")){
            encrypting = false;
            encrypt(input, key, encrypting);
        }
    }
}
