package com.example.td1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class Main2Activity extends AppCompatActivity {

    private TextView text;
    private TextView taille;
    private ImageView img;
    private Bitmap bitmap;
    private Button button, to_gray1, to_gray2;
    private int width;
    private int height;
    private int tmp_color;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        // Ajout des widgets
        text = findViewById(R.id.textView2);
        taille = findViewById(R.id.textView3);
        img = findViewById(R.id.idimage);
        button = findViewById(R.id.idbutton);

        // Convertion de l'image

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inMutable = true;

        // convertToMutable(img_bp);
        bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.fruit,options);

        taille.setText( "Taille : " + bitmap.getWidth() + "*" + bitmap.getHeight());

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ActivityIntent = new Intent(Main2Activity.this, Main3Activity.class);
                startActivity(ActivityIntent);
            }
        });
    }

    public void colorize (Bitmap bmp){

    }

    public  void RGBToHSV(Bitmap bitmap){

    }

    public void HSVToColor(Bitmap bitmap){

    }

    public void Conserver(){

    }
}
