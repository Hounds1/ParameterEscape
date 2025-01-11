package util.injection.guard.core.aspect.decorator;

import java.lang.reflect.Field;
import java.util.List;

public class InjectionGuardExceptionDecorator {

    public static void decorate(List<Field> fields) {
        StringBuilder builder = new StringBuilder();
        builder.append("Invalid Field Type List [");

        fields.forEach(field -> {
            builder.append("(")
                    .append(field.getType().getName())
                    .append(field.getName())
                    .append(")");
        });

        builder.append("]");

        //todo exchange with Logger
        System.out.println(builder);
    }
}
