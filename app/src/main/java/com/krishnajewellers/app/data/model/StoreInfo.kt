package com.krishnajewellers.app.data.model

data class StoreInfo(
    val name: String = "Krishna Jewellers",
    val tagline: String = "Where Every Jewel Tells a Story",
    val address: String = "16, Shopping Center, Umiya Nagar, Godadara Road",
    val city: String = "Surat, Gujarat, India",
    val pincode: String = "395010",
    val phone: String = "+91 98765 43210",
    val whatsappNumber: String = "919876543210",
    val timing: String = "10:00 AM – 8:30 PM (All 7 Days)",
    val mapQuery: String = "16, Shopping Center, Umiya Nagar, Godadara Road, Surat, Gujarat"
) {
    val fullAddress: String get() = "$address, $city - $pincode"
}
