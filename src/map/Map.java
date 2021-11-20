package map;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;

import json.JSONArray;
import json.JSONException;
import json.JSONObject;
import json.JSONUtil;
import main.GameObject;
import resources.Sprite;

public class Map extends GameObject {

	private JSONObject params;
	private Sprite bgSprite;
	
	private ArrayList<PathNode> startNodes;
	private ArrayList<PathNode> endNodes;
	HashMap<String, PathNode> nodeIdMap;
	
	public Map (String path) {
		
		//Load JSON data
		try {
			params = JSONUtil.loadJSONFile (path + "params.json");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		JSONObject bgParams = params.getJSONObject ("bg");
		JSONObject pathParams = params.getJSONObject ("path");
		
		//Load bg data
		bgSprite = new Sprite (path + bgParams.getString ("img"));
		setSprite (bgSprite);
		
		//Loading the path data
		startNodes = new ArrayList<PathNode> ();
		endNodes = new ArrayList<PathNode> ();
		nodeIdMap = new HashMap<String, PathNode> ();
		
		//Parse start nodes
		JSONArray jsonStartNodes = pathParams.getJSONArray ("start");
		for (int i = 0; i < jsonStartNodes.getContents ().size (); i++) {
			JSONObject curr = (JSONObject)jsonStartNodes.get (i);
			String nid = curr.getString ("id");
			int nx = curr.getInt ("x");
			int ny = curr.getInt ("y");
			PathNode currNode = new PathNode (new Point (nx, ny), nid);
			currNode.setStart ();
			startNodes.add (currNode);
		}
		
		//Parse end nodes
		JSONArray jsonEndNodes = pathParams.getJSONArray ("end");
		for (int i = 0; i < jsonEndNodes.getContents ().size (); i++) {
			JSONObject curr = (JSONObject)jsonEndNodes.get (i);
			String nid = curr.getString ("id");
			int nx = curr.getInt ("x");
			int ny = curr.getInt ("y");
			PathNode currNode = new PathNode (new Point (nx, ny), nid);
			currNode.setEnd ();
			endNodes.add (currNode);
		}
		
		//Parse in-between nodes and construct the path
		JSONArray pathsNodes = pathParams.getJSONArray ("paths");
		for (int i = 0; i < pathsNodes.getContents ().size (); i++) {
			JSONObject currPath = (JSONObject)pathsNodes.get (i);
			String startId = currPath.getString ("start");
			String endId = currPath.getString ("end");
			JSONArray currNodes = currPath.getJSONArray ("nodes");
			String prev = null;
			if (currNodes == null || currNodes.getContents ().size () == 0) {
				nodeIdMap.get (startId).addLinkTo (endId);
			} else {
				for (int j = 0; j < currNodes.getContents ().size (); j++) {
					JSONObject curr = (JSONObject)currNodes.get (j);
					String nid = curr.getString ("id");
					int nx = curr.getInt ("x");
					int ny = curr.getInt ("y");
					PathNode currNode = new PathNode (new Point (nx, ny), nid);
					if (j == 0 && startId != null) {
						//Link the start
						nodeIdMap.get (startId).addLinkTo (nid);
					}
					if (j == currNodes.getContents ().size () - 1 && endId != null) {
						//Link the end
						currNode.addLinkTo (endId);
					}
					if (prev != null) {
						//Link prev node
						nodeIdMap.get (prev).addLinkTo (nid);
					}
					prev = nid;
				}
			}
		}
		
		//Resolve the links
		Set<Entry<String, PathNode>> entries = nodeIdMap.entrySet ();
		Iterator<Entry<String, PathNode>> iter = entries.iterator ();
		while (iter.hasNext ()) {
			iter.next ().getValue ().resolveLinks ();
		}
		
	}
	
	public PathNode getStartNode () {
		if (startNodes.size () == 1) {
			return startNodes.get (0);
		} else {
			return startNodes.get ((int)(Math.random () * startNodes.size ()));
		}
	}
	
	public class PathNode {
		
		Point position;
		String id;
		ArrayList<PathNode> next;
		ArrayList<String> nextIds;
		boolean isStart;
		boolean isEnd;
		
		public PathNode (Point pos, String id) {
			
			//Save position and id
			position = pos;
			this.id = id;
			nodeIdMap.put (id, this);
			
			//Initialize next list
			next = new ArrayList<PathNode> ();
			nextIds = new ArrayList<String> ();
			
		}
		
		public void addLinkTo (String s) {
			nextIds.add (s);
		}
		
		public void resolveLinks () {
			for (int i = 0; i < nextIds.size (); i++) {
				next.add (nodeIdMap.get (nextIds.get (i)));
			}
		}
		
		public Point getPosition () {
			return position;
		}
		
		public PathNode getNext () {
			if (isEnd ()) {
				return null;
			}
			if (next.size () == 1) {
				return next.get (0);
			} else {
				return next.get ((int)(Math.random () * next.size ()));
			}
		}
		
		public void setStart () {
			isStart = true;
		}
		
		public void setEnd () {
			isEnd = true;
		}
		
		public boolean isStart () {
			return isStart;
		}
		
		public boolean isEnd () {
			return isEnd;
		}
		
		public void printPath () {
			System.out.println (id);
			for (int i = 0; i < next.size (); i++) {
				PathNode n = next.get (i);
				n.printPath ();
			}
		}
		
	}
	
}
