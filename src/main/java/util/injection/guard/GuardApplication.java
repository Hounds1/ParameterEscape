package util.injection.guard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class GuardApplication {

	public static void main(String[] args) {
		SpringApplication.run(GuardApplication.class, args);
	}

}
