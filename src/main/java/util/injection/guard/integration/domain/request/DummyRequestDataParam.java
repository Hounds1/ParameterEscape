package util.injection.guard.integration.domain.request;

import lombok.*;
import util.injection.guard.core.aspect.annotation.GuardPoint;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class DummyRequestDataParam {

    @GuardPoint
    private String dummy;

    public static DummyRequestDataParam of(String dummy) {
        return new DummyRequestDataParam(dummy);
    }
}
