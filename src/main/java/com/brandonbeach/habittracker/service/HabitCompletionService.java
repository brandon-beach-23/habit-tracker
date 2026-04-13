package com.brandonbeach.habittracker.service;

import com.brandonbeach.habittracker.model.Habit;
import com.brandonbeach.habittracker.model.HabitCompletion;
import com.brandonbeach.habittracker.repository.HabitCompletionRepository;
import com.brandonbeach.habittracker.repository.HabitRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class HabitCompletionService {

    private final HabitCompletionRepository habitCompletionRepository;
    private final HabitRepository habitRepository;

    public HabitCompletionService(HabitCompletionRepository habitCompletionRepository, HabitRepository habitRepository){
        this.habitCompletionRepository = habitCompletionRepository;
        this.habitRepository = habitRepository;
    }

    public boolean completeHabitForToday(Long habitId){
        Habit habit = habitRepository.findById(habitId).orElseThrow(() -> new IllegalArgumentException("Habit not found"));

        LocalDate today = LocalDate.now();

        boolean alreadyCompleted = habitCompletionRepository.findByHabitAndCompletionDate(habit, today).isPresent();

        if(alreadyCompleted){
            return false;
        }

        HabitCompletion habitCompletion = new HabitCompletion(habit, today);
        habitCompletionRepository.save(habitCompletion);
        return true;
    }

    public List<HabitCompletion> getCompletionsForHabit(Habit habit){
        return habitCompletionRepository.findByHabitOrderByCompletionDate(habit);
    }

    public int calculateStreak(Long habitId){
        Habit habit = habitRepository.findById(habitId).orElseThrow(() -> new IllegalArgumentException("Habit not found"));
        List<HabitCompletion> completions =  habitCompletionRepository.findByHabitOrderByCompletionDate(habit);

        if(completions.isEmpty()){
            return 0;
        }

        Set<LocalDate> completionDates = completions.stream()
                .map(HabitCompletion::getCompletionDate)
                .collect(Collectors.toSet());

        LocalDate today = LocalDate.now();
        int streak = 0;

        while(completionDates.contains(today.minusDays(streak))){
            streak++;
        }
        return streak;
    }
}
