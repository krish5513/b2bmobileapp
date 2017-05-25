package com.rightclickit.b2bsaleon.imageloading;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * @author Sekhar Kuppa
 */
public class ImageLoader {

    private String profile = "";

    String baseAuthStr = "acetv" + ":" + "Ja8ahu4uM$carAfR";
    MemoryCache memoryCache = new MemoryCache();
    FileCache fileCache;
    private Map<ImageView, String> imageViews = Collections
            .synchronizedMap(new WeakHashMap<ImageView, String>());
    ExecutorService executorService;
    Handler handler = new Handler();// handler to display images in UI thread
    ProgressBar mLoader;
    private Activity mContext;

    /**
     * @param context context
     */
    public ImageLoader(Activity context) {
        fileCache = new FileCache(context);
        executorService = Executors.newFixedThreadPool(5);
        this.mContext = context;

    }

    //final int stub_id = R.drawable.no_imag;

    /**
     * @param url          url string
     * @param imageView    imageview reference
     * @param image_loader
     * @param profile
     */
    @SuppressWarnings("deprecation")
    public void DisplayImage(String url, ImageView imageView,
                             ProgressBar image_loader, String profile) {
        this.profile = profile;
        this.mLoader = image_loader;
        imageViews.put(imageView, url);
        Bitmap bitmap = memoryCache.get(url);
        if (bitmap != null) {
            if (mLoader != null) {
                mLoader.setVisibility(View.GONE);
            }

//            BitmapDrawable d = new BitmapDrawable(mContext.getResources(),
//                    bitmap);
//            imageView.setBackgroundDrawable(null);
//            imageView.setBackgroundDrawable(d);
            // imageView.setImageBitmap(bitmap);

            if (profile.equals("profile")) {
//                if (mUtils.isTabletDevice(mContext)) {
//                    BitmapDrawable d = new BitmapDrawable(mContext.getResources(),
//                            bitmap);
//                    ProfileView.mPofilePic.setImageDrawable(d);
//                    imageView.setBackgroundDrawable(null);
//                    imageView.setBackgroundDrawable(d);
//                    imageView.setImageBitmap(bitmap);
//
//                } else {
//                    com.neobric.imagecache.MakeImageCircle roundedImage = new com.neobric.imagecache.MakeImageCircle(bitmap);
//                    ProfileView.mPofilePic.setImageDrawable(roundedImage);
//                }


            } else {
//                BitmapDrawable d = new BitmapDrawable(mContext.getResources(),
//                        bitmap);
//                imageView.setBackgroundDrawable(null);
//                imageView.setBackgroundDrawable(d);
                //              imageView.setImageBitmap(bitmap);
                Drawable d = new BitmapDrawable(bitmap);
                imageView.setBackground(d);
            }
        } else {
            queuePhoto(url, imageView);
            if (mLoader != null) {
                mLoader.setVisibility(View.GONE);
            }
            // imageView.setImageResource(stub_id);
        }
    }

    private void queuePhoto(String url, ImageView imageView) {
        PhotoToLoad p = new PhotoToLoad(url, imageView);
        executorService.submit(new PhotosLoader(p));
    }

