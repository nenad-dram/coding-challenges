package com.endyary;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;

public class SpaceSurfaceMapper {
    private static final String SHIP_CHAR = "#";
    private static final String BUILDABLE_CHAR = "@";
    private static final Queue<Square> squareQueue = new LinkedList<>();
    private static int floodLevel = 0;

    public static void main(String[] args) {

        String filePath = args[0];
        floodLevel = Integer.parseInt(args[1]);

        String[][] squareMap = getMapFromFile(filePath);
        Square shipSquare = getShipSquare(squareMap);
        squareQueue.add(shipSquare);

        while (!squareQueue.isEmpty()) {
            Square refSquare = squareQueue.poll();
            List<Square> adjacentSquares = getAdjacentSquares(refSquare, squareMap);
            adjacentSquares.forEach(s -> processSquare(s, refSquare.value, squareMap));
        }

        printMap(squareMap);
    }

    /**
     * Checks whether provided Square is buildable. If the Square is buildable then its value will
     * be replaced with a proper character and that square will be added in the queue for further
     * processing.
     *
     * @param square
     * @param refSquareHeight
     * @param squareMap
     */
    private static void processSquare(final Square square, final String refSquareHeight,
            final String[][] squareMap) {
        if (isSquareProcessed(square)
                && isAboveFloodLevel(square) && isHeightDiffValid(square, refSquareHeight)) {
            squareMap[square.x][square.y] = BUILDABLE_CHAR;
            squareQueue.add(square);
        }
    }

    /**
     * Get list of the top, bottom, left and right Squares from the given i.e. the reference
     * Square.
     *
     * @param refSquare
     * @param squareMap
     * @return
     */
    private static List<Square> getAdjacentSquares(final Square refSquare,
            final String[][] squareMap) {
        List<Square> squares = new ArrayList<>();
        int rows = squareMap.length;
        int columns = squareMap[0].length;

        if (refSquare.y != 0) {
            Square left = new Square(refSquare.x, refSquare.y - 1,
                    squareMap[refSquare.x][refSquare.y - 1]);
            squares.add(left);
        }

        if (refSquare.y != columns - 1) {
            Square right = new Square(refSquare.x, refSquare.y + 1,
                    squareMap[refSquare.x][refSquare.y + 1]);
            squares.add(right);
        }

        if (refSquare.x != 0) {
            Square top = new Square(refSquare.x - 1, refSquare.y,
                    squareMap[refSquare.x - 1][refSquare.y]);
            squares.add(top);
        }

        if (refSquare.x != rows - 1) {
            Square bottom = new Square(refSquare.x + 1, refSquare.y,
                    squareMap[refSquare.x + 1][refSquare.y]);
            squares.add(bottom);
        }

        return squares;
    }

    /**
     * Get a Square whose value is #. This will be used as a starting point for square processing.
     *
     * @param squareMap
     * @return
     */
    private static Square getShipSquare(final String[][] squareMap) {
        Square shipSquare = null;
        for (int i = 0; i < squareMap.length; i++) {
            for (int j = 0; j < squareMap[i].length; j++) {
                if (SHIP_CHAR.equals(squareMap[i][j])) {
                    shipSquare = new Square(i, j, SHIP_CHAR);
                }
            }
        }
        return shipSquare;
    }

    /**
     * Get a map from the given file. The result map is represented as a 2D array.
     *
     * @param filePath
     * @return
     */
    private static String[][] getMapFromFile(final String filePath) {
        List<List<String>> squareList = new ArrayList<>();
        try (Scanner sc = new Scanner(new BufferedReader(new FileReader(filePath)))) {
            while (sc.hasNextLine()) {
                squareList.add(Arrays.stream(sc.nextLine().split("")).toList());
            }
        } catch (IOException e) {
            System.err.println("Error while reading map file: " + e.getMessage());
            throw new RuntimeException(e);
        }
        return squareList.stream().map(l -> l.toArray(new String[0])).toArray(String[][]::new);
    }

    private static boolean isAboveFloodLevel(final Square square) {
        int squareValue = Integer.parseInt(square.value);
        return squareValue != 0 && squareValue * 10 > floodLevel;
    }

    /**
     * Checks whether two squares have the same height or their diff is -1. The road can only go
     * down, not up. The validation will be skipped if one of the given squares holds the ship.
     *
     * @param square
     * @param refSquareValue
     * @return
     */
    private static boolean isHeightDiffValid(final Square square, final String refSquareValue) {
        if (SHIP_CHAR.equals(square.value) || SHIP_CHAR.equals(refSquareValue)) {
            return true;
        } else {
            int refHeight = Integer.parseInt(refSquareValue);
            int squareHeight = Integer.parseInt(square.value);
            return squareHeight == refHeight || squareHeight - refHeight == -1;
        }
    }

    /**
     * Squares marked as the ship holder or buildable should be skipped
     *
     * @param square
     * @return
     */
    private static boolean isSquareProcessed(final Square square) {
        return !SHIP_CHAR.equals(square.value) && !BUILDABLE_CHAR.equals(square.value);
    }

    /**
     * Print a map after processing all squares
     *
     * @param squareMap
     */
    private static void printMap(final String[][] squareMap) {
        Arrays.stream(squareMap).forEach(row -> {
            Arrays.stream(row).forEach(System.out::print);
            System.out.println();
        });
    }

    private record Square(int x, int y, String value) {
    }
}