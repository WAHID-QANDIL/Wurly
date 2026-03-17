package com.wahid.wurly.presentation.common

import android.content.res.Resources
import android.graphics.BitmapFactory
import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalResources

/**
 * Global in-memory cache for decoded background bitmaps.
 *
 * Because every screen shares the same 1-2 background images, decoding
 * them once and caching the [ImageBitmap] avoids the expensive JPEG
 * decode that was happening on every navigation (~70-100 MB allocation
 * per decode on the original 6016×4000 / 4000×6000 images).
 */
private object BackgroundBitmapCache {
    private val cache = mutableMapOf<Int, ImageBitmap>()

    fun get(resources: Resources, @DrawableRes resId: Int): ImageBitmap {
        return cache.getOrPut(resId) {
            val options = BitmapFactory.Options().apply {
                // Decode at reduced density if still too large
                inSampleSize = 1
            }
            BitmapFactory.decodeResource(resources, resId, options).asImageBitmap()
        }
    }
}

/**
 * Returns a [BitmapPainter] backed by a globally cached [ImageBitmap].
 *
 * Use this instead of `painterResource(resId)` for large background
 * images that are shared across multiple screens.  The bitmap is decoded
 * **once** and kept alive for the process lifetime, so navigating between
 * screens no longer triggers a costly re-decode.
 */
@Composable
fun rememberCachedBackgroundPainter(@DrawableRes resId: Int): BitmapPainter {
    val resources = LocalResources.current
    return remember(resId) {
        BitmapPainter(BackgroundBitmapCache.get(resources, resId))
    }
}