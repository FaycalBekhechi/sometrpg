package com.ziodyne.sometrpg.logic.loader;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import com.google.common.io.Resources;
import com.ziodyne.sometrpg.logic.loader.models.CharacterSpec;
import org.junit.Assert;
import org.junit.Test;

public class CharacterLoaderTest {
  @Test
  public void testLoadCharacters() throws Exception {
    CharacterLoader loader = new CharacterLoader();

    URL url = Resources.getResource("marth.json");
    Path path = Paths.get(url.toURI());
    List<CharacterSpec> specs = loader.loadCharacters(Files.newInputStream(path));

    Assert.assertNotNull(specs);
    Assert.assertEquals(1, specs.size());

    CharacterSpec marth = specs.get(0);
    Assert.assertEquals("Marth", marth.getName());
    Assert.assertNotNull(marth.getGrowths());
    Assert.assertNotNull(marth.getStats());
  }
}
