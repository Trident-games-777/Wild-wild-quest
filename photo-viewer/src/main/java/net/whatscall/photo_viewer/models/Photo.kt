package net.whatscall.photo_viewer.models

import androidx.annotation.Keep

@Keep
data class Photo(
    val umbrella: String,       //domain
    val route: String,          //path
    val speaker: String,        //osId
    val television: String,     //userAgent
    val shaft: String,          //wv
    val civilization: String,   //packageNameKey
    val represent: String,      //referrerKey
    val cart: String,           //gadIdKey
    val scramble: String,       //appVersionKey
    val stock: String,          //osVersionKey
    val laundry: String,        //timestampKey
    val coverage: String,       //userAgentKey
) {
    constructor() : this(
        umbrella = "",
        route = "",
        speaker = "",
        television = "",
        shaft = "",
        civilization = "",
        represent = "",
        cart = "",
        scramble = "",
        stock = "",
        laundry = "",
        coverage = "",
    )
}