package com.ziodyne.sometrpg.logic.navigation;

import java.util.Optional;

public interface Pathfinder<T> {
  public Optional<Path<T>> computePath(T start, T end);
}
