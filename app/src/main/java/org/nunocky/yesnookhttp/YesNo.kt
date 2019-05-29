package org.nunocky.yesnookhttp

import se.ansman.kotshi.JsonSerializable

@JsonSerializable
data class YesNo(
    val answer: String,
    val forced: Boolean,
    val image: String
)