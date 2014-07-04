/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.omalley.clotho;

import com.github.omalley.clotho.NBT.IO;
import com.github.omalley.clotho.NBT.Tag;
import com.google.gson.stream.JsonWriter;
import com.martiansoftware.jsap.JSAP;
import com.martiansoftware.jsap.JSAPResult;
import com.martiansoftware.jsap.Switch;
import com.martiansoftware.jsap.UnflaggedOption;

import java.io.File;
import java.io.FileInputStream;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.util.Iterator;

/**
 * Print a save game's level file.
 */
public class DumpLevel {

  public static void main(String[] args) throws Exception {
    JSAP jsap = new JSAP();
    String HOME = System.getProperty("user.home");
    jsap.registerParameter(new Switch("typed", 't', "typed",
        "annotate each object with its type"));
    jsap.registerParameter(new Switch("help", 'h', "help",
        "provide help"));
    jsap.registerParameter(new UnflaggedOption("directory", JSAP.STRING_PARSER,
        HOME + "/Library/Application Support/minecraft/saves/Creative", true,
        true, "list of save directories"));
    JSAPResult options = jsap.parse(args);

    if (options.success() && !options.getBoolean("help")) {
      for (String arg : options.getStringArray("directory")) {
        try (FileInputStream fis =
                 new FileInputStream(new File(arg, "level.dat"))) {
          Tag.Compound level = IO.Read(fis);
          JsonWriter writer = new JsonWriter(new PrintWriter(System.out));
          writer.setIndent("  ");
          level.writeJson(writer, options.getBoolean("typed"));
          writer.flush();
        }
        System.out.println();
      }
    } else {
      for(Iterator errs = options.getErrorMessageIterator(); errs.hasNext(); ) {
        System.err.println("Error: " + errs.next());
      }
      System.err.println("Usage: level " + jsap.getUsage());
      System.err.println();
      System.err.println(jsap.getHelp());
      System.exit(1);
    }
  }
}
