package com.zzh.ecalculator.calculator.operators

import com.zzh.ecalculator.calculator.Associativity
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

/**
 * 运算符测试
 */
class OperatorTest {
    
    @Test
    fun testAddOperator() {
        val addOp = AddOperator()
        assertEquals("+", addOp.symbol)
        assertEquals(1, addOp.precedence)
        assertEquals(Associativity.LEFT, addOp.associativity)
        assertEquals(2, addOp.operandCount)
        
        assertEquals(5.0, addOp.calculate(listOf(2.0, 3.0)))
        assertEquals(0.0, addOp.calculate(listOf(-2.0, 2.0)))
        assertEquals(3.14, addOp.calculate(listOf(1.0, 2.14)), 0.001)
    }
    
    @Test
    fun testSubtractOperator() {
        val subOp = SubtractOperator()
        assertEquals("-", subOp.symbol)
        assertEquals(1, subOp.precedence)
        assertEquals(Associativity.LEFT, subOp.associativity)
        assertEquals(2, subOp.operandCount)
        
        assertEquals(-1.0, subOp.calculate(listOf(2.0, 3.0)))
        assertEquals(0.0, subOp.calculate(listOf(5.0, 5.0)))
        assertEquals(1.86, subOp.calculate(listOf(3.0, 1.14)), 0.001)
    }
    
    @Test
    fun testMultiplyOperator() {
        val mulOp = MultiplyOperator()
        assertEquals("*", mulOp.symbol)
        assertEquals(2, mulOp.precedence)
        assertEquals(Associativity.LEFT, mulOp.associativity)
        assertEquals(2, mulOp.operandCount)
        
        assertEquals(6.0, mulOp.calculate(listOf(2.0, 3.0)))
        assertEquals(0.0, mulOp.calculate(listOf(0.0, 5.0)))
        assertEquals(3.14, mulOp.calculate(listOf(3.14, 1.0)), 0.001)
    }
    
    @Test
    fun testDivideOperator() {
        val divOp = DivideOperator()
        assertEquals("/", divOp.symbol)
        assertEquals(2, divOp.precedence)
        assertEquals(Associativity.LEFT, divOp.associativity)
        assertEquals(2, divOp.operandCount)
        
        assertEquals(2.0, divOp.calculate(listOf(6.0, 3.0)))
        assertEquals(2.5, divOp.calculate(listOf(5.0, 2.0)))
        
        // 测试除零错误
        assertFailsWith<ArithmeticException> {
            divOp.calculate(listOf(5.0, 0.0))
        }
    }
    
    @Test
    fun testOperatorValidation() {
        val addOp = AddOperator()
        
        // 正确的操作数数量
        addOp.validateOperands(listOf(1.0, 2.0))
        
        // 错误的操作数数量
        assertFailsWith<IllegalArgumentException> {
            addOp.validateOperands(listOf(1.0))
        }
        
        assertFailsWith<IllegalArgumentException> {
            addOp.validateOperands(listOf(1.0, 2.0, 3.0))
        }
    }
}
