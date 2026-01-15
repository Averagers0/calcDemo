package com.zzh.ecalculator.platform

/**
 * 跨平台时间提供者
 * 
 * 使用expect/actual机制为不同平台提供时间获取功能
 * 这是kotlinx.datetime的备选方案
 */
expect object TimeProvider {
    /**
     * 获取当前时间的毫秒时间戳
     * @return 自1970年1月1日以来的毫秒数
     */
    fun currentTimeMillis(): Long
}
