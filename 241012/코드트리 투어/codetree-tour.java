import java.io.*;
import java.util.*;

public class Main {
	static int N, M;
	static List<List<Node>> graph;
	static int[] cost;
	static Travle[] travle;
	static int start;
	
    public static void main(String[] args) throws IOException {
        // 여기에 코드를 작성해주세요.
    	BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    	BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
    	StringBuilder sb = new StringBuilder();
    	StringTokenizer st = new StringTokenizer(br.readLine());
    	
    	
    	int Q = Integer.parseInt(st.nextToken());
    	graph = new ArrayList<>();
    	travle = new Travle[30001];
    	start = 0;
    	
    	while(Q-- > 0) {
    		st = new StringTokenizer(br.readLine());
    		int input = Integer.parseInt(st.nextToken());
    		
    		if(input == 100) {
    			//  그래프 초기화 
    			N = Integer.parseInt(st.nextToken());
    			M = Integer.parseInt(st.nextToken());
    			for(int i=0; i<N; i++) {
    				graph.add(new ArrayList<>());
    			}
    			for(int i=0; i<M; i++) {
    				int v = Integer.parseInt(st.nextToken());
    				int u = Integer.parseInt(st.nextToken());
    				int cost = Integer.parseInt(st.nextToken());
    				graph.get(v).add(new Node(u, cost));
    				graph.get(u).add(new Node(v, cost));
    			}
    			cost = new int[N];
    			Arrays.fill(cost, Integer.MAX_VALUE);
    			dijkstra();
    		}
    		else if(input == 200) {
    			// 여행상품추가 
    			int id = Integer.parseInt(st.nextToken());
    			int revenue = Integer.parseInt(st.nextToken());
    			int dest = Integer.parseInt(st.nextToken());
    			travle[id] = new Travle(revenue, dest);
    			
    		} else if(input == 300) {
    			// 여행상품삭제 
    			int id = Integer.parseInt(st.nextToken());
    			travle[id] = null;
    		} else if(input == 400){
    			// 최적여행상품
    			int max = -1;
    			int id = -1;
    			for(int i=1; i<=30000; i++) {
    				if(travle[i] == null) continue;
    				int getCost = travle[i].revenue - cost[travle[i].dest];
    				if(max < getCost) {
    					max = getCost;
    					id = i;
    				}
    			}
    			if(id != -1) travle[id] = null;
    			sb.append(id).append("\n");
    		} else {
    			// 출발지 변경
    			start = Integer.parseInt(st.nextToken());
    			Arrays.fill(cost, Integer.MAX_VALUE);
    			dijkstra();
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
    	
    	while(!pq.isEmpty()) {
    		Node curr = pq.poll();
    		if(visit[curr.v]) continue;
    		visit[curr.v] = true;
    		
    		List<Node> list = graph.get(curr.v);
    		for(int i=0; i<list.size(); i++) {
    			Node next = list.get(i);
    			
    			int nextCost = Integer.MAX_VALUE;
    			if(cost[curr.v] != Integer.MAX_VALUE) nextCost = cost[curr.v] + next.cost;
    			
    			if(cost[next.v] > nextCost) {
    				cost[next.v] = nextCost;
    				pq.offer(new Node(next.v, nextCost));
    			}
    			
    		}
    		
    	}
    	
    }
    
    public static class Node {
    	int v, cost;
    	
    	Node (int v, int cost) {
    		this.v = v;
    		this.cost = cost;
    	}
    }
    
    public static class Travle {
    	int revenue, dest;
    	
    	Travle(int revenue, int dest) {
    		this.revenue = revenue;
    		this.dest = dest;
    	}

		@Override
		public String toString() {
			return "Travle [revenue=" + revenue + ", dest=" + dest + "]";
		}	
    }
}