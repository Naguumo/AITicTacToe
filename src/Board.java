
import java.util.ArrayList;

public class Board
{

    public static final int EMPTY = 0;
    public static final int AI = 1;
    public static final int PLAYER = 2;

    public static int[][] board = new int[3][3];

    public static Location AIMove;

    public boolean hasWon(int player)
    {
        return ((board[0][0] == player && board[0][0] == board[0][1] && board[0][0] == board[0][2])
                || (board[1][0] == player && board[1][0] == board[1][1] && board[1][0] == board[1][2])
                || (board[2][0] == player && board[2][0] == board[2][1] && board[2][0] == board[2][2])
                || (board[0][0] == player && board[0][0] == board[1][0] && board[0][0] == board[2][0])
                || (board[0][1] == player && board[0][1] == board[1][1] && board[0][1] == board[2][1])
                || (board[0][2] == player && board[0][2] == board[1][2] && board[0][2] == board[2][2])
                || (board[0][0] == player && board[0][0] == board[1][1] && board[0][0] == board[2][2])
                || (board[2][0] == player && board[2][0] == board[1][1] && board[2][0] == board[0][2]));
    }

    public boolean isOver()
    {
        return hasWon(PLAYER) || hasWon(AI) || getAvailable().isEmpty();
    }

    public ArrayList<Location> getAvailable()
    {
        ArrayList<Location> cells = new ArrayList<>();
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                if (board[i][j] == EMPTY)
                    cells.add(new Location(i, j));
        return cells;
    }

    public boolean makeMove(Location loc, int player)
    {
        if (board[loc.x][loc.y] == EMPTY)
        {
            board[loc.x][loc.y] = player;
            return true;
        }
        return false;
    }

    public void display()
    {
        System.out.println("");
        for (int i = 0; i < 3; i++)
        {
            for (int j = 0; j < 3; j++)
            {
                String value = "?";
                if (board[i][j] == PLAYER)
                    value = "X";
                else if (board[i][j] == AI)
                    value = "O";

                System.out.print(value + " ");
            }
            System.out.println("");
        }
        System.out.println("");
    }

    public int minmax(int depth, int turn)
    {
        if (hasWon(AI))
            return 1;
        if (hasWon(PLAYER))
            return -1;

        ArrayList<Location> cells = getAvailable();
        if (cells.isEmpty())
            return 0;

        int min = Integer.MAX_VALUE, max = Integer.MIN_VALUE;
        for (Location loc : cells)
        {
            if (turn == AI)
            {
                makeMove(loc, AI);
                int score = minmax(depth + 1, PLAYER);
                max = Math.max(score, max);

                if (score >= 0 && depth == 0)
                    AIMove = loc;

                if (score == 1)
                {
                    board[loc.x][loc.y] = EMPTY;
                    break;
                }
                if (loc == cells.get(cells.size() - 1) && max < 0 && depth == 0)
                    AIMove = loc;
            } else if (turn == PLAYER)
            {
                makeMove(loc, PLAYER);
                int score = minmax(depth + 1, AI);
                min = Math.min(score, min);

                if (min == -1)
                {
                    board[loc.x][loc.y] = EMPTY;
                    break;
                }
            }
            board[loc.x][loc.y] = EMPTY;
        }
        return turn == AI ? max : min;
    }
}
