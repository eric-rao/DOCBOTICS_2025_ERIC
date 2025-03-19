package robotlib;

public class Util {

    public static final double toRad = (double) Math.PI / 180f;
    public static final double toDeg = 1f / toRad;
    public static final double PI = (double) Math.PI;

    public static double clamp(double val, double min, double max) {
        return Math.max(min, Math.min(val, max));
    }

    public static boolean sameSign(double... n) {
        boolean sign = n[0] >= 0 ? true : false;

        for (int i = 1; i < n.length; i++)
            if ((sign && n[i] < 0) || (!sign && n[i] >= 0))
                return false;
        return true;
    }

    public static double round(double a) {
        return ((int) (a * 100f)) / 100f;
    }

    public static float clamp(float val, float min, float max) {
        return Math.max(min, Math.min(val, max));
    }

    public static boolean sameSign(float... n) {
        boolean sign = n[0] >= 0 ? true : false;

        for (int i = 1; i < n.length; i++)
            if ((sign && n[i] < 0) || (!sign && n[i] >= 0))
                return false;
        return true;
    }

    public static float round(float a) {
        return ((int) (a * 100f)) / 100f;
    }
}
