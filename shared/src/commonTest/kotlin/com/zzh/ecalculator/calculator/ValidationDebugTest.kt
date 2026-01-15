package com.zzh.ecalculator.calculator

import kotlin.test.Test

class ValidationDebugTest {
    
    @Test
    fun debugValidation() {
        val calculator = Calculator()
        
        val testCases = listOf(
            "2 + 3",
            "(1 + 2) * 3",
            "2 +",
            "(2 + 3"
        )
        
        testCases.forEach { expr ->
            println("Testing: '$expr'")
            val result = calculator.validateExpression(expr)
            println("  Valid: ${result.isValid}")
            println("  Error: ${result.errorMessage}")
            println()
        }
    }
}
