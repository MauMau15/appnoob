package mau.train.com.appnoob;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class SecondActivity extends AppCompatActivity {

    private TextView txtGreeter;
    private Button secondBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        //activar flecha para ir atras
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        this.txtGreeter = findViewById(R.id.txt_greeter);
        this.secondBtn = findViewById(R.id.second_btn);
        Bundle bundle = getIntent().getExtras();
        String greeter = bundle != null ? bundle.getString("greeter") : "No hay texto disponible";
        this.txtGreeter.setText(greeter);
        Toast.makeText(SecondActivity.this,greeter,Toast.LENGTH_SHORT);
        this.secondBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SecondActivity.this,ThirdActivity.class);
                startActivity(intent);
            }
        });
    }
}
