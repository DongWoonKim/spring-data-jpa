package study.datajpa.entity;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestConstructor;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.repository.MemberRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.context.TestConstructor.AutowireMode.*;

@SpringBootTest
@Transactional
@Rollback(value = false)
@TestConstructor(autowireMode = ALL)
class MemberTest {

    @PersistenceContext
    EntityManager em;

    private final MemberRepository memberRepository;

    public MemberTest(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Test
    public void testEntity() {
        Team teamA = Team.builder()
                .name("teamA")
                .build();

        Team teamB = Team.builder()
                .name("teamB")
                .build();

        em.persist(teamA);
        em.persist(teamB);

        Member member1 = new Member( 10, "member1",  teamA);
        Member member2 = new Member( 20, "member2",  teamA);
        Member member3 = new Member( 30, "member3",  teamB);
        Member member4 = new Member( 40, "member4",  teamB);

        em.persist(member1);
        em.persist(member2);
        em.persist(member3);
        em.persist(member4);

        // 초기화
        em.flush();
        em.clear();

        // 확인
        List<Member> members = em.createQuery("select m from Member m", Member.class)
                .getResultList();

        for (Member member : members) {
            System.out.println( "member = " + member );
            System.out.println( "-> member.team = " + member.getTeam() );
        }

    }

    @Test
    public void JpaEventBaseEntity() throws InterruptedException {

        // given
        Member member = new Member("member1");
        memberRepository.save(member); // @PrePersist

        Thread.sleep(100);
        member.setUsername("member2");

        em.flush();
        em.clear();

        // when
        Member findMember = memberRepository.findById(member.getId()).get();

        // then
        System.out.println("find member getCreatedDate = " + findMember.getCreatedDate());
        System.out.println("find member getUpdatedDate = " + findMember.getLastModifiedDate());
        System.out.println("find member getCreatedBy = " + findMember.getCreatedBy());
        System.out.println("find member getLastModifiedBy = " + findMember.getLastModifiedBy());

    }

}