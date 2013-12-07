package com.ziodyne.sometrpg.logic.navigation;

import com.google.common.base.Optional;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.apache.commons.lang3.tuple.Pair;

import java.util.concurrent.ExecutionException;

public class CachingPathfinder<T> implements Pathfinder<T> {
  private final DefaultPathfinder<T> defaultPathfinder;

  public CachingPathfinder(PathfindingStrategy<T> strategy) {
    defaultPathfinder = new DefaultPathfinder<T>(strategy);
  }

  private final LoadingCache<Pair<T, T>, Optional<Path<T>>> resultCache = CacheBuilder.newBuilder()
    .build(new CacheLoader<Pair<T, T>, Optional<Path<T>>>() {
      @Override
      public Optional<Path<T>> load(Pair<T, T> key) throws Exception {
        return defaultPathfinder.computePath(key.getLeft(), key.getRight());
      }
    });

  @Override
  public Optional<Path<T>> computePath(T start, T goal) {
    try {
      return resultCache.get(Pair.of(start, goal));
    } catch (ExecutionException e) {
      return Optional.absent();
    }
  }
}
