package com.zzh.ecalculator.calculator

import kotlin.test.Test

class DebugTest {
    
    @Test
    fun debugInvalidNumbers() {
        val parser = ExpressionParser()
        
        println("Testing '2.3.4 + 1':")
        try {
            val result = parser.parseToPostfix("2.3.4 + 1")
            println("Result: $result")
        } catch (e: Exception) {
            println("Exception: ${e.message}")
        }
        
        println("\nTesting '. + 1':")
        try {
            val result = parser.parseToPostfix(". + 1")
            println("Result: $result")
        } catch (e: Exception) {
            println("Exception: ${e.message}")
        }
        
        println("\nTesting complex expression:")
        try {
            val calculator = Calculator()
            val result = calculator.calculate("(1 + 2) * (3 + 4) + 4")
            println("Result: $result")
        } catch (e: Exception) {
            println("Exception: ${e.message}")
        }
    }
}
