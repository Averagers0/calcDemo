package com.zzh.ecalculator.calculator

import kotlin.test.Test

class SimpleTest {
    
    @Test
    fun testSimpleCalculation() {
        try {
            val calculator = Calculator()
            val result = calculator.calculate("2 + 3")
            println("Result: $result")
        } catch (e: Exception) {
            println("Error: ${e.message}")
            e.printStackTrace()
        }
    }
    
    @Test
    fun testParser() {
        try {
            val parser = ExpressionParser()
            val result = parser.parseToPostfix("2 + 3")
            println("Postfix: $result")
        } catch (e: Exception) {
            println("Error: ${e.message}")
            e.printStackTrace()
        }
    }
    
    @Test
    fun testInvalidNumberParsing() {
        try {
            val parser = ExpressionParser()
            val result = parser.parseToPostfix("2.3.4 + 1")
            println("Unexpected success: $result")
        } catch (e: Exception) {
            println("Expected error: ${e.message}")
        }
    }
}
