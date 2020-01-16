package com.example.td1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.renderscript.RenderScript;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {


    private TextView text;
    private TextView taille;
    private ImageView img;
    private Bitmap bitmap;
    private Button nextbutton1, to_gray1, to_gray2;
    private int width;
    private int height;
    private int tmp_color;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            // Ajout des widgets

            text = findViewById(R.id.idtext);
            taille = findViewById(R.id.idtaille);
            img = findViewById(R.id.idimage);
            nextbutton1 = findViewById(R.id.idbutton);
            to_gray1 = findViewById(R.id.idbuttonGray);
            to_gray2 = findViewById(R.id.idbuttonGrays);

            // Convertion de l'image

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inMutable = true;

            bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.fruit,options);

            taille.setText( "SIZE : " + bitmap.getWidth() + "*" + bitmap.getHeight());

            nextbutton1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent ActivityIntent = new Intent(MainActivity.this, Main2Activity.class);
                    startActivity(ActivityIntent);
                }
            });
            to_gray1.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    toGray(bitmap);
                    img.setImageBitmap(bitmap);
                }
            });
            to_gray2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    toGray2(bitmap);
                    img.setImageBitmap(bitmap);
                }
            });

            width = bitmap.getWidth();
            height = bitmap.getHeight();

            for(int x = 0; x < width; ++x){
                for (int y = height/2; y < height; y+=(height/2)){
                    bitmap.setPixel(x,y, Color.BLACK);
                }
            }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_reset:
                Toast.makeText(this,"reset menu selected",Toast.LENGTH_LONG).show();
                return true;
            case R.id.menu_next:
                Toast.makeText(this,"next menu selected",Toast.LENGTH_LONG).show();
                return true;
            case R.id.menu_to_gray1:
                Toast.makeText(this,"to gray selected",Toast.LENGTH_LONG).show();
                Intent intentTogray = new Intent(this,MainActivity.class);
                startActivity(intentTogray);
                return true;
            case R.id.menu_to_gray2:
                Toast.makeText(this,"to grays selected",Toast.LENGTH_LONG).show();
                Intent intentTograys = new Intent(this,MainActivity.class);
                startActivity(intentTograys);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void toGray(Bitmap bmp){
        width = bmp.getWidth();
        height = bmp.getHeight();
        int R, G, B, gray;

        for(int x = 0; x < width; x++){
            for (int y = 0; y < height; y++){
                tmp_color = bmp.getPixel(x,y);
                R = Color.red(tmp_color);
                G = Color.green(tmp_color);
                B = Color.blue(tmp_color);

                gray =  (int) (0.3*R + 0.59*G + 0.11*B);
                gray = Color.rgb(gray,gray,gray);

                bmp.setPixel(x,y,gray);
            }
        }
    }

    public void toGray2(Bitmap bmp){
        width = bmp.getWidth();
        height = bmp.getHeight();
        int R, G, B, gray ;

        int[] pixels = new int[width * height];
        bmp.getPixels(pixels,0,width,0,0,width,height);

        for(int x = 0; x < width*height; x++){
                tmp_color = pixels[x];

                R = Color.red(tmp_color);
                G = Color.green(tmp_color);
                B = Color.blue(tmp_color);

                gray = (int) (0.3*R + 0.59*G + 0.11*B);
                pixels[x] = Color.rgb(gray,gray,gray);

        }
        bmp.setPixels(pixels,0,width,0,0,width,height);
    }

}
