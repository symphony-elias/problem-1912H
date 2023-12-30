package org.ecroze;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class Main {

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
      if (!passengersLeft()) {
        return legs;
      }

      int rounds = 0;
      while (passengersLeft()) {
        int start = findCityWithMostPassengers(getCitiesWithPassengers());
        int destination = findCityWithMostPassengers(graph.get(start));
        catapultPassengers(start, destination);
        legs.add(new Leg(start, destination));
        rounds++;
        if (rounds == 1000) {
          break;
        }
      }
      return legs;
    }

    public boolean isSolution(List<Leg> legs) {
      for (Leg leg : legs) {
        if (isInvalidLeg(leg.start(), leg.destination())) {
          return false; // this is not a valid leg
        }
        catapultPassengers(leg.start(), leg.destination());
      }
      return !passengersLeft();
    }

    private boolean passengersLeft() {
      return graph.stream().anyMatch(s -> !s.isEmpty());
    }

    private int findCityWithMostPassengers(Set<Integer> possibleDestinations) {
      int passengerCount = -1;
      int city = -1;
      for (int destination: possibleDestinations) {
        int numberOfPassengers = graph.get(destination).size();
        if (numberOfPassengers > passengerCount) {
          passengerCount = numberOfPassengers;
          city = destination;
        }
      }
      return city;
    }

    private Set<Integer> getCitiesWithPassengers() {
      Set<Integer> cities = new HashSet<>();
      for (int i = 0; i < graph.size(); i++) {
        if (!graph.get(i).isEmpty()) {
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
        System.out.println((leg.start() + 1) + " " + (leg.destination + 1));
      }
    }
  }
}
