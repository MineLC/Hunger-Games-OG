package lc.mine.hg.utilities;

public class ParticleItem
{
    private int cost;
    private String effect;
    private String name;
    
    public ParticleItem(final String effect, final String name, final int cost) {
        this.cost = cost;
        this.effect = effect;
        this.name = name;
    }
    
    public int getCost() {
        return this.cost;
    }
    
    public String getEffect() {
        return this.effect;
    }
    
    public String getName() {
        return this.name;
    }
}
