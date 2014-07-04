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

public class Driver {

  private static String[] tail(String[] args) {
    String[] result = new String[args.length - 1];
    System.arraycopy(args, 1, result, 0, result.length);
    return result;
  }

  public static void main(String[] args) throws Exception {
    if (args.length > 0) {
      if ("level".equals(args[0])) {
        DumpLevel.main(tail(args));
        return;
      } else if ("map".equals(args[0])) {
        MapMaker.main(tail(args));
        return;
      }
    }
    System.out.println("Usage: java -jar clotho.jar (level|map) [option]*");
    System.out.println("  level - print the information about the save game");
    System.out.println("  map   - create a map of the save game");
  }
}
