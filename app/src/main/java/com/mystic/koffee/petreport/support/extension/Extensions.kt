package com.mystic.koffee.petreport.support.extension

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Resources
import android.graphics.Rect
import android.os.Build
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsets
import android.view.inputmethod.InputMethodManager
import androidx.core.content.FileProvider
import androidx.fragment.app.DialogFragment
import java.io.File
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun getDate(): String {
    val date =    LocalDateTime.now().toString()
    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    return date.format(formatter)
}

fun DialogFragment.setWidthPercent(percentage: Int) {
    val percent = percentage.toFloat() / 100
    val dm = Resources.getSystem().displayMetrics
    val rect = dm.run { Rect(0, 0, widthPixels, heightPixels) }
    val percentWidth = rect.width() * percent
    dialog?.window?.setLayout(percentWidth.toInt(), ViewGroup.LayoutParams.WRAP_CONTENT)
}

fun LocalDate.toString(pattern: String): String {
    return this.format(DateTimeFormatter.ofPattern(pattern))
}

fun Context.hideKeyboard(view: View) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        view.windowInsetsController?.hide(WindowInsets.Type.ime())
        view.clearFocus()
    } else {
        val inputMethodManager =
            getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }
}

fun generateFile(context: Context, fileName: String): File? {
    val csvFile = File(context.filesDir, fileName)
    csvFile.createNewFile()
    return csvFile.takeIf { csvFile.exists() }
}

fun setPermissionUri(context: Context, file: File, intent: Intent) {
    val contentUri =
        FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
    val resInfoList = context.getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY)

    resInfoList.forEach { resolveInfo ->
        val packageName = resolveInfo.activityInfo.packageName
        context.grantUriPermission(
            packageName,
            contentUri,
            Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION
        )
    }
}

fun shareFileIntent(context: Context, file: File): Intent {
    val contentUri =
        FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
    val mimeType = context.contentResolver.getType(contentUri)
    val shareIntent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_STREAM, contentUri)
        type = mimeType
        flags = Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
    }
    return Intent.createChooser(shareIntent, null)
}
