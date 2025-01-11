package util.injection.guard.integration.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import util.injection.guard.integration.domain.request.DummyRequestDataParam;
import util.injection.guard.integration.service.TestService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/guard")
public class TestController {

    private final TestService testService;

    @GetMapping
    public ResponseEntity<String> test() {
        final String keyword = "<script>test</script>";
        DummyRequestDataParam dummyRequestDataParam = DummyRequestDataParam.of(keyword);

        testService.testInvoke(dummyRequestDataParam);

        return ResponseEntity.ok("0");
    }
}
