package com.github.zaozbr.batalhanaval;

public class ScreenUtil {

    public static void printTextLine(String text) {
        printTextLine(text, 80, false, ' ');
    }

    public static void printTextLine(String text, boolean centerInScreen) {
        printTextLine(text, 80, centerInScreen, ' ');
    }

    public static void printTextLine(String text, int lineLen) {
        printTextLine(text, lineLen, false, ' ');
    }

    public static void printTextLine(String text, int lineLen, boolean centerInScreen) {
        printTextLine(text, lineLen, centerInScreen, ' ');
    }

    public static void printTextLine(String text, int lineLen, boolean centerInScreen, char fillChar) {

        while (text.length() > lineLen) {
            String line = text.substring(0, lineLen);

            text = text.substring(lineLen);

            printTextLine(line, lineLen, centerInScreen, fillChar);
        }

        if (centerInScreen) {
            int spaces = (lineLen - text.length()) / 2;

            if (spaces > 0) {
                System.out.printf("%" + spaces + "s %s %" + spaces + "s%n", fillChar, text, fillChar);
            } else {
                System.out.printf(" %" + lineLen + "s %n", text);
            }
        } else {
            System.out.println(text);
        }
    }

    public static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
}
