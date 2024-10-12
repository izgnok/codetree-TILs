import java.io.*;
import java.util.*;

public class Main {

	static Node[] tree;
	static boolean[] check;

	public static void main(String[] args) throws IOException {
		// 여기에 코드를 작성해주세요.
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
		StringBuilder sb = new StringBuilder();
		StringTokenizer st = new StringTokenizer(br.readLine());

		int Q = Integer.parseInt(st.nextToken());
		tree = new Node[100001];
		check = new boolean[100001];
		for(int q=1; q<=Q; q++) {
			st = new StringTokenizer(br.readLine());
			int query = Integer.parseInt(st.nextToken());

			if (query == 100) {
				int id = Integer.parseInt(st.nextToken());
				int parent = Integer.parseInt(st.nextToken());
				int color = Integer.parseInt(st.nextToken());
				int depth = Integer.parseInt(st.nextToken());
				
				if(parent == -1) {
					tree[id] = new Node(id, parent, color, depth, depth, q);
					check[id] = true;
				}
				else {
					if(!check[parent]) continue;
					Node node = tree[parent];
					if(node.own_depth == 1 || node.total_depth == 1) continue;
					int total_depth = Math.min(node.own_depth, node.total_depth) - 1;
					tree[parent].childs.add(id);
					tree[id] = new Node(id, parent, color, depth, total_depth, q);
					check[id] = true;
				}

			} else if (query == 200) {
				int id = Integer.parseInt(st.nextToken());
				if(!check[id]) continue;
				int color = Integer.parseInt(st.nextToken());
				tree[id].color = color;
				tree[id].last_update = q;
			} else if (query == 300) {
				int id = Integer.parseInt(st.nextToken());
				int color = getColor(tree[id])[0];
				sb.append(color).append("\n");
			} else {
				int sum = 0;
				for(int i=1; i<=100000; i++) {
					if(!check[i]) continue;
					if(tree[i].parent == -1) {
						sum += (Integer) getSum(tree[i], tree[i].color, tree[i].last_update)[0];
					}
				}
				sb.append(sum).append("\n");
			}

		}
		bw.write(sb.toString());
		bw.flush();
		bw.close();
	}
	
	public static int[] getColor(Node node) {
		
		if(node.parent == -1) {
			return new int[] { node.color, node.last_update };
		}
		
		int[] arr = getColor(tree[node.parent]);
		if(arr[1] > node.last_update) {
			return arr;
		}
		else {
			return new int[] { node.color, node.last_update };
		}
	}
	
	public static Object[] getSum(Node node, int color, int last_update) {
		
		if(last_update < node.last_update) {
			color = node.color;
			last_update = node.last_update;
		}
		
		boolean[] colorCheck = new boolean[6];
		colorCheck[color] = true;
		int sum = 0;
		List<Integer> list = node.childs;
		for(int i=0; i<list.size(); i++) {
			Object[] obj = getSum(tree[list.get(i)], color, last_update);
			
			sum += (Integer) obj[0];
			boolean[] tmp = (boolean[]) obj[1];
			
			for(int k=1; k<=5; k++) {
				if(colorCheck[k] || tmp[k]) {
					colorCheck[k] = true;
				}
			}
		}
		int cnt = 0;
		for(int i=1; i<=5; i++) {
			if(colorCheck[i]) cnt++;
		}
		sum += cnt * cnt;
		
		return new Object[] { sum, colorCheck };
	}

	public static class Node {
		int id, parent, color, own_depth, total_depth, last_update;
		List<Integer> childs = new ArrayList<>();

		public Node(int id, int parent, int color, int own_depth, int total_depth, int last_update) {
			super();
			this.id = id;
			this.parent = parent;
			this.color = color;
			this.own_depth = own_depth;
			this.total_depth = total_depth;
			this.last_update = last_update;
		}

		@Override
		public String toString() {
			return "Node [id=" + id + ", parent=" + parent + ", color=" + color + ", own_depth=" + own_depth
					+ ", total_depth=" + total_depth + ", last_update=" + last_update + "]";
		}

	}
}