import jdk.nashorn.internal.ir.Block;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class SudokuGrid {
    //initially defining number of rows
    private ArrayList<ArrayList<Box>> grid = new ArrayList<>();
    int emptyCount = 0;

    public SudokuGrid(int size) {
        //initialize the grid with all possible values
        //but add some initial facts
        //r is row c is column
        for (int r = 0; r < size; r++) {
            ArrayList<Box> row = new ArrayList<>();
            for (int c = 0; c < size; c++) {
                row.add(new Box(r, c));
            }
            grid.add(row);
        }
        //add block value to each box
        setBlocks();
        //set initial facts
        //region initial facts
        setFact(grid.get(0).get(3), "7");
        setFact(grid.get(0).get(4), "9");
        setFact(grid.get(0).get(7), "5");
        setFact(grid.get(1).get(0), "3");
        setFact(grid.get(1).get(1), "5");
        setFact(grid.get(1).get(2), "2");
        setFact(grid.get(1).get(5), "8");
        setFact(grid.get(1).get(7), "4");
        setFact(grid.get(2).get(7), "8");
        setFact(grid.get(3).get(1), "1");
        setFact(grid.get(3).get(4), "7");
        setFact(grid.get(3).get(8), "4");
        setFact(grid.get(4).get(0), "6");
        setFact(grid.get(4).get(3), "3");
        setFact(grid.get(4).get(5), "1");
        setFact(grid.get(4).get(8), "8");
        setFact(grid.get(5).get(0), "9");
        setFact(grid.get(5).get(4), "8");
        setFact(grid.get(5).get(7), "1");
        setFact(grid.get(6).get(1), "2");
        setFact(grid.get(7).get(1), "4");
        setFact(grid.get(7).get(3), "5");
        setFact(grid.get(7).get(6), "8");
        setFact(grid.get(7).get(7), "9");
        setFact(grid.get(7).get(8), "1");
        setFact(grid.get(8).get(1), "8");
        setFact(grid.get(8).get(4), "3");
        setFact(grid.get(8).get(5), "7");
        //endregion
    }

    //pass in fact set box of choice
    public void setFact(Box boxInput, String fact) {
        //list of all boxes that had 1 possibility left after call removePossibility
        ArrayList<Box> boxes = new ArrayList<>();
        Box boxy = boxInput;
        //use stream.anyMatch to check row for validity
        if (grid.get(boxy.row).stream().anyMatch(box -> box.getFact().equals(fact))) {
            return;
        }
        //check column for validity
        for (ArrayList<Box> boxRow : grid) {
            //check each column return if invalid
            if (boxRow.get(boxy.col).fact.equals(fact)) {
                return;
            }
        }
        //check block
        if (grid.stream().anyMatch(line -> line.stream().anyMatch(box -> {
            if (box.getBlock().equals(boxy.Block) && box.fact.equals(fact)) {
                return true;
            } else return false;
        }))) {
            return;
        }
        //all conditions are met thus a valid fact can be set
        boxy.setValue(fact);

        //remove possibility from every other box sharing row
        for (Box box : grid.get(boxy.getRow())) {
            if (box.equals(boxy)) continue;
            if (box.removePossibility(fact)) {
                boxes.add(box);
            }
        }
        //remove possibility from every other box sharing col
        for (ArrayList<Box> boxRow : grid) {
            Box box = boxRow.get(boxy.getCol());
            if (box.equals(boxy)) continue;
            if (box.removePossibility(fact)) {
                boxes.add(box);
            }
        }
        //remove possibilities from block
        for (ArrayList<Box> boxRow : grid) {
            for (Box box : boxRow) {
                if (box.Block.equals(boxy.Block) && !box.equals(boxy)) {
                    if (box.removePossibility(fact)) {
                        boxes.add(box);
                    }
                }
            }
        }
        //use recursion to set fact for any boxes that had 1 possibility left
        if (boxes.size() > 0) {
            for (Box box : boxes) {
                setFact(box, box.getValues().get(0));
            }
        }
    }

    public void displayGrid() {
        System.out.println(emptyCount);
        for (int i = 0; i < grid.size(); i++) {
            System.out.print(grid.get(i));
            System.out.println();
        }
    }

    public ArrayList<ArrayList<Box>> getGrid() {
        return grid;
    }

    private void setBlocks() {
        //first three rows
        for (int i = 0; i < 3; i++) {
            //columns
            for (int j = 0; j < grid.size(); j++) {
                //first three columns
                if (j == 0 || j == 1 || j == 2) {
                    grid.get(i).get(j).setBlock("A");
                }
                //middle three
                if (j == 3 || j == 4 || j == 5) {
                    grid.get(i).get(j).setBlock("B");
                }
                //last three
                if (j == 6 || j == 7 || j == 8) {
                    grid.get(i).get(j).setBlock("C");
                }
            }
        }
        //middle three
        for (int i = 3; i < 6; i++) {
            //columns
            for (int j = 0; j < grid.size(); j++) {
                //first three columns
                if (j == 0 || j == 1 || j == 2) {
                    grid.get(i).get(j).setBlock("D");
                }
                //middle three
                if (j == 3 || j == 4 || j == 5) {
                    grid.get(i).get(j).setBlock("E");
                }
                //last three
                if (j == 6 || j == 7 || j == 8) {
                    grid.get(i).get(j).setBlock("F");
                }
            }
        }
        //last three
        for (int i = 6; i < 9; i++) {
            //columns
            for (int j = 0; j < grid.size(); j++) {
                //first three columns
                if (j == 0 || j == 1 || j == 2) {
                    grid.get(i).get(j).setBlock("G");
                }
                //middle three
                if (j == 3 || j == 4 || j == 5) {
                    grid.get(i).get(j).setBlock("H");
                }
                //last three
                if (j == 6 || j == 7 || j == 8) {
                    grid.get(i).get(j).setBlock("I");
                }
            }
        }
    }

    public class Box {
        //initial values
        private ArrayList<String> values = new ArrayList<>(Arrays.asList("1", "2", "3", "4", "5", "6", "7", "8", "9"));
        private int row;
        private int col;
        private String fact = "";
        private String Block;

        public Box(int row, int col) {
            this.row = row;
            this.col = col;
        }

        //use streaming to set box to one value
        public void setValue(String fact) {
            if (fact.equals("")) return;
            values = (ArrayList<String>) values.stream().
                    filter(item -> item.equals(fact)).collect(Collectors.toList());
            this.fact = fact;
        }

        public boolean removePossibility(String possibility) {
            values.remove(possibility);
            if (values.size() == 0) {
                emptyCount++;
            }
            if (values.size() == 1 && this.fact.equals("")) {
                return true;
            }
            return false;
        }

        public void clearBlock() {
            values.removeAll(values);
        }

        @Override
        public String toString() {
            return values.toString();
        }

        public List<String> getValues() {
            return values;
        }

        public int getRow() {
            return row;
        }

        public int getCol() {
            return col;
        }

        public String getFact() {
            return fact;
        }

        public String getBlock() {
            return Block;
        }

        public void setBlock(String block) {
            Block = block;
        }

    }
}