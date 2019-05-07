package com.jianzhong.lyag.glide;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.module.GlideModule;

/**
 *
 */
public class GlideConfiguration implements GlideModule {

    @Override
    public void applyOptions(final Context context, GlideBuilder builder) {
        // Apply options to the builder here.
        builder.setDecodeFormat(DecodeFormat.ALWAYS_ARGB_8888);
//        final int cacheSize300MegaBytes = 104857600 * 3;
//        // or any other path
//        builder.setDiskCache(new DiskCache.Factory() {
//            @Override
//            public DiskCache build() {
//                // Careful: the external cache directory doesn't enforce permissions
//                File cacheLocation = new File(AppConstants.DEFAULT_SAVE_IMAGE_PATH);
//                cacheLocation.mkdirs();
//                return DiskLruCacheWrapper.get(cacheLocation, cacheSize300MegaBytes);
//            }
//        });

    }

    @Override
    public void registerComponents(Context context, Glide glide) {
    }

}