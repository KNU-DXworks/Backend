package project.DxWorks.goal.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Goal {

    @Id
    @GeneratedValue
    @Column(name = "goal_id")
    private Long goalId;

    @Column(name = "created_date")
    private LocalDateTime createdDate;

    private Double weight;

    private Double muscle;

    private Double fat;

    private Double bmi;

    private Double arm;

    private Double body;

    private Double leg;

    @Column(name = "goal_group")
    private Double goalGroup;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
