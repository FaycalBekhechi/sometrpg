package com.ziodyne.sometrpg.logic.loader;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ziodyne.sometrpg.logic.loader.models.CharacterSpec;
import com.ziodyne.sometrpg.logic.loader.models.Characters;

public class CharacterLoader {
  /**
   * Load character POJOs from a file
   * @param path The path to the file
   * @return A list of {@link com.ziodyne.sometrpg.logic.loader.models.CharacterSpec} loaded from the file.
   * @throws IOException
   */
  public List<CharacterSpec> loadCharacters(String path) throws IOException {

    return loadCharacters(new FileInputStream(new File(path)));
  }

  List<CharacterSpec> loadCharacters(InputStream inputStream) throws IOException {

    ObjectMapper mapper = new ObjectMapper().setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
    return mapper.readValue(inputStream, Characters.class).getCharacters();
  }
}
