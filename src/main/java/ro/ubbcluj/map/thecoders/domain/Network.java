package ro.ubbcluj.map.thecoders.domain;

import ro.ubbcluj.map.thecoders.repository.Repository;

import java.util.*;

public class Network<ID, E extends Entity<ID>> {
    private LinkedList<Long>[] adj;
    private Long[] componenteConexe;
    Repository<ID, E> repository;

    public Network(int size, Set<ID> keys, Repository<ID, E> repository){
        this.repository = repository;
        long dim = size;
        adj = new LinkedList[(int) (dim + 2)];
        for(ID k : keys){
            adj[Math.toIntExact((Long) k)] = new LinkedList<>();
        }
        for(ID k : keys) {
            E user = repository.findOne(k);
            User utilizator = (User) user;
            for (User usr : utilizator.getFriends()) {
                adj[Math.toIntExact((Long) k)].add(usr.getId());
            }
        }
    }

    private void BFS(LinkedList<Long>[] adj, List<Long> viz, Long[] componenteConexe, long source, long nrc){
        Queue<Long> queue = new LinkedList<>();
        viz.add(source);
        componenteConexe[(int) source] = nrc;
        queue.add(source);
        while(!queue.isEmpty()){
            long currentNode = queue.peek();
            queue.remove();
            for(int i = 0; i < adj[(int) currentNode].size(); i++){
                if(adj[(int) currentNode].get(i) != null && !viz.contains(adj[(int) currentNode].get(i))){
                    queue.add(adj[(int) currentNode].get(i));
                    viz.add(adj[(int) currentNode].get(i));
                    componenteConexe[Math.toIntExact(adj[(int) currentNode].get(i))] = nrc;
                }
            }
        }
    }

    /**
     * calculam numarul de componente conexe
     * @param keys nodurile grafului
     * @param maxx ultima cheie a grafului
     * @return numarul comunitatilor din retea
     */
    public int numberOfCommunities(Set<ID> keys, int maxx){
        List<Long> viz = new ArrayList<>();
        int nrc = 0;
        this.componenteConexe = new Long[(int) (maxx + 2)];
        for(ID k : keys){
            if(!viz.contains((Long) k)) {
                nrc++;
                BFS(adj, viz, componenteConexe, (Long) k, nrc);
            }
        }
        return nrc;
    }

    /**
     *
     * @param k The last position in the solution array
     * @param maxLength The values of the longest road
     * @param actualRoad The array which will contain the longest road in a community
     * @param s The solution array that will contain all the roads
     * @return The longest road determined
     */
    private Long[] findLongestRoad(int k, int[] maxLength, Long[] actualRoad ,Long[] s){
        if(k > maxLength[0]){
            maxLength[0] = k;
            for(int i = 0; i <= k; i++)
                actualRoad[i] = s[i];
            return actualRoad;
        }
        return actualRoad;
    }

    /**
     *
     * @param k The last position in the solution array
     * @param adj The adjacency list of the graph
     * @param s The solution array that will contain all the roads in a community
     * @return 1 if the road is a valid one or 0 otherwise
     */
    private int ok(int k, LinkedList<Long>[] adj, Long[] s){
        for(int i = 0; i < k; i++){
            for(int j = i + 1; j <= k; j++){
                if(Objects.equals(s[i], s[j]))
                    return 0;
            }
        }
        for(int i = 0; i < k; i++){
            if(!adj[Math.toIntExact(s[i + 1])].contains(s[i]))
                return 0;
        }
        return 1;
    }

    /**
     *
     * @param k The current position in the solution array
     * @param index The number of vertices
     * @param maxLength The maximum length of a road
     * @param actualRoad The array that will contain the longest road
     * @param potentialRoad The array that will contain the vertices in a community
     * @param adj The adjacency list of the graph
     * @param s The solution array that will have all the possible road in a community
     */
    private void back(int k, int index, int[] maxLength, Long[] actualRoad, Long[] potentialRoad, LinkedList<Long>[] adj, Long[] s){
        for(int i = 0; i < index; i++){
            s[k] = potentialRoad[i];
            if(ok(k, adj, s) == 1)
                actualRoad = findLongestRoad(k, maxLength, actualRoad, s);
            if(k < index) {
                back(k + 1, index, maxLength, actualRoad, potentialRoad, adj, s);
            }
        }
    }

    /**
     *
     * @param keys The vertices of the graph
     * @param maxx The maximum value of a node
     * @return The array that will contain the longest road from a community
     */
    public Long[] longestRoadInACommunity(Set<ID> keys, int maxx){
        int nrc = numberOfCommunities(keys, maxx);
        Long[] actualRoad = new Long[(int) (maxx + 2)];
        for(int i = 1; i <= nrc; i++){
            Long[] potentialRoad = new Long[(int) (maxx + 2)];
            Long[] s = new Long[(int) (maxx + 2)];
            int index = 0;
            for(ID k : keys){
                if(componenteConexe[Math.toIntExact((Long) k)] == i)
                    potentialRoad[index++] = (Long) k;
            }
            int[] maxLength = {0};
            back(0, index, maxLength, actualRoad, potentialRoad, adj, s);
        }
        return actualRoad;
    }
}
