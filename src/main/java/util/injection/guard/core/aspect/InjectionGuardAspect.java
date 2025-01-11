package util.injection.guard.core.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import util.injection.guard.core.aspect.annotation.AnnotatedWithGuard;
import util.injection.guard.core.aspect.annotation.GuardPoint;
import util.injection.guard.core.aspect.annotation.element.GuardType;
import util.injection.guard.core.aspect.decorator.InjectionGuardExceptionDecorator;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Aspect
@Component
public class InjectionGuardAspect {

    private static final Map<Class<?>, List<Field>> FIELD_STRUCTURE_STORAGE;
    private static final String DENIED_PATTERN;
    private static final Pattern SQL_PATTERN;
    private static final Pattern HTML_PATTERN;

    static {
        FIELD_STRUCTURE_STORAGE = new ConcurrentHashMap<>();
        DENIED_PATTERN = "\\\\";
        SQL_PATTERN = Pattern.compile("[_%']");
        HTML_PATTERN = Pattern.compile("<[^>]*>");
    }

    @Before("@annotation(util.injection.guard.core.aspect.annotation.AnnotatedWithGuard) || @within(util.injection.guard.core.aspect.annotation.AnnotatedWithGuard)")
    public void point(final JoinPoint joinPoint) throws IllegalAccessException {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method m = signature.getMethod();

        Object[] args = joinPoint.getArgs();

        for (Object arg : args) {
            if (arg != null) {
                escape(arg, m);
            }
        }
    }

    private void escape(Object arg, Method m) throws IllegalAccessException {
        Class<?> c = arg.getClass();
        List<Field> fields = FIELD_STRUCTURE_STORAGE.computeIfAbsent(c, this::extractFields);
        List<Field> decorateTargets = new ArrayList<>();

        AnnotatedWithGuard guardEntryPoint = m.getAnnotation(AnnotatedWithGuard.class);

        for (Field field : fields) {
            field.setAccessible(true);

            try {
                Object val = field.get(arg);

                if (val == null) {
                    continue;
                }
                if (val instanceof String) {
                    GuardPoint gp = field.getAnnotation(GuardPoint.class);
                    field.set(arg, escape((String) val, gp.point()));
                } else {
                    decorateTargets.add(field);
                }
            } catch (IllegalAccessException | IllegalArgumentException e) {
                if (guardEntryPoint.exceptionNotify()) {
                    throw e;
                }
            } finally {
                if (guardEntryPoint.decoratePoints()) {
                    InjectionGuardExceptionDecorator.decorate(decorateTargets);
                }
            }
        }
    }

    private List<Field> extractFields(Class<?> c) {
        List<Field> fields = new ArrayList<>();

        Field[] df = c.getDeclaredFields();

        for (Field f : df) {
            if (f.isAnnotationPresent(GuardPoint.class)) {
                fields.add(f);
            }
        }

        return fields;
    }

    private String escape(String val, GuardType type) {
        if (val.isBlank() || val.isEmpty()) {
            return "";
        }

        if (val.contains(DENIED_PATTERN)) {
            throw new IllegalArgumentException("An error has been occurred while replacing value caused denied char detected.");
        }

        switch (type) {
            case GUARD_TYPE_SQL -> {return escapeSqlOnly(val);}
            case GUARD_TYPE_HTML -> {return escapeHTMLOnly(val);}
            case GUARD_TYPE_DUAL -> {return escapeDula(val);}
        }

        return val;
    }

    private String escapeDula(String val) {
        String s = escapeHTMLOnly(val);

        return escapeSqlOnly(s);
    }

    private String escapeHTMLOnly(String val) {
        Matcher matcher = HTML_PATTERN.matcher(val);

        if (matcher.find()) {
            return val.replaceAll("<", "&lt;")
                    .replaceAll(">", "&gt;")
                    .replaceAll("\"", "&quot;")
                    .replaceAll("'", "&#39;");
        }

        return val;
    }

    private String escapeSqlOnly(String val) {
        Matcher matcher = SQL_PATTERN.matcher(val);

        if (matcher.find()) {
            return val.replaceAll("_", "\\\\_")
                    .replaceAll("%", "\\\\%")
                    .replaceAll("'", "''");
        }

        return val;
    }
}
