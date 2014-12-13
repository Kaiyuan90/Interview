import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;


public class SkyLine {

	static class Building {
		int left;
		int right;
		int height;
		public Building(int l, int r, int h) {
			this.left = l;
			this.right = r;
			this.height = h;
		}
	}
	
	static class Point {
		int u;
		int v;
		public Point(int u, int v) {
			this.u = u;
			this.v = v;
			
		}
	}
	
	static class HalfPoint {
		int val;
		boolean left;
		Building b;
		public HalfPoint(int val, boolean left, Building b) {
			this.val = val;
			this.left = left;
			this.b = b;
		}
	}
	
	public static List<Point> sykline(List<Building> builds) {
		if (builds == null) {
			throw new IllegalArgumentException();
		}
		
		// heap to store the current active buildings
		PriorityQueue<Building> active = new PriorityQueue<Building>(11, new Comparator<Building>() {
			@Override
			public int compare(Building o1, Building o2) {
				return o2.height - o1.height;
			}
		});
		// add a dummy node into the heap
		active.add(new Building(Integer.MIN_VALUE, Integer.MAX_VALUE, 0));
		List<Point> result = new ArrayList<Point>();
		List<HalfPoint> hps = new ArrayList<HalfPoint>();
		for (Building b : builds) {
			hps.add(new HalfPoint(b.left, true, b));
			hps.add(new HalfPoint(b.right, false, b));
		}
		
		// sort the halfpoints based on the  x coordinate, if x is the same, higher height will in front 
		Collections.sort(hps, new Comparator<HalfPoint>() {
			@Override
			public int compare(HalfPoint o1, HalfPoint o2) {
				if (o1.val == o2.val) {
					return o2.b.height - o1.b.height;
				}
				return o1.val - o2.val;
			}
			
		});
		
		// traverse the list of halfpoints
		for (HalfPoint hp : hps) {
			// if this is a left bound of a building, we see if we should print and add it to heap
			if (hp.left) {
				if (active.peek().height < hp.b.height) {
					result.add(new Point(hp.val, active.peek().height));
					result.add(new Point(hp.val, hp.b.height));
				}
				active.add(hp.b);
			} 
			// if this is a right bound of a building, we remove the building and see if we should print
			else {
				active.remove(hp.b);
				if (hp.b.height > active.peek().height) {
					result.add(new Point(hp.b.right, hp.b.height));
					result.add(new Point(hp.b.right, active.peek().height));
				}
			}
		}
		return result;
	}
	
	
	public static void main(String[] args) {
		List<Building> bs = new ArrayList<Building>();
		bs.add(new Building(1, 3, 2));
		bs.add(new Building(2, 5, 3));
		bs.add(new Building(4, 7, 2));
		bs.add(new Building(10, 12, 2));
		bs.add(new Building(1, 11, 1));
		List<Point> p = sykline(bs);
		for (Point pp : p) {
			System.out.println("Point is " + pp.u + ": " + pp.v);
		}
	}

}
