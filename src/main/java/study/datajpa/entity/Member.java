package study.datajpa.entity;

import jakarta.persistence.*;
import lombok.*;

import static jakarta.persistence.FetchType.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of = {"id", "username", "age"})
public class Member {

    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;
    private int age;
    private String username;
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "team_id")
    private Team team;

    @Builder(builderMethodName = "builder1")
    public Member(String username) {
        this.username = username;
    }

    public Member(int age, String username, Team team) {
        this.age = age;
        this.username = username;
        if ( team != null ){
            changeTeam( team );
        }
    }

    public void changeTeam(Team team) {
        this.team = team;
        team.getMembers().add( this );
    }
}
