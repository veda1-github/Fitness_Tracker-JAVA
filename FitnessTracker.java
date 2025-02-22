import java.util.*;

class User {
    private String name;
    private int age;
    private double weight;
    private double height;
    private int goalCalories;
    private int totalCaloriesBurned = 0;

    public User(String name, int age, double weight, double height, int goalCalories) {
        this.name = name;
        this.age = age;
        this.weight = weight;
        this.height = height;
        this.goalCalories = goalCalories;
    }

    public double calculateBMI() {
        return weight / (height * height);
    }

    public String getBMICategory() {
        double bmi = calculateBMI();
        if (bmi < 18.5) return "Underweight";
        else if (bmi < 24.9) return "Normal weight";
        else if (bmi < 29.9) return "Overweight";
        else return "Obese";
    }

    public void updateCaloriesBurned(int calories) {
        totalCaloriesBurned += calories;
    }

    public void displayUserInfo() {
        System.out.println("\n=== User Profile ===");
        System.out.println("Name: " + name);
        System.out.println("Age: " + age);
        System.out.println("Weight: " + weight + " kg");
        System.out.println("Height: " + height + " m");
        System.out.printf("BMI: %.2f (%s)\n", calculateBMI(), getBMICategory());
        System.out.println("Fitness Goal: " + goalCalories + " calories");
        System.out.println("Calories Burned So Far: " + totalCaloriesBurned);
        System.out.println("=====================");
    }
}

abstract class Workout {
    protected String type;
    protected int duration;
    protected int caloriesBurned;

    public Workout(String type, int duration, int caloriesBurned) {
        this.type = type;
        this.duration = duration;
        this.caloriesBurned = caloriesBurned;
    }

    public abstract void showWorkoutDetails();

    public int getCaloriesBurned() {
        return caloriesBurned;
    }
}

class CardioExercise extends Workout {
    public CardioExercise(int duration, int caloriesBurned) {
        super("Cardio", duration, caloriesBurned);
    }

    @Override
    public void showWorkoutDetails() {
        System.out.println("🏃 Cardio Workout - Duration: " + duration + " mins, Calories Burned: " + caloriesBurned);
    }
}

class StrengthExercise extends Workout {
    private String muscleGroup;

    public StrengthExercise(int duration, String muscleGroup, int caloriesBurned) {
        super("Strength", duration, caloriesBurned);
        this.muscleGroup = muscleGroup;
    }

    @Override
    public void showWorkoutDetails() {
        System.out.println("💪 Strength Workout - Duration: " + duration + " mins, Focus: " + muscleGroup + ", Calories Burned: " + caloriesBurned);
    }
}

class BMICalculator implements Runnable {
    private User user;

    public BMICalculator(User user) {
        this.user = user;
    }

    @Override
    public void run() {
        System.out.println("\n[Thread] Calculating BMI...");
        try {
            Thread.sleep(1000);
            user.displayUserInfo();
        } catch (InterruptedException e) {
            System.out.println("BMI Calculation interrupted.");
        }
    }
}

class WorkoutTracker implements Runnable {
    private User user;
    private List<Workout> workouts;

    public WorkoutTracker(User user, List<Workout> workouts) {
        this.user = user;
        this.workouts = workouts;
    }

    @Override
    public void run() {
        System.out.println("\n[Thread] Starting Workouts...");
        try {
            for (Workout workout : workouts) {
                System.out.println("\n🎵 Starting: " + workout.type + " workout...");
                Thread.sleep(1500);
                workout.showWorkoutDetails();
                user.updateCaloriesBurned(workout.getCaloriesBurned());
                System.out.println("✅ Workout Completed!");
                Thread.sleep(1500);
            }
        } catch (InterruptedException e) {
            System.out.println("Workout tracking interrupted.");
        }
    }
}

public class FitnessTracker {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        User user;
        
        try {
            System.out.print("Enter your name: ");
            String name = scanner.nextLine();

            System.out.print("Enter your age: ");
            int age = scanner.nextInt();
            System.out.print("Enter your weight (kg): ");
            double weight = scanner.nextDouble();
            System.out.print("Enter your height (m): ");
            double height = scanner.nextDouble();
            System.out.print("Set your daily calorie burn goal: ");
            int goalCalories = scanner.nextInt();

            user = new User(name, age, weight, height, goalCalories);
        } catch (Exception e) {
            System.out.println("❌ Invalid input! Please restart and enter correct values.");
            return;
        }

        List<Workout> workoutList = new ArrayList<>();
        workoutList.add(new CardioExercise(30, 300));
        workoutList.add(new StrengthExercise(40, "Upper Body", 200));

        Thread bmiThread = new Thread(new BMICalculator(user));
        Thread workoutThread = new Thread(new WorkoutTracker(user, workoutList));

        bmiThread.start();
        workoutThread.start();

        try {
            bmiThread.join();
            workoutThread.join();
        } catch (InterruptedException e) {
            System.out.println("Thread execution interrupted.");
        }

        System.out.println("\n🎉 Fitness Tracking Completed! Keep pushing towards your goals! 🔥");
        scanner.close();
    }
}
