package lc.mine.hg.data.top;

import java.io.File;

import lc.mine.hg.main.BGMain;

public final class TopManager {

    private TopFile topFile;
    private final File folder = new File(BGMain.instance.getDataFolder(), "tops");

    public void start() {
        topFile = new TopFile();
        TopStorage.set(new TopStorage(
            read("kills"),
            read("deaths"),
            read("wins"),
            read("played")
        ));
    }

    private Top read(final String name) {
        return topFile.readTop(new File(folder, name + ".csv"));
    }

    private void save(final String name, final Top top) {
        new TopFile().saveTop(new File(folder, name + ".csv"), top);
    }
   
    void save() {
        if (!folder.exists()) {
            folder.mkdir();
        }
        final TopStorage tops = TopStorage.get();
        save("kills", tops.kills());
        save("deaths", tops.deaths());
        save("wins", tops.wins());
        save("played", tops.played());
    }
}
