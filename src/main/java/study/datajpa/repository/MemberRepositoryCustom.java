package study.datajpa.repository;

import study.datajpa.entity.Member;

import java.util.List;

// MemberReposiroty 에서 커스텀으로 처리하고 싶을 때!
// 1. 지금처럼 커스텀 인터페이스 생성
// 2. 이 인터페이스를 상속하는 클래스에서 해당 커스텀 함수 구현 (현재 코드 상 MemberRepositoryImpl)
// 3. 이 인터페이스를 JpaRepository 와 함께 extends 처리 (현재 코드 상 MemberRepository)
// -> 해당 기능은 Java 기능은 아니고, Jpa 에서 알아서 찾아서 처리해줌
// 중요! 2 클래스 이름은 3번의 이름에 Impl 을 붙여주어야 한다
// : 수정도 가능하지만, 관례를 따르는 게 편하다

// cf. 핵심 비지니스 로직과 별개로, 화면에 맞춘 Repository는 (DTO 등) 별도의 클래스로 나누는 게 낫다
// 사용자 정의 Repository는 그 의미로 만드는 것과는 거리가 멀다
// - 이걸 Custom에 넣은 경우, 결론적으로 MemberRepository 자체가 커지는 것이기 때문
public interface MemberRepositoryCustom {
    List<Member> findMemberCustom();
}
