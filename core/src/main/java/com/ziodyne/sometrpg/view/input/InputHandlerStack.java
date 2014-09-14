package com.ziodyne.sometrpg.view.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;

import java.util.LinkedList;

/**
 * This class manages multiplexing through a stack of chained {@link InputProcessor}, providing a stack-like
 * interface.
 */
public class InputHandlerStack {
  private final InputMultiplexer muxer = new InputMultiplexer();
  private final LinkedList<InputProcessor> queue = new LinkedList<>();

  public void enable() {
    Gdx.input.setInputProcessor(muxer);
  }

  public void push(InputProcessor processor) {
    queue.add(processor);
    muxer.addProcessor(processor);
  }

  public InputProcessor pop() {
    InputProcessor first = queue.removeFirst();
    muxer.removeProcessor(first);
    return first;
  }
}
