package com.ziodyne.sometrpg.logic.navigation;

import com.google.common.base.Optional;

public interface Pathfinder<T> {
  public Optional<Path<T>> computePath(T start, T end);
}
