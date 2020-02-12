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
import static android.graphics.Color.blue;
import static android.graphics.Color.green;
import static android.graphics.Color.red;
import static android.graphics.Color.rgb;

public class MainActivity extends AppCompatActivity {


    private TextView text,size;
    private Button reset, loading;
    private ImageView img;
    private Bitmap bitmap,bitmapr,bitmap2,bitmap2r;
    private int width;
    private int height;
    private int tmp_color;

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

            bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.imgris,options);
            bitmapr = BitmapFactory.decodeResource(getResources(),R.drawable.leguime,options);
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

            /*
            width = bitmap2.getWidth();
            height = bitmap2.getHeight();

            for(int x = 0; x < width; ++x){
                for (int y = height/2; y < height; y+=(height/2)){
                    bitmap2.setPixel(x,y, Color.BLACK);
                }
            }
            */
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
                Uri photoURI = FileProvider.getUriForFile(this,"com.example.android.td1",photoFile);
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
        String timeStamp = new SimpleDateFormat("filename").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_" ;
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName,/*prefix*/
                ".jpg",/*suffix*/
                storageDir/*directory*/);
        // Enregistrer un fichier: chemin à utiliser avec ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(currentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
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
                //upContrasteColor(bitmap);
                img.setImageBitmap(bitmap);
                Toast.makeText(this,"up contrast selected",Toast.LENGTH_LONG).show();
                return true;
            case R.id.menu_downContrastLut:
                downContrasteColor(bitmapr);
                img.setImageBitmap(bitmapr);
                Toast.makeText(this,"down contrast selected",Toast.LENGTH_LONG).show();
                return true;
            case R.id.menu_to_convolution:
                convolutionMoy(bitmap2);
                img.setImageBitmap(bitmap2);
                Toast.makeText(this,"Convolution Moy selected",Toast.LENGTH_LONG).show();
                return true;
                /*
            case R.id.menu_to_convolutionGaus:
                convolutionGaus(bitmap2);
                Toast.makeText(this,"Convolution Moy selected",Toast.LENGTH_LONG).show();
                img.setImageBitmap(bitmap2);

                 */
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

    /******************************************************Garder une color ******BUtton -> Cannod Color**********/

    public void cannedColor(Bitmap img) {
            int width = img.getWidth();
            int height = img.getHeight();
            float[] hsv = new float[3];
            int[] pixels = new int[width * height];

            img.getPixels(pixels, 0, width, 0, 0, width, height);

            for(int i=0;i<width*height;i++){
                int color=pixels[i];
                int r = Color.red(color);
                int g = Color.green(color);
                int b = Color.blue(color);
                Color.RGBToHSV(r, g, b, hsv);
                if((hsv[0] > 20) && (hsv[0] < 340)){
                    int grv=(int) (r * 0.3 + g * 0.59 +  b * 0.11);
                    pixels[i] = Color.rgb(grv,grv,grv);
                }
            }
            img.setPixels(pixels, 0, width, 0, 0, width, height);


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


    /*
     * TODO:
     *  - add "N" parameter to the method
     *  - let the user choose the size of the filter in a list of predefined sizes (n%2 != 0)
     */
    protected void convolutionMoy(Bitmap bmp /*,int n*/){
        int h = bmp.getHeight(), w = bmp.getWidth();    //Get the size of the bmp
        int pixels[] = new int[h*w];                    //Create a table to contain the pixels of the bmp
        bmp.getPixels(pixels, 0, w, 0, 0, w, h);    //Get the pixels of the bmp in the table
        int red=0, green=0, blue=0;                     //Create args to get the pixel's color values
        int filterSize = 9, halfSize = filterSize/2, filterSlots = filterSize*filterSize; //Size of the filter
        for(int i=halfSize; i<w-halfSize; i++) {                 //For each pixels apply the filter
            for (int j = halfSize; j < h - halfSize; j++) {
                for (int k = i - halfSize; k <= i + halfSize; k++) {
                    for (int l = j - halfSize; l <= j + halfSize; l++) {
                        red += Color.red((pixels[k + l * w]));
                        green += Color.green((pixels[k + l * w]));
                        blue += Color.blue((pixels[k + l * w]));
                    }
                }
                red/=filterSlots; green/=filterSlots; blue/=filterSlots;
                pixels[i + j * w] = Color.rgb(red, green, blue);
                red=0;
                green=0;
                blue=0;
            }
        }
        bmp.setPixels(pixels, 0, w, 0, 0, w, h);    //Set the pixels in the bmp
    }

    /*
     * TODO:
     *  - add "N" parameter to the method
     *  - let the user choose the size of the filter in a list of predefined sizes (n%2 != 0)
     */
    protected void convolutionGaus(Bitmap bmp /*, int n*/){
        int h = bmp.getHeight(), w = bmp.getWidth();        //Get the size of the bmp
        int pixels[] = new int[h*w];                        //Create a table to contain the pixels of the bmp
        bmp.getPixels(pixels, 0, w,0,0, w, h);  //Get the pixels of the bmp in the table
        int red = 0, green = 0, blue = 0;                   //Create args to get the pixel's color values
        //size : n/2 + 1
        int halfSize = 13, filterSize = 2*halfSize+1, filterSlots = filterSize*filterSize;   //Size of the filter
        int filter[] = new int[filterSlots];        //Create a table to contain the filter
        int sumFilter = 0;                          //Sum of values in the filter -> values of pixels to divide by to be in [0;255]
        double ecartType = halfSize/Math.sqrt(-2*Math.log(0.05));   //Need to calculate filter's values
        for(int i=0; i<filterSize; i++){            //Calculate the values of the filter
            for(int j=0; j<filterSize; j++){
                int val = (int) ((Math.exp(-((i*i+j*j)/2*ecartType*ecartType)))*10000);
                filter[i+j*filterSize] = (10001-val);
                sumFilter+=val;
            }
        }

        for(int i=halfSize; i<w-halfSize; i++) {                 //For each pixels apply the filter
            for (int j = halfSize; j < h - halfSize; j++) {
                int slot=0;
                for (int k = i - halfSize; k <= i + halfSize; k++) {
                    for (int l = j - halfSize; l <= j + halfSize; l++) {
                        System.out.println("TEEEEEST    red "+Color.green(pixels[k+l*w]));
                        System.out.println("TEEEEEST    filter "+filter[slot]/sumFilter);
                        //red = Color.red(pixels[k+l*w])*filter[slot]/sumFilter;
                        green = Color.green(pixels[k+l*w])*filter[slot]/sumFilter;
                        blue = Color.blue(pixels[k+l*w])*filter[slot]/sumFilter;
                        pixels[k+l*w] = Color.rgb(red,green,blue);
                        slot++;
                    }
                }
            }
        }
        bmp.setPixels(pixels, 0, w, 0, 0, w, h);    //Set the pixels in the bmp
    }



/**************************************************************************************************************************************/
                 /****************************************************************************************/
                         /***TRANSFORMATIONS D'HISTORGRAMME POUR REGLER LE CONTRASTE****/
                         public int[] redScale(Bitmap im) {
                             int[] hist = new int[256];

                             for (int i = 0; i < im.getWidth() ; i++) {
                                 for (int j = 0; j < im.getHeight(); j++) {
                                     int redLevel = red(im.getPixel(i, j));
                                     hist[redLevel]++;
                                 }
                             }
                             return hist;
                         }

    //calcule l'histoggrame du niveau de bleu d'une image
    public int[] blueScale(Bitmap im) {
        int[] hist = new int[256];
        for (int i = 0; i < im.getWidth() ; i++) {
            for (int j = 0; j < im.getHeight() ; j++) {
                int blueLevel = blue(im.getPixel(i, j));
                hist[blueLevel]++;
            }
        }
        return hist;
    }

    //calcule l'histogramme du niveau de vert d'une image
    public int[] greenScale(Bitmap im) {
        int[] hist = new int[256];
        for (int i = 0; i < im.getWidth() ; i++) {
            for (int j = 0; j < im.getHeight(); j++) {
                int greenLevel = green(im.getPixel(i, j));
                hist[greenLevel]++;
            }
        }
        return hist;
    }
    /*************************diminution du contraste d'une image couleur par égalisation d'histogramme*****Button -> Down Contrast**************/

    public void downContrasteColor(Bitmap im) {
        int[] histR = redScale(im);
        int[] histG = greenScale(im);
        int[] histB = blueScale(im);
        int width = im.getWidth();
        int height = im.getHeight();
        int i;
        int[] histCR = new int[256];
        int[] histCG = new int[256];
        int[] histCB = new int[256];
        int cmptR = 0;
        int cmptG = 0;
        int cmptB = 0;
        for (i = 0; i < 256; i++) {
            cmptR = cmptR + histR[i];
            histCR[i] = cmptR;
            cmptG = cmptG + histG[i];
            histCG[i] = cmptG;
            cmptB = cmptB + histB[i];
            histCB[i] = cmptB;
        }
        int j;
        for (j = 0; j <width; j++) {
            for(int x=0;x<height;x++) {
                double newRed = ((histCR[red(im.getPixel(j,x))]));
                double newGreen = ((histCG[green(im.getPixel(j,x))]));
                double newBlue = ((histCB[blue(im.getPixel(j,x))])) ;
                im.setPixel(j,x,rgb((int)((newRed*255) /cmptR),(int)((newGreen*255) / cmptG),(int)((newBlue*255))/ cmptB));
            }
        }
    }

    //calcule l'histogramme du niveau de gris d'une image
    public int[] greyScale(Bitmap im) {
        int[] hist = new int[256];
        for (int i = 0; i < im.getWidth() - 1; i++) {
            for (int j = 0; j < im.getHeight() - 1; j++) {
                int greyLevel = red(im.getPixel(i, j));
                hist[greyLevel]++;
            }
        }
        return hist;
    }
/*******************************augmentation du contraste d'une image grise par égalité d'histogramme*******Button -> Increases Contrast***********/

public int minArray(int[] array) {
    int i = 0;
    while (array[i] == 0) {
        i++;
    }
    return i;
}

    public int maxArray(int[] array) {
        int i = (array.length) - 1;
        while (array[i] == 0) {
            i--;
        }
        return i;
    }

    public void  increasesContrast(Bitmap im) {

            int[] hist = greyScale(im);
            int width = im.getWidth();
            int height = im.getHeight();
            int i;
            int[] histC = new int[256];
            int cmpt=0;
            for (i = 1; i < 256; i++) {
                cmpt = cmpt + hist[i];
                histC[i] = cmpt;
            }
            int[] pixels = new int[width * height];
            im.getPixels(pixels, 0, width, 0, 0, width, height);
            int j;
            for (j = 0; j < pixels.length; j++) {
                int newGrey =(histC[red(pixels[j])]*255)/cmpt;
                pixels[j] = rgb(newGrey, newGrey, newGrey);
            }
            im.setPixels(pixels, 0, width, 0, 0, width, height);

    }

    /**********augmentation du contraste d'une image couleur par extension dynamique********** Button ->Increases Contrast(LUT)**********/
    public void increasesContrastLUT(Bitmap im) {
        int[] hist1 = redScale(im);
        int[] hist2 = blueScale(im);
        int[] hist3 = greenScale(im);
        int width = im.getWidth();
        int height = im.getHeight();
        int maxr = maxArray(hist1);
        int minr = minArray(hist1);
        int maxg = maxArray(hist2);
        int ming = minArray(hist2);
        int maxb = maxArray(hist3);
        int minb = minArray(hist3);
        int[] LUTr = new int[256];
        int[] LUTg = new int[256];
        int[] LUTb = new int[256];
        for (int n = 0; n < 256; n++) {
            LUTr[n] = (255 * (n - minr)) / (maxr - minr);
            LUTg[n] = (255 * (n - ming)) / (maxg - ming);
            LUTb[n] = (255 * (n - minb)) / (maxb - minb);
        }
        int[] pixels = new int[width * height];
        im.getPixels(pixels, 0, width, 0, 0, width, height);
        int i;
        for (i = 0; i < pixels.length; i++) {
            pixels[i] = rgb(LUTr[red(pixels[i])], LUTg[green(pixels[i])], LUTb[blue(pixels[i])]);
        }
        im.setPixels(pixels, 0, width, 0, 0, width, height);
    }


/********************diminution du contraste d'une image grise*************Button  -> Decreases Contrast(LUT)************/
    public void decreasesContrastLUT(Bitmap im) {
        int[] hist = greyScale(im);
        int width = im.getWidth();
        int height = im.getHeight();
        int[] LUT = new int[256];
        int[] newLUT = new int[256];
        int size=0;
        for(int j=0;j<256;j++){
            if(hist[j]>50){
                LUT[size]=hist[j];
                size++;
            }
        }
        for(int z=0;z<size;z++){
            newLUT[z]=LUT[z];
        }
        int[] pixels = new int[width * height];
        im.getPixels(pixels, 0, width, 0, 0, width, height);
        int i;
        for (i = 0; i < pixels.length; i++) {
            int grey=red(pixels[i]);
            int newGrey=newLUT[grey];
            pixels[i]=rgb(newGrey,newGrey,newGrey);

        }
        im.setPixels(pixels, 0, width, 0, 0, width, height);
      }


}