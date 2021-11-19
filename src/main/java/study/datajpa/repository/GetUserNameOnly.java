package study.datajpa.repository;

import org.springframework.beans.factory.annotation.Value;

public interface GetUserNameOnly {

    // Spring SpEL 문법도 지원
//    @Value("#{target.username + ' ' + target.age + ' ' + target.team.name}")
    // 하지만, 위와 같이 @Value로 처리하면 모든 Entity 값을 가져와서 위 String값처럼 조작하여 처리한다
    String getUsername();
}
