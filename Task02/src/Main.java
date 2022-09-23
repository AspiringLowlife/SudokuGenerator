import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Random;
import java.util.Stack;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    public static int Repetitions =50000;
    //Counters for successes
    public static int uniqueCount = 0;
    public static int uniqueTextFiles = 0;
    public static int validBoardsCreated = 0;
    //Counters for failures
    public static int failedUniqueCount = 0;
    public static int failedUniqueTextFiles = 0;
    public static int failedValidBoardsCreated = 0;


    public static Stack<ArrayList<ArrayList<SudokuGrid.Box>>> validGrids = new Stack<>();
    public static Stack<ArrayList<ArrayList<SudokuGrid.Box>>> UniqueGrids = new Stack<>();

    public static void main(String[] args) {
//        new generateValidBoard().run();
//        new checkUniqueGrid().run();
//        new createTextFile().run();

        //threads and ting
        Thread validBoardMaker = new Thread(new generateValidBoard());
        Thread uniqueBoardChecker = new Thread(new checkUniqueGrid());
        Thread createBoardTextFile = new Thread(new createTextFile());
        Runnable stats = () -> {
            System.out.println("valid Boards pushed: " + validBoardsCreated);
            System.out.println("Unique Boards pushed: " + uniqueCount);
            System.out.println("Unique board text files created: " + uniqueTextFiles);

            System.out.println("Unique Boards failed: " + failedUniqueCount);
            System.out.println("valid Boards failed: " + failedValidBoardsCreated);
            System.out.println("Unique board text files failed: " + failedUniqueTextFiles);
        };
        Thread displayStats = new Thread(stats);
        //validBoardMaker.setPriority();
        ExecutorService pool = Executors.newFixedThreadPool(3);
        pool.execute(validBoardMaker);
        pool.execute(uniqueBoardChecker);
        pool.execute(createBoardTextFile);
        pool.execute(displayStats);
    }
}

class generateValidBoard implements Runnable {

    @Override
    public void run() {
        while (Main.uniqueCount < Main.Repetitions) {
            try {
                //make valid solved sudoku grid
                SudokuGrid sudokuGrid;
                do {
                    SudokuGrid sudokuGrid1 = new SudokuGrid(9);
                    sudokuGrid = sudokuGrid1;
                    SudokuGenerator generator = new SudokuGenerator(sudokuGrid);
                } while (isGridNotValid(sudokuGrid));
                //valid grid made
//                System.out.println("valid board created");
                Main.validBoardsCreated++;
                Main.validGrids.push(sudokuGrid.getGrid());
                Thread.sleep(1);
            } catch (Exception e) {
//                System.out.println("valid board creation interrupted");
                Main.failedValidBoardsCreated++;
            }
        }
    }

    private static boolean isGridNotValid(SudokuGrid grid) {
        for (ArrayList<SudokuGrid.Box> row : grid.getGrid()) {
            for (SudokuGrid.Box box : row) {
                if (box.getFact().equals("")) {
                    return true;
                }
            }
        }
        return false;
    }
}

class checkUniqueGrid implements Runnable {

    @Override
    public void run() {
        while (Main.uniqueCount < Main.Repetitions) {
            try {
//                if (Main.validGrids.isEmpty()) {
//                    new generateValidBoard().run();
//                }
                Main.UniqueGrids.push(Main.validGrids.pop());
//                System.out.println("UniqueGrid Added");
                Thread.sleep(2);
            } catch (Exception e) {
//                System.out.println("UniqueGrid Failed");
                Main.failedUniqueCount++;
            }
        }
    }
}

class createTextFile implements Runnable {

    @Override
    public void run() {
        while (Main.uniqueCount < Main.Repetitions) {
            try {
//            if (Main.UniqueGrids.isEmpty()) {
//                return;
//                new checkUniqueGrid().run();
//            }
                //terminating condition
                ArrayList<ArrayList<SudokuGrid.Box>> grid = Main.UniqueGrids.pop();
                Main.uniqueCount++;
                //create the file then write to it
                FileWriter myWriter = new FileWriter("Task02/resources/"+Main.uniqueTextFiles+".txt");
                for (int i = 0; i < grid.size(); i++) {
                    myWriter.write(grid.get(i).toString());
                    myWriter.write(System.getProperty("line.separator"));
                }
                myWriter.close();
                Main.uniqueTextFiles++;
                // myWriter.write(Main.UniqueGrids.lastElement().toString());
                // myWriter.close();
                Thread.sleep(1);
            } catch (Exception e) {
//                System.out.println("Text creation failed");
                Main.failedUniqueTextFiles++;
            }
        }
    }
}



