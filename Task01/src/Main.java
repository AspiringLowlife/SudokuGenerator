import java.util.*;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        //make valid solved sudoku grid
        SudokuGrid sudokuGrid;
          do {
        SudokuGrid sudokuGrid1 = new SudokuGrid(9);
        sudokuGrid = sudokuGrid1;
        SudokuGenerator generator = new SudokuGenerator(sudokuGrid);
        System.out.println("Processing");
         } while (isGridNotValid(sudokuGrid));
        sudokuGrid.displayGrid();
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

class SudokuGenerator {

    private SudokuGrid sudokuGrid;

    public SudokuGenerator(SudokuGrid grid) {
        this.sudokuGrid = grid;
        generateValidGrid();
        // ensureFactSet();
    }

    private void generateValidGrid() {
        //find min box possibilities
        int count = 0;
        while (count < 100) {
            int elementsMin = Integer.MAX_VALUE;
            SudokuGrid.Box boxMin = null;

            for (ArrayList<SudokuGrid.Box> boxRow : sudokuGrid.getGrid()) {
                //returns the minimum box in this row
                SudokuGrid.Box runningMin = boxRow.stream().
                        min(Comparator.comparing(box -> {
                            if (box.getValues().size() > 1) return box.getValues().size();
                            else return 500;
                        })).get();

                //get the min but only if the box has not had it's fact set
                if (runningMin.getValues().size() < elementsMin && runningMin.getValues().size() > 1) {
                    elementsMin = runningMin.getValues().size();
                    boxMin = runningMin;
                }
            }

            //set a random valid number as the fact for min box
            Random rd = new Random();
            if (boxMin != null) {
                while (true) {
                    final Integer input = rd.nextInt(9);
                    try {
                        String now = boxMin.getValues().stream().filter(string -> string.equals(input.toString())).findAny().get();
                        sudokuGrid.setFact(boxMin, input.toString());
                        break;
                    } catch (Exception e) {
                    }
                }
            }
            count++;
        }
    }
}


