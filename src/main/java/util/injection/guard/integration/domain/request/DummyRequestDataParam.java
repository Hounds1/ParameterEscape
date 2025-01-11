package util.injection.guard.integration.domain.request;

import lombok.*;
import util.injection.guard.core.aspect.annotation.GuardPoint;
import util.injection.guard.core.aspect.annotation.element.GuardType;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class DummyRequestDataParam {

    @GuardPoint(point = GuardType.GUARD_TYPE_SQL)
    private String sqlOnly;

    @GuardPoint(point = GuardType.GUARD_TYPE_HTML)
    private String htmlOnly;

    @GuardPoint
    private String defaultDulaGuard;


    public static DummyRequestDataParam of(String sqlOnly, String htmlOnly, String defaultDulaGuard) {
        return new DummyRequestDataParam(sqlOnly, htmlOnly, defaultDulaGuard);
    }
}
