#  跨平台计算器应用需求文档

**（Kotlin Multiplatform + 面向对象设计实践）**

---

## 1. 项目背景（Background）

随着 Kotlin Multiplatform（KMP）技术的成熟，开发者可以在多个平台之间共享核心业务逻辑。本项目旨在通过实现一个**跨平台计算器应用**，系统性地练习和理解：

* 面向对象设计思想（OOP）
* 接口抽象与职责拆分
* 开闭原则（OCP）与单一职责原则（SRP）
* KMP 项目结构与 commonMain 代码组织方式

---

## 2. 项目目标（Objectives）

### 2.1 功能目标

实现一个支持**基础算术表达式计算**的计算器核心模块，具备以下能力：

* 支持整数运算
* 支持括号
* 支持运算符优先级
* 能正确解析并计算字符串形式的数学表达式

### 2.2 技术目标

* **核心计算逻辑完全位于 `commonMain`**
* 不依赖任何平台 API
* 通过面向对象方式设计，避免过程式写法
* 支持后续扩展新的运算符而无需修改现有核心代码

---

## 3. 支持的运算能力（Scope）

### 3.1 基础运算符

| 运算符 | 描述   | 示例          |
| --- | ---- | ----------- |
| `+` | 加法   | `1 + 2`     |
| `-` | 减法   | `5 - 3`     |
| `*` | 乘法   | `2 * 4`     |
| `/` | 整数除法 | `7 / 2 = 3` |
| `%` | 取余   | `7 % 2 = 1` |

### 3.2 表达式能力

* 支持中缀表达式输入
* 支持括号改变优先级

示例：

```
1 + 2 * 3
(1 + 2) * 3
10 % (3 + 2)
```

---

## 4. 非功能性需求（Non-Functional Requirements）

### 4.1 可扩展性

* 新增运算符（如 `^`、`max`）时：

    * 不修改现有运算逻辑
    * 仅通过新增类完成扩展

### 4.2 可测试性

* 每个核心组件可单独进行单元测试：

    * 运算符
    * 表达式解析器
    * 表达式求值器

### 4.3 可维护性

* 各模块职责清晰
* 避免大型 `when` / `if-else` 判断逻辑

---

## 5. 系统架构设计（High-Level Design）

### 5.1 模块划分

```
shared/
 └── commonMain/
      └── calculator/
           ├── Calculator
           ├── Operator
           ├── OperatorRegistry
           ├── ExpressionParser
           ├── ExpressionEvaluator
           └── operators/
```

---

## 6. 核心抽象设计（Core Abstractions）

### 6.1 运算符抽象（Operator）

```kotlin
interface Operator {
    val symbol: String
    val priority: Int
    fun apply(left: Int, right: Int): Int
}
```

**设计目的：**

* 每个运算符封装自己的行为
* 消除条件判断
* 符合开闭原则（OCP）

---

### 6.2 运算符注册中心（OperatorRegistry）

```kotlin
class OperatorRegistry {
    fun get(symbol: String): Operator?
}
```

**职责：**

* 管理系统中已支持的运算符
* 作为解析器与运算符实现之间的解耦层

---

### 6.3 计算器接口（Calculator）

```kotlin
interface Calculator {
    fun calculate(expression: String): Int
}
```

**职责：**

* 提供统一的计算入口
* 屏蔽内部解析与计算细节

---

### 6.4 表达式解析器（ExpressionParser）

**职责：**

* 将中缀表达式解析为后缀表达式（逆波兰表达式）
* 处理：

    * 运算符优先级
    * 括号

---

### 6.5 表达式求值器（ExpressionEvaluator）

**职责：**

* 根据后缀表达式进行计算
* 使用栈结构执行运算

---

## 7. 典型使用流程（Workflow）

```text
输入表达式
   ↓
ExpressionParser（解析）
   ↓
后缀表达式 Token 列表
   ↓
ExpressionEvaluator（求值）
   ↓
结果
```

---

## 8. 错误处理约定（Error Handling）

| 场景      | 行为                          |
| ------- | --------------------------- |
| 非法字符    | 抛出 IllegalArgumentException |
| 括号不匹配   | 抛出 IllegalStateException    |
| 除数为 0   | 抛出 ArithmeticException      |
| 未支持的运算符 | 明确异常提示                      |

---

## 9. 扩展设计（Future Extensions）

本设计应支持以下扩展方向：

1. 支持 `Double` 运算
2. 支持一元运算符（如 `-5`）
3. 支持函数式调用（如 `max(1, 2)`）
4. 运算符优先级可配置
5. 支持表达式 AST（抽象语法树）

---

## 10. 设计原则总结

本项目需遵循以下设计原则：

* **单一职责原则（SRP）**
* **开闭原则（OCP）**
* **组合优于继承**
* **接口隔离**
* **面向抽象编程**

---

## 11. 项目完成标准（Definition of Done）

* 所有功能在 `commonMain` 可运行
* 无平台相关依赖
* 至少覆盖以下测试用例：

    * `1 + 2 * 3`
    * `(1 + 2) * 3`
    * `10 % 3`
* 新增运算符无需修改已有代码


