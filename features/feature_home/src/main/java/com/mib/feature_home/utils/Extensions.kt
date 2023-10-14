package com.mib.feature_home.utils

import android.Manifest
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.karumi.dexter.listener.single.PermissionListener
import com.mib.feature_home.R
import com.mib.feature_home.utils.AppUtils.Companion.convertDate
import com.mib.feature_home.utils.utils_interface.DatePickerListener
import com.mib.feature_home.utils.utils_interface.TimeDialogListener
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import pl.aprilapps.easyphotopicker.EasyImage

fun Fragment.createEasyImage(context: Context): EasyImage {
    return EasyImage.Builder(context)
        .setCopyImagesToPublicGalleryFolder(false)
        .setFolderName(getString(R.string.app_name))
        .allowMultiple(false)
        .build()
}

fun Fragment.openCamera(easyImage: EasyImage) {
    Dexter.withContext(this.activity)
        .withPermissions(
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ).withListener(object : MultiplePermissionsListener {
            override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                if (report.areAllPermissionsGranted()) {
                    easyImage.openCameraForImage(this@openCamera)
                } else {
                    Toast.makeText(context, "warning permission", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onPermissionRationaleShouldBeShown(permissions: List<PermissionRequest>, token: PermissionToken) {
                token.continuePermissionRequest()
            }
        }).check()
}

fun Fragment.openGallery(easyImage: EasyImage) {
    Dexter.withContext(this.activity)
        .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        .withListener(object : PermissionListener {
            override fun onPermissionGranted(response: PermissionGrantedResponse) {
                easyImage.openGallery(this@openGallery)
            }

            override fun onPermissionDenied(response: PermissionDeniedResponse) {
                Toast.makeText(context, "warning permission", Toast.LENGTH_SHORT).show()
            }

            override fun onPermissionRationaleShouldBeShown(permission: PermissionRequest, token: PermissionToken) {
                token.continuePermissionRequest()
            }
        }).check()
}

fun EditText.openTimePicker(context: Context, timeDialogListener: TimeDialogListener) {
    val cal = Calendar.getInstance()
    val mTimePicker = TimePickerDialog(context, R.style.DialogTheme, { _, selectedHour, selectedMinute ->
            timeDialogListener.onFinishSelectTime(
                convertDate(selectedHour) + ":" + convertDate(
                    selectedMinute
                )
            )
        },
        cal[Calendar.HOUR_OF_DAY],
        cal[Calendar.MINUTE],
        true
    ) //Yes 24 hour time
    mTimePicker.show()
}

fun EditText.openDatePicker(context: Context, datePickerListener: DatePickerListener) {
    val c: Calendar = Calendar.getInstance()
    val mYear: Int = c.get(Calendar.YEAR) // current year
    val mMonth: Int = c.get(Calendar.MONTH) // current month
    val mDay: Int = c.get(Calendar.DAY_OF_MONTH) // current day

    // date picker dialog
    val datePickerDialog = DatePickerDialog(context, R.style.DialogTheme,
        { _, year, monthOfYear, dayOfMonth -> // set day of month , month and year value in the edit text
            datePickerListener.onFinishSelectDate(
                "${convertDate(dayOfMonth)}-${convertDate((monthOfYear + 1))}-$year")
        }, mYear, mMonth, mDay
    )
    datePickerDialog.show()
}

fun Double.withThousandSeparator(): String {
    return withThousandSeparator(0)
}

fun Double.withThousandSeparator(fraction: Int, format: String = "#,###.##"): String
{
    val decimalFormatSymbols = DecimalFormatSymbols(Locale.getDefault())
    decimalFormatSymbols.groupingSeparator = '.'
    decimalFormatSymbols.decimalSeparator = ','
    val df = DecimalFormat(format, decimalFormatSymbols)
    if (fraction > 0)
    {
        df.isDecimalSeparatorAlwaysShown = true
        df.minimumFractionDigits = fraction
    }
    else
    {
        df.isDecimalSeparatorAlwaysShown = false
    }
    return df.format(this)
}

fun String.withThousandSeparator(): String
{
    val double = this.toDoubleOrNull() ?: 0.0
    return double.withThousandSeparator(0)
}

fun String.removeThousandSeparator(): String
{
    return this.replace(".", "")
}

fun String.stringToDate(): Date?
{
    var date: Date? = null

    val dateFormat = "yyyy-MM-dd"
    val sdf = SimpleDateFormat(dateFormat)
    date = sdf.parse(this)

    return date
}

fun String.stringToDate(dateFormat: String): Date?
{
    var date: Date? = null

    val sdf = SimpleDateFormat(dateFormat)
    date = sdf.parse(this)

    return date
}

fun Date.dateToString(): String
{
    val dateFormat = "dd MMMM yyyy"
    val sdf = SimpleDateFormat(dateFormat)

    return sdf.format(this)
}

fun Date.dateToString(dateFormat: String): String
{
    val sdf = SimpleDateFormat(dateFormat)

    return sdf.format(this)
}