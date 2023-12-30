package org.ecroze;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class Main {

  public record Leg(int start, int destination) {
  }

  public static class CommuteGraph {

    // list index is the city number, set is the passengers with their desired destination.
    private final List<Set<Integer>> graph;
    private final Set<Integer> usedCatapults;

    public static CommuteGraph parseGraph() {
      Scanner scanner = new Scanner(System.in);
      int numberOfCities = scanner.nextInt();
      int numberOfPassengers = scanner.nextInt();

      List<Set<Integer>> graph = new ArrayList<>(numberOfCities);
      for (int i = 0; i < numberOfCities; i++) {
        graph.add(new HashSet<>());
      }

      for (int i = 0; i < numberOfPassengers; i++) {
        // cities are indexed starting from 1
        int departure = scanner.nextInt() - 1;
        int arrival = scanner.nextInt() - 1;
        graph.get(departure).add(arrival);
      }
      return new CommuteGraph(graph);
    }

    public CommuteGraph(List<Set<Integer>> commuteGraph) {
      this(commuteGraph, new HashSet<>());
    }

    public CommuteGraph(List<Set<Integer>> commuteGraph, Set<Integer> usedCatapults) {
      // We want to have a deep copy
      this.graph = new ArrayList<>(commuteGraph.size());
      for (Set<Integer> integers : commuteGraph) {
        this.graph.add(new HashSet<>(integers));
      }
      this.usedCatapults = new HashSet<>(usedCatapults);
    }

    public CommuteGraph(CommuteGraph commuteGraph) {
      this(commuteGraph.graph, commuteGraph.usedCatapults);
    }

    public boolean isSolution(List<Leg> legs) {
      for (Leg leg : legs) {
        if (isInvalidLeg(leg.start(), leg.destination())) {
          return false; // having an invalid leg leads to an invalid solution
        }
        catapultPassengers(leg.start(), leg.destination());
      }
      return noPassengersLeft(); // valid solution if all passengers arrived at their destination
    }

    private boolean noPassengersLeft() {
      return graph.stream().allMatch(Set::isEmpty);
    }

    public List<Leg> solve() {
      if (noPassengersLeft()) {
        return List.of(); // no passengers to transport: already solved
      }

      Set<Leg> allPossibleLegs = getAllPossibleLegs();
      for (Leg possibleLeg : allPossibleLegs) {
        List<Leg> legs = new CommuteGraph(this).recursiveSolve(possibleLeg, new ArrayList<>());
        if (legs != null) {
          return legs;
        }
      }
      return null; // no solution found
    }

    private List<Leg> recursiveSolve(Leg leg, List<Leg> initialLegs) {
      initialLegs.add(leg);
      catapultPassengers(leg.start(), leg.destination());
      if (noPassengersLeft()) {
        return initialLegs; // we found a solution
      }

      Set<Leg> allPossibleLegs = getAllPossibleLegs();
      for (Leg possibleLeg : allPossibleLegs) {
        List<Leg> attempt = new CommuteGraph(this).recursiveSolve(possibleLeg, new ArrayList<>(initialLegs));
        if (attempt != null) {
          return attempt;
        }
      }
      return null; // no solution found
    }

    private Set<Integer> getCitiesWithPassengers() {
      Set<Integer> cities = new HashSet<>();
      for (int i = 0; i < graph.size(); i++) {
        if (!graph.get(i).isEmpty() && !usedCatapults.contains(i)) {
          cities.add(i);
        }
      }
      return cities;
    }

    private Set<Integer> getCitiesWithPassengersOrInDestination(int start, Set<Integer> destinations) {
      Set<Integer> cities = new HashSet<>();
      for (int i = 0; i < graph.size(); i++) {
        if (i != start && (!graph.get(i).isEmpty() || destinations.contains(i))) {
          cities.add(i);
        }
      }
      return cities;
    }

    private void catapultPassengers(int start, int destination) {
      if (isInvalidLeg(start, destination)) {
        throw new IllegalStateException("Catapult already used or destination same as start");
      }
      usedCatapults.add(start);

      Set<Integer> destinations = graph.get(start);
      destinations.remove(destination); // remove passengers which arrive at wanted destination
      graph.get(destination).addAll(destinations); // we keep all other passengers
      destinations.clear(); // we remove passengers from the former city
    }

    private boolean isInvalidLeg(int start, int destination) {
      return start == destination || usedCatapults.contains(start);
    }

    private Set<Leg> getAllPossibleLegs() {
      Set<Leg> possibleLegs = new HashSet<>();
      Set<Integer> starts = getCitiesWithPassengers();
      for (Integer start : starts) {
        Set<Integer> destinations = getCitiesWithPassengersOrInDestination(start, graph.get(start));
        for (Integer destination : destinations) {
          if (!isInvalidLeg(start, destination)) { // we filter out invalid legs
            possibleLegs.add(new Leg(start, destination));
          }
        }
      }
      return possibleLegs;
    }
  }

  public static void main(String[] args) {
    CommuteGraph graph = CommuteGraph.parseGraph();
    List<Leg> legs = graph.solve();

    if (legs == null) {
      System.out.println("-1");
    } else {
      System.out.println(legs.size());
      for (Leg leg : legs) {
        System.out.println((leg.start() + 1) + " " + (leg.destination + 1));
      }
    }
  }
}
