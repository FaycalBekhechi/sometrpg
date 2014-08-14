package com.ziodyne.sometrpg.events;

import com.google.common.eventbus.Subscribe;

// Create a new FunctionalInterface just to get Guava / EventBus' @Subscribe interface
// on the actual functional method.
@FunctionalInterface
public interface EventListener<T> {
  @Subscribe
  void accept(T t);
}

