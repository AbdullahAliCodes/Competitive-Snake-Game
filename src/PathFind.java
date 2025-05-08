import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

public class PathFind {
    int mySnakeNum;
    int[] apple1coords;
    int[][] board;
    Snake[] snakes;

    public PathFind(int[] apple1coords, Snake[] snakes, int mySnakeNum, int[][] board){
        this.apple1coords = apple1coords;

        this.snakes = snakes;
        this.mySnakeNum = mySnakeNum;
        this.board = board;
    }

    /**
     * Workflow to calculate best move based on current board information and returns best possible move, if there's no best move return -1
     * @return move
     */
    public int calculateMove(){
        Snake mySnake = snakes[mySnakeNum];
        int move;
        double[] dists = new double[snakes.length];

        dists = calcDists(dists, apple1coords);
        boolean closest = isClosest(dists);


        move = aStar(mySnake, apple1coords);



        if(move == -1){ // follow tail if there's no path to apple's
            board = mySnake.markTailWalkable(board);
            move = aStar(mySnake, mySnake.getTailPos());
            //System.out.println("logFollow Tail: " + moveToEng.get(move));
        }

        if(move == -1){ // try survive if there's no path to tail or apple
            move = survivalInstincts();
            System.out.println("logSurvive: " + moveToEng.get(move));
        }
        return move;
    }

    /**
     * A-star algorithm to find best path to apple
     * @return best move, if there is no possible path the method return -1
     */
    private int aStar(Snake snake, int[] goal){

        int[][] closed = new int[board.length][board[0].length];

        // get coords of start and goal
        int startX = snake.getHeadPos()[0], startY = snake.getHeadPos()[1];
        int goalX = goal[0], goalY = goal[1];

        Node headNode = new Node(startX, startY);
        PriorityQueue<Node> pq = new PriorityQueue<>();
        pq.add(headNode);

        while(!pq.isEmpty()){

            Node currN = pq.poll(); // get node with lowest f cost
            int x = currN.x, y = currN.y;

            if(isClosed(x, y, closed)){ // if node has been explored go to start of loop
                continue;
            }
            setClosed(x, y, closed);

            if(goalFound(x, y, goalX, goalY)){
                snake.turned = false;
                while(currN.parentNode.x != startX || currN.parentNode.y != startY){ // backtrack
                    currN = currN.parentNode;
                }
                return getMove(currN.x, currN.y);
            }

            // ↑
            if(isTraversable(x, y - 1)){
                Node newN = new Node(x, y - 1, currN);
                newN.calcFcost(currN.g, goalX, goalY);
                pq.add(newN);
            }
            // ↓
            if(isTraversable(x, y + 1)){
                Node newN = new Node(x, y + 1, currN);
                newN.calcFcost(currN.g, goalX, goalY);
                pq.add(newN);
            }
            // →
            if(isTraversable(x + 1, y)){
                Node newN = new Node(x + 1, y, currN);
                newN.calcFcost(currN.g, goalX, goalY);
                pq.add(newN);
            }
            // ←
            if(isTraversable(x - 1, y)){
                Node newN = new Node(x - 1, y, currN);
                newN.calcFcost(currN.g, goalX, goalY);
                pq.add(newN);
            }
        }
        return -1; // return -1 if no path is possible
    }

    /**
     * Algorithm that tells snake to perform zig-zag motion to increase survivability
     * @return move
     */
    private int survivalInstincts(){
        int currDirection = snakes[mySnakeNum].getCurrDir();

        int[] straight = getStraight(currDirection);
        int[] left = getLeft(currDirection);
        int[] right = getRight(currDirection);

        if(snakes[mySnakeNum].turned){
            // →
            if(isTraversable(right[0], right[1])){
                snakes[mySnakeNum].turned = false;
                return getMove(right[0], right[1]);
            }
            // ←
            if(isTraversable(left[0], left[1])){
                snakes[mySnakeNum].turned = false;
                return getMove(left[0], left[1]);
            }
        }

        // ↑ (Straight)
        if(isTraversable(straight[0], straight[1])){
            return getMove(straight[0], straight[1]);
        }
        // →
        if(isTraversable(right[0], right[1])){
            snakes[mySnakeNum].turned = true;
            return getMove(right[0], right[1]);
        }
        // ←
        if(isTraversable(left[0], left[1])){
            snakes[mySnakeNum].turned = true;
            return getMove(left[0], left[1]);
        }

        return -1; // gg rip snek
    }

