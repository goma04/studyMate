package goma.tanulotars.extension

import android.content.Context

private fun Context.getImageId(context: Context, imageName: String): Int {
    return context.resources
        .getIdentifier("drawable/$imageName", null, context.packageName)
}