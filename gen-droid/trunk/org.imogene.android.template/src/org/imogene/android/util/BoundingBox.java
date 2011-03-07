package org.imogene.android.util;

import java.util.ArrayList;

import org.imogene.android.Constants.Extras;

import android.os.Bundle;

public class BoundingBox {
	
	private int eastE6;
	private int northE6;
	private int southE6;
	private int westE6;
	private boolean enabled;
	
	public BoundingBox() {
	}
	
	public BoundingBox(int eastE6, int northE6, int southE6, int westE6, boolean enabled) {
		this.enabled = enabled;
		this.eastE6 = eastE6;
		this.northE6 = northE6;
		this.southE6 = southE6;
		this.westE6 = westE6;
	}
	
	public BoundingBox(double east, double north, double south, double west, boolean enabled) {
		this.enabled = enabled;
		this.eastE6 = (int) (east * 1E6);
		this.northE6 = (int) (north * 1E6);
		this.southE6 = (int) (south * 1E6);
		this.westE6 = (int) (west * 1E6);
	}

	public int getEastE6() {
		return eastE6;
	}
	
	public double getEast() {
		return (double) (eastE6 / 1E6);
	}

	public void setEastE6(int eastE6) {
		this.eastE6 = eastE6;
	}
	
	public void setEast(double east) {
		this.eastE6 = (int) (east * 1E6);
	}

	public int getNorthE6() {
		return northE6;
	}
	
	public double getNorth() {
		return (double) (northE6 / 1E6);
	}

	public void setNorthE6(int northE6) {
		this.northE6 = northE6;
	}
	
	public void setNorth(double north) {
		this.northE6 = (int) (north * 1E6);
	}

	public int getSouthE6() {
		return southE6;
	}
	
	public double getSouth() {
		return (double) (southE6 / 1E6);
	}

	public void setSouthE6(int southE6) {
		this.southE6 = southE6;
	}
	
	public void setSouth(double south) {
		this.southE6 = (int) (south * 1E6);
	}

	public int getWestE6() {
		return westE6;
	}
	
	public double getWest() {
		return (double) (westE6 / 1E6);
	}

	public void setWestE6(int westE6) {
		this.westE6 = westE6;
	}
	
	public void setWest(double west) {
		this.westE6 = (int) (west * 1E6);
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
					builder.append(';');
				}
				builder.append(bb.isEnabled() ? 1 : 0);
				builder.append(':');
				builder.append(bb.getEastE6());
				builder.append(':');
				builder.append(bb.getNorthE6());
				builder.append(':');
				builder.append(bb.getSouthE6());
				builder.append(':');
				builder.append(bb.getWestE6());
			}
			return builder.toString();
		}
	}
	
	public static final ArrayList<BoundingBox> fromString(String value) {
		if (value == null) {
			return null;
		}
		String[] boxes = value.split(";");
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
			east[i] = box.getEastE6();
			north[i] = box.getNorthE6();
			south[i] = box.getSouthE6();
			west[i] = box.getWestE6();
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
