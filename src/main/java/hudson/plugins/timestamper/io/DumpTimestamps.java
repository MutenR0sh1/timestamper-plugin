/*
 * The MIT License
 * 
 * Copyright (c) 2013 Steven G. Brown
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package hudson.plugins.timestamper.io;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import com.google.common.base.Joiner;
import com.google.common.io.Files;

/**
 * Debugging tool which outputs the contents of a timestamper directory.
 * 
 * @author Steven G. Brown
 */
public final class DumpTimestamps {

  /**
   * Read the values from the timestamper directory path given by the
   * command-line arguments and output these values to the console. This is
   * intended only for debugging. It is not invoked by Jenkins.
   * 
   * @param args
   *          the command-line arguments, expected to contain a timestamper
   *          directory path
   * @throws IOException
   */
  public static void main(String... args) throws IOException {
    if (args.length == 0) {
      throw new IllegalArgumentException("no command-line arguments");
    }
    File timestamperDir = new File(Joiner.on(' ').join(args));
    System.out.println("timestamps");
    dump(TimestampsWriter.timestampsFile(timestamperDir), 1, System.out);
    File timeShiftsFile = TimestampsReader.timeShiftsFile(timestamperDir);
    if (timeShiftsFile.isFile()) {
      System.out.println("timeshifts");
      dump(timeShiftsFile, 2, System.out);
    }
  }

  private static void dump(File file, int columns, PrintStream output)
      throws IOException {
    final byte[] fileContents = Files.toByteArray(file);
    TimestampsReader.InputStreamByteReader byteReader = new TimestampsReader.InputStreamByteReader(
        new ByteArrayInputStream(fileContents));
    List<Long> values = new ArrayList<Long>();
    while (byteReader.bytesRead < fileContents.length) {
      values.add(Varint.read(byteReader));
      if (values.size() == columns) {
        output.println(Joiner.on('\t').join(values));
        values.clear();
      }
    }
    if (!values.isEmpty()) {
      output.println(Joiner.on('\t').join(values));
    }
    output.println();
  }

  private DumpTimestamps() {
  }
}
