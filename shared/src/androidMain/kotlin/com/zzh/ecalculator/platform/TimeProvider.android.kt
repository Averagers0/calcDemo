package com.zzh.ecalculator.platform

/**
 * Android平台的时间提供者实现
 */
actual object TimeProvider {
    actual fun currentTimeMillis(): Long {
        return System.currentTimeMillis()
    }
}
