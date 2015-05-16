

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ssp {

	public static ArrayList<String> runDijkstra(File file, String srcId, String dstId) throws IOException {
		/*Creates paths from source to detination. Calls Fibonacci heap and extracts the path with minimum weight*/
		FibonacciHeap<String> fHeap ;
		String source,dest, weight = " ";
		BufferedReader reader = new BufferedReader(new FileReader(file));

		// HashMap to store the graph
		HashMap<String, Set<String[]>> graph = new HashMap<String, Set<String[]>> ();

		//Read the file to create the adjacency list		
		String line = reader.readLine();
		if (line != null)
		{
			//Start from second line skipping the No. of nodes in first line
			while((line= reader.readLine())!= null)
			{
				//Skip Spaces if any
				if("".equals(line))
					continue;

				// Set the source and destination node
				String[] nodes = line.split(" ");
				source = nodes[0];
				dest = nodes[1];
				weight = nodes[2];

				//The adjacency list is built with the adjacent nodes and the weights are stored on HashMap			

				if(graph.containsKey(source))
				{
					Set< String[]> adjList = graph.get(source);
					String[] edgWgt = {dest,weight};
					adjList.add(edgWgt);
					graph.put(source,adjList );
				}

				else
				{
					Set< String[]> adjList = new HashSet< String[]>();
					String[] edgWgt = {dest,weight};
					adjList.add(edgWgt);
					graph.put(source,adjList );
				}

				if(graph.containsKey(dest))
				{
					Set< String[]> adjList = graph.get(dest);
					String[] edgWgt = {source,weight};
					adjList.add(edgWgt);
					graph.put(dest,adjList );
				}

				else
				{
					Set< String[]> adjList = new HashSet< String[]>();
					String[] edgWgt = {source,weight};
					adjList.add(edgWgt);
					graph.put(dest,adjList );
				}

			}

			reader.close();
			//All the nodes are initialized with infinite priority
			fHeap = new FibonacciHeap<String> ();

			//Store the Fibonacci heap nodes in heapNode 
			Map<String, FibonacciHeap.FibNode<String>> fHeapNodes = new HashMap<String, FibonacciHeap.FibNode<String>> ();			

			for (Map.Entry<String, Set<String[]>> entry: graph.entrySet())
			{
				FibonacciHeap.FibNode<String> fNode = fHeap.enqueue(entry.getKey(), Double.POSITIVE_INFINITY);
				fHeapNodes.put(entry.getKey(),fNode );

			} 

			//Set the source node as 0
			fHeap.decreaseKey(fHeapNodes.get(srcId), 0.0);

			Map<String, Double> result = new HashMap<String, Double> ();

			// Map to store previous node
			Map<String, String> paths = new HashMap<String, String> ();

			//Start extracting the min element and adjust the distances of nodes in its adjacency list.			
			String curNode= " ";

			while(! fHeap.isEmpty())
			{
				FibonacciHeap.FibNode<String> minNode = fHeap.dequeueMin();
				curNode = minNode.getValue();

				//The next minimum node is stored in the list
				double curDistance = minNode.getPriority(); 
				result.put(curNode, curDistance);

				//The nodes are stored with the previous nodes
				Set< String[]> adjNodeList = graph.get(curNode);
				for(String[] adjNodes: adjNodeList)
				{
					if(result.containsKey(adjNodes[0]))
						continue;

					if( fHeapNodes.get(adjNodes[0]).getPriority() > (curDistance + Double.parseDouble(adjNodes[1])) )
					{
						double newDistance = (curDistance + Double.parseDouble(adjNodes[1]));
						fHeap.decreaseKey(fHeapNodes.get(adjNodes[0]), newDistance);

						paths.put(adjNodes[0], curNode);
					}
				}
			}


			ArrayList<String> shortestPath = new ArrayList<String> ();				
			String printNode= paths.get(dstId);			
			shortestPath.add(dstId);

			while(!printNode.equals( srcId)) 
			{
				shortestPath.add(printNode);
				printNode= paths.get(printNode);
			}

			shortestPath.add(printNode);
			Integer currweight = result.get(dstId).intValue();
			//add weight at end of list
			shortestPath.add(currweight.toString());
			return shortestPath;
		}

		else
		{
			System.out.println("Incorrect input file format");

		}
		reader.close();
		return null;


	}

	public static void main(String args[]) throws IOException{
		File file1 = new File(args[0]);
		String srcId = args[1];
		String dstId = args[2];
		ArrayList<String> dijkstrResult = new ArrayList<String>();
		dijkstrResult = runDijkstra(file1, srcId,dstId);
		//Print Weight
		System.out.println(dijkstrResult.get(dijkstrResult.size()-1));
		for (int i = dijkstrResult.size()-2; i>=0;i--)
		{
			//Print Source to Destination shortest path
			System.out.print(dijkstrResult.get(i));
			System.out.print(" ");
		}
	}

}