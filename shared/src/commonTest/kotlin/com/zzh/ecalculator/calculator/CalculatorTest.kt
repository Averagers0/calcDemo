package com.zzh.ecalculator.calculator

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

/**
 * Calculator核心功能测试
 */
class CalculatorTest {
    
    private val calculator = Calculator()
    
    @Test
    fun testBasicArithmetic() {
        // 基础算术测试
        assertEquals(5.0, calculator.calculate("2 + 3"))
        assertEquals(-1.0, calculator.calculate("2 - 3"))
        assertEquals(6.0, calculator.calculate("2 * 3"))
        assertEquals(2.0, calculator.calculate("6 / 3"))
    }
    
    @Test
    fun testOperatorPrecedence() {
        // 运算符优先级测试
        assertEquals(14.0, calculator.calculate("2 + 3 * 4"))  // 2 + (3 * 4) = 14
        assertEquals(10.0, calculator.calculate("2 * 3 + 4"))  // (2 * 3) + 4 = 10
        assertEquals(1.5, calculator.calculate("6 / 2 / 2"))   // (6 / 2) / 2 = 1.5
    }
    
    @Test
    fun testParentheses() {
        // 括号测试
        assertEquals(20.0, calculator.calculate("(2 + 3) * 4"))
        assertEquals(5.0, calculator.calculate("(10 - 5) / 1"))
        assertEquals(7.0, calculator.calculate("1 + (2 * 3)"))
        assertEquals(9.0, calculator.calculate("((1 + 2) * 3)"))
    }
    
    @Test
    fun testComplexExpressions() {
        // 复杂表达式测试
        assertEquals(25.0, calculator.calculate("1 + 2 * 3 + 4 * 5 - 2"))
        assertEquals(25.0, calculator.calculate("(1 + 2) * (3 + 4) + 4"))
        assertEquals(2.0, calculator.calculate("(10 - 6) / (4 - 2)"))
    }
    
    @Test
    fun testDecimalNumbers() {
        // 小数测试
        assertEquals(3.5, calculator.calculate("1.5 + 2"))
        assertEquals(2.5, calculator.calculate("5 / 2"))
        assertEquals(3.14, calculator.calculate("3.14 * 1"), 0.001)
    }
    
    @Test
    fun testWhitespace() {
        // 空格处理测试
        assertEquals(5.0, calculator.calculate("   2   +   3   "))
        assertEquals(5.0, calculator.calculate("2+3"))
        assertEquals(5.0, calculator.calculate(" 2+ 3 "))
    }
    
    @Test
    fun testDivisionByZero() {
        // 除零测试
        assertFailsWith<CalculationException> {
            calculator.calculate("5 / 0")
        }
        assertFailsWith<CalculationException> {
            calculator.calculate("1 / (2 - 2)")
        }
    }
    
    @Test
    fun testInvalidExpressions() {
        // 无效表达式测试
        assertFailsWith<CalculationException> {
            calculator.calculate("")
        }
        assertFailsWith<CalculationException> {
            calculator.calculate("2 +")
        }
        assertFailsWith<CalculationException> {
            calculator.calculate("+ 2")
        }
        assertFailsWith<CalculationException> {
            calculator.calculate("2 + + 3")
        }
        assertFailsWith<CalculationException> {
            calculator.calculate("(2 + 3")  // 缺少右括号
        }
        assertFailsWith<CalculationException> {
            calculator.calculate("2 + 3)")  // 缺少左括号
        }
    }
    
    @Test
    fun testValidateExpression() {
        // 表达式验证测试
        assertTrue(calculator.validateExpression("2 + 3").isValid)
        assertTrue(calculator.validateExpression("(1 + 2) * 3").isValid)
        assertTrue(!calculator.validateExpression("2 +").isValid)
        assertTrue(!calculator.validateExpression("(2 + 3").isValid)
    }
    
    @Test
    fun testCalculationHistory() {
        // 历史记录测试
        calculator.clearHistory()
        assertEquals(0, calculator.getHistory().size)
        
        calculator.calculate("2 + 3")
        calculator.calculate("4 * 5")
        assertEquals(2, calculator.getHistory().size)
        
        val history = calculator.getHistory()
        assertEquals("2 + 3", history[0].expression)
        assertEquals(5.0, history[0].result)
        assertEquals("4 * 5", history[1].expression)
        assertEquals(20.0, history[1].result)
    }
    
    @Test
    fun testDetailedCalculation() {
        // 详细计算结果测试
        val result = calculator.calculateDetailed("2 + 3 * 4")
        
        assertTrue(result.success)
        assertEquals("2 + 3 * 4", result.originalExpression)
        assertEquals(14.0, result.result)
        assertEquals(listOf("2", "3", "4", "*", "+"), result.postfixExpression)
        assertTrue(result.calculationTimeMs >= 0)
    }
}
