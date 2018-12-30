package fr.paul_ficot.android_javamail_api_with_attachment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Classe de sélection du choix de la source de l'image
 * ainsi que de détermination du chemin vers cette image
 *
 * @author Paul FICOT
 * @version 1.1
 */

class MediaUtils {

    private Activity mActivity;
    private GetImg mGetImg;
    private final int REQ_CAMERA = 101;
    private final int REQ_GALLERY = 102;
    private final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 103;

    private Uri imageUri;

    private final String TAG = MediaUtils.class.getSimpleName();


    public MediaUtils(Activity activity) {
        mActivity = activity;
        mGetImg = (GetImg) activity;
    }


    public void openImageDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mActivity);
        alertDialogBuilder.setTitle(R.string.select_source).setItems(R.array.attachment_choice, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    // camera
                    if (Build.VERSION.SDK_INT > 23) {
                        // check Permission
                        checkPermission(REQ_CAMERA);
                    } else {
                        openCamera();
                    }

                } else {
                    // gallery
                    if (Build.VERSION.SDK_INT > 23) {
                        // check Permission
                        checkPermission(REQ_GALLERY);
                    } else {
                        openGallery();
                    }
                }
            }
        });
        alertDialogBuilder.create().show();
    }


    @SuppressLint("IntentReset")
    private void openGallery() {
        Intent intent2 = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent2.setType("image/*");
        mActivity.startActivityForResult(intent2, REQ_GALLERY);
    }

    private void openCamera() {
        imageUri = mActivity.getContentResolver().insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new ContentValues());
        Intent camIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        camIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            mActivity.startActivityForResult(camIntent, REQ_CAMERA);
    }

    private void checkPermission(int reqSource) {
        if (ContextCompat.checkSelfPermission(mActivity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            ActivityCompat.requestPermissions(mActivity,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    reqSource);

        } else {
            // Permission has already been granted
            if (reqSource == REQ_CAMERA) {
                openCamera();
            } else {
                openGallery();
            }
        }
    }

    void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            switch (requestCode) {
                case REQ_CAMERA:
                    openCamera();
                    break;
                case REQ_GALLERY:
                    openGallery();
                    break;
            }
        } else {
            Toast.makeText(mActivity, mActivity.getString(R.string.permission_denied), Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * Actions à effectuer en fonction de l'image selectionnée
     *
     * @param requestCode Code de vérification de la requête
     * @param resultCode Code de vérification du résultat
     * @param data Intent
     */
    void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQ_CAMERA) {
                //camera
//                String imgPath = getPath(mActivity, imageUri);
               String imgPath = getFileFromBitmap(mActivity,rotateImageIfNeed(mActivity, imageUri));
                mGetImg.imgdata(imgPath);
            } else {
                //gallery
                imageUri = data.getData();
                String imgPath = getPath(mActivity, imageUri);
                mGetImg.imgdata(imgPath);
            }
        }
    }

    /**
     * Fonction va permettre d'obtenir le chemin vers l'image sélectionnée dans la galerie
     *
     * @param context Activité actuelle
     * @param uri uri de l'image
     * @return le chemin vers l'image
     */
    private String getPath(Context context, Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);
        int column_index = 0;
        if (cursor != null) {
            column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            String path = cursor.getString(column_index);
            cursor.close();
            return path;
        } else
            return uri.getPath();
    }

    /**
     * Fonction qui va faire pivoter l'image dans le bon sens
     *
     * @param context Activité actuelle
     * @param uri uri de l'image
     * @return chemin vers l'image
     */
    private Bitmap rotateImageIfNeed(Context context, Uri uri) {

//        String filePath = getRealPathFromURI(context, uri);
        String filePath = getPath(context, uri);

        /*BitmapFactory.Options options = new BitmapFactory.Options();

        // by setting this field as true, the actual bitmap pixels are not loaded in the memory. Just the bounds are loaded. If
        // you try the use the bitmap here, you will get null.

        options.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(filePath, options);*/


        Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), imageUri);
        } catch (IOException e) {
            e.printStackTrace();
        }


        // check the rotation of the image and display it properly

        ExifInterface exif;
        try {
            exif = new ExifInterface(filePath);

            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION, 0);
            Log.d("EXIF", "Exif: " + orientation);
            Matrix matrix = new Matrix();
            if (orientation == 6) {
                matrix.postRotate(90);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 3) {
                matrix.postRotate(180);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 8) {
                matrix.postRotate(270);
                Log.d("EXIF", "Exif: " + orientation);
            }
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix,
                    true);

            return bitmap;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Fonction qui va créer un fichier Bitmap grâce à la photo prise
     *
     * @param context Activité actuelle
     * @param bitmap fichier bitmap pris en photo
     * @return chemin vers la photo
     */
    private String getFileFromBitmap(Context context, Bitmap bitmap) {
        String folderPath =  Environment.getExternalStorageDirectory() + File.separator + context.getString(R.string.app_name) + File.separator;
        File file = new File(folderPath);

        if(!file.exists()){
            file.mkdir();
        }

        File imageFile =
                new File(folderPath,
                        System.currentTimeMillis() + ".jpg");

        OutputStream os;
        try {
            os = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
            os.flush();
            os.close();
        } catch (Exception e) {
            Log.e(TAG, "Bitmap writing error", e);
        }
        return imageFile.getPath();
    }

    /**
     * Interface qui va permettre de sortir le chemin vers l'image hors de la classe
     */
    interface GetImg {
        void imgdata(String imgPath);
    }

}
