package mau.train.com.appnoob;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.logging.Logger;

public class ThirdActivity extends AppCompatActivity {
    private ImageButton mailBtn;
    private ImageButton phoneBtn;
    private EditText mailTxt;
    private EditText phoneTxt;
    private Button thirdBtn;

    private final int PHONE_CALL_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);

        mailBtn = findViewById(R.id.mailBtn);
        phoneBtn = findViewById(R.id.phoneBtn);
        mailTxt = findViewById(R.id.mailTxt);
        phoneTxt = findViewById(R.id.phoneTxt);
        thirdBtn = findViewById(R.id.thirdBtn);

        this.phoneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNumber = phoneTxt.getText().toString();
                if(phoneNumber != null && !TextUtils.isEmpty(phoneNumber)){
                    //comprobar version de android
                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                        Toast.makeText(ThirdActivity.this,"Version Mayor",Toast.LENGTH_SHORT).show();
                        //esto es para las nuevas versiones. Dispara el metodo onRequestPermission
                        requestPermissions(new String[]{Manifest.permission.CALL_PHONE},PHONE_CALL_CODE);
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
            default:
                super.onRequestPermissionsResult(requestCode,permissions,grantResults);
                break;
        }
    }

    private boolean CheckPermission(String permission){
        int result = this.checkCallingOrSelfPermission(permission);
        return result == PackageManager.PERMISSION_GRANTED;
    }
}
