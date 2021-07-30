package sg.edu.np.mad.Sharecipe.ui.create.infomation;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.Point;
import android.graphics.Rect;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.github.dhaval2404.imagepicker.ImagePicker;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import sg.edu.np.mad.Sharecipe.R;

public class ImagesAdapter extends RecyclerView.Adapter<ImagesViewHolder> {

    private static final int LIMIT = 5;

    private final int shortAnimationDuration;
    private Animator currentAnimator;

    private final Activity activity;
    private final ArrayList<Uri> images;
    private final List<File> imageFileList;
    private final ImageView enlarge;
    private final View view;

    public ImagesAdapter(Activity activity, ArrayList<Uri> input, List<File> imageFileList, ImageView enlargedImage, View view) {
        this.activity = activity;
        this.images = input;
        this.imageFileList = imageFileList;
        this.enlarge = enlargedImage;
        this.view = view;
        this.shortAnimationDuration = activity.getResources().getInteger(android.R.integer.config_shortAnimTime);
    }

    @Override
    @NotNull
    public ImagesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_holder_recipe_images, parent, false);
        ImagesViewHolder holder = new ImagesViewHolder(item);

        if (viewType == 0) {
            holder.image.setOnClickListener(v -> ImagePicker.with(activity)
                    .crop()
                    .compress(1024)
                    .maxResultSize(1080, 1080)
                    .start());
            return holder;
        }

        holder.image.setOnClickListener(v -> {
            displayLargeImage(holder.image, holder.imgUri);
        });

        holder.image.setOnLongClickListener(v -> {
            AlertDialog(holder.imgUri);
            return false;
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(@NotNull ImagesViewHolder holder, int position) {
        if (position == 0) {
            return;
        }

        Uri image = images.get(position - 1);
        holder.image.setImageURI(image);
        holder.imgUri = images.get(position - 1);
    }

    @Override
    public int getItemCount() {
        return images.size() + 1; // +1 because of the add button that remains
    }

    @Override
    public int getItemViewType(int position) {
        return position == 0 ? 0 : 1; // if position == 0, set position = 0 else position = 1
    }

    private void displayLargeImage(final ImageView thumbView, Uri imguri) {
        // If there's an animation in progress, cancel it
        // immediately and proceed with this one.
        if (currentAnimator != null) {
            currentAnimator.cancel();
        }
        final ImageView expandedImageView = enlarge;
        expandedImageView.setImageURI(imguri);

        final Rect startBounds = new Rect();
        final Rect finalBounds = new Rect();
        final Point globalOffset = new Point();

        thumbView.getGlobalVisibleRect(startBounds);
        view.getGlobalVisibleRect(finalBounds, globalOffset);
        startBounds.offset(-globalOffset.x, -globalOffset.y);
        finalBounds.offset(-globalOffset.x, -globalOffset.y);

        float startScale;
        if ((float) finalBounds.width() / finalBounds.height() > (float) startBounds.width() / startBounds.height()) {
            startScale = (float) startBounds.height() / finalBounds.height();
            float startWidth = startScale * finalBounds.width();
            float deltaWidth = (startWidth - startBounds.width()) / 2;
            startBounds.left -= deltaWidth;
            startBounds.right += deltaWidth;
        } else {
            startScale = (float) startBounds.width() / finalBounds.width();
            float startHeight = startScale * finalBounds.height();
            float deltaHeight = (startHeight - startBounds.height()) / 2;
            startBounds.top -= deltaHeight;
            startBounds.bottom += deltaHeight;
        }

        // Hide the thumbnail and show the zoomed-in view. When the animation
        // begins, it will position the zoomed-in view in the place of the
        // thumbnail.
        thumbView.setAlpha(0f);
        expandedImageView.setVisibility(View.VISIBLE);

        // Set the pivot point for SCALE_X and SCALE_Y transformations
        // to the top-left corner of the zoomed-in view (the default
        // is the center of the view).
        expandedImageView.setPivotX(0f);
        expandedImageView.setPivotY(0f);

        // Construct and run the parallel animation of the four translation and
        // scale properties (X, Y, SCALE_X, and SCALE_Y).
        AnimatorSet set = new AnimatorSet();
        set.play(ObjectAnimator.ofFloat(expandedImageView, View.X, startBounds.left, finalBounds.left))
                .with(ObjectAnimator.ofFloat(expandedImageView, View.Y, startBounds.top, finalBounds.top))
                .with(ObjectAnimator.ofFloat(expandedImageView, View.SCALE_X, startScale, 1f))
                .with(ObjectAnimator.ofFloat(expandedImageView, View.SCALE_Y, startScale, 1f));
        set.setDuration(shortAnimationDuration);
        set.setInterpolator(new DecelerateInterpolator());
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                currentAnimator = null;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                currentAnimator = null;
            }
        });
        set.start();
        currentAnimator = set;

        // Upon clicking the zoomed-in image, it should zoom back down
        // to the original bounds and show the thumbnail instead of
        // the expanded image.
        final float startScaleFinal = startScale;
        expandedImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentAnimator != null) {
                    currentAnimator.cancel();
                }

                // Animate the four positioning/sizing properties in parallel,
                // back to their original values.
                AnimatorSet set = new AnimatorSet();
                set.play(ObjectAnimator
                        .ofFloat(expandedImageView, View.X, startBounds.left))
                        .with(ObjectAnimator.ofFloat(expandedImageView, View.Y,startBounds.top))
                        .with(ObjectAnimator.ofFloat(expandedImageView, View.SCALE_X, startScaleFinal))
                        .with(ObjectAnimator.ofFloat(expandedImageView, View.SCALE_Y, startScaleFinal));
                set.setDuration(shortAnimationDuration);
                set.setInterpolator(new DecelerateInterpolator());
                set.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        thumbView.setAlpha(1f);
                        expandedImageView.setVisibility(View.GONE);
                        currentAnimator = null;
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                        thumbView.setAlpha(1f);
                        expandedImageView.setVisibility(View.GONE);
                        currentAnimator = null;
                    }
                });
                set.start();
                currentAnimator = set;
            }
        });
    }

    private void AlertDialog(Uri image) {
        View view = LayoutInflater.from(activity).inflate(R.layout.dialog_image, null);
        ImageView targetImage = view.findViewById(R.id.alertImage);
        targetImage.setImageURI(image);

        new AlertDialog.Builder(activity)
                .setView(view)
                .setTitle("Remove image")
                .setMessage("Would you like to remove this image?")
                .setPositiveButton("Remove", (dialog, which) -> {
                    int i = images.indexOf(image);
                    images.remove(i);
                    imageFileList.remove(i);
                    notifyDataSetChanged();

                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}
