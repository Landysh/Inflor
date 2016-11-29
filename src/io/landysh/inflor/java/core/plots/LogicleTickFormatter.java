package io.landysh.inflor.java.core.plots;

public class LogicleTickFormatter {

  /**
   * @param value - an integer wholly divisble by 10.
   * @return Tick label such as "10eN"
   */
  public static String findLogTick(int value) {
    return "10" + "e" + Integer.toString((int) Math.log10(value));
  }
}