package aoc2020.day18

class ExpressionTreeBasic(expression: List<Char>) {
    val self: Char
    val left: ExpressionTreeBasic?
    val right: ExpressionTreeBasic?

    init {
        val expr = if (expression.isEnclosed()) expression.drop(1).dropLast(1) else expression

        if (expr.size == 1) {
            self = expr.first()
            left = null
            right = null
        } else {
            var pointer = expr.size - 1

            right = if (expr[pointer] == ')') {
                var newPointer = pointer
                var parenLevel = 1
                while (parenLevel > 0) {
                    newPointer--
                    when (expr[newPointer]) {
                        ')' -> parenLevel++
                        '(' -> parenLevel--
                    }
                }
                ExpressionTreeBasic(expr.slice(newPointer+1..pointer-1))
                    .also { pointer = newPointer }
            } else ExpressionTreeBasic(listOf(expr[pointer]))

            pointer--

            self = expr[pointer]

            pointer--

            left = ExpressionTreeBasic(expr.slice(0..pointer))
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