package bj.p2468;

//https://www.acmicpc.net/problem/2468

import java.io.*;
import java.util.*;
import java.lang.*;

public class Main {
    public static void main(String[] args) throws IOException {
        // n을 받아서 n,n의 배열을 만든다.
        // map값을 받는다. 값을 받으면서, 높이 값의 최대값, 최소 값을 알아낸다.
        // 배열 sort써서, 해당 열의 0은 최소값, n-1은 최대값.
        // 다음 열이 들어올 때, 최소값보다 더 작다면 업뎃한다. 최대는 반대 첫 값을 받기 위해 최소,최대값이 0이면 업데이트한다.

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        int n = Integer.parseInt(st.nextToken());
        List<Integer> res = new ArrayList<>();

        int [][] map = new int[n][n];

        int min =0, max=0;
        for (int i =0; i<n;i++) {
            st = new StringTokenizer(br.readLine());
            int [] tmp = new int[n];
            for (int j = 0; j<n; j++) {
                map[j][i] = Integer.parseInt(st.nextToken());
                tmp[j] = map[j][i];
            }

            Arrays.sort(tmp);
            //System.out.println("sort["+i+"]="+ Arrays.toString(tmp));

            if (min > 0 && min > tmp[0]) {
                min = tmp[0];
            } else if (min ==0) {
                min = tmp[0];
            }

            if(max > 0 && max < tmp[n-1]) {
                max = tmp[n-1];
            } else if (max == 0) {
                max = tmp[n-1];
            }

        }

        //System.out.println("min("+min+")"+" / max("+max+")");
        //System.out.println("");

        // 높이의 최소값부터 최대값 -1이하일 때 각각 안전영역을 파악해야한다.
        // 높이가 h일때,
        // 배열을 훑으며 잠기지 않는 곳은 1, 잠기는 곳은 0으로 표시해 각 높이별 새로운 map을 만든다.

        for(int i = min; i<max; i++) {
            int[][] tmp = new int[n][n]; // 높이에 따라 0,1으로 구분한 map
            for (int x =0; x<n;x++) {
                for (int y=0; y<n;y++) {
                    if (map[x][y] > i) {
                        tmp[x][y] = 1;
                    }
                }
            }

            Queue<int[]> q = new LinkedList<>();
            boolean[][] visited = new boolean[n][n];

            int num_safezone =0;
            for (int x =0; x<n;x++) {
                for (int y=0; y<n;y++) {
                    //System.out.print(tmp[x][y]+" ");
                    if (tmp[x][y] == 1) {
                        if(!visited[x][y]) {
                            q.offer(new int[]{x,y});
                            visited[x][y] = true;
                            num_safezone++;
                            while(!q.isEmpty()) {
                                bfs(tmp,n, q, visited);
                            }
                        }
                    }
                }
                //System.out.println("");
            }
            //System.out.println("높이("+i+") : safe zone("+num_safezone+")");
            //System.out.println("");
            res.add(num_safezone);
        }
        // n*n번 돌 수 있는 반복문을 만든다.
        // cur_map의 x,y가 1이고, 방문한 적이 없으면 그 값을 queue에 넣고 while문으로 큐가 빌때까지 돌린다.
        // while에서는 bfs를 호출할거고. 결과값 +1을한다. (결과리스트에 추가해줄 예정)
        // 방문 여부는 offer과 함께 업데이트한다

        // 반복문을 다 돌면, 나온 결과 값은 높이가 h일때 안전한영역의 갯수이다.
        // 그것을 결과 리스트에 추가한다.

        // 그리고 다음 높이에 대해 또 동일하게 동작한다.
        // 다 끝나면, 결과 리스트를 sort해서, 최대 값을 리턴한다.

        Collections.sort(res);

        //높이가 같은 경우(min==max) for문을 돌지 않으므로, 1 리턴.
        System.out.println(res.isEmpty() ? 1 : res.get(res.size()-1));
    }
    public static void bfs(int[][] tmp, int n,Queue<int[]> q, boolean[][] visited) {
        int[] cur = q.poll();
        int x = cur[0];
        int y = cur[1];

        if (x-1 >=0) {
            if (tmp[x-1][y]==1) {
                if(!visited[x-1][y]) {
                    q.offer(new int[]{x-1,y});
                    visited[x-1][y] =true;
                }
            }
        }
        if(x+1<= n-1) {
            if (tmp[x+1][y]==1) {
                if(!visited[x+1][y]) {
                    q.offer(new int[]{x+1,y});
                    visited[x+1][y] =true;
                }
            }
        }
        if (y-1 >=0) {
            if (tmp[x][y-1]==1) {
                if(!visited[x][y-1]) {
                    q.offer(new int[]{x,y-1});
                    visited[x][y-1] =true;
                }
            }
        }
        if (y+1 <=n-1) {
            if (tmp[x][y+1]==1) {
                if(!visited[x][y+1]) {
                    q.offer(new int[]{x,y+1});
                    visited[x][y+1] =true;
                }
            }
        }
    }
}