    private Bitmap getBitmap(String url) {
        File f = fileCache.getFile(url);

        // from SD cache
        Bitmap b = decodeFile(f);
        if (b != null)
            return b;
        // byte[] data = null;
        // from web
        try {


            Bitmap bitmap = null;
            URL imageUrl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) imageUrl
                    .openConnection();
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setConnectTimeout(30000);
            conn.setReadTimeout(30000);
            conn.setInstanceFollowRedirects(true);
            InputStream is = conn.getInputStream();
            OutputStream os = new FileOutputStream(f);
            Utils.CopyStream(is, os);
            os.close();
            conn.disconnect();
            bitmap = decodeFile(f);
            // data = getBytesFromURL(url);
            // // Log.d("lenght", "" + data.length);
            // byte[] imageAsBytes = Base64.decode(data, Base64.DEFAULT);
            // if (imageAsBytes.length > 0) {
            // // bitmap = ImageHelper.getRoundedBitmap(BitmapFactory
            // // .decodeByteArray(imageAsBytes, 0, imageAsBytes.length),
            // // 10, Color.BLUE);
            // bitmap = BitmapFactory.decodeByteArray(imageAsBytes, 0,
            // imageAsBytes.length);
            // }
            return bitmap;
        } catch (Throwable ex) {
            ex.printStackTrace();
            if (ex instanceof OutOfMemoryError)
                memoryCache.clear();
            return null;
        }
    }

    // decodes image and scales it to reduce memory consumption
    private Bitmap decodeFile(File f) {
        try {
            // decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            o.inTempStorage = new byte[32 * 1024];
            o.inPurgeable = true;
            FileInputStream stream1 = new FileInputStream(f);
            BitmapFactory.decodeStream(stream1, null, o);
            stream1.close();

            // Find the correct scale value. It should be the power of 2.
            final int REQUIRED_SIZE = 70;
            int width_tmp = o.outWidth, height_tmp = o.outHeight;
            int scale = 1;
            // while (true) {
            // if (width_tmp / 2 < REQUIRED_SIZE
            // || height_tmp / 2 < REQUIRED_SIZE)
            // break;
            // width_tmp /= 2;
            // height_tmp /= 2;
            // scale *= 2;
            // }
            if (width_tmp / 2 < REQUIRED_SIZE || height_tmp / 2 < REQUIRED_SIZE) {
                scale = (int) Math.pow(
                        2,
                        (int) Math.ceil(Math.log(REQUIRED_SIZE
                                / (double) Math.max(o.outHeight, o.outWidth))
                                / Math.log(0.5)));
            }

            // decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            FileInputStream stream2 = new FileInputStream(f);
            Bitmap bitmap = BitmapFactory.decodeStream(stream2, null, o2);

//            Bitmap bitmap1 = Bitmap.createBitmap(mUtils.setWidth(700), mUtils.setHeight(364), Bitmap.Config.ARGB_8888);
//
//            Paint p = new Paint();
//            p.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
//            Canvas c = new Canvas(bitmap1);
//            c.drawBitmap(bitmap, 0, 0, null);
//            c.drawRect(0, 0, mUtils.setWidth(700), mUtils.setHeight(364), p);

            stream2.close();
            bitmap = resize(bitmap, 100, 100);
            return bitmap;
        } catch (FileNotFoundException e) {
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static Bitmap resize(Bitmap image, int maxWidth, int maxHeight) {
        if (maxHeight > 0 && maxWidth > 0) {
            int width = image.getWidth();
            int height = image.getHeight();
            float ratioBitmap = (float) width / (float) height;
            float ratioMax = (float) maxWidth / (float) maxHeight;

            int finalWidth = maxWidth;
            int finalHeight = maxHeight;
            if (ratioMax > 1) {
                finalWidth = (int) ((float) maxHeight * ratioBitmap);
            } else {
                finalHeight = (int) ((float) maxWidth / ratioBitmap);
            }
            image = Bitmap.createScaledBitmap(image, finalWidth, finalHeight, true);
            System.out.println("NEW bitmap.WIDTH========= = " + image.getWidth() + "NEW bitmap HEIGHT =============== " + image.getHeight());

            return image;
        } else {
            return image;
        }
    }

    // Task for the queue
    private class PhotoToLoad {
        public String url;
        public ImageView imageView;

        public PhotoToLoad(String u, ImageView i) {
            url = u;
            imageView = i;
        }
    }

    class PhotosLoader implements Runnable {
        PhotoToLoad photoToLoad;

        PhotosLoader(PhotoToLoad photoToLoad) {
            this.photoToLoad = photoToLoad;
        }

        @Override
        public void run() {
            try {
                if (imageViewReused(photoToLoad))
                    return;
                Bitmap bmp = getBitmap(photoToLoad.url);
                memoryCache.put(photoToLoad.url, bmp);
                if (imageViewReused(photoToLoad))
                    return;
                BitmapDisplayer bd = new BitmapDisplayer(bmp, photoToLoad);
                handler.post(bd);
            } catch (Throwable th) {
                th.printStackTrace();
            }
        }
    }

    boolean imageViewReused(PhotoToLoad photoToLoad) {
        String tag = imageViews.get(photoToLoad.imageView);
        if (tag == null || !tag.equals(photoToLoad.url))
            return true;
        return false;
    }

    // Used to display bitmap in the UI thread
    class BitmapDisplayer implements Runnable {
        Bitmap bitmap;
        PhotoToLoad photoToLoad;

        public BitmapDisplayer(Bitmap b, PhotoToLoad p) {
            bitmap = b;
            photoToLoad = p;
        }

        @SuppressWarnings("deprecation")
        @Override
        public void run() {
            if (imageViewReused(photoToLoad))
                return;
            if (bitmap != null) {
                if (profile.equals("profile")) {
//                    if (mUtils.isTabletDevice(mContext)) {
//                        BitmapDrawable d = new BitmapDrawable(mContext.getResources(),
//                                bitmap);
//                        ProfileView.mPofilePic.setImageDrawable(d);
//                        photoToLoad.imageView.setBackgroundDrawable(null);
//                        photoToLoad.imageView.setBackgroundDrawable(d);
//                        photoToLoad.imageView.setImageBitmap(bitmap);
//
//                    }else {
//                        com.neobric.imagecache.MakeImageCircle roundedImage = new com.neobric.imagecache.MakeImageCircle(bitmap);
//                        ProfileView.mPofilePic.setImageDrawable(roundedImage);
//                    }
                } else {
//                    BitmapDrawable d = new BitmapDrawable(mContext.getResources(),
//                            bitmap);
//                    photoToLoad.imageView.setBackgroundDrawable(null);
//                    photoToLoad.imageView.setBackgroundDrawable(d);
                    //  photoToLoad.imageView.setImageBitmap(bitmap);
                    Drawable d = new BitmapDrawable(bitmap);
                    photoToLoad.imageView.setBackground(d);
                }
                // photoToLoad.imageView.setImageBitmap(bitmap);
                if (mLoader != null) {
                    mLoader.setVisibility(View.GONE);
                }
            } else {
                if (mLoader != null) {
                    mLoader.setVisibility(View.GONE);
                }
                // photoToLoad.imageView.setImageResource(stub_id);
            }
        }
    }

    /**
     * we can clear cache
     */
    // public void clearCache() {
    // memoryCache.clear();
    // fileCache.clear();
    // }

    /**
     * @param url Image url
     * @return image byte array
     * @throws IOException IOException
     */
    public static byte[] getBytesFromURL(String url) throws IOException {
        URL u = new URL(url);
        InputStream in = null;
        ByteArrayOutputStream out = null;
        try {
            in = u.openStream();
            out = new ByteArrayOutputStream();
            int len;
            byte[] buf = new byte[1024 * 4];
            while ((len = in.read(buf)) >= 0) {
                out.write(buf, 0, len);
            }
            byte[] bytes = out.toByteArray();
            return bytes;

        } catch (IOException e) {
            throw e;
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                }
            }
        }
    }

}
