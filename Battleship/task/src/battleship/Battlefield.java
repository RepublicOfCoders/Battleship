package battleship;
import battleship.Ships.*;

import java.util.Scanner;

/**
 * Class of the playing field.
 */
public class Battlefield {
    final int SIZE = 10;
    final int AIRCRAFT_SIZE = 5;
    final int BATTLESHIP_SIZE = 4;
    final int SUBMARINE_SIZE = 3;
    final int CRUISER_SIZE = 3;
    final int DESTROYER_SIZE = 2;
    final char EMPTY = '~';
    final char MISS = 'M';
    final char DESTROYED = 'X';
    char[][] filed = new char[SIZE][SIZE];
    char[][] blindFiled = new char[SIZE][SIZE];
    Ship[] ships;
    int sunkedShips;
    String playerName;

    /**
     * Constructor of the playing field.
     * Creating an array of fields with the specified size.
     */
    public Battlefield(String player) {
        this.playerName = player;
        sunkedShips =0;
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                filed[i][j] = EMPTY;
                blindFiled[i][j] = EMPTY;
            }
        }
    }

    /**
     * Fill the playing field with ships.
     * We ask the user for coordinates and if they are suitable, we pass them to the ship objects.
     */
    public void initfield() {
        Scanner scanner = new Scanner(System.in);
        System.out.println(this.playerName + ", place your ships on the game field");
        System.out.println(this.toString());
        ships = new Ship[5];
        ships[0] = new AircraftCarrier(AIRCRAFT_SIZE, "Aircraft Carrier");
        ships[1] = new Battleship(BATTLESHIP_SIZE, "Battleship");
        ships[2] = new Submarine(SUBMARINE_SIZE, "Submarine");
        ships[3] = new Cruiser(CRUISER_SIZE, "Cruiser");
        ships[4] = new Destroyer(DESTROYER_SIZE, "Destroyer");
        for (Ship ship : ships) {
            System.out.printf("Enter the coordinates of the %s (%d cells):\n", ship.getName(), ship.getSize());
            while (true) {
                String[] coordinates = scanner.nextLine().split(" ");
                int rowBegin = coordinates[0].charAt(0) - 65;
                int columnBegin = Integer.parseInt(coordinates[0].substring(1));
                int rowEnd = coordinates[1].charAt(0) - 65;
                int columnEnd = Integer.parseInt(coordinates[1].substring(1));
                if(rowBegin > rowEnd) {
                    int tmp = rowEnd;
                    rowEnd = rowBegin;
                    rowBegin = tmp;
                }
                if(columnBegin > columnEnd) {
                    int tmp = columnEnd;
                    columnEnd = columnBegin;
                    columnBegin = tmp;
                }
                if (ship.setCoordinates(rowBegin, columnBegin, rowEnd, columnEnd)) {
                    if (putShipOnField(rowBegin, columnBegin, rowEnd, columnEnd, ship)) {
                        System.out.println(this.toString());
                        break;
                    }
                }
            }
        }
        this.promptEnterKey();
    }
    public void promptEnterKey(){
        System.out.println("Press \"ENTER\" to continue...");
        Scanner scanner = new Scanner(System.in);
        scanner.nextLine();
    }

    /**
     * Place the ship on the playing field, if it does not interfere with other ships
     * @param _rowBegin - int, coordinate of the beginning of the ship.
     * @param _columnBegin - int, coordinate of the beginning of the ship.
     * @param _rowEnd - int, coordinate of the end of the ship.
     * @param _columnEnd - int, coordinate of the end of the ship.
     * @param _ship - Ship, object that is placed on the field.
     * @return - boolean, true if placing is success.
     */
    public boolean putShipOnField(int _rowBegin, int _columnBegin, int _rowEnd, int _columnEnd, Ship _ship) {
        //for each ships
        for(Ship ship: ships) {
            //if the ship being compared is not an installable ship and the ship isn't on the field yet
            if(ship != _ship && ship.isPlaced()) {
                //find out if there are any coordinates of other ships near the one being placed
                for (int i = _rowBegin - 1; i <= _rowEnd + 1; i++) {
                    for (int j = _columnBegin - 1; j <= _columnEnd + 1 ; j++) {
                        if((i == ship.getRowBegin() && j == ship.getColumnBegin()) ||
                                (i == ship.getRowEnd() && j == ship.getColumnEnd())) {
                            System.out.println("Error! You placed it too close to another one. Try again:");
                            return false;
                        }
                    }
                }
            }
        }

        //put the ship symbols in the game field according to its coordinates
        if (_rowBegin == _rowEnd) {
            for (int i = _columnBegin; i <= _columnEnd; i++) {
                this.filed[_rowBegin][i - 1] = _ship.getCells()[i - _columnBegin];
            }
        } else {
            for (int i = _rowBegin; i <= _rowEnd; i++) {
                this.filed[i][_columnBegin - 1] = _ship.getCells()[i - _rowBegin];
            }
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder("  1 2 3 4 5 6 7 8 9 10\n");
        for (int i = 0; i < SIZE; i++) {
            result.append(Character.toChars(65 + i));
            for (int j = 0; j < SIZE; j++) {
                result.append(" ").append(filed[i][j]);
            }
            result.append("\n");
        }
        return String.valueOf(result);
    }
    public String blindMap(){
        StringBuilder result = new StringBuilder("  1 2 3 4 5 6 7 8 9 10\n");
        for (int i = 0; i < SIZE; i++) {
            result.append(Character.toChars(65 + i));
            for (int j = 0; j < SIZE; j++) {
                result.append(" ").append(blindFiled[i][j]);
            }
            result.append("\n");
        }
        return String.valueOf(result);
    }
    public boolean startShooting(){
        Scanner scanner = new Scanner(System.in);
        System.out.println(this.playerName+" it`s your turn:\n");
        while(true){
            String coordinates = scanner.nextLine();
            int row = coordinates.charAt(0) - 65;
            int column = Integer.parseInt(coordinates.substring(1));
            if(row<0 || row>SIZE || column<0 || column>SIZE){
                System.out.println("Error! You entered the wrong coordinates! Try again:");
            }
            else if(alreadyShot(row, column)){
                // Mark this place
                putMarkOnField(row,column);
            }
            if(checkVictory()){
                System.out.println("You sank the last ship. You won. CONGRATULATIONS!");
                return false;
            }
            break;
        }
        promptEnterKey();
        return true;
    }
    public boolean alreadyShot(int row, int column){
        if(this.filed[row][column-1] == MISS || this.filed[row][column-1] == DESTROYED){
            System.out.println("Error: you already typed that coordinates!");
            return true;
        }
        return true;
    }
    public boolean putMarkOnField(int row, int column){
        for(Ship ship: ships){
            // If the coordinates match ship coordinates
            if(row >= ship.getRowBegin() && row <= ship.getRowEnd() && column >= ship.getColumnBegin() && column <= ship.getColumnEnd()){
                char[] cells = ship.getCells();
                if(ship.getColumnBegin() == ship.getColumnEnd()){
                    cells[row - ship.getRowBegin()] = ship.getSHIP_DAMAGED();
                    ship.setCells(cells);
                    putShipOnField(ship.getRowBegin(), ship.getColumnBegin(), ship.getRowEnd(), ship.getColumnEnd(), ship);
                }
                if(ship.getRowBegin() == ship.getRowEnd()){
                    cells[column - ship.getColumnBegin()] = ship.getSHIP_DAMAGED();
                    ship.setCells(cells);
                    putShipOnField(ship.getRowBegin(), ship.getColumnBegin(), ship.getRowEnd(), ship.getColumnEnd(), ship);
                }
                this.blindFiled[row][column-1] = ship.getSHIP_DAMAGED();
                System.out.println(this.blindMap());
                System.out.println("You hit a ship!");
                if(ship.isDestroyed()){
                    System.out.println("You sank a ship!");
                }
                //System.out.println(this.toString());
                return true;
            }
        }
        // If none of ships covers this field (MISS)
        this.filed[row][column-1] = MISS;
        this.blindFiled[row][column-1] = MISS;
        System.out.println("You missed!");
        //System.out.println(this.toString());
        return true;

    }
    private boolean checkVictory(){
        int destroyedShips=0;
        for(Ship ship: ships){
            if(ship.isDestroyed()){
                destroyedShips++;
            }
        }
        if(destroyedShips==ships.length){
            return true;
        }
        return false;
    }
}
