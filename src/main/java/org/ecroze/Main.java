package org.ecroze;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class Main {

  public static void main(String[] args) {
    List<Set<Integer>> graph = getGraph();

    System.out.println("Graph: " + graph);
  }

  private static List<Set<Integer>> getGraph() {
    Scanner scanner = new Scanner(System.in);
    int numberOfCities = scanner.nextInt();
    int numberOfPassengers = scanner.nextInt();

    // cities are indexed starting from 1. First element is useless
    List<Set<Integer>> graph = new ArrayList<>(numberOfCities + 1);
    for (int i = 0; i < numberOfCities + 1; i++) {
      graph.add(new HashSet<>());
    }

    for (int i = 1; i <= numberOfPassengers; i++) {
      int departure = scanner.nextInt();
      int arrival = scanner.nextInt();
      graph.get(departure).add(arrival);
    }
    return graph;
  }
}
