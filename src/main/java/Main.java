import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.invoke.SwitchPoint;
import com.google.gson.Gson;

public class Main {

    public static final int WIDTH = 20;
    public static final int HEIGHT = 20;
    public static boolean[][] gameGrid = new boolean[WIDTH][HEIGHT];
    public static boolean[][] nextGameGrid = new boolean[WIDTH][HEIGHT];

    public static void printGrid(boolean[][] grid){
        String output_string = "";

        for(int y=0; y<HEIGHT; y++){
            for(int x=0; x<WIDTH; x++){
                output_string += grid[x][y] ? "\u001B[47m  " : "\u001B[40m  ";
            }
            output_string += "\033[0m\n";
            System.out.print(output_string);
            output_string = "";
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
                outGrid[x][y] = verifyCellLife(inGrid, x, y);
            }
        }
    }

    public static boolean[][] convertInt2dArrayToBool(int[][] in_grid){
        int rows = in_grid.length;
        int column = in_grid[0].length;
        boolean[][] out_grid = new boolean[rows][column];

        for(int y=0; y<rows; y++){
            for(int x=0; x<column; x++){
                out_grid[y][x] = in_grid[y][x] == 1;
            }
        }
        return out_grid;
    }

    public static boolean[][] readFigureFromFile(String filePath){
        boolean[][] figure;
        try {
            Gson gson = new Gson();
            BufferedReader br = new BufferedReader(new FileReader(filePath));
            figure =  convertInt2dArrayToBool(gson.fromJson(br, int[][].class));
        }
        catch (IOException e)
        { throw new RuntimeException(e); }

        return figure;
    }

    public static void copyFigureToGrid(boolean[][] grid, boolean[][] figure, int x_start, int y_start){
        for(int y=y_start; y<HEIGHT && (y-y_start)<figure.length; y++){
            for(int x=x_start; x<WIDTH && (x-x_start)<figure[y-y_start].length; x++){
                grid[x][y] = figure[y-y_start][x-x_start];
            }
        }
    }

    public static void main(String[] args) throws IOException {
        initGrid(gameGrid);
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(System.in));

        System.out.println("Quel figure voulez-vous ajoutez?");
        System.out.println("0-Rien");
        System.out.println("1-Barre");
        System.out.println("2-Copperhead");
        System.out.println("3-Gider Gun");
        System.out.println("4-Menu");
        System.out.println("5-Smiley");
        System.out.println("6-Walker");
        System.out.println("7-Random Grid");
        String input = reader.readLine();

        int x=0;
        int y=0;

        try {
            System.out.println("X:");
            String x_str = String.valueOf(reader.readLine());
            if (x_str != "") {
                x = Integer.valueOf(x_str);
            }
        } catch (NumberFormatException e) {
            System.out.println("X invalide, il sera donc 0");
        }

        try {
            System.out.println("Y:");
            String y_str = String.valueOf(reader.readLine());
            if (y_str != "") {
                y = Integer.valueOf(y_str);
            }
        } catch (NumberFormatException e) {
            System.out.println("Y invalide, il sera donc 0");
        }

        boolean[][] figure;
        switch (input) {
            case "0":
                break;
            case "1":
                figure = readFigureFromFile("./src/main/java/formes/barre.json");
                copyFigureToGrid(gameGrid, figure, x, y);
                break;
            case "2":
                figure = readFigureFromFile("./src/main/java/formes/copperhead.json");
                copyFigureToGrid(gameGrid, figure, x, y);
                break;
            case "3":
                figure = readFigureFromFile("./src/main/java/formes/glider_gun.json");
                copyFigureToGrid(gameGrid, figure, x, y);
                break;
            case "4":
                figure = readFigureFromFile("./src/main/java/formes/menu.json");
                copyFigureToGrid(gameGrid, figure, x, y);
                break;
            case "5":
                figure = readFigureFromFile("./src/main/java/formes/smiley.json");
                copyFigureToGrid(gameGrid, figure, x, y);
                break;
            case "6":
                figure = readFigureFromFile("./src/main/java/formes/walker.json");
                copyFigureToGrid(gameGrid, figure, x, y);
                break;
            case "7":
                initRandomGrid(gameGrid);
                break;
        }

        while (true) {
            printGrid(gameGrid);
            reader.readLine();
            tick(gameGrid, nextGameGrid);
            copyGrid(nextGameGrid, gameGrid);
        }

    }
}