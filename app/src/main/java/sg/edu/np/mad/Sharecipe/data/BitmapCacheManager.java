package sg.edu.np.mad.Sharecipe.data;

import android.graphics.Bitmap;
import android.util.LruCache;

public class BitmapCacheManager {

    private static BitmapCacheManager instance;

    /**
     * Gets common {@link AccountManager} instance throughout the app.
     *
     * @return The instance.
     */
    public static BitmapCacheManager getInstance() {
        if (instance == null) {
            instance = new BitmapCacheManager();
        }
        return instance;
    }

    private final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
    private final int cacheSize = maxMemory / 6;
    private final LruCache<String, Bitmap> bitmapCache;

    private BitmapCacheManager() {
        bitmapCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                return bitmap.getByteCount() / 1024;
            }
        };
    }

    public void addBitmapToMemoryCache(String fileId, Bitmap bitmap) {
        bitmapCache.put(fileId, bitmap);
    }

    public Bitmap getBitmapFromMemCache(String fileId) {
        return bitmapCache.get(fileId);
    }
}
