package team16.cs261.backend.mcl;

import team16.cs261.common.entity.graph.Edge;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by martin on 06/03/15.
 */
public class Graph {

    private final List<Edge> edges;

    private final List<Integer> nodeIds;
    //private final List<Instance> nodes;

    double[][] weights;


    public Graph(List<Edge> edges) {
        this.edges = edges;

        Set<Integer> nodeSet = new TreeSet<>();

        for (Edge e : edges) {
            int s = e.getSource();
            int t = e.getTarget();

            nodeSet.add(s);
            nodeSet.add(t);
        }

        //this.nodes = new Instance[nodeSet.size()];
        nodeIds = new ArrayList<>();
        for (Integer n : nodeSet) {
            nodeIds.add(n);
        }

        System.out.println("nodes: " + nodeSet);
    }

    public List<Integer> getNodeIds() {
        return nodeIds;
    }

    public int getNodeCount() {
        return nodeIds.size();
    }

    public double getWeight(int i, int j) {
        int n1 = nodeIds.get(i);
        int n2 = nodeIds.get(j);
        double weight = 0;

        for (Edge e : edges) {
            if (e.getSource() == n1 && e.getTarget() == n2)
                weight = e.getWeight();
            if (e.getSource() == n2 && e.getTarget() == n1)
                weight = e.getWeight();
        }

        return weight;
    }

    public List<Set<Integer>> getClusterIds(List<Set<Integer>> inCls) {
        List<Set<Integer>> outCls = new ArrayList<>();

        for (Set<Integer> inCl : inCls) {
            Set<Integer> outCl = new TreeSet<>();

            for (Integer node : inCl) {
                outCl.add(nodeIds.get(node));
            }

            outCls.add(outCl);
        }

        return outCls;
    }

    public String toMcl() {
        StringBuilder sb = new StringBuilder();

        for(Edge e : edges) {
            sb.append(e.getSource()).append("\t").append(e.getTarget()).append("\t").append(e.getWeight()).append("\n");
        }

        return sb.toString();
    }

    public double[][] toMatrix() {
        int size = getNodeCount();
        double[][] matrix = new double[size][size];

/*        for (int y = 0; y < size; y++) {
            for (int x = 0; x <= y; x++) {
                matrix[y][x] = getWeight(y, x);
            }
        }*/

        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                matrix[y][x] = getWeight(y, x);
                matrix[x][y] = getWeight(y, x);
            }
        }

/*
        SparseMatrix dataSparseMatrix = new SparseMatrix();
        for (int i = 0; i < graph.getNodeCount(); i++) {
            for (int j = 0; j <= i; j++) {
*//*                Instance x = data.instance(i);
                Instance y = data.instance(j);
                double dist = dm.measure(x, y);
                if (dist > maxZero)
                    dataSparseMatrix.add(i, j, dm.measure(x, y));*//*
                double dist = graph.getWeight(i, j);
                System.out.println("(" + i + ", " + j + ") : " + dist);
                if (dist > maxZero)
                    dataSparseMatrix.add(i, j, dist);
            }
        }*/

        return matrix;
    }


}
