package com.brandonbeach.habittracker.config;

import com.brandonbeach.habittracker.model.Habit;
import com.brandonbeach.habittracker.model.HabitCompletion;
import com.brandonbeach.habittracker.model.HabitFrequency;
import com.brandonbeach.habittracker.repository.HabitCompletionRepository;
import com.brandonbeach.habittracker.repository.HabitRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class DataInitializer implements CommandLineRunner {

    private final HabitRepository habitRepository;
    private final HabitCompletionRepository habitCompletionRepository;

    public DataInitializer(HabitRepository habitRepository,
                           HabitCompletionRepository habitCompletionRepository) {
        this.habitRepository = habitRepository;
        this.habitCompletionRepository = habitCompletionRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        // Only seed if no data exists
        if (habitRepository.count() > 0) return;

        // Create habits and completions here
        LocalDate today = LocalDate.now();

        // --- Coding Practice — full 7 day streak ---
        Habit coding = new Habit("Coding Practice", "Daily coding practice", HabitFrequency.DAILY);
        coding.setCreatedDate(today.minusDays(7));
        habitRepository.save(coding);

        for (int i = 6; i >= 0; i--) {
            habitCompletionRepository.save(new HabitCompletion(coding, today.minusDays(i)));
        }

        // --- Workout — broken at day 3 ---
        // Last completion was 3 days ago, nothing since, so streak is 0
        Habit workout = new Habit("Workout", "Daily workout", HabitFrequency.DAILY);
        workout.setCreatedDate(today.minusDays(7));
        habitRepository.save(workout);

        for (int i = 6; i >= 3; i--) {
            habitCompletionRepository.save(new HabitCompletion(workout, today.minusDays(i)));
        }

        // ---Read 30 Minutes - Full 7 days
        Habit reading = new Habit("Reading", "Daily reading for 30 minutes", HabitFrequency.DAILY);
        reading.setCreatedDate(today.minusDays(7));
        habitRepository.save(reading);

        for (int i = 6; i >= 0; i--) {
            habitCompletionRepository.save(new HabitCompletion(reading, today.minusDays(i)));
        }

        // --- Stretching — Only completed yesterday ---

        Habit stretching = new Habit("Stretching", "Daily stretches", HabitFrequency.DAILY);
        stretching.setCreatedDate(today.minusDays(7));
        habitRepository.save(stretching);

        habitCompletionRepository.save(new HabitCompletion(stretching, today.minusDays(1)));

        // New Habit - Walking
        Habit walking = new Habit("Walking", "Outside walk with the dog", HabitFrequency.DAILY);
        walking.setCreatedDate(today);
        habitRepository.save(walking);
    }
}
