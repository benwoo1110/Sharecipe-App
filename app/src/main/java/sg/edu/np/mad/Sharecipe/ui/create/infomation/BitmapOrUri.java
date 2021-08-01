package sg.edu.np.mad.Sharecipe.ui.create.infomation;

import android.graphics.Bitmap;
import android.net.Uri;
import android.widget.ImageView;

public class BitmapOrUri {

    private final Type type;
    private String fileId;
    private Bitmap bitmap;
    private Uri uri;

    public BitmapOrUri(String fileId, Bitmap bitmap) {
        this.fileId = fileId;
        this.bitmap = bitmap;
        this.type = Type.BITMAP;
    }

    public BitmapOrUri(Uri uri) {
        this.uri = uri;
        this.type = Type.URI;
    }

    public void setImageView(ImageView imageView) {
        switch (type) {
            case BITMAP:
                imageView.setImageBitmap(bitmap);
                break;
            case URI:
                imageView.setImageURI(uri);
                break;
        }
    }

    public Type getType() {
        return type;
    }

    public String getFileId() {
        return fileId;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public Uri getUri() {
        return uri;
    }

    public enum Type {
        BITMAP,
        URI;
    }
}
