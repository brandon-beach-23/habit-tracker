package com.brandonbeach.habittracker.model;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(
        name = "habit_completions",
        uniqueConstraints = {
            @UniqueConstraint(columnNames = {"habit_id", "completion_date"})
        }
)
public class HabitCompletion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "habit_id", nullable = false)
    private Habit habit;

    @Column(nullable = false)
    private LocalDate completionDate;

    protected HabitCompletion() {}

    public HabitCompletion(Habit habit, LocalDate completionDate) {
        this.habit = habit;
        this.completionDate = completionDate;
    }

    public Long getId() {
        return id;
    }

    public LocalDate getCompletionDate() {
        return completionDate;
    }

    public Habit getHabit() {
        return habit;
    }
}
