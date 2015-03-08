package team16.cs261.backend.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
* Created by martin on 06/03/15.
*/
public class MclOutput {
    final List<Set<Integer>> clusters;

    public MclOutput(List<String> lines) {
        clusters = new ArrayList<>();

        for (String line : lines) {
            Set<Integer> cl = new TreeSet<>();
            for (String node : line.split("\\s+")) {
                //System.out.println("node: " + node);
                cl.add(Integer.parseInt(node));
            }
            clusters.add(cl);
        }
    }

    public List<Set<Integer>> getClusters() {
        return clusters;
    }
}
