package org.imogene.android.util;

import java.util.ArrayList;

import org.imogene.android.Constants.Extras;

import android.os.Bundle;

public class BoundingBox {
	
	private int east;
	private int north;
	private int south;
	private int west;
	private boolean enabled;
	
	public BoundingBox() {
		
	}
	
	public BoundingBox(int eastE6, int northE6, int southE6, int westE6, boolean enabled) {
		this.enabled = enabled;
		east = eastE6;
		north = northE6;
		south = southE6;
		west = westE6;
	}

	public int getEast() {
		return east;
	}

	public void setEast(int east) {
		this.east = east;
	}

	public int getNorth() {
		return north;
	}

	public void setNorth(int north) {
		this.north = north;
	}

	public int getSouth() {
		return south;
	}

	public void setSouth(int south) {
		this.south = south;
	}

	public int getWest() {
		return west;
	}

	public void setWest(int west) {
		this.west = west;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	
	public static final String toString(ArrayList<BoundingBox> boxes) {
		if (boxes == null || boxes.isEmpty()) {
			return null;
		} else {
			StringBuilder builder = new StringBuilder();
			boolean first = true;
			for (BoundingBox bb : boxes) {
				if (first) {
					first = false;
				} else {
					builder.append('|');
				}
				builder.append(bb.isEnabled() ? 1 : 0);
				builder.append(':');
				builder.append(bb.getEast());
				builder.append(':');
				builder.append(bb.getNorth());
				builder.append(':');
				builder.append(bb.getSouth());
				builder.append(':');
				builder.append(bb.getWest());
			}
			return builder.toString();
		}
	}

	public static final ArrayList<BoundingBox> fromString(String value) {
		if (value == null) {
			return null;
		}
		String[] boxes = value.split("|");
		if (boxes.length <= 0) {
			return null;
		}
		ArrayList<BoundingBox> result = new ArrayList<BoundingBox>(boxes.length);
		for (int i = 0; i < boxes.length; i++) {
			String[] values = boxes[i].split(":");
			if (values.length == 5) {
				boolean enabled = Integer.parseInt(values[0]) != 0;
				int east = Integer.parseInt(values[1]);
				int north = Integer.parseInt(values[2]);
				int south = Integer.parseInt(values[3]);
				int west = Integer.parseInt(values[4]);
				result.add(new BoundingBox(east, north, south, west, enabled));
			}
		}
		return result;
	}
	
	public static final Bundle toBundle(ArrayList<BoundingBox> boxes) {
		if (boxes == null || boxes.isEmpty()) {
			return null;
		}
		int size = boxes.size();
		boolean[] enabled = new boolean[size];
		int[] east = new int[size];
		int[] north = new int[size];
		int[] south = new int[size];
		int[] west = new int[size];
		for (int i = 0; i < size; i++) {
			BoundingBox box = boxes.get(i);
			enabled[i] = box.isEnabled();
			east[i] = box.getEast();
			north[i] = box.getNorth();
			south[i] = box.getSouth();
			west[i] = box.getWest();
		}
		Bundle result = new Bundle();
		result.putInt(Extras.EXTRA_BOXES_COUNT, size);
		result.putBooleanArray(Extras.EXTRA_ENABLED_BOXES, enabled);
		result.putIntArray(Extras.EXTRA_EAST_LONGITUDES, east);
		result.putIntArray(Extras.EXTRA_NORTH_LATITUDES, north);
		result.putIntArray(Extras.EXTRA_SOUTH_LATITUDES, south);
		result.putIntArray(Extras.EXTRA_WEST_LONGITUDES, west);
		return result;
	}
	
	public static final ArrayList<BoundingBox> fromBundle(Bundle bundle) {
		if (!isBundleValid(bundle)) {
			return null;
		}
		
		final int count = bundle.getInt(Extras.EXTRA_BOXES_COUNT);
		final boolean[] enabled = bundle.getBooleanArray(Extras.EXTRA_ENABLED_BOXES);
		final int[] east = bundle.getIntArray(Extras.EXTRA_EAST_LONGITUDES);
		final int[] north = bundle.getIntArray(Extras.EXTRA_NORTH_LATITUDES);
		final int[] south = bundle.getIntArray(Extras.EXTRA_SOUTH_LATITUDES);
		final int[] west = bundle.getIntArray(Extras.EXTRA_WEST_LONGITUDES);
		final ArrayList<BoundingBox> result = new ArrayList<BoundingBox>(count);
		for (int i = 0; i < count; i++) {
			result.add(new BoundingBox(east[i], north[i], south[i], west[i], enabled[i]));
		}
		return result;
	}
	
	private static final boolean isBundleValid(Bundle bundle) {
		return bundle != null &&
		bundle.containsKey(Extras.EXTRA_BOXES_COUNT) &&
		bundle.containsKey(Extras.EXTRA_EAST_LONGITUDES) &&
		bundle.containsKey(Extras.EXTRA_NORTH_LATITUDES) &&
		bundle.containsKey(Extras.EXTRA_SOUTH_LATITUDES) &&
		bundle.containsKey(Extras.EXTRA_WEST_LONGITUDES);
	}

}
