package org.ecroze;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class CommuteGraphTest {

  @Test
  void testSolveEmptyGraph() {
    List<Main.Leg> legs = new Main.CommuteGraph(new ArrayList<>()).solve();
    assertTrue(legs.isEmpty());
  }

  @Test
  void testSolveOneCity() {
    List<Main.Leg> legs = new Main.CommuteGraph(List.of(Set.of())).solve();
    assertTrue(legs.isEmpty());
  }

  @Test
  void testSolveSeveralCitiesNoPassenger() {
    List<Main.Leg> legs = new Main.CommuteGraph(List.of(Set.of(), Set.of(), Set.of())).solve();
    assertTrue(legs.isEmpty());
  }

  @Test
  void testSolveTwoCitiesWithPassengers() {
    List<Main.Leg> legs = new Main.CommuteGraph(List.of(Set.of(1), Set.of(0))).solve();
    assertEquals(List.of(new Main.Leg(0, 1), new Main.Leg(1, 0)), legs);
  }

  @Test
  void testSolveTwoCitiesWithPassengersInOnlyOneCity() {
    List<Main.Leg> legs = new Main.CommuteGraph(List.of(Set.of(), Set.of(0))).solve();
    assertEquals(List.of(new Main.Leg(1, 0)), legs);
  }

  @Test
  void testIsSolutionEmptyGraph() {
    assertTrue(new Main.CommuteGraph(List.of()).isSolution(List.of()));
  }

  @Test
  void testIsSolutionGraphWithNoPassengersNoLegs() {
    assertTrue(new Main.CommuteGraph(List.of(Set.of())).isSolution(List.of()));
  }

  @Test
  void testIsSolutionGraphWithSeveralCitiesNoPassengersNoLegs() {
    assertTrue(new Main.CommuteGraph(List.of(Set.of(), Set.of(), Set.of())).isSolution(List.of()));
  }

  @Test
  void testIsSolutionGraphWithPassengersNoLegs() {
    assertFalse(new Main.CommuteGraph(List.of(Set.of(1), Set.of())).isSolution(List.of()));
  }

  @Test
  void testIsSolutionGraphWithTwoCitiesOneLeg() {
    assertTrue(new Main.CommuteGraph(List.of(Set.of(1), Set.of())).isSolution(List.of(new Main.Leg(0, 1))));
  }

  @Test
  void testIsSolutionGraphWithTwoCitiesOneLegRemainingPassengers() {
    assertFalse(new Main.CommuteGraph(List.of(Set.of(1), Set.of(0))).isSolution(List.of(new Main.Leg(0, 1))));
  }

  @Test
  void testIsSolutionGraphWithTwoCitiesTwoLegs() {
    assertTrue(new Main.CommuteGraph(List.of(Set.of(1), Set.of(0)))
        .isSolution(List.of(new Main.Leg(0, 1), new Main.Leg(1, 0))));
  }

  @Test
  void testIsSolutionCatapultUsedTwice() {
    assertFalse(new Main.CommuteGraph(List.of(Set.of(1, 2), Set.of(0, 2), Set.of(0, 1)))
        .isSolution(List.of(new Main.Leg(0, 1), new Main.Leg(1, 2),
            new Main.Leg(2, 0), new Main.Leg(0, 1))));
  }

  @Test
  void testIsSolutionComplexGraph() {
    assertTrue(new Main.CommuteGraph(List.of(Set.of(1, 2, 4), Set.of(2), Set.of(), Set.of(1), Set.of(0)))
        .isSolution(List.of(new Main.Leg(4, 0), new Main.Leg(0, 1),
            new Main.Leg(3, 1), new Main.Leg(1, 2), new Main.Leg(2, 4))));
  }
}
