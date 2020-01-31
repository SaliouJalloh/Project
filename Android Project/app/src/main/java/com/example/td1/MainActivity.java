package com.example.td1;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Bundle;
import androidx.renderscript.Allocation;
import androidx.renderscript.RenderScript;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.security.PublicKey;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {


    private static final int CAMERA_ACTIVITY_REQUEST_CODE = 0;
    private TextView text;
    private TextView taille;
    private ImageView img;
    private Bitmap bitmap,bitmap1;
    private int width;
    private int height;
    private int tmp_color;
    private ImageView takenPhoto;
    private String photoFileName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            // Ajout des widgets

            text = findViewById(R.id.idtext);
            taille = findViewById(R.id.idtaille);
            img = findViewById(R.id.idimage);

            // Convertion de l'image

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inMutable = true;

            bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.fruit,options);
            bitmap1 = BitmapFactory.decodeResource(getResources(),R.drawable.fruit,options);

            taille.setText( "SIZE : " + bitmap.getWidth() + "*" + bitmap.getHeight());

            width = bitmap.getWidth();
            height = bitmap.getHeight();

            for(int x = 0; x < width; ++x){
                for (int y = height/2; y < height; y+=(height/2)){
                    bitmap.setPixel(x,y, Color.BLACK);
                }
            }
    }

    public void launchCameraIntent(){
        Intent intent = new Intent();
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, getPhotoFileUri(photoFileName));
        startActivityForResult(intent,CAMERA_ACTIVITY_REQUEST_CODE);
    }

    public Uri getPhotoFileUri(String fileName){
        if(isExternalStorageAvailable()){
            File mediaStorageDir = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), Tag);
            if(!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()){
                Log.d(TAG, "Failed to create directory");
            }
            return  Uri.fromFile(new File(mediaStorageDir.getPath() + File.separator + fileName));
        }
        return null;
    }

    private boolean isExternalStorageAvailable(){
        String state = Environment.getExternalStorageState();
        return state.equals(Environment.MEDIA_MOUNTED);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode == CAMERA_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK){
            Uri takenPhotoUri = getPhotoFileUri(photoFileName);
            Bitmap takenPhotoBitmap = rotateBitmap(takenPhotoUri.getPath());
            takenPhoto.setImageBitmap(takenPhotoBitmap);
        }else{
            Toast.makeText(this, "Photo wasn't taken!", Toast.LENGTH_SHORT).show();
        }
    }

    public Bitmap rotateBitmap(String photoFilePath){
        return null;
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
                img.setImageBitmap(bitmap1);
                Toast.makeText(this,"reset menu selected",Toast.LENGTH_LONG).show();
                return true;
            case R.id.menu_next:
                Intent ActivityIntent = new Intent(MainActivity.this, Main2Activity.class);
                startActivity(ActivityIntent);
                Toast.makeText(this,"next menu selected",Toast.LENGTH_LONG).show();
                return true;
            case R.id.menu_to_gray1:
                Toast.makeText(this,"to gray selected",Toast.LENGTH_LONG).show();
                toGray(bitmap);
                img.setImageBitmap(bitmap);
                return true;
            case R.id.menu_to_gray2:
                Toast.makeText(this,"to grays selected",Toast.LENGTH_LONG).show();
                toGray2(bitmap);
                img.setImageBitmap(bitmap);
                return true;
            case R.id.menu_to_grayRS:
                Toast.makeText(this,"to graysRS selected",Toast.LENGTH_LONG).show();
                toGrayRS(bitmap);
                img.setImageBitmap(bitmap);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void toGray(Bitmap bmp){
        width = bmp.getWidth();
        height = bmp.getHeight();
        int red, green, blue, gray;

        for(int x = 0; x < width; x++){
            for (int y = 0; y < height; y++){
                tmp_color = bmp.getPixel(x,y);
                red = Color.red(tmp_color);
                green = Color.green(tmp_color);
                blue = Color.blue(tmp_color);

                gray =  (int) (0.3*red + 0.59*green + 0.11*blue);
                gray = Color.rgb(gray,gray,gray);

                bmp.setPixel(x,y,gray);
            }
        }
    }

    public void toGray2(Bitmap bmp){
        width = bmp.getWidth();
        height = bmp.getHeight();
        int red, green, blue, gray;

        int[] pixels = new int[width * height];
        bmp.getPixels(pixels,0,width,0,0,width,height);

        for(int x = 0; x < width*height; x++){
                tmp_color = pixels[x];

                red = Color.red(tmp_color);
                green = Color.green(tmp_color);
                blue = Color.blue(tmp_color);

                gray = (int) (0.3*red + 0.59*green + 0.11*blue);
                pixels[x] = Color.rgb(gray,gray,gray);

        }
        bmp.setPixels(pixels,0,width,0,0,width,height);
    }

    private void toGrayRS ( Bitmap bmp ) {
        // 1) Creer un contexte RenderScript
                RenderScript rs = RenderScript.create (this) ;
        // 2) Creer des Allocations pour passer les donnees
                Allocation input = Allocation.createFromBitmap (rs,bmp) ;
                Allocation output = Allocation.createTyped (rs,input.getType()) ;
        // 3) Creer le script
                ScriptC_grays grayScript = new ScriptC_grays(rs) ;
        // 4) Copier les donnees dans les Allocations
        // ...
        // 5) Initialiser les variables globales potentielles
        // ...
        // 6) Lancer le noyau
                grayScript.forEach_toGray (input,output) ;
        // 7) Recuperer les donnees des Allocation (s)
                output.copyTo (bmp) ;
        // 8) Detruire le context , les Allocation (s) et le script
                input.destroy();
                output.destroy();
                grayScript.destroy();
                rs.destroy();
    }

}
