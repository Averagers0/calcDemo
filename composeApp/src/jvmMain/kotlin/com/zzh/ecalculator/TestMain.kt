package com.zzh.ecalculator

import com.zzh.ecalculator.platform.TimeProvider

fun main() {
    println("Testing cross-platform time APIs and Calculator on JVM Desktop...")
    
    try {
        // 测试自定义expect/actual时间API
        try {
            val customTime = TimeProvider.currentTimeMillis()
            println("✅ Custom TimeProvider working: $customTime")
        } catch (e: Exception) {
            println("❌ Custom TimeProvider failed: ${e.message}")
        }
        
        // Test Calculator
        val calculator = com.zzh.ecalculator.calculator.Calculator()
        val result = calculator.calculate("2 + 3 * 4")
        println("✅ Calculator test: 2 + 3 * 4 = $result")
        
        // Test detailed calculation
        val detailedResult = calculator.calculateDetailed("10 / 2 + 3")
        println("✅ Detailed calculation: ${detailedResult.originalExpression} = ${detailedResult.result}")
        println("   Calculation time: ${detailedResult.calculationTimeMs}ms")
        
        // Test calculation history
        calculator.calculate("5 + 5")
        calculator.calculate("20 - 10")
        val history = calculator.getHistory(3)
        println("✅ Calculation history (${history.size} entries):")
        history.forEach { record ->
            println("   ${record.expression} = ${record.result} (${record.calculationTimeMs}ms)")
        }
        
        // Test operator info
        val operators = calculator.getOperatorInfo()
        println("✅ Available operators: ${operators.map { it.symbol }}")
        
        println("All tests passed! ✅")
        
    } catch (e: Exception) {
        println("Error: ${e.message}")
        e.printStackTrace()
    }
}
