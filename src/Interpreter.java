import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class Interpreter implements Expr.Visitor<Object> {
    @Override
    public Object visitBinaryExpr(Expr.Binary expr) {
        Object left = evaluate(expr.left);
        Object right = evaluate(expr.right);

        switch (expr.operator.type) {
            case PLUS:
                if (left instanceof Double && right instanceof Double) {
                    return (double) left + (double) right;
                }
                throw new RuntimeError(expr.operator,
                        "Operands must be two numbers.");
            case GREATER:
                checkNumberOperand(expr.operator, left, right);
                return (double) left > (double) right ? 1 : 0;
            case LESS:
                checkNumberOperand(expr.operator, left, right);
                return (double) left < (double) right ? 1 : 0;
            case EQUAL:
                return isEqual(left, right);
            case MINUS:
                checkNumberOperand(expr.operator, left, right);
                return (double) left - (double) right;
            case SLASH:
                checkNumberOperand(expr.operator, left, right);
                return (double) left / (double) right;
            case STAR:
                checkNumberOperand(expr.operator, left, right);
                return (double) left * (double) right;
        }
        return null;
    }

    @NotNull
    @Contract(pure = true)
    private Object isEqual(Object left, Object right) {
        if (left == null && right == null) return 1;
        if (left == null) return 0;

        return left.equals(right) ? 1 : 0;
    }

    @Contract("_, null, _ -> fail")
    private void checkNumberOperand(Token operator, Object left, Object right) {
        if (left instanceof Double && right instanceof Double) return;

        throw new RuntimeError(operator, "Operands must be numbers.");
    }

    private Object evaluate(@NotNull Expr expr) {
        return expr.accept(this);
    }

    @Override
    public Object visitGroupingExpr(Expr.Grouping expr) {
        return evaluate(expr.expression);
    }

    @Override
    public Object visitLiteralExpr(Expr.Literal expr) {
        return expr.value;
    }

    void interpret(List<Expr> exprs) {
        try {
            for (Expr expr : exprs) {
                execute(expr);
            }
        } catch (RuntimeError error) {
            Main.runtimeError(error);
        }
    }

    private void execute(Expr expr) {
        Object value = evaluate(expr);
        System.out.println(stringify(value));
    }

    @Contract("null -> !null")
    private String stringify(Object object) {
        if (object == null) return "nil";

        if (object instanceof Double) {
            String text = object.toString();
            if (text.endsWith(".0")) {
                text = text.substring(0, text.length() - 2);
            }
            return text;
        }
        return object.toString();
    }
}
