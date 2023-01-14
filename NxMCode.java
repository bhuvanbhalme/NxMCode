package basic;

import java.util.LinkedList;

class Map {
	public final int width;
	public final int height;

	private final Cell[][] cells;
	private final Move[] moves;
	private Point startPoint;

	public Map(int[][] mapData) {
		this.width = mapData[0].length;
		this.height = mapData.length;

		cells = new Cell[height][width];

		moves = new Move[] { new Move(1, 1), new Move(-1, 1), new Move(1, -1), new Move(-1, -1) };

		generateCells(mapData);
	}

	public Point getStartPoint() {
		return startPoint;
	}

	public void setStartPoint(Point p) {
		if (!isValidLocation(p))
			throw new IllegalArgumentException("Invalid point");

		startPoint.setLocation(p);
	}

	public Cell getStartCell1() {
		return getCellAtPoint(getStartPoint());
	}

	public Cell getCellAtPoint(Point p) {
		if (!isValidLocation(p))
			throw new IllegalArgumentException("Invalid point");

		return cells[p.y][p.x];
	}

	private void generateCells(int[][] mapData) {
		boolean foundStart = false;
		for (int i = 0; i < mapData.length; i++) {
			for (int j = 0; j < mapData[i].length; j++) {

				if (mapData[i][j] == 2) {
					if (foundStart)
						throw new IllegalArgumentException("Cannot have more than one start position");

					foundStart = true;
					startPoint = new Point(j, i);
				} else if (mapData[i][j] != 0 && mapData[i][j] != 1) {
					throw new IllegalArgumentException("Map input data must contain only 0, 1, 2");
				}
				cells[i][j] = new Cell(j, i, mapData[i][j] == 1);
			}
		}

		if (!foundStart)
			throw new IllegalArgumentException("No start point in map data");

		generateAdj();
	}

	private void generateAdj() {
		for (int i = 0; i < cells.length; i++) {
			for (int j = 0; j < cells[i].length; j++) {
				for (Move m : moves) {
					int a = j + m.getX();
					int b = i + m.getY();
					Point p2 = new Point(a, b);
					if (isValidLocation(p2)) {
						cells[i][j].addAdjCell(cells[p2.y][p2.x]);
					}
				}
			}
		}
	}

	private boolean isValidLocation(Point p) {
		if (p == null)
			throw new IllegalArgumentException("Point cannot be null");

		return (p.x >= 0 && p.y >= 0) && (p.y < cells.length && p.x < cells[p.y].length);
	}

	private class Move {
		private int x;
		private int y;

		public Move(int x, int y) {
			this.x = x;
			this.y = y;
		}

		public int getX() {
			return x;
		}

		public int getY() {
			return y;
		}
	}

	public Cell getStartCell() {

		return null;
	}

	private class Point {

		public int y;
		public int x;
		public Point(int y, int x) {

		}
		public void setLocation(Point p) {

		}

	}
}

class Cell {
	public final int x;
	public final int y;
	public final boolean isWall;
	private final LinkedList<Cell> adjCells;

	public Cell(int x, int y, boolean isWall) {
		if (x < 0 || y < 0)
			throw new IllegalArgumentException("x, y must be greater than 0");

		this.x = x;
		this.y = y;
		this.isWall = isWall;

		adjCells = new LinkedList<>();
	}

	public void addAdjCell(Cell c) {
		if (c == null)
			throw new IllegalArgumentException("Cell cannot be null");

		adjCells.add(c);
	}

	public LinkedList<Cell> getAdjCells() {
		return adjCells;
	}
}

class MapHelper {
	public static int countReachableCells(Map map) {
		if (map == null)
			throw new IllegalArgumentException("Arguments cannot be null");
		boolean[][] visited = new boolean[map.height][map.width];

		return dfs(map.getStartCell1(), visited) - 1;
	}

	private static int dfs(Cell currentCell, boolean[][] visited) {
		visited[currentCell.y][currentCell.x] = true;
		int touchedCells = 0;

		for (Cell adjCell : currentCell.getAdjCells()) {
			if (!adjCell.isWall && !visited[adjCell.y][adjCell.x]) {
				touchedCells += dfs(adjCell, visited);
			}
		}

		return ++touchedCells;
	}
}

public class NxMCode {

	public static void main(String[] args) {
		int[][] gridData = { { 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 1, 0, 0, 0, 1, 0, 0 }, { 0, 0, 0, 0, 1, 0, 0, 0 },
				{ 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 1, 0, 0, 1, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0 },
				{ 0, 0, 0, 0, 0, 0, 1, 0 }, { 0, 0, 1, 0, 0, 1, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0 },
				{ 0, 0, 0, 2, 1, 0, 0, 0 } };

		Map grid = new Map(gridData);
		MapHelper solution = new MapHelper();
		System.out.println(solution.countReachableCells(grid));

	}

}