    // ↑
    // ↓
    // →
    // ←

    // ==== helper methods ====

    private double calcDist(Snake snake, int[] apple){
        int startX = snake.getHeadPos()[0], startY = snake.getHeadPos()[1];
        int goalX = apple[0], goalY = apple[1];
        return Math.abs(startX - goalX) + Math.abs(startY - goalY);
    }

    private double[] calcDists(double[] dists, int[] apple){
        for(int i = 0; i < snakes.length; i++){
            if(!snakes[i].alive){
                continue;
            }
            if(snakes[i].invisible && i != mySnakeNum){
                continue;
            }
            dists[i] = calcDist(snakes[i], apple);
        }
        return dists;
    }



    private int getMove(int x, int y){
        int headX = snakes[mySnakeNum].getHeadPos()[0], headY = snakes[mySnakeNum].getHeadPos()[1];
        if(headX < x && headY == y){
            return directions.get("right");
        }else if(headX > x  && headY == y){
            return directions.get("left");
        }else if(headY < y  && headX == x){
            return directions.get("down");
        }else{
            return directions.get("up");
        }
    }

    private boolean goalFound(int x, int y, int goalX, int goalY){
        return x == goalX && y == goalY;
    }

    private boolean isTraversable(int x, int y){
        if(!(x >= 0 && x < board.length)){
            return false;
        }
        if(!(y >= 0 && y < board[0].length)){
            return false;
        }
        if(board[y][x] == 1){
            return false;
        }
        return true;
    }

    private boolean isClosed(int x, int y, int[][] closed){
        return closed[y][x] == 2;
    }

    private void setClosed(int x, int y, int[][] closed){
        closed[y][x] = 2;
    }


    private int[] getStraight(int currDirection){
        int[] head = snakes[mySnakeNum].getHeadPos();
        switch(currDirection){
            case 0: //up
                head[1] = head[1] - 1;
                return head;
            case 1: // down
                head[1] = head[1] + 1;
                return head;
            case 2: // left
                head[0] = head[0] - 1;
                return head;
            case 3: //right
                head[0] = head[0] + 1;
                return head;
        }
        return head;
    }

    private int[] getLeft(int currDirection){
        int[] head = snakes[mySnakeNum].getHeadPos();
        switch(currDirection){
            case 0: //up
                head[0] = head[0] - 1;
                return head;
            case 1: // down
                head[0] = head[0] + 1;
                return head;
            case 2: // left
                head[1] = head[1] + 1;
                return head;
            case 3: //right
                head[1] = head[1] - 1;
                return head;
        }
        return head;
    }

    private int[] getRight(int currDirection){
        int[] head = snakes[mySnakeNum].getHeadPos();
        switch(currDirection){
            case 0: //up
                head[0] = head[0] + 1;
                return head;
            case 1: // down
                head[0] = head[0] - 1;
                return head;
            case 2: // left
                head[1] = head[1] - 1;
                return head;
            case 3: //right
                head[1] = head[1] + 1;
                return head;
        }
        return head;
    }

    private boolean isClosest(double[] dists){
        double max = dists[0];
        int snakeNum = 0;
        for(int i = 1; i < snakes.length; i++){
            if(max < dists[i]){
                max = dists[i];
                snakeNum = i;
            }
        }
        return snakeNum == mySnakeNum;
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

    private Map<Integer, String> moveToEng = new HashMap<Integer, String>() {{
        put(0, "up");
        put(1, "down");
        put(2, "left");
        put(3, "right");
        put(-1, "unknown");
    }};

    // ==== helper end ====
}