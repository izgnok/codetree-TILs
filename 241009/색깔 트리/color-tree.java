import java.io.*;
import java.util.*;

public class Main {
	static int N;
	static Node[] nodeList;
	static boolean[] checkColor;

	public static void main(String[] args) throws IOException {
		// 여기에 코드를 작성해주세요.
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
		StringBuilder sb = new StringBuilder();
		StringTokenizer st = new StringTokenizer(br.readLine());

		N = Integer.parseInt(st.nextToken());
		nodeList = new Node[100001];

		for(int i=1; i<=N; i++) {
			
			st = new StringTokenizer(br.readLine());
			int K = Integer.parseInt(st.nextToken());

			if (K == 100) {
				// 삽입
				int id = Integer.parseInt(st.nextToken());
				int parent_id = Integer.parseInt(st.nextToken());
				int color = Integer.parseInt(st.nextToken());
				int own_depth = Integer.parseInt(st.nextToken());

				if (parent_id == -1) {
					nodeList[id] = new Node(parent_id, color, own_depth, own_depth, i);
				} else {
					Node node = nodeList[parent_id];
					if (node == null)
						continue;
					if (node.own_depth == 1 || node.total_depth == 1)
						continue;

					int total_depth = Math.min(node.own_depth, node.total_depth) - 1;
					nodeList[id] = new Node(parent_id, color, own_depth, total_depth, i);
					nodeList[parent_id].child.add(id);
				}
			} else if (K == 200) {
				// 색깔 변경
				int id = Integer.parseInt(st.nextToken());
				int color = Integer.parseInt(st.nextToken());
				Node node = nodeList[id];
				if(node == null) continue;
				node.color = color;
				node.last_update = i;
			} else if (K == 300) {
				// 색깔 조회
				int id = Integer.parseInt(st.nextToken());
				int info[] = getColor(nodeList[id]);
				sb.append(info[0]).append("\n");
			} else if (K == 400) {
				// 가치 조회
				int sum = 0;
				for(int j=1; j<= 100000; j++) {
					Node node = nodeList[j];
					checkColor = new boolean[6];
					if(node == null) continue;
					if(node.parent_id != -1) continue;
					sum += getSum(node, node.color, node.last_update);
				}
				sb.append(sum).append("\n");
			}
		}

		bw.write(sb.toString());
		bw.flush();
		bw.close();
	}
	
	public static int getSum(Node node, int color, int last_update) {
		
		if(last_update < node.last_update) {
			last_update = node.last_update;
			color = node.color;
		}
		List<Integer> childs = node.child;
		int sum = 0;
		for(int i=0; i<childs.size(); i++) {
			Node child = nodeList[childs.get(i)];
			sum += getSum(child, color, last_update);
		}
		checkColor[color] = true;
		int cnt = 0;
		for(int i=1; i<=5; i++) {
			if(checkColor[i]) cnt++;
		}
		return sum + (cnt*cnt);
	}
	
	public static int[] getColor(Node node)  {
		if(node.parent_id == -1) {
			return new int[] { node.color, node.last_update };
		}
		int[] info = getColor(nodeList[node.parent_id]);
		if(info[1] > node.last_update) {
			return info;
		}
		else {
			return new int[] { node.color, node.last_update };
		}
	}

	public static class Node {
		int parent_id, color, own_depth, total_depth, last_update;
		List<Integer> child;

		Node(int parent_id, int color, int own_depth, int total_depth, int last_update) {
			this.parent_id = parent_id;
			this.color = color;
			this.own_depth = own_depth;
			this.total_depth = total_depth;
			this.last_update = last_update;
			this.child = new ArrayList<>();
		}

		@Override
		public String toString() {
			return "Node [parent_id=" + parent_id + ", color=" + color + ", own_depth=" + own_depth + ", total_depth="
					+ total_depth + ", last_update=" + last_update + ", child=" + child + "]";
		}
		
		
	}
}