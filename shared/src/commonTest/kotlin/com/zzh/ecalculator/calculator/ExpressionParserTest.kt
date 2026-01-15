package com.zzh.ecalculator.calculator

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

/**
 * 表达式解析器测试
 */
class ExpressionParserTest {
    
    private val parser = ExpressionParser()
    
    @Test
    fun testSimpleExpressions() {
        // 简单表达式测试
        assertEquals(listOf("2", "3", "+"), parser.parseToPostfix("2 + 3"))
        assertEquals(listOf("5", "2", "-"), parser.parseToPostfix("5 - 2"))
        assertEquals(listOf("3", "4", "*"), parser.parseToPostfix("3 * 4"))
        assertEquals(listOf("8", "2", "/"), parser.parseToPostfix("8 / 2"))
    }
    
    @Test
    fun testOperatorPrecedence() {
        // 运算符优先级测试
        assertEquals(listOf("2", "3", "4", "*", "+"), parser.parseToPostfix("2 + 3 * 4"))
        assertEquals(listOf("2", "3", "*", "4", "+"), parser.parseToPostfix("2 * 3 + 4"))
        assertEquals(listOf("8", "2", "/", "2", "/"), parser.parseToPostfix("8 / 2 / 2"))
    }
    
    @Test
    fun testParentheses() {
        // 括号测试
        assertEquals(listOf("2", "3", "+", "4", "*"), parser.parseToPostfix("(2 + 3) * 4"))
        assertEquals(listOf("10", "5", "-", "1", "/"), parser.parseToPostfix("(10 - 5) / 1"))
        assertEquals(listOf("1", "2", "3", "*", "+"), parser.parseToPostfix("1 + (2 * 3)"))
        assertEquals(listOf("1", "2", "+", "3", "*"), parser.parseToPostfix("((1 + 2) * 3)"))
    }
    
    @Test
    fun testComplexExpressions() {
        // 复杂表达式测试
        assertEquals(
            listOf("1", "2", "3", "*", "+", "4", "5", "*", "+", "2", "-"),
            parser.parseToPostfix("1 + 2 * 3 + 4 * 5 - 2")
        )
        
        assertEquals(
            listOf("1", "2", "+", "3", "4", "+", "*", "4", "+"),
            parser.parseToPostfix("(1 + 2) * (3 + 4) + 4")
        )
    }
    
    @Test
    fun testDecimalNumbers() {
        // 小数测试
        assertEquals(listOf("1.5", "2", "+"), parser.parseToPostfix("1.5 + 2"))
        assertEquals(listOf("3.14", "2.5", "*"), parser.parseToPostfix("3.14 * 2.5"))
        assertEquals(listOf("0.1", "0.2", "+"), parser.parseToPostfix("0.1 + 0.2"))
    }
    
    @Test
    fun testWhitespace() {
        // 空格处理测试
        assertEquals(listOf("2", "3", "+"), parser.parseToPostfix("  2  +  3  "))
        assertEquals(listOf("2", "3", "+"), parser.parseToPostfix("2+3"))
        assertEquals(listOf("2", "3", "+"), parser.parseToPostfix(" 2+ 3 "))
    }
    
    @Test
    fun testInvalidExpressions() {
        // 无效表达式测试
        assertFailsWith<IllegalArgumentException> {
            parser.parseToPostfix("")
        }
        
        assertFailsWith<IllegalArgumentException> {
            parser.parseToPostfix("   ")
        }
        
        assertFailsWith<IllegalArgumentException> {
            parser.parseToPostfix("2 + + 3")
        }
        
        assertFailsWith<IllegalArgumentException> {
            parser.parseToPostfix("(2 + 3")  // 缺少右括号
        }
        
        assertFailsWith<IllegalArgumentException> {
            parser.parseToPostfix("2 + 3)")  // 缺少左括号
        }
        
        assertFailsWith<IllegalArgumentException> {
            parser.parseToPostfix("2 & 3")   // 未知运算符
        }
    }
    
    @Test
    fun testInvalidNumbers() {
        // 无效数字测试
        assertFailsWith<IllegalArgumentException> {
            parser.parseToPostfix("2.3.4 + 1")  // 多个小数点
        }
        
        assertFailsWith<IllegalArgumentException> {
            parser.parseToPostfix(". + 1")      // 只有小数点
        }
    }
}
