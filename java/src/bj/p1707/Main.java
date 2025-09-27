package bj.p1707;

// https://www.acmicpc.net/problem/1707

import java.io.*;
import java.util.*;
import java.lang.*;

public class Main {
    public static void main(String[] args) throws IOException {

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        int k = Integer.parseInt(st.nextToken());

        // graphs : 그래프들 배열 만들기
        // 그래프 k 개 만들어야함. 최소 2개에서 5개

        // 테스트 케이스 개수 k 받기
        // 정점의 갯수 v, 간선의 갯수 e 받기
        // 정점의 갯수 v 크기의 그래프 arraylist만들기 이중
        // 간선의 갯수 e 만큼 반복문으로 인접 정보 받아 그래프에 저장하기

        List<List<List<Integer>>> graphs = new ArrayList<>();
        for (int i = 0; i < k; i++) {
            st = new StringTokenizer(br.readLine());
            int v = Integer.parseInt(st.nextToken()); // 정점수
            int e = Integer.parseInt(st.nextToken()); // 간선갯수

            //graph : 그래프 만들기
            List<List<Integer>> graph = new ArrayList<>();
            for (int l = 0; l < v + 1; l++) { // 정점수 만큼 (0 ~ 정점수)
                graph.add(new ArrayList<>());
            }


            for (int j = 0; j < e; j++) // 간선 수 만큼 받아 인접리스트만들기
            {
                st = new StringTokenizer(br.readLine());
                int a = Integer.parseInt(st.nextToken());
                int b = Integer.parseInt(st.nextToken());
                //List<Integer> list = graph.get(a);
                //list.add(b);
                graph.get(a).add(b);
                graph.get(b).add(a);
            }
            graphs.add(graph);

            //System.out.println(graph);
            int[] coloring = new int[v + 1];
            Queue<Integer> q = new LinkedList<>();

            boolean rst = false;
            //System.out.println("");
            for (int t = 1; t < v + 1; t++) {
                if (coloring[t] == 0) {
                    coloring[t] = 1;
                    q.offer(t);
                }

                while (!q.isEmpty()) {
                    rst = bfs(graph, q, coloring);
                    if (!rst) break;
                }
                if (!rst) break;
            }

            if (!rst) System.out.println("NO");
            else System.out.println("YES");
                /*int s = 1;
                int cnt = 0;
                while (s <= v) {
                    if (coloring[s] == -1 || coloring[s] == 1) {
                        cnt++;
                    }
                    s++;
                }
                if (cnt == v) {
                    System.out.println("YES");
                } else {
                    System.out.println("NO2");
                }*/
        }
    }


    // 이분 그래프 확인 방법 => 모든 값에 대해 확인하게 됨. 비연결 그래프면 가능하지만 아니면 리소스를 많이 씀.
    // 두 그룹으로 나눈다. 한 그룹의 갯수는 1부터 v/2까지 (나머지 버림) 반복문
    // 그리고 각 그룹을 bfs에 넣고, graph를 참고해 인접이 있으면 continue한다.

    // 인접 그래프를 순회할 건데, 1부터 시작
    // 색을 칠한 것임 나와 인접한 건 나와 다른 색으로 칠하기
    // 처음 값에는 1을 넣는다. 그리고 인접한 값에는 -1을 넣는다.
    // 인접한 값을 큐에 넣는다.
    // 그 값의 인접한 값에는 1을 넣는다.(나와 다른 값)
    // 이미 값이 나와 같은 값이 들어가 있다면 NO를 출력한다.
    // 그런 경우가 없이 종료된다면, YES를 출력한다.


    public static boolean bfs(List<List<Integer>> graph, Queue<Integer> q, int[] coloring) {
        int cur = q.poll();
        //System.out.println("bfs(cur ="+cur+")");
        int myColor = coloring[cur];
        int r_myColor = 1;
        if (myColor==1) r_myColor = -1;
        for(int i : graph.get(cur)) {
            //System.out.println("get("+i+")");
            if (coloring[i] == 0) {
                coloring[i] = r_myColor;
                //System.out.println("V["+i+"]'s color = " + coloring[i]);
                q.offer(i);
            } else if (coloring[i] == myColor) {
                return false;

            }

        }
        return true;
    }

}
