package trademill.apiserver.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import trademill.apiserver.demo.domain.Demo;

@Repository
public interface DemoRepository extends JpaRepository<Demo, Long> {

}
