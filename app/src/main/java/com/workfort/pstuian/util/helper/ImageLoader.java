package com.workfort.pstuian.util.helper;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;
import androidx.annotation.Nullable;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.workfort.pstuian.R;

public class ImageLoader {
    public static void load(ImageView imageView, int imageResource){
        if(imageView == null) return;

        Glide.with(imageView.getContext())
                .load(imageResource)
                .into(imageView);
    }

    public static void load(ImageView imageView, String imgUrl) {
        if(imageView == null) return;

        RequestOptions requestOptions = RequestOptions
                .diskCacheStrategyOf(DiskCacheStrategy.ALL)
                .placeholderOf(R.drawable.ic_logo)
                .error(R.drawable.ic_logo);

        Glide.with(imageView.getContext())
                .load(imgUrl)
                .apply(requestOptions)
                .into(imageView);
    }
}
