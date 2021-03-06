package com.example.td1;

import android.Manifest;
import android.app.WallpaperManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.google.android.material.snackbar.Snackbar.LENGTH_LONG;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private ImageView img;
    private Bitmap bitmap, origin_bitmap;

    // Constantes
    private static final int REQUEST_TAKE_PHOTO = 100;
    private static final int REQUEST_IMAGE_LOAD = 1;

    private DrawerLayout drawer;
    private SeekBar seekBar;
    private LinearLayout laySmg;
    //instansce de classe
    private Gray gray;
    private Colorize colorize;
    private Colorize canned;
    private Contrast constrast;
    private Convolution convolution;
    private String photoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawer,toolbar,
                R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        initActivity();

    }

    private void initActivity() {
        // instanciation
        img = (ImageView) findViewById(R.id.idimage);
        laySmg = (LinearLayout)findViewById(R.id.laymsg);

        // Conversion de l'image
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inMutable = true;

        //initialisation des bitmap
        bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.fruits_exotiques, options);
        origin_bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.fruits_exotiques, options);
        //init constructor
        gray = new Gray(bitmap,this);
        colorize = new Colorize(bitmap, this);
        canned = new Colorize(bitmap,this);
        constrast = new Contrast(bitmap,this);
        convolution = new Convolution(bitmap,this);

    }

    /**
     * @author Saliou Diallo
     * @param item
     * @return
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.nav_message:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new MessageFragment()).commit();
                break;
            case R.id.nav_chat:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new ChatFragment()).commit();
                break;
            case R.id.nav_profile:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new ProfileFragment()).commit();
                break;
            case R.id.nav_camera:
                takePicture();
                Toast.makeText(this,"Camera",Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_gallery:
                openGallery();
                Toast.makeText(this,"Gallery",Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_save:
                saveImage(bitmap);
                Toast.makeText(this,"Saved",Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_send:
                Toast.makeText(this,"Send",Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_share:
                shareImage();
                Toast.makeText(this,"Share",Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_wallpaper:
                setImgWallpaper();
                Toast.makeText(this,"Share",Toast.LENGTH_SHORT).show();
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + item.getItemId());
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * @author Saliou Diallo!-->
     * ouvre la gallery sans demander de permission
     */
    private void gallery(){
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent,REQUEST_IMAGE_LOAD);
    }
    /**
     * @author Saliou Diallo
     * Method of access in the gallery phone
     */
    private void openGallery() {
        //Verifie si la permission est activée
        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_GRANTED ){
            gallery();

        }else {
            //demande une fois de donner la permmission
            if (!ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,Manifest.permission.WRITE_EXTERNAL_STORAGE) &&
                    !ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,Manifest.permission.READ_EXTERNAL_STORAGE)){

                String [] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE};
                //affiche la demande de permission
                ActivityCompat.requestPermissions(MainActivity.this,permissions,2);
                gallery();
            }else {
                //affiche un message precisant que la permission est oblogatoire
                messagePermissionRequired();
            }
        }
    }

    /**
     * @author Saliou Diallo
     * this method affiche un message precisant que la permission est oblogatoire
     */
    private void messagePermissionRequired() {
        Snackbar.make(laySmg, "Permission Required", LENGTH_LONG).setAction("Parameter", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                final Uri uri = Uri.fromParts("package",MainActivity.this.getPackageName(),null);
                intent.setData(uri);
                startActivity(intent);

            }
        }).show();

    }

    /**
     * @author Saliou Diallo
     * ouvre la camera sans demander de permission
     */
    private void openCamera(){
        // creer un intent pour ouvrir une fenetre pour prendre une photo
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // test pour
        if(intent.resolveActivity(getPackageManager()) != null){
            // creer un noveau fichier
            String time = new SimpleDateFormat("yyyyMMMdd_HHmmss").format(new Date());
            File photoDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            try {
                File photoFile = File.createTempFile("photo"+time,".jpg",photoDir);
                // Enregistre le chemin complet
                photoPath = photoFile.getAbsolutePath();
                // creer l'Uri
                Uri photoUri = FileProvider.getUriForFile(MainActivity.this, MainActivity.this.getApplicationContext().getPackageName()+ ".fileprovider",photoFile);

                intent.putExtra(MediaStore.EXTRA_OUTPUT,photoUri);
                startActivityForResult(intent,REQUEST_TAKE_PHOTO);

            }catch (IOException e){
                e.printStackTrace();
            }
        }else {
            Toast.makeText(this,"Problem opening camera",Toast.LENGTH_LONG).show();
        }
    }
    /**
     * @author Saliou Diallo
     * Methode qui permet de prendre une photo
     * depuis la ou les camera(s) du telephone avec les permissions necessaires
     */
    private void takePicture(){
        //Verifie si la permission est activée
        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED  ){
            openCamera();
        }else {
            //demande une fois de donner la permmission
            if (!ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,Manifest.permission.CAMERA)){
                String [] permissions = {Manifest.permission.CAMERA};
                //affiche la demande de permission
                ActivityCompat.requestPermissions(MainActivity.this,permissions,2);
                //openCamera();
            }else {
                //affiche un message precisant que la permission est oblogatoire
                messagePermissionRequired();
            }
        }
    }

    /**
     * @author Saliou Diallo
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

            Cursor cursor = this.getContentResolver().query(selectImage,filePathColumn,
                    null,null,null);

            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String imgPath = cursor.getString(columnIndex);
            cursor.close();

            bitmap = BitmapFactory.decodeFile(imgPath);
            origin_bitmap = BitmapFactory.decodeFile(imgPath);

            // redimenssioner l'image
            bitmap = changeSizeBitmap(bitmap,0.95f);
            origin_bitmap = changeSizeBitmap(origin_bitmap,0.95f);

            //affichage
            img.setImageBitmap(bitmap);
        }
        //Camera
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK ){
            //recupere l'image
            bitmap = BitmapFactory.decodeFile(photoPath);
            origin_bitmap = BitmapFactory.decodeFile(photoPath);

            // redimenssioner l'image
            bitmap = changeSizeBitmap(bitmap,0.95f);
            origin_bitmap = changeSizeBitmap(origin_bitmap,0.95f);

            //affiche l'image
            img.setImageBitmap(bitmap);
        }

    }


    /**
     * @author Saliou Diallo
     * Methode pour la demande des permissions necessaires pour l'ouverture de la camera et gallery
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 0) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED && grantResults[2] ==PackageManager.PERMISSION_GRANTED) {
                takePicture();
                openGallery();
            }
        }

    }

    /**
     * @author Saliou Diallo
     * Methode pour la sauvegarde de l'image dans la gallery du telephone
     * @param finalBitmap
     */
    private void saveImage(Bitmap finalBitmap) {
        String root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString();
        File myDir = new File(root + "/Image_Studio");
        myDir.mkdirs();

        String fileName = "Image-" + System.currentTimeMillis() + ".jpg";
        File file = new File(myDir, fileName);
        if (file.exists()) file.delete();
        try {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Tell the media scanner about the new file so that it is immediately available to the user.
        MediaScannerConnection.scanFile(this, new String[]{file.toString()}, null, null);
    }

    /**
     * @author Saliou Diallo
     *  Methode pour la sauvegarde de l'image dans la gallery du telephone
     */

    private void startSave(){
        FileOutputStream fileOutputStream = null;
        File file = getDisc();
        if(!file.exists() && !file.mkdirs()){
            Toast.makeText(this,"can't create directory to save image",Toast.LENGTH_SHORT).show();
            return;
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyymmsshhmmss");
        String data = simpleDateFormat.format(new Date());
        String name = "Img"+data+".jpeg";
        String file_name = file.getAbsolutePath()+"/"+name;
        File new_file = new File(file_name);
        try {
            fileOutputStream = new FileOutputStream(new_file);
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,fileOutputStream);
            Toast.makeText(this,"Save image success",Toast.LENGTH_SHORT).show();
            fileOutputStream.flush();
            fileOutputStream.close();
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }
        refreshGallery(new_file);
    }

    /**
     * @author Saliou Diallo
     * Methode pour actualiser la gallery
     * @param file
     */
    private void refreshGallery(File file) {
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        intent.setData(Uri.fromFile(file));
        sendBroadcast(intent);
    }
    private File getDisc(){
        File file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
        return new File(file, "Image Demo");
    }

    /**
     * @author Saliou Diallo
     * Methode de partage d'image reseau social et e-mail
     */

    private void shareImage() {
        try {
            Drawable drawable = img.getDrawable();
            Bitmap bitmap = ((BitmapDrawable)drawable).getBitmap();
            File file = new File(getExternalCacheDir(), "sample.png");
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG,100,fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
            file.setReadable(true,false);
            //sharing intent image
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra(Intent.EXTRA_STREAM,Uri.fromFile(file));
            intent.setType("image/png");
            startActivity(Intent.createChooser(intent,"Share Image via"));

        }catch (FileNotFoundException e){
            e.printStackTrace();
            Toast.makeText(this,"File not found",Toast.LENGTH_SHORT).show();
        }
        catch (IOException e){
            e.printStackTrace();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * @author Saliou Diallo
     * Methode pour definir l'image comme fond d'ecran du telephone
     */
    private void setImgWallpaper(){
        WallpaperManager wallpaperManager = WallpaperManager.getInstance(getApplicationContext());
        try {
            wallpaperManager.setBitmap(bitmap);
            Toast.makeText(this,"Set Wallpaper ...",Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            Toast.makeText(this,e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }else {
            super.onBackPressed();
        }
    }


    /**
     * @author Saliou Diallo
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
     * @author Saliou Diallo
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
     * @author Saliou Diallo
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
     * @author Saliou Diallo
     * pour la creation du menu et l'appel des differentes methodes implementées
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_reset:
                Toast.makeText(this,"Reset menu selected",Toast.LENGTH_LONG).show();
                img.setImageBitmap(origin_bitmap);
                return true;
            case R.id.menu_to_grayRS:
                Toast.makeText(this,"to graysRS selected",Toast.LENGTH_LONG).show();
                gray.toGrayRS(bitmap);
                img.setImageBitmap(bitmap);
                return true;
            case R.id.menu_colorize:
                Toast.makeText(this,"colorize menu selected",Toast.LENGTH_LONG).show();
                return true;
            case R.id.menu_to_colorized:
                colorize.colorized(bitmap);
                img.setImageBitmap(bitmap);
                Toast.makeText(this,"to colorized selected",Toast.LENGTH_LONG).show();
                return true;
            case R.id.menu_to_colorize:
                float[] hsv = new float[3];
                colorize.colorize(bitmap,hsv[0]);
                Toast.makeText(this,"to colorize selected",Toast.LENGTH_LONG).show();
                img.setImageBitmap(bitmap);
                return true;
            case R.id.menu_to_colorizeRS:
                hsv = new float[3];
                colorize.colorizeRS(bitmap,hsv[0]);
                Toast.makeText(this,"to colorize RS selected",Toast.LENGTH_LONG).show();
                img.setImageBitmap(bitmap);
                return true;
            case R.id.menu_canned_color:
                colorize.cannedColor(bitmap);
                img.setImageBitmap(bitmap);
                Toast.makeText(this,"canned color selected",Toast.LENGTH_LONG).show();
                return true;
            case R.id.menu_canned_colorRS:
                canned.cannedColorRS(bitmap,90);
                img.setImageBitmap(bitmap);
                Toast.makeText(this,"canned color RS selected",Toast.LENGTH_LONG).show();
                return true;
            case R.id.menu_contrast:
                Toast.makeText(this,"contrast menu selected",Toast.LENGTH_LONG).show();
                return true;
            case R.id.menu_increasesContrast:
                constrast.increasesContrast(bitmap);
                img.setImageBitmap(bitmap);
                Toast.makeText(this,"increases selected",Toast.LENGTH_LONG).show();
                return true;
            case R.id.menu_increasesContrastLut:
                Toast.makeText(this,"increases lut selected",Toast.LENGTH_LONG).show();
                constrast.increasesContrastLUT(bitmap);
                img.setImageBitmap(bitmap);
                return true;
            case R.id.menu_decreasesContrastLut:
                constrast.decreasesContrastLUT(bitmap);
                img.setImageBitmap(bitmap);
                Toast.makeText(this,"decreases selected",Toast.LENGTH_LONG).show();
                return true;
            case R.id.menu_upContrastLut:
                constrast.UpContrasteColor(bitmap);
                img.setImageBitmap(bitmap);
                Toast.makeText(this,"up contrast selected",Toast.LENGTH_LONG).show();
                return true;
            case R.id.menu_downContrastLut:
                constrast.downContrasteColor(bitmap);
                img.setImageBitmap(bitmap);
                Toast.makeText(this,"down contrast selected",Toast.LENGTH_LONG).show();
                return true;
            case R.id.menu_EqualHistogram:
                img.setImageBitmap(constrast.histogram_equalize(bitmap));
                Toast.makeText(this,"equal histogramme selected",Toast.LENGTH_LONG).show();
                return true;
            case R.id.menu_to_convolution:
                Toast.makeText(this,"Convolution Moy selected",Toast.LENGTH_LONG).show();
                convolution.convolutionMoy(bitmap,13);
                img.setImageBitmap(bitmap);
                return true;
            case R.id.menu_to_convolutionGaus:
                Toast.makeText(this,"Convolution Gauss selected",Toast.LENGTH_LONG).show();
                convolution.convolutionGaussN(bitmap,9);
                img.setImageBitmap(bitmap);
                return true;
            case R.id.menu_to_convolutionSobel:
                Toast.makeText(this,"Contour selected",Toast.LENGTH_LONG).show();
                convolution.convolutionSobel(bitmap);
                img.setImageBitmap(bitmap);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}