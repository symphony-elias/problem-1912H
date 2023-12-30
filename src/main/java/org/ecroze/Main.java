package org.ecroze;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class Main {

  public static class CommuteGraph {

    // list index is the city number, set is the passengers with their desired destination.
    private List<Set<Integer>> graph;
    private Set<Integer> usedCatapults;

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

    public CommuteGraph(List<Set<Integer>> commuteGraph) {
      // We want to have a deep copy
      this.graph = new ArrayList<>(commuteGraph.size());
      for (Set<Integer> integers : commuteGraph) {
        this.graph.add(new HashSet<>(integers));
      }
      this.usedCatapults = new HashSet<>();
    }

    public CommuteGraph(CommuteGraph commuteGraph) {
      this(commuteGraph.graph);
    }

    public List<Leg> solve() {
      ArrayList<Leg> legs = new ArrayList<>();
      for (int city = 0; city < graph.size(); city++) {
        Set<Integer> passengerDestinations = graph.get(city);
        if (!passengerDestinations.isEmpty()) {
          int destination = passengerDestinations.stream().findAny().get();
          legs.add(new Leg(city, destination));
        }
      }
      return legs;
    }

    public boolean isSolution(List<Leg> legs) {
      for (Leg leg : legs) {
        if (usedCatapults.contains(leg.start())) {
          return false; // catapult already used
        }
        usedCatapults.add(leg.start());

        Set<Integer> destinations = graph.get(leg.start());
        destinations.remove(leg.destination()); // remove passengers which arrive at wanted destination
        graph.get(leg.destination()).addAll(destinations); // we keep all other passengers
        destinations.clear(); // we remove passengers from the former city
      }
      return graph.stream().allMatch(Set::isEmpty);
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
