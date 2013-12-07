package com.ziodyne.sometrpg.logic.navigation;

import com.google.common.base.Optional;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.apache.commons.lang3.tuple.Pair;

import java.util.concurrent.ExecutionException;

public class CachingPathfinder<T> extends DefaultPathfinder<T> {
  public CachingPathfinder(PathfindingStrategy<T> strategy) {
    super(strategy);
  }

  private final LoadingCache<Pair<T, T>, Optional<Path<T>>> resultCache = CacheBuilder.newBuilder()
    .build(new CacheLoader<Pair<T, T>, Optional<Path<T>>>() {
      @Override
      public Optional<Path<T>> load(Pair<T, T> key) throws Exception {
        return CachingPathfinder.super.computePath(key.getLeft(), key.getRight());
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
