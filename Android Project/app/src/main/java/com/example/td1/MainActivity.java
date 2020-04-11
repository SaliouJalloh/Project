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
import android.widget.RelativeLayout;
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
import androidx.renderscript.Allocation;
import androidx.renderscript.RenderScript;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static androidx.core.view.accessibility.AccessibilityEventCompat.setAction;
import static com.google.android.material.snackbar.Snackbar.LENGTH_LONG;
import static com.google.android.material.snackbar.Snackbar.make;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private ImageView img;
    private Bitmap bitmap, origin_bitmap;
    private  String photoPath = null;

    // Constantes
    private static final int REQUEST_TAKE_PHOTO = 100;
    private static final int REQUEST_IMAGE_LOAD = 1;

    Uri photoUri;

    private DrawerLayout drawer;
    private SeekBar seekBar;
    private RelativeLayout laySmg;

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

        seekBar = (SeekBar) findViewById(R.id.id_seekBar);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                //progressBar.setProgress(progress);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                Toast.makeText(MainActivity.this,"SeekBar selected",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Toast.makeText(MainActivity.this," Stop SeekBar selected",Toast.LENGTH_SHORT).show();
            }
        });

        initActivity();
    }

    private void initActivity() {
        // instanciation
        img = (ImageView) findViewById(R.id.idimage);
        laySmg = (RelativeLayout)findViewById(R.id.laymsg);

        // Convertion de l'image
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inMutable = true;

        //initialisation des bitmap
        bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.fruits_exotiques, options);
        origin_bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.fruits_exotiques, options);

    }

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
                startSave();
               // MediaStore.Images.Media.insertImage(getContentResolver(),bitmap,"image saved","description");
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

    private void openGallery() {
        //Verifie si la permission est activée
        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED ){

            Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(galleryIntent,REQUEST_IMAGE_LOAD);
        }else {
            //demande une fois de donner la permmission
            if (!ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,Manifest.permission.WRITE_EXTERNAL_STORAGE) &&
                    !ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,Manifest.permission.READ_EXTERNAL_STORAGE)){

                String [] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE};
                //affiche la demande de permission
                ActivityCompat.requestPermissions(MainActivity.this,permissions,2);
                //créer l'intent de la gallery
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent,REQUEST_IMAGE_LOAD);
            }else {
                //affiche un message precisant que la permission est oblogatoire
                messagePermissionRequired();
            }
        }

    }
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
                photoUri = FileProvider.getUriForFile(MainActivity.this, MainActivity.this.getApplicationContext().getPackageName()+ ".provider",photoFile);

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
     * Methode qui permet de prendre une photo
     * depuis la ou les camera(s) du telephone
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
                openCamera();
            }else {
                //affiche un message precisant que la permission est oblogatoire
                messagePermissionRequired();
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
            origin_bitmap = BitmapFactory.decodeFile(imgPath);

            // redimenssioner l'image
            bitmap = changeSizeBitmap(bitmap,0.95f);
            origin_bitmap = changeSizeBitmap(origin_bitmap,0.95f);

            //affichage
            img.setImageBitmap(bitmap);
        }
        else { //Camera
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

    }

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

    private void refreshGallery(File file) {
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        intent.setData(Uri.fromFile(file));
        sendBroadcast(intent);
    }
    private File getDisc(){
        File file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
        return new File(file, "Image Demo");
    }

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
            case R.id.menu_reset:
                Toast.makeText(this,"Reset menu selected",Toast.LENGTH_LONG).show();
                img.setImageBitmap(origin_bitmap);
                return true;
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