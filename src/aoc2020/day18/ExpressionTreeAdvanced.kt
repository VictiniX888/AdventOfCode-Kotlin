package aoc2020.day18

class ExpressionTreeAdvanced(expression: List<Char>) {
    val self: Char
    val left: ExpressionTreeAdvanced?
    val right: ExpressionTreeAdvanced?

    init {
        val expr = if (expression.isEnclosed()) expression.drop(1).dropLast(1) else expression

        if (expr.size == 1) {
            self = expr.first()
            left = null
            right = null
        } else {
            var pointer = expr.size - 1

            right = expr.let {
                var firstAdd = -1
                var newPointer = pointer+1
                var parenLevel = 0
                while (newPointer > 0) {
                    newPointer--
                    if (parenLevel > 0) {
                        when (it[newPointer]) {
                            ')' -> parenLevel++
                            '(' -> parenLevel--
                        }
                    } else {
                        when (it[newPointer]) {
                            ')' -> parenLevel++
                            '+' -> firstAdd = newPointer
                            '*' -> break
                        }
                    }
                }

                if (newPointer == 0) {  // no multiplication
                    if (firstAdd != -1) {
                        ExpressionTreeAdvanced(expr.slice(firstAdd+1..pointer)).also { pointer = firstAdd+1 }
                    } else {
                        ExpressionTreeAdvanced(listOf(expr[pointer]))
                    }
                } else {                // found multiplication
                    ExpressionTreeAdvanced(expr.slice(newPointer+1..pointer)).also { pointer = newPointer+1 }
                }
            }

            pointer--

            self = expr[pointer]

            pointer--

            left = ExpressionTreeAdvanced(expr.slice(0..pointer))
        }
    }

    fun eval(): Long {
        return if (left == null || right == null) {
            Character.getNumericValue(self).toLong()
        } else {
            when (self) {
                '+' -> left.eval() + right.eval()
                '*' -> left.eval() * right.eval()
                else -> error("Unknown operator during evaluation: $self")
            }
        }
    }

    private fun List<Char>.isEnclosed(): Boolean {
        if (this.first() == '(' && this.last() == ')') {
            var pointer = 0
            var parenLevel = 1

            while (parenLevel > 0) {
                pointer++
                when (this[pointer]) {
                    '(' -> parenLevel++
                    ')' -> parenLevel--
                }
            }

            return pointer == this.size - 1
        } else {
            return false
        }
    }
}