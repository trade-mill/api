package trademill.apiserver.demo.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import trademill.apiserver.demo.domain.Demo;
import trademill.apiserver.demo.service.DemoService;

@RestController()
@RequestMapping("/api/demo")
@RequiredArgsConstructor
public class DemoController {

	private final DemoService demoService;

	@GetMapping("/hello")
	public String hello() {
		return this.demoService.hello();
	}

	@GetMapping
	public List<Demo> getAll() {
		return this.demoService.getAll();
	}

}
