package bj.p14502;

// https://www.acmicpc.net/problem/14502
import java.io.*;
import java.util.*;
import java.lang.*;

public class Main {
    public static void main(String[] args) throws IOException {
        // 입력
        // n*m 입력받기
        // map 입력받기 세로크기 n, 가로크기 m : int[n][m]

        // 벽 3개 세우기 (모든 경우에 대해)
        // 경우마다 바이러스를 최대로 퍼트리고, 더이상 퍼지지 않을때 0의 갯수를 구해서 저장한다. list에 저장
        // list를 sort해서 가장 큰 값을 출력한다.

        List<int []> zeros = new ArrayList<>(); //map 에서 값이 0인 위치 정보
        List<int []> startPoints = new ArrayList<>(); // map에서 값이 2인 위치 정보
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        int n = Integer.parseInt(st.nextToken()); // 세로 y
        int m = Integer.parseInt(st.nextToken()); // 가로 x
        List<Integer> safeToUnsafe = new ArrayList<>();
        int maxSafe =0;

        int [][] map = new int[n][m];
        for (int i =0; i < n; i++) {
            st = new StringTokenizer(br.readLine());
            for (int j = 0; j < m; j++) {
                map[i][j] = Integer.parseInt(st.nextToken());
                if (map[i][j] == 0 ) {
                    zeros.add(new int[]{i,j});
                } else if(map[i][j] == 2) {
                    startPoints.add(new int[]{i,j});
                }
            }
        }

        //  벽 3개 세우기
        for (int i = 0; i < zeros.size() ; i++) {
            int[] cur_i = zeros.get(i);
            map[cur_i[0]][cur_i[1]] = 1;
            for (int j = i+1; j < zeros.size(); j++) {
                int[] cur_j = zeros.get(j);
                map[cur_j[0]][cur_j[1]] = 1;
                for (int k =j+1; k < zeros.size(); k++) {
                    int[] cur_k = zeros.get(k);
                    map[cur_k[0]][cur_k[1]] = 1;

                    //System.out.println("zeros("+zeros.size()+")");
                    int changed = bfs(startPoints, map, n,m);
                    int safed = zeros.size() - changed - 3;
                    maxSafe = Math.max(maxSafe, safed);

                    //safeToUnsafe.add(changed);
                    map[cur_k[0]][cur_k[1]] = 0;
                }
                map[cur_j[0]][cur_j[1]] = 0;
            }
            map[cur_i[0]][cur_i[1]] = 0;
        }

        //Collections.sort(safeToUnsafe);
        //System.out.println("safeToUnsafe = "+ Arrays.toString(safeToUnsafe.toArray()));
        //int res = zeros.size() - safeToUnsafe.get(0) -3;
        System.out.println(maxSafe);
    }

    public static int bfs(List<int[]> startPoints, int[][] map, int n, int m) {

        //System.out.println("##enter bfs - show a new map##");

        int[][] new_map = new int[n][m];
        for (int i = 0; i < map.length; i++) {
            int[] inArr = map[i];
            for (int j = 0; j < inArr.length; j++) {
                new_map[i][j] = map[i][j];
                //System.out.print(inArr[j] + " ");
            }
            //System.out.println();
        }

        int changed = 0;
        int[][] matrix = {{0,1},{0,-1},{1,0},{-1,0}};
        Queue<int[]> q = new LinkedList<>();
        for(int i=0; i<startPoints.size(); i++) {
            q.offer(startPoints.get(i));
            //System.out.println("## startPoint is ("+ startPoints.get(i)[0]+", "+ startPoints.get(i)[1]+")");

            while (!q.isEmpty()) {
                int[] cur = q.poll();
                for (int[] d : matrix) {
                    int dy = cur[0] + d[0];
                    int dx = cur[1] + d[1];

                    if(dy>=0 && dy <n && dx >= 0 && dx < m) {
                        if (new_map[dy][dx] == 0){
                            new_map[dy][dx] = 2;
                            q.offer(new int[]{dy, dx});
                            changed++;
                        }
                    }
                }
            }
        }

        //System.out.println("### Final map - changed is "+ changed);

        for (int i = 0; i < new_map.length; i++) {
            int[] inArr = new_map[i];
            for (int j = 0; j < inArr.length; j++) {
                //System.out.print(inArr[j] + " ");
            }
            //System.out.println();
        }
        //System.out.println("");
        return changed;
    }
}
