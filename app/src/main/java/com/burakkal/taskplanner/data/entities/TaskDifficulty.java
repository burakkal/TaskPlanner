package com.burakkal.taskplanner.data.entities;

public enum TaskDifficulty {
    VERY_EASY(1),
    EASY(2),
    NORMAL(3),
    HARD(4),
    VERY_HARD(5),
    FIENDISH(6);

    private int value;

    TaskDifficulty(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    @Override
    public String toString() {
        switch (value) {
            case 1:
                return "Very Easy";
            case 2:
                return "Easy";
            case 3:
                return "Normal";
            case 4:
                return "Hard";
            case 5:
                return "Very Hard";
            case 6:
                return "Fiendish";
        }
        throw new IllegalArgumentException();
    }
}
