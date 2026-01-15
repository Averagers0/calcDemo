package com.zzh.ecalculator

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform