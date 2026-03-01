package it.edu.informatica.mobilita;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

public class TesterMobilita {

    public static void main(String[] args) {
        ClassTestManager m = new ClassTestManager( "it.edu.informatica.mobilita",false);

        m.classe = "MezzoDiTrasporto";
        try {
            Object m1 = m.crea();
            Object m2 = m.crea("mdt",100.0);
            m.brontolaStringa(m2.toString(),"mezzoditrasporto:mdt,100.0€");
            m.chiama(m1,"setNome","pluto");
            m.stampa("get/set nome", (m.chiama(m1,"getNome").equals("pluto")) );
            m.chiama(m1,"setCosto",2.0);
            m.stampa("get/set costo", (m.chiama(m1,"getCosto").equals(2.00)) );
        }catch(Exception e){
            m.stampa(e);
        }

        m.classe = "AMuscoli";
        try {
            @SuppressWarnings("unused")
            Object am1 = m.crea();
            Object am2 = m.crea("am",20.00,"bicipiti");
            m.brontolaStringa(am2.toString(),"veicoloamuscoli:am,20.0€(usabicipiti)");
            m.stampa("parteAlta", m.chiama(am2,"parteAlta").equals(true) );
            m.chiama(am2,"setMuscoliCoinvolti","xx");
            m.stampa("get/set muscoli", m.chiama(am2,"getMuscoliCoinvolti").equals("xx"));
        }catch(Exception e) {
            m.stampa(e);
        }

        m.classe = "AMotore";
        try {
            Object am1 = m.crea();
            Object am2 = m.crea("am2", 1000, 10.0, "benzina");
            m.brontolaStringa(am2.toString(),"Mezzo motorizzato: am2 (1000.0€) che emette 10.0 db di rumore, consuma benzina");
            m.stampa("possibileCentriAbitati",m.chiama(am2, "possibileCentriAbitati").equals(true));
            m.chiama(am1, "setRumorosita", 88.0);
            m.stampa("get/set rumorosità", m.chiama(am1, "getRumorosita").equals(88.0));
            m.chiama(am1, "setCarburante", "olio");
            m.stampa("get/set carburante", m.chiama(am1, "getCarburante").equals("olio"));
        }catch(Exception e) {
            m.stampa(e);
        }
    }

    private static class ClassTestManager {

    private final String errore;
    private final String ok;
    public String pacchetto;
    public String classe;

    public ClassTestManager(String pacchetto, boolean terminale) {
        errore = terminale ? "\u001B[31mERRORE\u001B[0m" : "ERRORE";
        ok = terminale ? "\u001B[32mOK\u001B[0m" : "OK";
        this.pacchetto=pacchetto;
    }

    public void stampa(String messaggio, boolean test) {
        System.out.println( classe+" "+messaggio+" "+ ( test ? ok : errore ));
    }

    private static int distanzaLevenshtein(String s1, String s2) {
        s1 = s1.replaceAll(" ", "").toLowerCase();
        s2 = s2.replaceAll(" ", "").toLowerCase();

        int[][] distanza = new int[s1.length() + 1][s2.length() + 1];

        for (int i = 0; i <= s1.length(); i++) {
            distanza[i][0] = i;
        }
        for (int j = 0; j <= s2.length(); j++) {
            distanza[0][j] = j;
        }

        for (int i = 1; i <= s1.length(); i++) {
            for (int j = 1; j <= s2.length(); j++) {
                if (s1.charAt(i - 1) == s2.charAt(j - 1)) {
                    distanza[i][j] = distanza[i - 1][j - 1];
                } else {
                    distanza[i][j] = 1 + Math.min(distanza[i - 1][j], Math.min(distanza[i][j - 1], distanza[i - 1][j - 1]));
                }
            }
        }
        return distanza[s1.length()][s2.length()];
    }

    public void brontolaStringa(String s1, String s2) {
        int distanza = distanzaLevenshtein(s1, s2);
        if(distanza<2) {
            stampa("toString", true );
        }else {
            stampa("toString ottengo:\""+s1+"\", attesa:\""+s2+"\"", false );
        }
    }

    public void stampa(Exception ex) {
        String colpevole = ex.getMessage().replaceAll("\\b[a-z\\.]+\\.", "");
        String tipo = ex.getClass().getSimpleName();
        stampa(tipo+" "+colpevole, false);
    }

    private static Class<?> toPrimitive(Class<?> c) {
        if (c == Double.class)  return double.class;
        if (c == Integer.class) return int.class;
        if (c == Float.class)   return float.class;
        if (c == Long.class)    return long.class;
        if (c == Boolean.class) return boolean.class;
        return c;
    }

    public Object crea(Object... args) throws ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException {
        String className = this.pacchetto+"."+this.classe;
        Class<?> clazz = Class.forName(className);
        if (args.length == 0) {
            return clazz.getDeclaredConstructor().newInstance();
        }
        Class<?>[] types = new Class<?>[args.length];
        for (int i = 0; i < args.length; i++) {
            types[i] = toPrimitive(args[i].getClass());
        }
        return clazz.getDeclaredConstructor(types).newInstance(args);
    }

    public Object chiama(Object obj, String method, Object... args) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        if (obj == null) return null;
        Class<?>[] types = Arrays.stream(args)
            .map(a -> toPrimitive(a.getClass())).toArray(Class<?>[]::new);
        return obj.getClass().getMethod(method, types).invoke(obj, args);
    }
}

}