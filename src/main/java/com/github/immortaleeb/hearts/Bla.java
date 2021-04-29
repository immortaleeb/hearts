package com.github.immortaleeb.hearts;

import java.util.Deque;
import java.util.LinkedList;
import java.util.Queue;

public class Bla {

    public static void main(String[] args) {
        Queue<Integer> queue = new LinkedList<>();

        queue.add(1);
        queue.add(2);
        queue.add(3);

        System.out.println(queue.poll());
        System.out.println(queue.poll());
        System.out.println(queue.poll());
    }
}
