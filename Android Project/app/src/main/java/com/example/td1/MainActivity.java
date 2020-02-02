package com.example.td1;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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

public class MainActivity extends AppCompatActivity {


    private static final int CAMERA_ACTIVITY_REQUEST_CODE = 0;
    private TextView text,size;
    private Button reset, loading;
    private ImageView img;
    private Bitmap bitmap,bitmapr,bitmap2,bitmap2r;
    private int width;
    private int height;
    private int tmp_color;
    private ImageView takenPhoto;
    private String photoFileName;
    private ImageView imageView;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_TAKE_PHOTO = 1;
    private static final int SELECT_PICTURE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            // Ajout des widgets

            text = findViewById(R.id.idtext);
            size = findViewById(R.id.idtaille);
            img = findViewById(R.id.idimage);
            reset = findViewById(R.id.ResetID);
            loading = findViewById(R.id.loadingID);

            // Convertion de l'image

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inMutable = true;

            bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.fruit,options);
            bitmapr = BitmapFactory.decodeResource(getResources(),R.drawable.fruit,options);
            bitmap2 = BitmapFactory.decodeResource(getResources(),R.drawable.leguime,options);
            bitmap2r = BitmapFactory.decodeResource(getResources(),R.drawable.leguime,options);


            size.setText( "SIZE : " + bitmap2.getWidth() + "*" + bitmap2.getHeight());

            reset.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    img.setImageBitmap(bitmap2r);
                }
            });

            loading.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // in onCreate or any event where your want the user to
                    // select a file
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent,"Select Picture"), SELECT_PICTURE);
                }
            });

            width = bitmap2.getWidth();
            height = bitmap2.getHeight();

            for(int x = 0; x < width; ++x){
                for (int y = height/2; y < height; y+=(height/2)){
                    bitmap2.setPixel(x,y, Color.BLACK);
                }
            }
    }

    private void dispatchTakePictureIntent(){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE );
        // Assurez-vous qu'il existe une activité de caméra pour gérer l'intention
        if(takePictureIntent.resolveActivity(getPackageManager()) != null){
            //Créez le fichier où la photo doit aller
            File photoFile = null;
            try {
                photoFile = createImageFile();
            }catch(IOException ex){
                // Une erreur s'est produite lors de la création du fichier ...
            }
            // Continuer uniquement si le fichier a été créé avec succès
            if(photoFile != null){
                Uri photoURI = FileProvider.getUriForFile(this,"com.example.android.fileprovider",photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,photoURI);
                startActivityForResult(takePictureIntent,REQUEST_TAKE_PHOTO);
            }
        }
    }


    @Override
    protected void onActivityResult( int requestCode , int resultCode , Intent data ) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK ) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap)extras.get("data");
            imageView.setImageBitmap(imageBitmap);
        }
    }

    String currentPhotoPath ;
    private File createImageFile() throws IOException {
        // Créer un nom de fichier image
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_" ;
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName,/*prefix*/
                ".jpg",/*suffix*/
                storageDir/*directory*/);
        // Enregistrer un fichier: chemin à utiliser avec ACTION_VIEW intents
        currentPhotoPath = "file" + image.getAbsolutePath();
        return image;
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
            case R.id.menu_gray:
                Toast.makeText(this,"Gray menu selected",Toast.LENGTH_LONG).show();
                return true;
            case R.id.menu_to_gray1:
                Toast.makeText(this,"to gray selected",Toast.LENGTH_LONG).show();
                toGray(bitmap2);
                img.setImageBitmap(bitmap2);
                return true;
            case R.id.menu_to_gray2:
                Toast.makeText(this,"to grays selected",Toast.LENGTH_LONG).show();
                toGray2(bitmap2);
                img.setImageBitmap(bitmap2);
                return true;
            case R.id.menu_to_grayRS:
                Toast.makeText(this,"to graysRS selected",Toast.LENGTH_LONG).show();
                toGrayRS(bitmap2);
                img.setImageBitmap(bitmap2);
                return true;
            case R.id.menu_colorize:
                Toast.makeText(this,"colorize menu selected",Toast.LENGTH_LONG).show();
                return true;
            case R.id.menu_to_colorized:
                colorized(bitmap2);
                img.setImageBitmap(bitmap2);
                Toast.makeText(this,"to colorized selected",Toast.LENGTH_LONG).show();
                return true;
            case R.id.menu_to_colorize:
                colorize(bitmap2);
                Toast.makeText(this,"to colorize selected",Toast.LENGTH_LONG).show();
                img.setImageBitmap(bitmap2);
                return true;
            case R.id.menu_canned_color:
                cannedColor(bitmap2);
                img.setImageBitmap(bitmap2);
                Toast.makeText(this,"canned color selected",Toast.LENGTH_LONG).show();
                return true;
            case R.id.menu_contrast:
                Toast.makeText(this,"contrast menu selected",Toast.LENGTH_LONG).show();
                return true;
            case R.id.menu_increasesContrast:
                increasesContrast(bitmap2);
                img.setImageBitmap(bitmap2);
                Toast.makeText(this,"increases selected",Toast.LENGTH_LONG).show();
                return true;
            case R.id.menu_increasesContrastLut:
                Toast.makeText(this,"increases lut selected",Toast.LENGTH_LONG).show();
                increasesContrastLUT(bitmap);
                img.setImageBitmap(bitmap);
                return true;
            case R.id.menu_decreasesContrastLut:
                decreasesContrastLUT(bitmap);
                img.setImageBitmap(bitmap);
                Toast.makeText(this,"decreases selected",Toast.LENGTH_LONG).show();
                return true;
            case R.id.menu_upContrastLut:
                upContrasteColor(bitmap);
                img.setImageBitmap(bitmap);
                Toast.makeText(this,"up contrast selected",Toast.LENGTH_LONG).show();
                return true;
            case R.id.menu_downContrastLut:
                downContrasteColor(bitmap);
                img.setImageBitmap(bitmap);
                Toast.makeText(this,"down contrast selected",Toast.LENGTH_LONG).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    /*public void launchCameraIntent(){
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
*/

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

    /**********************************************************************************************/
    // methode colorize sans re-écrire RGBToHSV()/HSVToRGB() .

    public void colorized (Bitmap bmp){
        width = bmp.getWidth();
        height = bmp.getHeight();

        int[] pixels = new int[width*height];
        float [] hsv = new float[3];
        int red, green, blue;

        int random = (int) (Math.random()*(360+1));
        float rand = (float) random;
        bmp.getPixels(pixels,0,width,0,0,width,height);

        for (int x = 0; x < width*height; x++){
            tmp_color = pixels[x];

            red = Color.red(tmp_color);
            green = Color.green(tmp_color);
            blue = Color.blue(tmp_color);

            RGBToHSV(red,green,blue,hsv);

            hsv[0] = rand;

            pixels[x] = Color.HSVToColor(hsv);

        }
        bmp.setPixels(pixels,0,width,0,0,width,height);

    }

    /**********************************************************************************************/

    //Ré-ecrire de le methode RGBToHSV

    public void RGBToHSV_new(int red, int green, int blue, float[] h) {
        float hh = 0;
        float r = (float) red / 255;
        float g = (float) green / 255;
        float b = (float) blue / 255;

        float cmax = Math.max(Math.max(r, g), b);
        float cmin = Math.min(Math.min(r, g), b);
        float diff = cmax - cmin;

        // Calcule de H
        if (cmax == 0) {
            h[0] = 0;
            h[1] = 0;
            h[2] = 0;
            return;
        } else if (cmax == r)
            hh = (g - b) / diff;
        else if (cmax == g)
            hh = (r - g) / diff + 2;
        else if (cmax == b)
            hh = 4 + (r - g) / diff;

        hh *= 60.0;

        //Calcule de S
        if (hh < 0)
            hh += 360;

        float s = diff / cmax;

        h[0] = hh;
        h[1] = s;
        h[2] = cmax;

    }

    /*********************************************************************************/

    //Ré-ecrire de le methode colorize avec la nouvelle methode RGBToHSV

    public void colorize (Bitmap bmp){
        width = bmp.getWidth();
        height = bmp.getHeight();

        int[] pixels = new int[width*height];
        float [] hsv = new float[3];
        int red, green, blue;

        int random = (int) (Math.random()*(360+1));
        float rand = (float) random;
        bmp.getPixels(pixels,0,width,0,0,width,height);

        for (int x = 0; x < width*height; x++){
            tmp_color = pixels[x];

            red = Color.red(tmp_color);
            green = Color.green(tmp_color);
            blue = Color.blue(tmp_color);

            RGBToHSV_new(red,green,blue,hsv);

            hsv[0] = rand;

            pixels[x] = Color.HSVToColor(hsv);

        }
        bmp.setPixels(pixels,0,width,0,0,width,height);

    }

    /**********************************************************************/

    public void cannedColor(Bitmap bmp) {
        width = bmp.getWidth();
        height = bmp.getHeight();

        int red, green, blue, gray;
        int pixels [] = new int[width*height];
        float[] hsv = new float[3];

        bmp.getPixels(pixels,0,width,0,0,width,height);

        for (int x = 0; x < width*height; x++) {

            tmp_color = pixels[x];

            red = Color.red(tmp_color);
            green = Color.blue(tmp_color);
            blue = Color.blue(tmp_color);

            Color.RGBToHSV(red,green,blue,hsv);

            if (hsv[0]>20 && hsv[0]<345){
                gray = (int) ((0.3 * red) + (0.59 * green) + (0.11 * blue));
                pixels[x] = Color.rgb( gray, gray, gray);
            }

        }
        bmp.setPixels(pixels,0,width,0,0,width,height);
    }

    public static int HSBtoRGB(float hue, float saturation, float brightness) {
        int r = 0, g = 0, b = 0;
        if (saturation == 0) {
            r = g = b = (int) (brightness * 255.0f + 0.5f);
        } else {
            float h = (hue - (float)Math.floor(hue)) * 6.0f;
            float f = h - (float)java.lang.Math.floor(h);
            float p = brightness * (1.0f - saturation);
            float q = brightness * (1.0f - saturation * f);
            float t = brightness * (1.0f - (saturation * (1.0f - f)));
            switch ((int) h) {
                case 0:
                    r = (int) (brightness * 255.0f + 0.5f);
                    g = (int) (t * 255.0f + 0.5f);
                    b = (int) (p * 255.0f + 0.5f);
                    break;
                case 1:
                    r = (int) (q * 255.0f + 0.5f);
                    g = (int) (brightness * 255.0f + 0.5f);
                    b = (int) (p * 255.0f + 0.5f);
                    break;
                case 2:
                    r = (int) (p * 255.0f + 0.5f);
                    g = (int) (brightness * 255.0f + 0.5f);
                    b = (int) (t * 255.0f + 0.5f);
                    break;
                case 3:
                    r = (int) (p * 255.0f + 0.5f);
                    g = (int) (q * 255.0f + 0.5f);
                    b = (int) (brightness * 255.0f + 0.5f);
                    break;
                case 4:
                    r = (int) (t * 255.0f + 0.5f);
                    g = (int) (p * 255.0f + 0.5f);
                    b = (int) (brightness * 255.0f + 0.5f);
                    break;
                case 5:
                    r = (int) (brightness * 255.0f + 0.5f);
                    g = (int) (p * 255.0f + 0.5f);
                    b = (int) (q * 255.0f + 0.5f);
                    break;
            }
        }
        return 0xff000000 | (r << 16) | (g << 8) | (b << 0);
    }

    private void colorizeRS ( Bitmap bmp ) {
        // 1) Creer un contexte RenderScript
        RenderScript rs = RenderScript.create (this) ;
        // 2) Creer des Allocations pour passer les donnees
        Allocation input = Allocation.createFromBitmap (rs,bmp) ;
        Allocation output = Allocation.createTyped (rs,input.getType()) ;
        // 3) Creer le script
        ScriptC_colorize colorizeScript = new ScriptC_colorize(rs) ;
        // 4) Copier les donnees dans les Allocations
        // ...
        // 5) Initialiser les variables globales potentielles
        // ...
        // 6) Lancer le noyau
        colorizeScript.forEach_colorized(input,output) ;
        // 7) Recuperer les donnees des Allocation (s)
        output.copyTo (bmp) ;
        // 8) Detruire le context , les Allocation (s) et le script
        input.destroy();
        output.destroy();
        colorizeScript.destroy();
        rs.destroy();
    }

    private void cannedColorRS ( Bitmap bmp ) {
        // 1) Creer un contexte RenderScript
        RenderScript rs = RenderScript.create (this) ;
        // 2) Creer des Allocations pour passer les donnees
        Allocation input = Allocation.createFromBitmap (rs,bmp) ;
        Allocation output = Allocation.createTyped (rs,input.getType()) ;
        // 3) Creer le script
        ScriptC_cannedColor cannedColorScript = new ScriptC_cannedColor(rs) ;
        // 4) Copier les donnees dans les Allocations
        // ...
        // 5) Initialiser les variables globales potentielles
        // ...
        // 6) Lancer le noyau
        cannedColorScript.forEach_cannedColor(input,output) ;
        // 7) Recuperer les donnees des Allocation (s)
        output.copyTo (bmp) ;
        // 8) Detruire le context , les Allocation (s) et le script
        input.destroy();
        output.destroy();
        cannedColorScript.destroy();
        rs.destroy();
    }

    // Image en niveaux de gris augmentation/diminution du contraste par extension du dynamique.

    //Fonctions auxiliaires

    public int[] histogram(Bitmap bmp, int c) {
        width = bmp.getWidth();
        height = bmp.getHeight();
        int[] pixels = new int[width * height];
        int histo[]  = new int[256];

        int R ;

        bmp.getPixels (pixels, 0, width, 0, 0, width, height);

        for (int i = 0 ; i < width*height ; i++){

            tmp_color = pixels[i];

            if ( c == Color.RED)
                R = Color.red(tmp_color);

            if ( c == Color.GREEN)
                R = Color.green(tmp_color);

            if ( c == Color.BLUE)
                R = Color.blue(tmp_color);

            else
                R = ( Color.red(tmp_color) + Color.green(tmp_color) + Color.blue(tmp_color) ) / 3;

            histo[R] ++;
        }
        return histo;
    }

    public int[] minMax(int[] histotab) {
        int tab[]  = new int[2];

        int min = 0, max = 0;

        for (int i = 0 ; i < 256 ; i++){
            if (histotab[i] != 0){
                min = histotab[i];
                break;
            }
        }
        for (int i = 255 ; i >=0 ; i--){
            if (histotab[i] != 0){
                max = histotab[i];
                break;
            }
        }
        tab[0] = min;
        tab[1] = max;
        return tab;
    }

    public void increasesContrast(Bitmap bmp){
        width = bmp.getWidth();
        height = bmp.getHeight();
        int[] pixels = new int[width * height];
        int[] histo = histogram(bmp, Color.RED);
        int[] minMax = minMax(histo);
        int color, R;

        bmp.getPixels (pixels, 0, width, 0, 0, width, height);

        for (int i = 0 ; i < width*height ; i++){
            tmp_color = pixels[i];
            R = Color.red(tmp_color);
            color = ( 255 * (R - minMax[0]) ) / (minMax[1] - minMax[0]);
            pixels[i] = Color.rgb(color, color, color);
        }
        bmp.setPixels(pixels, 0, width, 0, 0, width, height);
    }

    //Utilisation d'une Look Up Table pour augmenter le contraste

    public void increasesContrastLUT(Bitmap bmp){
        width = bmp.getWidth();
        height = bmp.getHeight();
        int[] pixels = new int[width * height];
        int[] histo = histogram(bmp, Color.RED);
        int[] minMax = minMax(histo);
        int color, R = 0;

        bmp.getPixels (pixels, 0, width, 0, 0, width, height);

        // Initialisation de la LUT
        int[] LUT = new  int[256];

        for(int ng = 0; ng < 256; ng++){
            LUT[ng] = (255*(ng-minMax[0]))/(minMax[1]-minMax[0]);
        }

        // Calcul de la transformation
        for (int i = 0 ; i < width*height ; i++){
            tmp_color = pixels[i];
            R = Color.red(tmp_color);
            color = LUT[R];
            pixels[i] = Color.rgb(color, color, color);
        }
        bmp.setPixels(pixels, 0, width, 0, 0, width, height);
    }

    //Utilisation d'une Look Up Table pour diminuer le contraste

    public void decreasesContrastLUT(Bitmap bmp){
        width = bmp.getWidth();
        height = bmp.getHeight();
        int[] pixels = new int[width * height];
        int[] histo = histogram(bmp, Color.RED);
        int[] minMax = minMax(histo);
        int dist = minMax[1] - minMax[0];
        int percent = (dist*10)/100;
        minMax[0] = minMax[0] + percent;
        minMax[1] = minMax[1] - percent;
        int color, R;

        bmp.getPixels (pixels, 0, width, 0, 0, width, height);

        // Initialisation de la LUT
        int[] LUT = new  int[256];

        for(int ng = 0; ng < 256; ng++){
            LUT[ng] = ((ng*(minMax[1]-minMax[0]))/255)+minMax[0];
        }

        // Calcul de la transformation
        for (int i = 0 ; i < width*height ; i++){
            tmp_color = pixels[i];
            R = Color.red(tmp_color);
            color = LUT[R];
            pixels[i] = Color.rgb(color, color, color);
        }
        bmp.setPixels(pixels, 0, width, 0, 0, width, height);
    }

    /***************************************************************************************************/
    // augmentation du contraste d'une image en couleur par extension du dynamique.

    public void upContrasteColor(Bitmap bmp){
        width = bmp.getWidth();
        height = bmp.getHeight();
        int[] pixels = new int[width * height];

        int[] histoR = histogram(bmp, Color.RED);
        int[] histoG = histogram(bmp,Color.GREEN);
        int[] histoB = histogram(bmp,Color.BLUE);

        int[] minMaxR = minMax(histoR);
        int[] minMaxG = minMax(histoG);
        int[] minMaxB = minMax(histoB);

        int newR=0;
        int newG=0;
        int newB=0;

        int R, G, B;

        bmp.getPixels (pixels, 0, width, 0, 0, width, height);

        // Initialisation des LUT
        int[] LUTR = new  int[256];
        int[] LUTG = new  int[256];
        int[] LUTB = new  int[256];

        for(int ng = 0; ng < 256; ng++){
            LUTR[ng] = (255*(ng-minMaxR[0]))/(minMaxR[1]-minMaxR[0]);
            LUTG[ng] = (255*(ng-minMaxG[0]))/(minMaxG[1]-minMaxG[0]);
            LUTB[ng] = (255*(ng-minMaxB[0]))/(minMaxB[1]-minMaxB[0]);
        }

        // Calcul de la transformation
        for (int i = 0 ; i < width*height ; i++){
            tmp_color = pixels[i];
            R = Color.red(tmp_color);
            G = Color.green(tmp_color);
            B = Color.red(tmp_color);

            newR = LUTR[R];
            newG = LUTG[G];
            newB = LUTB[B];

            pixels[i] = Color.rgb(newR, newG, newB);
        }
        bmp.setPixels(pixels, 0, width, 0, 0, width, height);
    }

    public void downContrasteColor(Bitmap bmp) {
        width = bmp.getWidth();
        height = bmp.getHeight();
        int[] pixels = new int[width * height];

        int[] histoR = histogram(bmp, Color.RED);
        int[] histoG = histogram(bmp, Color.GREEN);
        int[] histoB = histogram(bmp, Color.BLUE);

        int[] minMaxR = minMax(histoR);
        int[] minMaxG = minMax(histoG);
        int[] minMaxB = minMax(histoB);

        //Calcul de la diminution
        int distR = minMaxR[1] - minMaxR[0];
        int distG = minMaxG[1] - minMaxG[0];
        int distB = minMaxB[1] - minMaxB[0];
        int percentR = (distR * 10) / 100;
        int percentG = (distG * 10) / 100;
        int percentB = (distB * 10) / 100;
        minMaxR[0] = minMaxR[0] + percentR;
        minMaxG[0] = minMaxG[0] + percentG;
        minMaxB[0] = minMaxB[0] + percentB;
        minMaxR[1] = minMaxR[1] - percentR;
        minMaxG[1] = minMaxG[1] - percentG;
        minMaxB[1] = minMaxB[1] - percentB;

        int R, G, B;

        int newR, newG, newB;

        bmp.getPixels(pixels, 0, width, 0, 0, width, height);

        // Initialisation des LUT
        int[] LUTR = new int[256];
        int[] LUTG = new int[256];
        int[] LUTB = new int[256];

        for (int ng = 0; ng < 256; ng++) {
            LUTR[ng] = ((ng * (minMaxR[1] - minMaxR[0])) / 255) + minMaxR[0];
            LUTG[ng] = ((ng * (minMaxG[1] - minMaxG[0])) / 255) + minMaxG[0];
            LUTB[ng] = ((ng * (minMaxB[1] - minMaxB[0])) / 255) + minMaxB[0];

            // Calcul de la transformation
            for (int i = 0; i < width*height; i++) {
                tmp_color = pixels[i];
                R = Color.red(tmp_color);
                G = Color.green(tmp_color);
                B = Color.red(tmp_color);

                newR = LUTR[R];
                newG = LUTG[G];
                newB = LUTB[B];

                pixels[i] = Color.rgb(newR,newG,newB);
            }
            bmp.setPixels(pixels, 0, width, 0, 0, width, height);
        }
    }

    /*# pragma version (1)
    # pragma rs java_package_name (com.example.td1)

    float brightnessScale = 0.5;

    private void RS_KERNEL changeBrightness ( Bitmap bmp ) {
        // 1) Creer un contexte RenderScript
        RenderScript rs = RenderScript . create ( this ) ;
        // 2) Creer des Allocations pour passer les donnees
        Allocation input = Allocation . createFromBitmap ( rs , bmp ) ;
        Allocation output = Allocation . createTyped ( rs , input.getType () ) ;
        // 3) Creer le script
        ScriptC_brightness bScript = new ScriptC_brightness (rs) ;
        // 4) Copier les donnees dans les Allocations
        // ...
        // 5) Initialiser les variables globales potentielles
        bScript.set_brightnessScale (0.8) ;
        // 6) Lancer le noyau
        bScript.forEach_changeBrightness ( input , output ) ;
        // 7) Recuperer les donnees des Allocation (s)
        output.copyTo ( bmp ) ;
        // 8) Detruire le context , les Allocation (s) et le script
        input.destroy () ; output.destroy () ;
        bScript.destroy () ; rs.destroy () ;
    }
    */


}
