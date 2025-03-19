
package edu.wpi.first.wpilibj;

public class SPI {
    public enum Port {
        kOnboardCS0(0), kOnboardCS1(1), kOnboardCS2(2), kOnboardCS3(3), kMXP(4);

        public final int value;

        Port(int value) {
            this.value = value;
        }
    }
}
