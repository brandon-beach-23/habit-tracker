package com.brandonbeach.habittracker.service;

import com.brandonbeach.habittracker.model.Habit;
import com.brandonbeach.habittracker.repository.HabitRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class HabitService {

    private final HabitRepository habitRepository;

    public HabitService(HabitRepository habitRepository) {
        this.habitRepository = habitRepository;
    }

    public Habit createHabit(Habit habit) {
        return habitRepository.save(habit);
    }

    public List<Habit> getAllActiveHabits() {
        return habitRepository.findByActiveTrue();
    }

    public Optional<Habit> getHabitById(Long id) {
        return habitRepository.findById(id);
    }

    public Habit updateHabit(Habit habit) {
        return habitRepository.save(habit);
    }

    public void deactivateHabit(Long id) {
        habitRepository.findById(id).ifPresent(habit -> {
            habit.setActive(false);
            habitRepository.save(habit);
        });
    }
}
