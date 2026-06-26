import edu.princeton.cs.algs4.StdRandom;

import java.awt.*;
import java.util.Map;

public class Particle {
    public ParticleFlavor flavor;
    public int lifespan;

    public static final int PLANT_LIFESPAN = 150;
    public static final int FLOWER_LIFESPAN = 75;
    public static final int FIRE_LIFESPAN = 10;
    public static final Map<ParticleFlavor, Integer> LIFESPANS =
            Map.of(ParticleFlavor.FLOWER, FLOWER_LIFESPAN,
                   ParticleFlavor.PLANT, PLANT_LIFESPAN,
                   ParticleFlavor.FIRE, FIRE_LIFESPAN);

    public Particle(ParticleFlavor flavor) {
        this.flavor = flavor;
        if (LIFESPANS.containsKey(flavor)) {
            lifespan = LIFESPANS.get(flavor);
        } else {
            lifespan = -1;
        }
    }

    public Color color() {
        if (flavor == ParticleFlavor.EMPTY) {
            return Color.BLACK;
        }
        else if (flavor == ParticleFlavor.SAND){
            return Color.YELLOW;
        }
        else if (flavor == ParticleFlavor.BARRIER){
            return Color.GRAY;
        }
        else if (flavor == ParticleFlavor.WATER){
            return Color.BLUE;
        }
        else if (flavor == ParticleFlavor.FOUNTAIN){
            return Color.CYAN;
        }
        else if (flavor == ParticleFlavor.FLOWER) {
            double ratio = (double) Math.max(0, Math.min(lifespan, FLOWER_LIFESPAN)) / FLOWER_LIFESPAN;
            int r = 120 + (int) Math.round((255 - 120) * ratio);
            int g = 70 + (int) Math.round((141 - 70) * ratio);
            int b = 80 + (int) Math.round((161 - 80) * ratio);
            return new Color(r, g, b);
        }
        else if (flavor == ParticleFlavor.PLANT) {
            double ratio = (double) Math.max(0, Math.min(lifespan, PLANT_LIFESPAN)) / PLANT_LIFESPAN;
            int g = 120 + (int) Math.round((255 - 120) * ratio);
            return new Color(0, g, 0);
        }
        else if (flavor == ParticleFlavor.FIRE) {
            double ratio = (double) Math.max(0, Math.min(lifespan, FIRE_LIFESPAN)) / FIRE_LIFESPAN;
            int r = (int) Math.round(255 * ratio);
            return new Color(r, 0, 0);
        }
        return Color.white;
    }

    public void moveInto(Particle other) {
        other.flavor = flavor;
        other.lifespan = lifespan;
        flavor = ParticleFlavor.EMPTY;
        lifespan = -1;
    }

    public void fall(Map<Direction, Particle> neighbors) {
        Particle p = neighbors.get(Direction.DOWN);
        if (p.flavor == ParticleFlavor.EMPTY) {
            moveInto(p);
        }
    }

    public void flow(Map<Direction, Particle> neighbors) {
        int roll = StdRandom.uniformInt(3);
        if (roll == 0) {
            return;
        }
        else if (roll == 1) {
            Particle left = neighbors.get(Direction.LEFT);
            if (left.flavor == ParticleFlavor.EMPTY) {
                moveInto(left);
            }
        }
        else {
                Particle right = neighbors.get(Direction.RIGHT);
                if (right.flavor == ParticleFlavor.EMPTY) {
                    moveInto(right);
                }
        }
    }

    public void grow(Map<Direction, Particle> neighbors) {
        int num = StdRandom.uniformInt(10);
        if (num == 0){
            Particle up = neighbors.get(Direction.UP);
            if (up.flavor == ParticleFlavor.EMPTY) {
                up.flavor = flavor;
                if (LIFESPANS.containsKey(flavor)) {
                    up.lifespan = LIFESPANS.get(flavor);
                } else {
                    up.lifespan = -1;
                }
            }
        }
        else if (num == 1){
            Particle left = neighbors.get(Direction.LEFT);
            if (left.flavor == ParticleFlavor.EMPTY) {
                left.flavor = flavor;
                if (LIFESPANS.containsKey(flavor)) {
                    left.lifespan = LIFESPANS.get(flavor);
                } else {
                    left.lifespan = -1;
                }
            }
        }
        else if (num == 2){
            Particle right = neighbors.get(Direction.RIGHT);
            if (right.flavor == ParticleFlavor.EMPTY) {
                right.flavor = flavor;
                if (LIFESPANS.containsKey(flavor)) {
                    right.lifespan = LIFESPANS.get(flavor);
                } else {
                    right.lifespan = -1;
                }
            }
        }
        else{
            return;
        }
    }

    public void burn(Map<Direction, Particle> neighbors) {
        boolean isBurn = false;
        Particle up = neighbors.get(Direction.UP);
        Particle down = neighbors.get(Direction.DOWN);
        Particle left = neighbors.get(Direction.LEFT);
        Particle right = neighbors.get(Direction.RIGHT);
        if (up.flavor == ParticleFlavor.FLOWER || up.flavor == ParticleFlavor.PLANT){
            isBurn = StdRandom.bernoulli(0.4);
            if (isBurn){
                up.flavor = ParticleFlavor.FIRE;
                up.lifespan = FIRE_LIFESPAN;
            }
        }
        if (down.flavor == ParticleFlavor.FLOWER || down.flavor == ParticleFlavor.PLANT){
            isBurn = StdRandom.bernoulli(0.4);
            if (isBurn){
                down.flavor = ParticleFlavor.FIRE;
                down.lifespan = FIRE_LIFESPAN;
            }
        }
        if (left.flavor == ParticleFlavor.FLOWER || left.flavor == ParticleFlavor.PLANT){
            isBurn = StdRandom.bernoulli(0.4);
            if (isBurn){
                left.flavor = ParticleFlavor.FIRE;
                left.lifespan = FIRE_LIFESPAN;
            }
        }
        if (right.flavor == ParticleFlavor.FLOWER || right.flavor == ParticleFlavor.PLANT){
            isBurn = StdRandom.bernoulli(0.4);
            if (isBurn){
                right.flavor = ParticleFlavor.FIRE;
                right.lifespan = FIRE_LIFESPAN;
            }
        }
    }

    public void action(Map<Direction, Particle> neighbors) {
        if (flavor == ParticleFlavor.EMPTY){
            return;
        }
        if (flavor != ParticleFlavor.BARRIER){
            fall(neighbors);
        }
        if (flavor == ParticleFlavor.WATER) {
            flow(neighbors);
        }
        if (flavor == ParticleFlavor.PLANT || flavor == ParticleFlavor.FLOWER) {
            grow(neighbors);
        }
        if (flavor == ParticleFlavor.FIRE){
            burn(neighbors);
        }
    }
    public void decrementLifespan(){
        if (lifespan > 0){
            lifespan--;
        }
        if (lifespan == 0){
            flavor = ParticleFlavor.EMPTY;
            lifespan = -1;
        }
    }
}