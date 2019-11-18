package com.example.td1;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class Main3Activity extends AppCompatActivity {

    private TextView text;
    private TextView Size;
    private ImageView img;
    private Bitmap bitmap;
    private Button nextbutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        // Ajout des widgets
        text = findViewById(R.id.idtext);
        Size = findViewById(R.id.idsize3);
        img = findViewById(R.id.idimage);
        nextbutton = findViewById(R.id.idnext3

        );

        // Convertion de l'image

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inMutable = true;

        // convertToMutable(img_bp);
        bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.fruit,options);

        Size.setText( "Taille : " + bitmap.getWidth() + "*" + bitmap.getHeight());
    }
}
