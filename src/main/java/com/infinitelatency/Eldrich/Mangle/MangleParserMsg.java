/* Licensed under Apache-2.0 */
package com.infinitelatency.Eldrich.Mangle;

import com.github.rodionmoiseev.c10n.annotations.En;

public interface MangleParserMsg {
  @En("Packages cannot be used with mangled Java code")
  String noPackages();

  @En("Body Declaration at LINE:{0}, COL:{1} was not recognised.\n"
      + "This is very alpha, and I have missed a case")
  String absentBodyCase(int line, int col);
}
