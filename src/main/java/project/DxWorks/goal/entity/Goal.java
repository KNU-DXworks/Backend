package project.DxWorks.goal.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
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

    @Column(nullable = false)
    private Double weight;

    @Column(nullable = false)
    private Double muscle;

    @Column(nullable = false)
    private Double fat;

    @Column(nullable = false)
    private Double bmi;

    @Column(nullable = false)
    private Double arm;

    @Column(nullable = false)
    private Double body;

    @Column(nullable = false)
    private Double leg;

    @Column(name = "goal_group", nullable = false)
    private Double goalGroup;
}
