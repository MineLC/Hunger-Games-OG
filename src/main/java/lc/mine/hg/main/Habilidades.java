package lc.mine.hg.main;

import lc.mine.hg.utilities.BGFiles;

public enum Habilidades
{
    ARROWEXPLOSION("ARROWEXPLOSION", 0, 1), 
    FASTTREE("FASTTREE", 1, 2), 
    ARROWEFFECT("ARROWEFFECT", 2, 3), 
    RIGHHANDTFIRE("RIGHHANDTFIRE", 3, 4), 
    STRENGHTCOOKIE("STRENGHTCOOKIE", 4, 5, new Modificador[] { Modificador.Duration.setup(10) }), 
    STRENGHTFIRE("STRENGHTFIRE", 5, 6), 
    PIGLOOTER("PIGLOOTER", 6, 7, new Modificador[] { Modificador.Amount.setup(4) }), 
    HEAVYFALL("HEAVYFALL", 7, 8), 
    INSTANTKILLARROW("INSTANTKILLARROW", 8, 9, new Modificador[] { Modificador.Distance.setup(25) }), 
    INSTANCECROPS("INSTANCECROPS", 9, 10), 
    AXETHUNDER("AXETHUNDER", 10, 11, new Modificador[] { Modificador.Cooldown.setup(6) }), 
    CANIBAL("CANIBAL", 11, 12, new Modificador[] { Modificador.Chance.setup(100) }), 
    STICKSTEAL("STICKSTEAL", 12, 13, new Modificador[] { Modificador.Chance.setup(8), Modificador.Cooldown.setup(15) }), 
    TIMELORD("TIMELORD", 13, 22, new Modificador[] { Modificador.Radius.setup(25), Modificador.Duration.setup(5), Modificador.Cooldown.setup(80) });
    
    int id;
    Modificador[] modificadores;
    
    private Habilidades(final String s, final int n, final int i, final Modificador... modificadores) {
        this.modificadores = new Modificador[0];
        this.id = i;
        this.modificadores = modificadores;
    }
    
    private Habilidades(final String s, final int n, final int i) {
        this.modificadores = new Modificador[0];
        this.id = i;
    }
    
    public int getId() {
        return this.id;
    }
    
    public <T> T getModifier(final Modificador<T> value) {
        if (BGFiles.abconf.getString("AB." + this.id + value.key) != null) {
            return (T)BGFiles.abconf.get("AB." + this.id + value.key);
        }
        if (this.modificadores.length == 0) {
            return null;
        }
        Modificador[] modificadores;
        for (int length = (modificadores = this.modificadores).length, i = 0; i < length; ++i) {
            final Modificador mod = modificadores[i];
            if (mod.key.equals(value.key)) {
                return (T)mod.value;
            }
        }
        return null;
    }
    
    public Object getModifier(final String key) {
        if (BGFiles.abconf.getString("AB." + this.id + key) != null) {
            return BGFiles.abconf.get("AB." + this.id + key);
        }
        if (this.modificadores.length == 0) {
            return null;
        }
        Modificador[] modificadores;
        for (int length = (modificadores = this.modificadores).length, i = 0; i < length; ++i) {
            final Modificador mod = modificadores[i];
            if (mod.key.equals(key)) {
                return mod.value;
            }
        }
        return null;
    }
    
    public static class Modificador<T>
    {
        static Modificador Duration;
        static Modificador Amount;
        static Modificador Distance;
        static Modificador Cooldown;
        static Modificador Chance;
        static Modificador Radius;
        static Modificador Burnneraby;
        T value;
        String key;
        
        static {
            Modificador.Duration = new Modificador("Duration");
            Modificador.Amount = new Modificador("Amount");
            Modificador.Distance = new Modificador("Distance");
            Modificador.Cooldown = new Modificador("Cooldown");
            Modificador.Chance = new Modificador("Chance");
            Modificador.Radius = new Modificador("Radius");
            Modificador.Burnneraby = new Modificador("Burn");
        }
        
        public Modificador(final String key) {
            this.key = key;
        }
        
        public Modificador<T> setup(final T value) {
            final Modificador mod = new Modificador(this.key);
            mod.value = value;
            return mod;
        }
    }
}
