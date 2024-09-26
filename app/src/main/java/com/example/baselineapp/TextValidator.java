package com.example.baselineapp;

public class TextValidator
{
    //Takes a string and ensures that specific characters do not occur
    public static String isValid(String str_stringToValidate)
    {
        String invalidCharacters = "";
        char[] char_arr = str_stringToValidate.toCharArray();
        boolean noColons = true, noSemiColons = true, noBackslashes = true, noNewlines = true;
        //Must check for these characters: ; : \\ \n
        for (char c : char_arr)
        {
            if (c == ':' && noColons)
            {
                noColons = false;
                invalidCharacters = invalidCharacters + " :";
            }
            else if(c == ';' && noSemiColons)
            {
                noSemiColons = false;
                invalidCharacters = invalidCharacters + " ;";
            }
            else if(c == '\\' && noBackslashes)
            {
                noBackslashes = false;
                invalidCharacters = invalidCharacters + " \\";
            }
            else if(c == '\n' && noNewlines)
            {
                noNewlines = false;
                invalidCharacters = invalidCharacters + " \\n (newline character)";
            }
        }
        return invalidCharacters;
    }

    public static boolean isAlpha(String str)
    {
        return str != null && str.matches("[a-zA-Z]+");
    }
}
