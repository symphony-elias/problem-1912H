package org.ecroze;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class CommuteGraphTest {

  @Test
  void testEmptyGraph() {
    List<Main.Leg> legs = new Main.CommuteGraph(new ArrayList<>()).solve();
    assertTrue(legs.isEmpty());
  }

  @Test
  void testOneCity() {
    List<Main.Leg> legs = new Main.CommuteGraph(List.of(Set.of())).solve();
    assertTrue(legs.isEmpty());
  }

  @Test
  void testSeveralCitiesNoPassenger() {
    List<Main.Leg> legs = new Main.CommuteGraph(List.of(Set.of(), Set.of(), Set.of())).solve();
    assertTrue(legs.isEmpty());
  }
}
