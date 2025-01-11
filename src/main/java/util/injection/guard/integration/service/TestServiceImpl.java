package util.injection.guard.integration.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import util.injection.guard.core.aspect.annotation.AnnotatedWithGuard;
import util.injection.guard.integration.domain.request.DummyRequestDataParam;

@Service
@Slf4j
public class TestServiceImpl implements TestService {

    @Override
    @AnnotatedWithGuard
    public void testInvoke(DummyRequestDataParam param) {
        log.info(param.getDummy());
    }
}
