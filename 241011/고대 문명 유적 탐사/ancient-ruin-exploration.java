import java.io.*;
import java.util.*;

public class Main {
	static int[][] map;
	static int[] rest;
	static int K, M;
	static int idx;
	static int[][] dir = { { -1, 0 }, { 1, 0 }, { 0, -1 }, { 0, 1 } };

	static RemoveInfo removeInfo;

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
		StringBuilder sb = new StringBuilder();
		StringTokenizer st = new StringTokenizer(br.readLine());

		map = new int[5][5];
		K = Integer.parseInt(st.nextToken());
		M = Integer.parseInt(st.nextToken());
		for (int i = 0; i < 5; i++) {
			st = new StringTokenizer(br.readLine());
			for (int j = 0; j < 5; j++) {
				map[i][j] = Integer.parseInt(st.nextToken());
			}
		}
		rest = new int[M];
		st = new StringTokenizer(br.readLine());
		for (int i = 0; i < M; i++) {
			rest[i] = Integer.parseInt(st.nextToken());
		}

		idx = 0;
		while (K-- > 0) {
			int result = 0;
			removeInfo = new RemoveInfo();
			for (int i = 0; i < 5; i++) {
				for (int j = 0; j < 5; j++) {
					if (i - 1 < 0 || i + 1 >= 5 || j - 1 < 0 || j + 1 >= 5)
						continue;

					rotate(i, j); // 90도
					bfs(new Node(i, j), 1); // 탐색

					rotate(i, j); // 180도
					bfs(new Node(i, j), 2); // 탐색

					rotate(i, j); // 270도
					bfs(new Node(i, j), 3); // 탐색

					rotate(i, j); // 360도 (원상복구)
				}
			}
			if (removeInfo.count < 3) {
				continue;
			}
			result += removeInfo.count;

			while (true) {
				removeAndAdd();
				removeInfo = new RemoveInfo();
				bfs(null, 0);
				if (removeInfo.count < 3) {
					break;
				} else {
					result += removeInfo.count;
				}
			}

			if (result == 0)
				sb.append(" ");
			else
				sb.append(result);
		}
		bw.write(sb.toString());
		bw.flush();
		bw.close();
	}

	static void removeAndAdd() {
		int angle = removeInfo.angle;
		Node center = removeInfo.center;

		for (int i = 0; i < angle; i++) {
			rotate(center.x, center.y);
		}

		PriorityQueue<Node> pq = removeInfo.pq;
		while (!pq.isEmpty()) {
			Node node = pq.poll();
			map[node.x][node.y] = rest[idx++];
		}
	}

	static void rotate(int x, int y) {

		int[][] tmp = new int[5][5];
		for (int i = 0; i < 5; i++) {
			tmp[i] = map[i].clone();
		}
		map[x - 1][y - 1] = tmp[x + 1][y - 1];
		map[x][y - 1] = tmp[x + 1][y];
		map[x + 1][y - 1] = tmp[x + 1][y + 1];
		map[x - 1][y] = tmp[x][y - 1];
		map[x + 1][y] = tmp[x][y + 1];
		map[x - 1][y + 1] = tmp[x - 1][y - 1];
		map[x][y + 1] = tmp[x - 1][y];
		map[x + 1][y + 1] = tmp[x - 1][y + 1];
	}

	static void bfs(Node center, int angle) {
		boolean[][] visit = new boolean[5][5];
		List<Node> removeList = new ArrayList<>();

		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 5; j++) {
				if (visit[i][j])
					continue;
				Queue<Node> q = new ArrayDeque<>();
				q.offer(new Node(i, j));
				visit[i][j] = true;
				int count = 1;
				List<Node> tmpList = new ArrayList<>();
				while (!q.isEmpty()) {
					Node node = q.poll();
					tmpList.add(node);
					for (int k = 0; k < 4; k++) {
						int row = node.x + dir[k][0];
						int col = node.y + dir[k][1];

						if (row < 0 || row >= 5 || col < 0 || col >= 5)
							continue;
						if (visit[row][col])
							continue;
						if (map[node.x][node.y] != map[row][col])
							continue;

						q.offer(new Node(row, col));
						visit[row][col] = true;
						count++;
					}
				}
				if (count >= 3) {
					removeList.addAll(tmpList);
				}
			}
		}

		int count = removeList.size();
		if (removeInfo.count < count) {
			removeInfo.pq.clear();
			for (Node node : removeList) {
				removeInfo.pq.offer(new Node(node.x, node.y));
			}
			removeInfo.count = count;
			removeInfo.angle = angle;
			removeInfo.center = center;
		} else if (removeInfo.count == count) {
			if (removeInfo.angle > angle) {
				removeInfo.pq.clear();
				for (Node node : removeList) {
					removeInfo.pq.offer(new Node(node.x, node.y));
				}
				removeInfo.angle = angle;
				removeInfo.center = center;
			} else if (center != null && removeInfo.angle == angle) {
				if (removeInfo.center.y > center.y) {
					removeInfo.pq.clear();
					for (Node node : removeList) {
						removeInfo.pq.offer(new Node(node.x, node.y));
					}
					removeInfo.center = center;
				} else if (center != null && removeInfo.center.x > center.x) {
					removeInfo.pq.clear();
					for (Node node : removeList) {
						removeInfo.pq.offer(new Node(node.x, node.y));
					}
					removeInfo.center = center;
				}
			}
		}
	}

	static class Node {
		int x, y;

		Node(int x, int y) {
			this.x = x;
			this.y = y;
		}
	}

	static class RemoveInfo {
		PriorityQueue<Node> pq = new PriorityQueue<>((o1, o2) -> {
			if (o1.y == o2.y) {
				return o2.x - o1.x;
			}
			return o1.y - o2.y;
		});
		Node center;
		int count, angle;
	}
}