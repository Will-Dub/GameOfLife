//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {

    public static final int WIDTH = 30;
    public static final int HEIGHT = 10;
    public static boolean[][] gameGrid = new boolean[WIDTH][HEIGHT];
    public static boolean[][] nextGameGrid = new boolean[WIDTH][HEIGHT];

    public static void printGrid(boolean[][] grid){
        System.out.print("\033[H\033[2J");
        System.out.flush();
        for(int y=0; y<HEIGHT; y++){
            for(int x=0; x<WIDTH; x++){
                System.out.print(grid[x][y] ? "\u001B[47m  " : "\u001B[40m  ");
            }
            System.out.print("\033[0m\n");
        }
    }

    public static void copyGrid(boolean[][] in, boolean[][] out){
        for(int y=0; y<HEIGHT; y++){
            for(int x=0; x<WIDTH; x++){
                out[x][y] = in[x][y];
            }
        }
    }

    public static void initGrid(boolean[][] grid){
        for(int y=0; y<HEIGHT; y++){
            for(int x=0; x<WIDTH; x++){
                grid[x][y] = false;
            }
        }
    }

    public static void initRandomGrid(boolean[][] grid){
        for(int y=0; y<HEIGHT; y++){
            for(int x=0; x<WIDTH; x++){
                grid[x][y] = Math.random() < 0.5;
            }
        }
    }

    public static boolean verifyCell(boolean[][] grid, int x, int y){
        if(x >= 0 && x < WIDTH){
            if(y>=0 && y < HEIGHT){
                 return grid[x][y];
            }
        }
        return false;
    }

    public static boolean verifyCellLife(boolean[][] grid, int x, int y){
        int count =0;
        count += verifyCell(grid, x-1, y-1) ? 1 : 0;
        count += verifyCell(grid, x, y-1) ? 1 : 0;
        count += verifyCell(grid, x+1, y-1) ? 1 : 0;
        count += verifyCell(grid, x-1, y) ? 1 : 0;
        //MILIEU
        count += verifyCell(grid, x+1, y) ? 1 : 0;
        count += verifyCell(grid, x-1, y+1) ? 1 : 0;
        count += verifyCell(grid, x, y+1) ? 1 : 0;
        count += verifyCell(grid, x + 1, y+1) ? 1 : 0;
        if(count ==2){
            return grid[x][y];
        }
        else if(count ==3){
            return true;
        }
        return false;
    }

    public static void tick(boolean[][] inGrid, boolean[][] outGrid){
        for(int y=0; y<HEIGHT; y++){
            for(int x=0; x<WIDTH; x++){
                boolean isAlive = verifyCellLife(inGrid, x, y);
                outGrid[x][y] = isAlive;
            }
        }
    }

    public static void main(String[] args) throws IOException {
        initRandomGrid(gameGrid);
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(System.in));
        //gameGrid[0][1] = true;
        //gameGrid[0][2] = true;
        //gameGrid[1][1] = true;
        //gameGrid[31][4] = true;

        while (true){
            printGrid(gameGrid);
            tick(gameGrid, nextGameGrid);
            copyGrid(nextGameGrid, gameGrid);
            reader.readLine();
        }

    }
}