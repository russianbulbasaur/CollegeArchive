package mein.wasser.ui;

public class screening_algorithm {
    int ACCEPT_DATA = 0;
    private screening_algorithm(String data)
    {

    }
    public static screening_algorithm getInstance(String data)
    {
     return new screening_algorithm(data);
    }
}
