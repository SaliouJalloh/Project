package com.example.td1;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.renderscript.Allocation;
import androidx.renderscript.RenderScript;

import static android.graphics.Color.RGBToHSV;
import static android.graphics.Color.blue;
import static android.graphics.Color.green;
import static android.graphics.Color.red;
import static android.graphics.Color.rgb;

public class MainActivity extends AppCompatActivity{

    private ImageView img;
    private Bitmap bitmap, originbitmap;
    private ImageButton photo, loading, save, reset;
    private  String photoPath = null;

    // Constantes
    private static final int REQUEST_TAKE_PHOTO = 10;
    private static final int REQUEST_IMAGE_LOAD = 1;

    Uri photoUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initActivity();

    }

    private void initActivity() {
        // instanciation
        img = findViewById(R.id.idimage);
        reset = findViewById(R.id.id_reset);
        photo = findViewById(R.id.id_photo);
        loading = findViewById(R.id.loadingID);
        save = findViewById(R.id.id_saved);

        // Convertion de l'image
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inMutable = true;

        //initialisation des bitmap
        bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.leguime, options);
        originbitmap = BitmapFactory.decodeResource(getResources(),R.drawable.leguime, options);
        //bitmap = BitmapFactory.decodeResource(getResources(),R.xml.provider_paths, options);
        //originbitmap = BitmapFactory.decodeResource(getResources(),R.xml.provider_paths, options);

        //Ouverture des boutons
        createOnClickButton();

    }

    /**
     * Methode pour la gestion des cliques bouton
     */
    private void createOnClickButton(){

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pix[] = new int [originbitmap.getHeight()*originbitmap.getWidth()];
                originbitmap.getPixels(pix, 0, originbitmap.getWidth(), 0, 0, originbitmap.getWidth(), originbitmap.getHeight());
                bitmap.setPixels(pix, 0, originbitmap.getWidth(), 0, 0, originbitmap.getWidth(), originbitmap.getHeight());
                img.setImageBitmap(bitmap);
            }
        });

        loading.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Accès à la gallery photo
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent,REQUEST_IMAGE_LOAD);
             }
        });

        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Accès à la gallery photo
                takePicture();

             }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MediaStore.Images.Media.insertImage(getContentResolver(),bitmap,"image saved","description");
             }
        });
    }

    /**
     * Methode qui permet de prendre une photo
     * depuis la ou les camera(s) du telephone
     */

    private void takePicture(){
        // creer un intent pour ouvrir une fenetre pour prendre une photo
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // test pour
        if(intent.resolveActivity(getPackageManager()) != null){
            // creer un noveau fichier
            String time = new SimpleDateFormat("yyyyMMMdd_HHmmss").format(new Date());
            File photoDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            try {
                File photoFile = File.createTempFile("photo"+time,".jpg",photoDir);
                // Enregistrer le chemin complet
                photoPath = photoFile.getAbsolutePath();
                // creer l'Uri
                photoUri = FileProvider.getUriForFile(MainActivity.this, MainActivity.this.getApplicationContext().getPackageName()+ ".provider",photoFile);

                //rotation de l'image
                //bitmap = rotateBitmap(photoUri.getPath());
                //img.setImageBitmap(bitmap);

                //Pour convertir l'Uri en Bitmap
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), photoUri);

                intent.putExtra(MediaStore.EXTRA_OUTPUT,bitmap);

                startActivityForResult(intent,REQUEST_TAKE_PHOTO);

            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    /**
     * It redirects to another activity like opens camera, gallery, etc.
     * After taking image from gallery or camera then come back to current activity first method that calls is
     * onActivityResult(int requestCode, int resultCode, Intent data) . We get the result in this method like taken image from camera or gallery.
     * @param requestCode
     * @param resultCode
     * @param data
     */

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Verifie si une image est recuperée
        if(requestCode == REQUEST_IMAGE_LOAD && resultCode == RESULT_OK ){

            // Accès à  l'image à  partir de data
            Uri selectImage = data.getData();
            String [] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = this.getContentResolver().query(selectImage,filePathColumn,null,null,null);

            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String imgPath = cursor.getString(columnIndex);
            cursor.close();

            bitmap = BitmapFactory.decodeFile(imgPath);
            originbitmap = BitmapFactory.decodeFile(imgPath);

            // redimenssioner l'image
            bitmap = changeSizeBitmap(bitmap,0.8f);
            originbitmap = changeSizeBitmap(originbitmap,0.8f);

            //affichage
            img.setImageBitmap(bitmap);
        }
        else { //Camera
            if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK ){

                //recupere l'image
                bitmap = BitmapFactory.decodeFile(photoPath);
                originbitmap = BitmapFactory.decodeFile(photoPath);

                // redimenssioner l'image
                //bitmap = changeSizeBitmap(bitmap,0.8f);
                //originbitmap = changeSizeBitmap(originbitmap,0.8f);

                //rotation de l'image
                //bitmap = rotateBitmap(photoUri.getPath());

                //afficher l'image
                img.setImageBitmap(bitmap);
            }
        }

    }

    /**
     * Methode permettant de ré-dimensionner une image selon une certaine proportion.
     * @param bitmap
     * @param proportion
     * @return
     */

    private Bitmap changeSizeBitmap(Bitmap bitmap, float proportion){
        // metrique
        DisplayMetrics metrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(metrics);

        float screenHeight = metrics.heightPixels*proportion;
        float screenWidth = metrics.widthPixels*proportion;

        float bitmapHeight = bitmap.getHeight();
        float bitmapWidth = bitmap.getWidth();

        float ratioHeight = screenHeight/bitmapHeight;
        float ratioWidth = screenWidth/bitmapWidth;

        float ratio = Math.min(ratioHeight,ratioWidth);

        bitmap = Bitmap.createScaledBitmap(bitmap,(int) (bitmapWidth*ratio), (int) (bitmapHeight*ratio), true);

        return bitmap;
    }

    /**
     * Methode qui retourne la photo prise depuis la camera dans le bon sens
     * car la photo prise aura subit une rotation
     * @param photoFilePath
     * @return
     */

    public Bitmap rotateBitmap(String photoFilePath){

        BitmapFactory.Options bounds = new BitmapFactory.Options();
        bounds.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(photoFilePath,bounds);

        BitmapFactory.Options opts = new BitmapFactory.Options();
        Bitmap bm = BitmapFactory.decodeFile(photoFilePath,opts);

        ExifInterface exif = null;
        try {
            exif = new ExifInterface(photoFilePath);
        }catch (IOException e){
            e.printStackTrace();
        }

        int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,ExifInterface.ORIENTATION_NORMAL);
        Matrix matrix = new Matrix();
        switch (orientation){
            case ExifInterface.ORIENTATION_ROTATE_90:
                matrix.postRotate(90);
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                matrix.postRotate(180);
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                matrix.postRotate(270);
        }
        Bitmap rotatedBitmap = Bitmap.createBitmap(bm,0,0,bm.getWidth(),bm.getHeight(),matrix,true);
        return rotatedBitmap;
    }

    /**
     * Methode qui grise tous les pixels du Bitmap en
     * utilisant le RenderScript.
     * @param bmp
     */
    public void toGrayRS(Bitmap bmp) {
        // 1) Creer un contexte RenderScript
        RenderScript rs = RenderScript.create(this);
        // 2) Creer des Allocations pour passer les donnees
        Allocation input = Allocation.createFromBitmap(rs, bmp);
        Allocation output = Allocation.createTyped(rs, input.getType());
        // 3) Creer le script
        ScriptC_grays grayScript = new ScriptC_grays(rs);
        // 4) Copier les donnees dans les Allocations
        // ...
        // 5) Initialiser les variables globales potentielles
        // ...
        // 6) Lancer le noyau
        grayScript.forEach_toGray(input, output);
        // 7) Recuperer les donnees des Allocation (s)
        output.copyTo(bmp);
        // 8) Detruire le context , les Allocation (s) et le script
        input.destroy();
        output.destroy();
        grayScript.destroy();
        rs.destroy();
    }

    /**
     * pour la creation du menu
     * @param menu
     * @return
     */

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu,menu);
        return true;
    }

    /**
     * pour la creation du menu et l'appel des differentes methodes implementées
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_gray:
                Toast.makeText(this,"Gray menu selected",Toast.LENGTH_LONG).show();
                return true;
            case R.id.menu_to_gray1:
                Toast.makeText(this,"to gray selected",Toast.LENGTH_LONG).show();
                Gray.toGray(bitmap);
                img.setImageBitmap(bitmap);
                return true;
            case R.id.menu_to_gray2:
                Toast.makeText(this,"to grays selected",Toast.LENGTH_LONG).show();
                Gray.toGray2(bitmap);
                img.setImageBitmap(bitmap);
                return true;
            case R.id.menu_to_grayRS:
                Toast.makeText(this,"to graysRS selected",Toast.LENGTH_LONG).show();
                toGrayRS(bitmap);
                img.setImageBitmap(bitmap);
                return true;
            case R.id.menu_colorize:
                Toast.makeText(this,"colorize menu selected",Toast.LENGTH_LONG).show();
                return true;
            case R.id.menu_to_colorized:
                Colorize.colorized(bitmap);
                img.setImageBitmap(bitmap);
                Toast.makeText(this,"to colorized selected",Toast.LENGTH_LONG).show();
                return true;
            case R.id.menu_to_colorize:
                Colorize.colorize(bitmap);
                Toast.makeText(this,"to colorize selected",Toast.LENGTH_LONG).show();
                img.setImageBitmap(bitmap);
                return true;
            case R.id.menu_canned_color:
                Colorize.cannedColor(bitmap);
                img.setImageBitmap(bitmap);
                Toast.makeText(this,"canned color selected",Toast.LENGTH_LONG).show();
                return true;
            case R.id.menu_contrast:
                Toast.makeText(this,"contrast menu selected",Toast.LENGTH_LONG).show();
                return true;
            case R.id.menu_increasesContrast:
                Contrast.increasesContrast(bitmap);
                img.setImageBitmap(bitmap);
                Toast.makeText(this,"increases selected",Toast.LENGTH_LONG).show();
                return true;
            case R.id.menu_increasesContrastLut:
                Toast.makeText(this,"increases lut selected",Toast.LENGTH_LONG).show();
                Contrast.increasesContrastLUT(bitmap);
                img.setImageBitmap(bitmap);
                return true;
            case R.id.menu_decreasesContrastLut:
                Contrast.decreasesContrastLUT(bitmap);
                img.setImageBitmap(bitmap);
                Toast.makeText(this,"decreases selected",Toast.LENGTH_LONG).show();
                return true;
            case R.id.menu_upContrastLut:
                Contrast.UpContrasteColor(bitmap);
                img.setImageBitmap(bitmap);
                Toast.makeText(this,"up contrast selected",Toast.LENGTH_LONG).show();
                return true;
            case R.id.menu_downContrastLut:
                Contrast.downContrasteColor(bitmap);
                img.setImageBitmap(bitmap);
                Toast.makeText(this,"down contrast selected",Toast.LENGTH_LONG).show();
                return true;
            case R.id.menu_to_convolution:
                Toast.makeText(this,"Convolution Moy selected",Toast.LENGTH_LONG).show();
                Convolution.convolutionMoy(bitmap,13);
                img.setImageBitmap(bitmap);
                return true;
            case R.id.menu_to_convolutionGaus:
                Toast.makeText(this,"Convolution Gauss selected",Toast.LENGTH_LONG).show();
                Convolution.convolutionGaussN(bitmap,9);
                img.setImageBitmap(bitmap);
                return true;
            case R.id.menu_to_convolutionSobel:
                Toast.makeText(this,"Contour selected",Toast.LENGTH_LONG).show();
                Convolution.convolutionSobel(bitmap);
                img.setImageBitmap(bitmap);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}