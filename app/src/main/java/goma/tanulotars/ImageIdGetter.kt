package goma.tanulotars

import android.content.Context

object ImageIdGetter {
    public fun getImageId(context: Context, imageName: String): Int {
        return context.resources
            .getIdentifier("drawable/$imageName", null, context.packageName)
    }
}
