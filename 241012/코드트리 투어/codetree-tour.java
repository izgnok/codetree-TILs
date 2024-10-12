import java.io.*;
import java.util.*;

public class Main {
	static int N, M;
	static List<List<Node>> graph;
	static int[] cost;
	static PriorityQueue<Travle> travle;
	static boolean[] check;
	static int start;
	static int INF = 987654321;

	public static void main(String[] args) throws IOException {
		// 여기에 코드를 작성해주세요.
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
		StringBuilder sb = new StringBuilder();
		StringTokenizer st = new StringTokenizer(br.readLine());

		int Q = Integer.parseInt(st.nextToken());
		graph = new ArrayList<>();
		travle = new PriorityQueue<>((o1, o2) -> {
			if (o1.benefit == o2.benefit) {
				return o1.id - o2.id;
			}
			return o2.benefit - o1.benefit;
		});

		start = 0;
		check = new boolean[30001];

		while (Q-- > 0) {
			st = new StringTokenizer(br.readLine());
			int input = Integer.parseInt(st.nextToken());

			if (input == 100) {
				// 그래프 초기화
				N = Integer.parseInt(st.nextToken());
				M = Integer.parseInt(st.nextToken());
				for (int i = 0; i < N; i++) {
					graph.add(new ArrayList<>());
				}
				for (int i = 0; i < M; i++) {
					int v = Integer.parseInt(st.nextToken());
					int u = Integer.parseInt(st.nextToken());
					int cost = Integer.parseInt(st.nextToken());
					graph.get(v).add(new Node(u, cost));
					graph.get(u).add(new Node(v, cost));
				}
				cost = new int[N];
				Arrays.fill(cost, INF);
				dijkstra();
			} else if (input == 200) {
				// 여행상품추가
				int id = Integer.parseInt(st.nextToken());
				int revenue = Integer.parseInt(st.nextToken());
				int dest = Integer.parseInt(st.nextToken());
				int benefit = revenue - cost[dest];
				travle.offer(new Travle(id, revenue, dest, benefit));
				check[id] = true;

			} else if (input == 300) {
				// 여행상품삭제
				int id = Integer.parseInt(st.nextToken());
				check[id] = false;
			} else if (input == 400) {
				// 최적여행상품
				if (travle.isEmpty()) {
					sb.append("-1").append("\n");
				} else {
					while (!travle.isEmpty()) {
						Travle t = travle.peek();
						if (t.benefit < 0) {
							sb.append("-1").append("\n");
							break;
						}
						travle.poll();
						if (check[t.id]) {
							check[t.id] = false;
							sb.append(t.id).append("\n");
							break;
						}
					}
				}
			} else {
				// 출발지 변경
				start = Integer.parseInt(st.nextToken());
				Arrays.fill(cost, INF);
				dijkstra();

				List<Travle> list = new ArrayList<>();
				while (!travle.isEmpty()) {
					Travle t = travle.poll();
					if (!check[t.id])
						continue;
					list.add(t);
				}
				for (int i = 0; i < list.size(); i++) {
					Travle t = list.get(i);
					travle.offer(new Travle(t.id, t.revenue, t.dest, t.revenue - cost[t.dest]));
				}

			}
		}
		bw.write(sb.toString());
		bw.flush();
		bw.close();
	}

	public static void dijkstra() {
		PriorityQueue<Node> pq = new PriorityQueue<>((o1, o2) -> o1.cost - o2.cost);
		boolean visit[] = new boolean[N];
		pq.offer(new Node(start, 0));
		cost[start] = 0;

		while (!pq.isEmpty()) {
			Node curr = pq.poll();
			if (visit[curr.v])
				continue;
			visit[curr.v] = true;

			List<Node> list = graph.get(curr.v);
			for (int i = 0; i < list.size(); i++) {
				Node next = list.get(i);

				int nextCost = INF;
				if (cost[curr.v] != INF)
					nextCost = cost[curr.v] + next.cost;

				if (cost[next.v] > nextCost) {
					cost[next.v] = nextCost;
					pq.offer(new Node(next.v, nextCost));
				}

			}

		}

	}

	public static class Node {
		int v, cost;

		Node(int v, int cost) {
			this.v = v;
			this.cost = cost;
		}

		@Override
		public String toString() {
			return "Node [v=" + v + ", cost=" + cost + "]";
		}
	}

	public static class Travle {
		int id, revenue, dest, benefit;

		Travle(int id, int revenue, int dest, int benefit) {
			this.id = id;
			this.revenue = revenue;
			this.dest = dest;
			this.benefit = benefit;
		}

		@Override
		public String toString() {
			return "Travle [id=" + id + ", revenue=" + revenue + ", dest=" + dest + ", benefit=" + benefit + "]";
		}

	}
}