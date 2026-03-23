package com.dio.devinperformer

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform