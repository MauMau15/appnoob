package mau.train.com.appnoob;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.List;
import java.util.logging.Logger;

public class ThirdActivity extends AppCompatActivity {
    private ImageButton mailBtn;
    private ImageButton phoneBtn;
    private EditText mailTxt;
    private EditText phoneTxt;
    private ImageButton cameraBtn;

    private final int PHONE_CALL_CODE = 100;
    private final int CAMERA_CODE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);

        //activar flecha para ir atras
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mailBtn = findViewById(R.id.mailBtn);
        phoneBtn = findViewById(R.id.phoneBtn);
        mailTxt = findViewById(R.id.mailTxt);
        phoneTxt = findViewById(R.id.phoneTxt);
        cameraBtn = findViewById(R.id.cameraBtn);

        this.phoneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNumber = phoneTxt.getText().toString();
                if(phoneNumber != null && !TextUtils.isEmpty(phoneNumber)){
                    //comprobar version de android
                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                        //comprobar si el usuario ha aceptado, no ha aceptado o nunca se le ha preguntado
                        //ha aceptado
                        if(CheckPermission(Manifest.permission.CALL_PHONE)){
                            Intent intent = new Intent(Intent.ACTION_CALL,Uri.parse("tel:" + phoneNumber));
                            if(ActivityCompat.checkSelfPermission(ThirdActivity.this,Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) return;
                            startActivity(intent);
                        }else{
                            //No se le ha preguntado
                            if(!shouldShowRequestPermissionRationale(Manifest.permission.CALL_PHONE)){
                                requestPermissions(new String[]{Manifest.permission.CALL_PHONE},PHONE_CALL_CODE);
                            }else{ //ha denegado
                                Toast.makeText(ThirdActivity.this,"Please, enable CALL_PHONE permission",Toast.LENGTH_SHORT).show();
                                Intent intentSettings = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                intentSettings.addCategory(Intent.CATEGORY_DEFAULT);
                                intentSettings.setData(Uri.parse("package:" + getPackageName()));
                                intentSettings.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intentSettings.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                                intentSettings.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                                startActivity(intentSettings);
                            }
                        }
                    }else
                        olderVersions(phoneNumber);
                }else
                    Toast.makeText(ThirdActivity.this,"Por favor, agregue su telefono",Toast.LENGTH_SHORT).show();
            }

            private void olderVersions(String phoneNumber){
                Intent intentCall = new Intent(Intent.ACTION_CALL,Uri.parse("tel:" + phoneNumber));
                if(CheckPermission(Manifest.permission.CALL_PHONE)){
                    startActivity(intentCall);
                }else{
                    Toast.makeText(ThirdActivity.this,"You declined the permission", Toast.LENGTH_SHORT).show();
                }
            }
        });

        this.mailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //String url = mailTxt.getText().toString();
                String mail = "mauriciorenero@gmail.com";
                //if(url != null && !url.isEmpty()){
                    //Intent intentWeb = new Intent(Intent.ACTION_VIEW,Uri.parse("http://"));
                    //intentWeb.setAction(Intent.ACTION_VIEW);
                    //intentWeb.setData(Uri.parse("http://" + url));

                    //Contacts
                    //Intent intentContact = new Intent(Intent.ACTION_VIEW,Uri.parse("content://contacts/people"));

                    //Email Rapido
                    //Intent intentMail = new Intent(Intent.ACTION_SENDTO,Uri.parse("mailto:"+url));

                    //Email Completo
                    Intent intentMailC = new Intent(Intent.ACTION_SEND,Uri.parse(mail));
                    //intentMailC.setClassName("com.google.android.gm","com.google.android.gm.ComposeActivityGmail");
                    intentMailC.setType("plain/text");
                    intentMailC.putExtra(Intent.EXTRA_SUBJECT,"Title");
                    intentMailC.putExtra(Intent.EXTRA_TEXT,"Hello World");
                    intentMailC.putExtra(Intent.EXTRA_EMAIL,new String[]{"mauriciorenerode3@gmail.com"});

                    PackageManager packageManager = getPackageManager();
                    List activities = packageManager.queryIntentActivities(intentMailC,PackageManager.MATCH_DEFAULT_ONLY);
                    if(activities.size() > 0){
                        Intent intentChooser = Intent.createChooser(intentMailC,"Selecciona un cliente");
                        if(intentMailC.resolveActivity(getPackageManager())!= null){
                            Toast.makeText(ThirdActivity.this,"Entró",Toast.LENGTH_SHORT).show();
                            startActivity(intentChooser);
                        }else
                            Toast.makeText(ThirdActivity.this,"No se puede abrir nada",Toast.LENGTH_SHORT).show();
                    }else
                        Toast.makeText(ThirdActivity.this,"No hay app",Toast.LENGTH_SHORT).show();



                //}else
                //    Toast.makeText(ThirdActivity.this,"Por favor, llene el campo",Toast.LENGTH_SHORT).show();
            }
        });

        this.cameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    requestPermissions(new String[]{Manifest.permission.CAMERA},CAMERA_CODE);
                    startActivityForResult(intentCamera,CAMERA_CODE);
                }else {
                    Toast.makeText(ThirdActivity.this,"No mame actualicese",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case PHONE_CALL_CODE:
                String permission = permissions[0]; //porque mandamos el permiso en la posicion 0 en el meth requestPermissions
                int result = grantResults[0];
                if(permission.equals(Manifest.permission.CALL_PHONE)){
                    //comprobar si ha sido aceptado o denegada la petición
                    if(result == PackageManager.PERMISSION_GRANTED){
                        //Concedio los permisos
                        String phoneNumber = phoneTxt.getText().toString();
                        Intent intentCall = new Intent(Intent.ACTION_CALL,Uri.parse("tel:" + phoneNumber));
                        if(ActivityCompat.checkSelfPermission(this,Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){
                            return;
                        }
                        startActivity(intentCall);
                    }else{
                        //No concedio su permiso
                        Toast.makeText(ThirdActivity.this,"You declined the permission",Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case CAMERA_CODE:
                String permissionCam = permissions[0];
                int resultCam = grantResults[0];
                if(permissionCam.equals(Manifest.permission.CAMERA)){
                    if(resultCam == PackageManager.PERMISSION_GRANTED){
                        //Concedio los permisos
                        Intent intentCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        if(ActivityCompat.checkSelfPermission(this,Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){
                            return;
                        }
                        startActivity(intentCamera);
                    }else{
                        //No concedio su permiso
                        Toast.makeText(ThirdActivity.this,"You declined the permission",Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode,permissions,grantResults);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode){
            case CAMERA_CODE:
                if(resultCode == Activity.RESULT_OK){
                    String result = data.toUri(0);
                    Toast.makeText(this,"Result: " + result,Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(this,"There was an error",Toast.LENGTH_LONG).show();
                }
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
                break;
        }
    }

    private boolean CheckPermission(String permission){
        int result = this.checkCallingOrSelfPermission(permission);
        return result == PackageManager.PERMISSION_GRANTED;
    }
}
