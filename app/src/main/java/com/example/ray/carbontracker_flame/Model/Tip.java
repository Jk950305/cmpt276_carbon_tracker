package com.example.ray.carbontracker_flame.Model;

import java.util.Comparator;
import java.util.Objects;

/**
 * Tip holds a message and a score
 */

public class Tip {
    private String message;
    private double score;

    public Tip(String message, double score) {
        this.message = message;
        this.score = score;
    }

    public String getMessage() {
        return this.message;
    }

    public double getScore() {
        return this.score;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Tip)) {
            return false;
        }
        Tip t = (Tip) o;
        return (Objects.equals(t.message, this.message));
    }

    public int compareTo(Tip tip) {
        return (int) (tip.getScore() * 100 - this.getScore() * 100);
    }
}
