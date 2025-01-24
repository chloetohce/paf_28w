package paf.workshop.paf_27w.model;

public class Game {
    private int gid;

    private String name;

    public Game() {
    }

    public Game(int gid, String name) {
        this.gid = gid;
        this.name = name;
    }

    public int getGid() {
        return gid;
    }

    public void setGid(int gid) {
        this.gid = gid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    
    
}
