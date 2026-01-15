package com.zzh.ecalculator.platform

import kotlin.js.Date

/**
 * JavaScript/Web平台的时间提供者实现
 */
actual object TimeProvider {
    actual fun currentTimeMillis(): Long {
        return Date.now().toLong()
    }
}
