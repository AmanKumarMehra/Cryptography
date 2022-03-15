package com.example.implementingcryptography;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.implementingcryptography.databinding.ActivityMainBinding;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URI;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ActivityResultLauncher<String> mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri uri) {

                binding.imageView.setImageURI(uri);
                binding.textView1.setText(uri.getPath());

                InputStream inputStream = null;
                String mimeType = getApplicationContext().getContentResolver().getType(uri);

                Cursor returnCursor =  getContentResolver().query(Uri.parse(uri.getPath()), null, null, null, null);


                try {
                    //inputStream = new FileInputStream(uri.getPath());
                    inputStream = getContentResolver().openInputStream(uri);

                } catch (FileNotFoundException e2) {
                    // TODO Auto-generated catch block

                    e2.printStackTrace();
                }

                try{
                    Cipher cipher=Cipher.getInstance("AES");
                    KeyGenerator keyg=KeyGenerator.getInstance("AES");
                    //Key key = keyg.generateKey();

                    SecretKey secretKey = new SecretKeySpec("abcdefghijklmnop".getBytes(), "AES");

                    cipher.init(Cipher.ENCRYPT_MODE, secretKey);


                    //CipherInputStream cipt=new CipherInputStream(new FileInputStream(new File("D:\\testing\\asd.jpg")), cipher);
                    CipherInputStream cipt=new CipherInputStream(inputStream, cipher);

                    String str = cipt.toString();

                    binding.textView2.setText(str);


                }catch (Exception e){
                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        });

        binding.attach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mGetContent.launch("image/*");
            }
        });


    }
}