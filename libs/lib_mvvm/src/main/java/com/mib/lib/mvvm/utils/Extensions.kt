package com.mib.lib.mvvm.utils

import android.Manifest
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import pl.aprilapps.easyphotopicker.EasyImage


fun Fragment.openCamera(easyImage: EasyImage) {
    Dexter.withContext(this.activity)
        .withPermission(Manifest.permission.CAMERA).withListener(object : PermissionListener {
            override fun onPermissionGranted(response: PermissionGrantedResponse) {
                easyImage.openCameraForImage(this@openCamera)
            }

            override fun onPermissionDenied(p0: PermissionDeniedResponse?) {
                Toast.makeText(context, "warning permission", Toast.LENGTH_SHORT).show()
            }

            override fun onPermissionRationaleShouldBeShown(permission: PermissionRequest, token: PermissionToken) {
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
