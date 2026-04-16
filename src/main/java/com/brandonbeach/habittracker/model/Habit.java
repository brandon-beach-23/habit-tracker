package com.brandonbeach.habittracker.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "habits")
public class Habit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name is required")
    @Column(nullable = false)
    private String name;

    @Size(max = 500, message = "Description cannot exceed 500 characters")
    @Column(length = 500)
    private String description;

    @NotNull(message = "Frequency is required")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private HabitFrequency frequency;

    @Column(nullable = false, updatable = false)
    private LocalDate createdDate;

    @Column(nullable = false)
    private boolean active = true;

    @OneToMany(mappedBy = "habit", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<HabitCompletion> habitCompletions =  new ArrayList<>();

    public Habit() {}

    public Habit(String name, String description, HabitFrequency frequency) {
        this.name = name;
        this.description = description;
        this.frequency = frequency;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public HabitFrequency getFrequency() {
        return frequency;
    }

    public void setFrequency(HabitFrequency frequency) {
        this.frequency = frequency;
    }

    public LocalDate getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDate createdDate) {
        this.createdDate = createdDate;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @PrePersist
    protected void onCreate() {
        if (createdDate == null) {
            this.createdDate = LocalDate.now();
        }
    }
}
