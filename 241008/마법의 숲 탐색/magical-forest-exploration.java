import java.io.*;
import java.util.*;

public class Main {
	static int[][] map;
	static int N, M, K;
	static int result = 0;
	static int[][] dir = { { -1, 0 }, { 1, 0 }, { 0, -1 }, { 0, 1 } };

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
		StringBuilder sb = new StringBuilder();
		StringTokenizer st = new StringTokenizer(br.readLine());

		N = Integer.parseInt(st.nextToken()) + 3;
		M = Integer.parseInt(st.nextToken()) + 1;
		K = Integer.parseInt(st.nextToken());

		map = new int[N][M];

		for (int i = 0; i < K; i++) {
			st = new StringTokenizer(br.readLine());
			int start = Integer.parseInt(st.nextToken());
			int dir = Integer.parseInt(st.nextToken());
			if (!simulation(1, start, dir, i + 1)) {
				map = new int[N][M];
			}
		}

		sb.append(result);
		bw.write(sb.toString());
		bw.flush();
		bw.close();
	}

	static boolean simulation(int row, int col, int dir, int num) {

		while (true) {

			// 막혀있지 않은 경우
			if (row + 2 < N && map[row + 2][col] == 0 && map[row + 1][col - 1] == 0 && map[row + 1][col + 1] == 0) {
				delMap(row, col);
				row = row + 1;
				setMap(row, col, dir, num);
			}
			// 서쪽이 안 막혀있는 경우
			else if (row < N - 2 && col - 2 > 0 && map[row][col - 2] == 0 && map[row - 1][col - 1] == 0
					&& map[row + 1][col - 1] == 0 && row + 2 < N && map[row + 2][col - 1] == 0
					&& map[row + 1][col - 2] == 0) {
				delMap(row, col);
				row = row + 1;
				col = col - 1;
				dir = changeDir(dir, 1);
				setMap(row, col, dir, num);
			}

			// 오른쪽이 안 막혀있는 경우
			else if (row < N - 2 && col + 2 < M && map[row][col + 2] == 0 && map[row - 1][col + 1] == 0
					&& map[row + 1][col + 1] == 0 && row + 2 < N && map[row + 2][col + 1] == 0
					&& map[row + 1][col + 2] == 0) {
				delMap(row, col);
				row = row + 1;
				col = col + 1;
				dir = changeDir(dir, 2);
				setMap(row, col, dir, num);
			}

			// 갈 곳이 없는 경우
			else {
				if (row > 2) {
					bfs(row, col, num);
					return true;
				}
				return false;
			}
		}
	}

	static void bfs(int row, int col, int num) {
		Queue<Node> q = new ArrayDeque<>();
		boolean[][] visit = new boolean[N][M];
		visit[row][col] = true;
		q.offer(new Node(row, col, num));

		int max = 0;
		while (!q.isEmpty()) {
			Node node = q.poll();
			if (node.x > max)
				max = node.x;

			for (int i = 0; i < 4; i++) {
				int x = node.x + dir[i][0];
				int y = node.y + dir[i][1];

				if (x < 0 || x >= N || y < 0 || y >= M) {
					continue;
				}

				if (visit[x][y]) {
					continue;
				}

				if (map[x][y] == 0) {
					continue;
				}

				if(node.num < 0) {
					visit[x][y] = true;
					q.offer(new Node(x, y, map[x][y]));
				}

				else {
					if(map[x][y] == node.num || map[x][y] == node.num * -1) {
						visit[x][y] = true;
						q.offer(new Node(x,y, map[x][y]));
					}
				}
			}

		}
		result += max - 2;
	}

	static int changeDir(int dir, int k) {
		if (k == 2) {
			dir++;
		} else {
			dir--;
		}
		if (dir < 0)
			dir = 3;
		if (dir > 3)
			dir = 0;
		return dir;
	}

	static void setMap(int row, int col, int dir, int num) {
		map[row][col] = num;
		map[row + 1][col] = num;
		map[row - 1][col] = num;
		map[row][col - 1] = num;
		map[row][col + 1] = num;
		if (dir == 0) {
			map[row - 1][col] = -1 * num;
		} else if (dir == 1) {
			map[row][col + 1] = -1 * num;
		} else if (dir == 2) {
			map[row + 1][col] = -1 * num;
		} else if (dir == 3) {
			map[row][col - 1] = -1 * num;
		}
	}

	static void delMap(int row, int col) {
		map[row][col] = 0;
		map[row + 1][col] = 0;
		map[row - 1][col] = 0;
		map[row][col - 1] = 0;
		map[row][col + 1] = 0;
	}

	static public class Node {
		int x, y, num;

		Node(int x, int y, int num) {
			this.x = x;
			this.y = y;
			this.num = num;
		}

		@Override
		public String toString() {
			return "Node [x=" + x + ", y=" + y + ", num=" + num + "]";
		}

	}
}