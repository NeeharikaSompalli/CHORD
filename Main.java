import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class Main{
	public static void main(String args[]) throws IOException {

		if (args.length == 3) {
			int numberofentries = Integer.parseInt(args[2]);
			Chord c = new Chord(numberofentries);
			c.nodemap = new HashMap<Integer, Node>();
			c.nodequeue = new PriorityQueue<Integer>();
			String path = args[1];
			FileInputStream fstream = new FileInputStream(path);
			BufferedReader br = new BufferedReader(new InputStreamReader(fstream));

			String strLine;

			while ((strLine = br.readLine()) != null) {
				String[] input = strLine.trim().split(" ");
				System.out.println(" > "+ strLine);
				process_check(input, numberofentries, c);
			}
			br.close();
		}

		else if (args.length == 1) {
			BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));
			String line = "";
			int numberofentries = Integer.parseInt(args[0]);
			Chord c = new Chord(numberofentries);
			c.nodemap = new HashMap<Integer, Node>();
			c.nodequeue = new PriorityQueue<Integer>();
			while (true) {
				line = bf.readLine();
				String[] input = line.trim().split(" ");
				process_check(input, numberofentries, c);

			}
		}
	}

	public static boolean isinteger(String s)
	{
		boolean isvalidinteger = false;
		try{
			Integer.parseInt(s);
			isvalidinteger=true;
		}
		catch (NumberFormatException e)
		{

		}
		return isvalidinteger;
	}

	private static void process_check(String[] input, int numberofentries, Chord c) {

		if (input[0].equalsIgnoreCase("end")) {
			if (input.length == 1) {
				System.exit(0);
			}
			else
			{
				System.out.println("SYNTAX ERROR: End expects 0 parameters not "+(input.length-1));
			}
		}


		else if (input[0].equalsIgnoreCase("add")) {
			if(input.length==2) {
				int size = (int) Math.pow(2, numberofentries);
				if(isinteger(input[1]))
				{
					int x = Integer.parseInt(input[1]);

					if (Integer.parseInt(input[1]) < size && Integer.parseInt(input[1]) >= 0) {
						c.add(new Node(Integer.parseInt(input[1]), c.numberofentries, c.nodemap));
					} else {
						System.out.println("ERROR: node id must be in [0, " + size + ")");
					}
				}
				else
				{
					System.out.println("ERROR : Enter a valid Number");
				}
			}
			else
			{
				System.out.println("SYNTAX ERROR: Add expects 1 parameter not "+(input.length-1));
			}
		}

		else if (input[0].equalsIgnoreCase("fix")) {

			if(input.length==2) {
					if(isinteger(input[1]))
					{
					if (c.nodemap.containsKey(Integer.parseInt(input[1]))) {
						c.nodemap.get(Integer.parseInt(input[1])).fixfingertable(c.nodemap, c.numberofentries);
					}
					else
					{
						System.out.println("Invalid integer for the node "+Integer.parseInt(input[1]));
					}
				}else
				{
					System.out.println("ERROR : Enter a valid number");
				}
			}
			else
			{
				System.out.println("SYNTAX ERROR: Fix expects 1 parameter not "+(input.length-1));
			}
		}


		else if (input[0].equalsIgnoreCase("list")) {
			if(input.length ==1) {
				List<Integer> alllist = new ArrayList<Integer>();
				alllist.addAll(c.nodemap.keySet());

				if (!alllist.isEmpty()) {
					Collections.sort(alllist);
					System.out.print("Nodes : ");
					int count =1;
					for (int i : alllist) {
						System.out.print(i);

						if(count != alllist.size())
							System.out.print(", ");
						count++;
//						System.out.print(" Node " + i + ": " + "suc  " + c.nodemap.get(i).successor.node_id + ", " + " Pre   " + ((c.nodemap.get(i).predecessor.node_id == i)?"None":c.nodemap.get(i).predecessor.node_id) + ": " + "finger:");
//						int[] fingers = c.nodemap.get(i).fingertable.getfingers();
//						for (i = 0; i < fingers.length; i++) {
//							System.out.print(", " + fingers[i]);
//						}
//						System.out.println();
					}
					System.out.println();
				}
				else
				{
					System.out.println("No Nodes formed the Chord ring");
				}
			}
			else
			{
				System.out.println("SYNTAX ERROR: List expects 0 parameters not "+(input.length-1));
			}

		}


		else if (input[0].equalsIgnoreCase("show")) {
			if (input.length == 2) {
					if(isinteger(input[1])) {

						if (c.nodemap.containsKey(Integer.parseInt(input[1]))) {
							Node id = c.nodemap.get(Integer.parseInt(input[1]));
							System.out.print(" Node :" + Integer.parseInt(input[1]) + ", " + "Suc   " + id.successor.node_id + ", " + " Pre   " + ((id.predecessor == null) ? "None" : id.predecessor.node_id) + ": " + "finger:");
							int[] fingers = id.fingertable.getfingers();
							for (int i = 0; i < fingers.length; i++) {
								System.out.print(fingers[i]);
								if (i != (fingers.length - 1)) System.out.print(", ");
							}
							System.out.println();
						} else {
							System.out.println("Node " + Integer.parseInt(input[1]) + " doesn't exist");
						}
					}
					else
					{
						System.out.println("ERROR: Enter a valid number");
				}
			}
			else {
				System.out.println(" SYNTAX ERROR: Show expects 1 parameter not "+(input.length-1));
			}
		}


		else if (input[0].equalsIgnoreCase("join"))
		{
			if (input.length == 3) {
				if (isinteger(input[2]) && isinteger(input[1]))
				{
					if (c.nodemap.containsKey(Integer.parseInt(input[2])) && c.nodemap.containsKey(Integer.parseInt(input[1]))) {
						Node newnode = c.nodemap.get(Integer.parseInt(input[1]));
						Node oldnode = c.nodemap.get(Integer.parseInt(input[2]));
						c.join(newnode, oldnode);
					} else if (c.nodemap.containsKey(Integer.parseInt(input[2])) && !c.nodemap.containsKey(Integer.parseInt(input[1]))) {
						System.out.println("Add the node and then join ");
					} else {
						System.out.println("ERROR: Node " + Integer.parseInt(input[2]) + " does not exist");
					}
				}
				else
					{
						System.out.println("ERROR: Enter a valid number");
					}
			}
			else {
				System.out.println(" SYNTAX ERROR: Join expects 2 parameters not "+(input.length-1));
			}
		}


		else if (input[0].equalsIgnoreCase("drop")) {
			if (input.length == 2) {
				String id = input[1];
				if (isinteger(input[1])) {
					Node node_del = c.nodemap.get(Integer.parseInt(id));
					if (node_del == null)
						System.out.println("ERROR: Node " + id + " does not exist");
					else if (c.nodemap.containsKey(node_del.node_id)) {
						c.leave(node_del);
						int start = c.nodequeue.peek();
						Node cur = c.nodemap.get(start);
						Node begin = cur;
					}
				} else {
					System.out.println("ERROR : Enter a valid number");
				}
			}
			else {
				System.out.println("SYNTAX ERROR: Drop expects 1 parameter not "+(input.length-1));
			}
		}


		else if (input[0].equalsIgnoreCase("stab")) {
			if (input.length == 2) {
				if (isinteger((input[1]))) {
					int id = Integer.parseInt(input[1]);
					Node temp = c.nodemap.get(id);
					if (temp != null) {
						temp.stabilize();
					} else {
						System.out.println(" Node " + Integer.parseInt(input[1]) + " doesn't exist in the ring");
					}
				} else {
					System.out.println("ERROR : Enter a valid number");
				}
			}
			else {
				System.out.println(" SYNTAX ERROR: Stab expects 1 parameter not "+(input.length-1));
			}
		}
		else
		{
			System.out.println(" SYNTAX ERROR: Invalid command");
		}
	}

}
		
		