import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class SkiOptimizer {
	
	public static int overallHighestPathLength;
	
	public static int overallHighestElevation;

	//2-dimensional array of Ski Nodes (each Ski node is represented by a value in the input text file)
	private static SkiNode[][] skiNodeMatrix;

	//Processes the map.txt file to get the elevation of each node
	private void processFile(){
		//Path path = Paths.get("map.txt");
		try {
			File file = new File("map.txt");			
			BufferedReader br = new BufferedReader(new FileReader(file));
			int i = 0;
			int numCols = 0;
			String line;
			while ((line = br.readLine()) != null) {
				String[] sArray = line.split(" ");
				if (i == 0){
					numCols = Integer.valueOf(sArray[1]);
					skiNodeMatrix = new SkiNode[Integer.valueOf(sArray[0])][numCols];
				}else{
					for (int j = 0; j < numCols; j++){
						skiNodeMatrix[i-1][j] = new SkiNode();
						skiNodeMatrix[i-1][j].setNodeElevation(Integer.valueOf(sArray[j]));
						skiNodeMatrix[i-1][j].setElevationOfLowestNode(Integer.valueOf(sArray[j]));
					}
				}
				i++;
			}
			br.close();
			for (int j = 0; j < skiNodeMatrix.length; j++){
				for (int k = 0; k < skiNodeMatrix[0].length; k++){
					updateNode(j,k);
				}
			}
			evaluateSkiNodes();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	//For every node, look at its four neighbours (north, south, east, west), and for each of its neighbours
	//update the node's own elevation difference and increment the number of nodes below it if it's 
	//
	public void updateNode(int i, int j){
		System.out.println("Evaluating Node at Row " + i + ", Col " + j);
		
		SkiNode currentNode = skiNodeMatrix[i][j];

		//Check North node
		if (i > 0){
			SkiNode northNode = skiNodeMatrix[i-1][j];
			if (currentNode.getNodeElevation() > northNode.getNodeElevation()){
				if (northNode.getHighestPathLength() + 1 > currentNode.getHighestPathLength()){
					currentNode.setHighestPathLength(northNode.getHighestPathLength() + 1);
					currentNode.setElevationOfLowestNode(northNode.getElevationOfLowestNode());
				}
				else if (northNode.getHighestPathLength() + 1 == currentNode.getHighestPathLength()){
					if (northNode.getElevationOfLowestNode() < currentNode.getElevationOfLowestNode())
						currentNode.setElevationOfLowestNode(northNode.getElevationOfLowestNode());
				}

			}else if (currentNode.getNodeElevation() < northNode.getNodeElevation()){
				currentNode.isNorthNodeHigher = true;
			}
		}


		//Check South node
		if (i < skiNodeMatrix.length - 1){
			SkiNode southNode = skiNodeMatrix[i+1][j];
			if (currentNode.getNodeElevation() > southNode.getNodeElevation()){
				if (southNode.getHighestPathLength() + 1 > currentNode.getHighestPathLength()){
					currentNode.setHighestPathLength(southNode.getHighestPathLength() + 1);
					currentNode.setElevationOfLowestNode(southNode.getElevationOfLowestNode());
				}
				else if (southNode.getHighestPathLength() + 1 == currentNode.getHighestPathLength()){
					if (southNode.getElevationOfLowestNode() < currentNode.getElevationOfLowestNode())
						currentNode.setElevationOfLowestNode(southNode.getElevationOfLowestNode());
				}

			}else if (currentNode.getNodeElevation() < southNode.getNodeElevation()){
				currentNode.isSouthNodeHigher = true;
			}
		}


		//Check East node
		if (j < skiNodeMatrix[0].length - 1){
			SkiNode eastNode = skiNodeMatrix[i][j+1];
			if (currentNode.getNodeElevation() > eastNode.getNodeElevation()){
				if (eastNode.getHighestPathLength() + 1 > currentNode.getHighestPathLength()){
					currentNode.setHighestPathLength(eastNode.getHighestPathLength() + 1);
					currentNode.setElevationOfLowestNode(eastNode.getElevationOfLowestNode());
				}
				else if (eastNode.getHighestPathLength() + 1 == currentNode.getHighestPathLength()){
					if (eastNode.getElevationOfLowestNode() < currentNode.getElevationOfLowestNode())
						currentNode.setElevationOfLowestNode(eastNode.getElevationOfLowestNode());
				}

			}else if (currentNode.getNodeElevation() < eastNode.getNodeElevation()){
				currentNode.isEastNodeHigher = true;
			}
		}


		//Check West node
		if (j > 0){
			SkiNode westNode = skiNodeMatrix[i][j-1];
			if (currentNode.getNodeElevation() > westNode.getNodeElevation()){
				if (westNode.getHighestPathLength() + 1 > currentNode.getHighestPathLength()){
					currentNode.setHighestPathLength(westNode.getHighestPathLength() + 1);
					currentNode.setElevationOfLowestNode(westNode.getElevationOfLowestNode());
				}
				else if (westNode.getHighestPathLength() + 1 == currentNode.getHighestPathLength()){
					if (westNode.getElevationOfLowestNode() < currentNode.getElevationOfLowestNode())
						currentNode.setElevationOfLowestNode(westNode.getElevationOfLowestNode());
				}

			}else if (currentNode.getNodeElevation() < westNode.getNodeElevation()){
				currentNode.isWestNodeHigher = true;
			}
		}

		//Update "parent" nodes recursively
		if (i > 0 && currentNode.isNorthNodeHigher){
			updateNode(i-1, j);
		}
		
		if ((i < skiNodeMatrix.length - 1) && currentNode.isSouthNodeHigher){
			updateNode(i+1, j);
		}
		
		if ((j < skiNodeMatrix[0].length - 1) && currentNode.isEastNodeHigher){
			updateNode(i, j+1);
		}
		
		if (j>0 && currentNode.isWestNodeHigher){
			updateNode(i, j-1);
		}
		

	}

	private static void evaluateSkiNodes(){
		for (int j = 0; j < skiNodeMatrix.length; j++){
			for (int k = 0; k < skiNodeMatrix[0].length; k++){
				SkiNode node = skiNodeMatrix[j][k];
				if (node.getHighestPathLength() > overallHighestPathLength){
					overallHighestPathLength = node.getHighestPathLength();
					overallHighestElevation = node.getNodeElevation() - node.getElevationOfLowestNode();
				}else if (node.getHighestPathLength() == overallHighestPathLength){
					if ((node.getNodeElevation() - node.getElevationOfLowestNode()) > overallHighestElevation){
						overallHighestElevation = node.getNodeElevation() - node.getElevationOfLowestNode();
					}
					
				}
			}
		}
	}
	
	
	public class SkiNode{
		
		int nodeElevation = 0;
		int highestPathLength = 0;
		int elevationOfLowestNode;
		boolean isSouthNodeHigher;
		boolean isNorthNodeHigher;
		boolean isEastNodeHigher;
		boolean isWestNodeHigher;

		public SkiNode(){
			
		}
		
		public int getHighestPathLength() {
			return highestPathLength;
		}
		public void setHighestPathLength(int highestPathLength) {
			this.highestPathLength = highestPathLength;
		}
		public int getNodeElevation() {
			return nodeElevation;
		}
		public void setNodeElevation(int nodeElevation) {
			this.nodeElevation = nodeElevation;
		}
		public int getElevationOfLowestNode() {
			return elevationOfLowestNode;
		}
		public void setElevationOfLowestNode(int elevationOfLowestNode) {
			this.elevationOfLowestNode = elevationOfLowestNode;
		}

	}

	//Main Method
	public static void main(String [] args){
		SkiOptimizer s = new SkiOptimizer();
		long startTime = System.currentTimeMillis();
		s.processFile();
		System.out.println("Highest Path Length: " + (overallHighestPathLength + 1));
		System.out.println("Highest Elevation: " + overallHighestElevation);
		long endTime = System.currentTimeMillis();
		System.out.println("Time taken to compute path: " + ((endTime-startTime)/1000) + "s");
	}

}
