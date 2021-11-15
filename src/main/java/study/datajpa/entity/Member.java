package study.datajpa.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of = {"id", "username", "age"})   // toString 출력 제한
public class Member {
    @Id
    @GeneratedValue
    @Column(name = "member_id")
    private Long id;
    private String username;
    private int age;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;

    // 프록시 구현체가 생성 시 사용해야 함 - NoArgsConstructor 처리
//    protected Member(){}

    public Member(String username) {
        this.username = username;
        this.age = 0;
    }

    public Member(String username, int age) {
        this.username = username;
        this.age = age;
    }

    public void changeTeam(Team newTeam) {
        this.team = newTeam;
        team.getMembers().add(this);
    }
}
