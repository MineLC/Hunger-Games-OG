package lc.mine.hg.data.top;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import org.apache.commons.lang3.StringUtils;

import lc.mine.hg.utilities.IntegerUtils;

public final class TopFile {

    private final int amountTops = 50;

    public void saveTop(final File file, final Top top) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            final StringBuilder builder = new StringBuilder();
            for (int i = 0; i < amountTops; i++) {
                if (top.players()[i] == null) {
                    break;
                }
                builder.append(top.players()[i].name);
                builder.append(',');
                builder.append(top.players()[i].value);
                builder.append('\n');
            }
            writer.write(builder.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Top readTop(final File file) {
        if (!file.exists()) {
            return new Top(new Top.Player[amountTops]);
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            int i = 0;

            final Top top = new Top(new Top.Player[amountTops]);
            while ( (line = reader.readLine()) != null) {
                final String[] split = StringUtils.split(line, ',');
                top.players()[i++] = new Top.Player(split[0], IntegerUtils.parsePositive(split[1], 0));
            }
            return top;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
