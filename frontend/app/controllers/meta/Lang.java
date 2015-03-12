package controllers.meta;

/**
 * Created by martin on 12/03/15.
 */
public class Lang {
    public static final String TRADES_HELP =
            "This view shows Raw Trades stored by the FRAuD Tool as read in from either the feeds or a static historical file. It is possible from this view to filter the data to a time range, Buyer, Seller, Symbol or Sector. Multiple filters can be applied at the same time and any of these views can be exported to CSV.";
    public static final String COMMS_HELP =
            "This view shows Raw Communications stored by the FRAuD Tool as read in from either the feeds or a static historical file. It is possible from this view to filter the data to a time range, Buyer, Seller, Symbol or Sector. Multiple filters can be applied at the same time and any of these views can be exported to CSV.";
    public static final String CLUSTERS_HELP =
            "Clusters are a collection of factors which the FRAuD Tool has deemed related. These clusters are unusual but not necessarily suspicious. Selecting a cluster will display a detailed view including a graph of the cluster and a break down of the contributing factors.";
    public static final String CLUSTER_HELP =
            "This view shows the graph for an individual Cluster and factors that contributed towards it’s creation. The edges in the cluster graph represent the Factors and the nodes are the Traders. It is possible to view the factors in the factor table or a filtered view of the raw data that led to the creation of the factor.";
    public static final String FACTORS_HELP =
            "Factors are the manifestation of patterns in the raw data. Here a factor displays information identifying it's type and detailing it’s significant features. It provides the option of viewing the raw data that resulted in the factor being generated.";
    public static final String DASHBOARD_HELP =
            "Welcome to the Financial Review & Automatic Detection Tool Dashboard. The purpose of this page is to provides a broad overview of system status. It displays the latest Clusters it has discovered, the most recent ticks and two graphs one of clusters discovered every 5 minutes and one of reader activity.";
}
