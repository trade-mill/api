package trademill.apiserver.demo.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import trademill.apiserver.demo.domain.Demo;
import trademill.apiserver.demo.repository.DemoRepository;

@Service
@RequiredArgsConstructor
public class DemoService {

	private final DemoRepository demoRepository;


	public String hello() {
		return "Hello World!";
	}

	public List<Demo> getAll() {
		return this.demoRepository.findAll();
	}

}
