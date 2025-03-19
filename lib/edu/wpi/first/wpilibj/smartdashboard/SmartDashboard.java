package edu.wpi.first.wpilibj.smartdashboard;

import java.util.Arrays;
import java.util.TreeMap;
import java.util.Map;
import javax.swing.table.DefaultTableModel;

public class SmartDashboard {
    private static TreeMap<String, String> entries = new TreeMap<String, String>();
    private static boolean changed = false;
    private static int longestStrLen = 0;
    private static int untitledDataCount = 1;

    public static void putBoolean(String key, boolean data) {
        putString(key, data ? "True" : "False");
    }

    public static void putString(String key, String data) {
        changed = true;
        longestStrLen = Math.max(key.length(), longestStrLen);
        entries.put(key, data);
    }

    public static void putNumber(String key, Object data) {
        putString(key, data.toString());
    }

    public static void putData(String key, Object data) {
        putString(key, data.toString());
    }

    public static void putData(Object data) {
        putString("Untitled " + untitledDataCount++, data.toString());
    }

    public static void putNumberArray(String key, double[] data) {
        putString(key, Arrays.toString(data));
    }

    public static void putBooleanArray(String key, boolean[] data) {
        putString(key, Arrays.toString(data));
    }

    public static void putDataArray(String key, Object[] data) {
        putString(key, Arrays.toString(data));
    }

    public static TreeMap<String, String> getTreeMap() {
        changed = false;
        return entries;
    }

    public static boolean isChanged() {
        return changed;
    }

    public static int getLongestStrLen() {
        return longestStrLen;
    }

    public static DefaultTableModel getDefaultTableModel() {
        changed = false;
        DefaultTableModel result = new DefaultTableModel(new String[] { "Key", "Value" }, entries.size());
        int i = 0;
        for (Map.Entry<String, String> entry : entries.entrySet()) {
            result.setValueAt(entry.getKey(), i, 0);
            result.setValueAt(entry.getValue(), i, 1);
            i++;
        }
        return result;
    }

    public static String[][] getStringArray() {
        changed = false;
        String[][] result = new String[entries.size()][2];
        int i = 0;
        for (Map.Entry<String, String> entry : entries.entrySet()) {
            result[i][0] = entry.getKey();
            result[i][1] = entry.getValue();
            i++;
        }
        return result;
    }

    public static void main(String[] args) {
        putNumberArray("asdf", new double[] { 1.1, 2.2, 3.3, 4.4, 5.5 });
        System.out.println("hello");
    }

}