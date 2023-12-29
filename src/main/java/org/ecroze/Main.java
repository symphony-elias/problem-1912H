package org.ecroze;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class Main {

  public static class CommuteGraph {

    private List<Set<Integer>> graph;

    public static CommuteGraph parseGraph() {
      Scanner scanner = new Scanner(System.in);
      int numberOfCities = scanner.nextInt();
      int numberOfPassengers = scanner.nextInt();

      List<Set<Integer>> graph = new ArrayList<>(numberOfCities);
      for (int i = 0; i < numberOfCities; i++) {
        graph.add(new HashSet<>());
      }

      for (int i = 1; i < numberOfPassengers; i++) {
        // cities are indexed starting from 1
        int departure = scanner.nextInt() - 1;
        int arrival = scanner.nextInt() - 1;
        graph.get(departure).add(arrival);
      }
      return new CommuteGraph(graph);
    }

    public CommuteGraph(List<Set<Integer>> graph) {
      this.graph = graph;
    }

    public List<Leg> solve() {
      return new ArrayList<>();
    }
  }

  public record Leg(int start, int destination) {
  }

  public static void main(String[] args) {
    CommuteGraph graph = CommuteGraph.parseGraph();
    List<Leg> legs = graph.solve();

    if (legs.isEmpty()) {
      System.out.println("-1");
    } else {
      System.out.println(legs.size());
      for (Leg leg : legs) {
        System.out.println(leg.start + " " + leg.destination);
      }
    }
  }
}
