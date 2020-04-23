package gol;

import java.util.*;
import java.text.*;
import java.time.*;
import java.time.format.*;

public interface Global {
    public static final DecimalFormat     nf2  = new DecimalFormat("#.##");
    public static final DecimalFormat     nf1  = new DecimalFormat("#.#" );
    public static final DecimalFormat     int7 = new DecimalFormat("00000000" );
    public static final DateTimeFormatter dtf  =
        DateTimeFormatter.ofPattern( "yyyy-MM-dd-HHmmss" )
        .withLocale( Locale.UK )
        .withZone( ZoneId.systemDefault() );
}
