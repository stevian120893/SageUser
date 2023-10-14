package com.mib.feature_home.interfaces

interface DialogOrderListener {
    fun order(bookingDate: String, bookingTime: String, bookingAddress: String, bookingNote: String)
}
