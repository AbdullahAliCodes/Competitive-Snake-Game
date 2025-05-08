import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
public class Snake {

    public boolean alive, invisible, turned;
    public int length, kills, topLength;
    public ArrayList<String> snakeCoords;
    public int currDirection;
    public String snakeLine; //use this to debug

    public Snake(){
        this.alive = true;
        this.length = 0;
        this.kills = 0;
        this.snakeCoords = new ArrayList<String>();
        this.topLength = 0;
        this.turned = false;
    }

    /**
     Update snake information and return updated board with snake drawn on it
     */
    public int[][] update(String snakeLine, int[][] board){

        this.snakeLine = snakeLine;

        // clear previous information
        int headIndex ;
        snakeCoords.clear();
        this.alive = true;
        this.invisible = false;

        // update with latest information
        String[] snakeInfo = snakeLine.split(" ");
        if(snakeInfo[0].equals("dead")){
            this.alive = false;
            this.turned = false;
            return board;
        }
        if(snakeInfo[0].equals("alive")){
            headIndex = 3;
            length = Integer.parseInt(snakeInfo[1]);
            kills = Integer.parseInt(snakeInfo[2]);

            if (length > topLength){
                topLength = length;
            }

            // add all snakes coords to arraylist
            for(int i = headIndex; i < snakeInfo.length; i++){
                snakeCoords.add(snakeInfo[i]);
            }
        }
        else{
            headIndex = 0;
            for(int i = headIndex; i < snakeInfo.length; i++){
                snakeCoords.add(snakeInfo[i]);
            }
        }


        // update board with snake
        board = drawSnake(board);
        return board;
    }

    private int[][] drawSnake(int[][] board){
        // loop through each bend
        for(int i = 0; i < snakeCoords.size() - 1; i++){
            board = drawLine(board, snakeCoords.get(i), snakeCoords.get(i + 1), 1);
        }
        return board;
    }

    private int[][] drawLine(int[][] board, String s1, String s2, int num){
        String[] p1 = s1.split(",");
        String[] p2 = s2.split(",");

        int x1 = Integer.parseInt(p1[1]);
        int x2 = Integer.parseInt(p2[1]);

        int y1 = Integer.parseInt(p1[0]);
        int y2 = Integer.parseInt(p2[0]);

        if(x1 > x2){ x2 = swap(x1, x1 = x2);}
        if(y1 > y2){ y2 = swap(y1, y1 = y2);}

        for(int row = x1; row <= x2; row++){
            board[row][y1] = num;
        }

        for(int col = y1; col <= y2; col++){
            board[x1][col] = num;
        }

        return board;
    }

    public int[] getHeadPos(){
        String[] sHead = snakeCoords.get(0).split(",");
        int[] head = {Integer.parseInt(sHead[0]), Integer.parseInt(sHead[1])};
        return head;
    }

    public int[] getTailPos(){
        String[] sTail = snakeCoords.get(snakeCoords.size() - 1).split(",");
        int[] tail = {Integer.parseInt(sTail[0]), Integer.parseInt(sTail[1])};
        return tail;
    }

    public int getCurrDir(){
        // when enemy snake is invis
        if(snakeCoords.size() == 1){
            return directions.get("unknown");
        }

        // get x, y coords of head
        String[] sHead = snakeCoords.get(0).split(",");
        int[] head = {Integer.parseInt(sHead[0]), Integer.parseInt(sHead[1])};

        // get x, y coords of first bend
        String[] sBend = snakeCoords.get(1).split(",");
        int[] bend = {Integer.parseInt(sBend[0]), Integer.parseInt(sBend[1])};

        if(head[0] == bend[0] && head[1] < bend[1]){
            return directions.get("up");
        }else if(head[0] == bend[0] && head[1] > bend[1]){
            return directions.get("down");
        }else if(head[1] == bend[1] && head[0] < bend[0]){
            return directions.get("left");
        }else{
            return directions.get("right");
        }
    }

    public int[][] markTailWalkable(int[][] board){
        int x = getTailPos()[0], y = getTailPos()[1];
        board[y][x] = 0;
        return board;
    }

    public int[][] markHeadSurr(int[][] board){
        if(!alive || invisible){
            return board;
        }
        int x = getHeadPos()[0], y = getHeadPos()[1];
        // ↑
        if(isTraversable(x, y - 1, board)){
            board[y - 1][x] = 1;
        }
        // ↓
        if(isTraversable(x, y + 1, board)){
            board[y + 1][x] = 1;
        }
        // ←
        if(isTraversable(x - 1, y, board)){
            board[y][x - 1] = 1;
        }
        // →
        if(isTraversable(x + 1, y, board)){
            board[y][x + 1] = 1;
        }
        return board;
    }

    private boolean isTraversable(int x, int y, int[][] board){
        if(!(x >= 0 && x <=board[0].length - 1)){
            return false;
        }
        if(!(y >= 0 && y <= board.length - 1)){
            return false;
        }
        return true;
    }

    // ==== helper methods ====

    private static int swap(int a, int b) {  // usage: y = swap(x, x=y);
        return a;
    }

    private Map<String, Integer> directions = new HashMap<String, Integer>() {{
        put("up", 0);
        put("down", 1);
        put("left", 2);
        put("right", 3);
        put("relative left", 4);
        put("straight", 5);
        put("relative right", 6);
        put("unknown", -1);
    }};

    // ==== helper end ====
}