package lc.mine.hg.data.top;

public final record Top(
    Player[] players
) {
    public static final class Player {
        public final String name;
        public int value;
        public Player(String name, int value) {
            this.name = name;
            this.value = value;
        }
    }

    public void calculatePosition(final Player player) {
        final int score = player.value;
        if (score == 0) {
            return;
        }

        delete(player.name); // Delete older data

        Player toMove = null;
        int i = 0;
        for (; i < players.length; i++) {
            final Player top = players[i];
            if (top == null) {
                players[i] = player;
                return;
            }
            if (top.value >= score) {
                continue;
            }
            toMove = top;
            break;
        }
        if (toMove == null) {
            return;
        }
        moveOneLeft(i, toMove);
        players[i] = player;
    }

    private void delete(final String playerName) {
        for (int i = 0; i < players.length; i++) {
            final Player top = players[i];
            if (top == null) {
                return;
            }
            if (top.name.equals(playerName)) {
                players[i] = null;
                return;
            }
        }
    }

    public void moveOneLeft(int i, Player next) {
        final Player current = players[i];
        players[i] = next;
        if (current == null || i+1 == players.length) {
            return;
        }
        moveOneLeft(i+1, current);
    }
}
