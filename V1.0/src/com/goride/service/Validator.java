package com.goride.service;

class Validator {
    // Validate email format
    public static boolean isValidEmail(String email) {
        String[] chunks = email.split("@");

        if (chunks.length != 2)
            return false;

        if (chunks[0].isEmpty() || chunks[1].length() < 3)
            return false;

        if(!chunks[1].contains("."))
            return false;

        if (!Character.isLetter((chunks[0].charAt(0))))
            return false;

        char character;
        for (int index=0; index<email.length(); index++)
        {
            character = email.charAt(index);
            if (!Character.isLetter(character) && !Character.isDigit(character) && character != '_' && character != '.' && character != '@')
                return false;
        }

        if (email.contains("..") || email.contains(".@") || email.contains("@.") || email.contains("._."))
            return false;

        if (email.endsWith("."))
            return false;

        return true;
    }

    // Validate password format
    public static boolean isValidPassword(String password) {
        // for checking if password length
        // is between 8 and 15
        if (!((password.length() >= 8)
                && (password.length() <= 15))) {
            return false;
        }

        // to check space
        if (password.contains(" ")) {
            return false;
        }

        int count = 0;

        // check digits from 0 to 9
        for (int i = 0; i <= 9; i++) {

            // to convert int to string
            String str1 = Integer.toString(i);

            if (password.contains(str1)) {
                count = 1;
            }
        }
        if (count == 0) {
            return false;
        }


        // for special characters
        if (!(password.contains("@") || password.contains("#")
                || password.contains("!") || password.contains("~")
                || password.contains("$") || password.contains("%")
                || password.contains("^") || password.contains("&")
                || password.contains("*") || password.contains("(")
                || password.contains(")") || password.contains("-")
                || password.contains("+") || password.contains("/")
                || password.contains(":") || password.contains(".")
                || password.contains(", ") || password.contains("<")
                || password.contains(">") || password.contains("?")
                || password.contains("|"))) {
            return false;
        }


        count = 0;

        // checking capital letters
        for (int i = 65; i <= 90; i++) {

            // type casting
            char c = (char) i;

            String str1 = Character.toString(c);
            if (password.contains(str1)) {
                count = 1;
            }
        }
        if (count == 0) {
            return false;
        }

        count = 0;

        // checking small letters
        for (int i = 97; i <= 122; i++) {

            // type casting
            char c = (char) i;
            String str1 = Character.toString(c);

            if (password.contains(str1)) {
                count = 1;
            }
        }
        if (count == 0) {
            return false;
        }

        // if all conditions fails
        return true;
    }

}
