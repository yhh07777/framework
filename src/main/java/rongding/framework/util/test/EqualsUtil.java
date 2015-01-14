package rongding.framework.util.test;

public final class EqualsUtil {

	static public boolean areEqual(boolean aThis, boolean aThat) {
		return aThis == aThat;
	}

	static public boolean areEqual(char aThis, char aThat) {
		return aThis == aThat;
	}

	static public boolean areEqual(long aThis, long aThat) {
		return aThis == aThat;
	}

	static public boolean areEqual(float aThis, float aThat) {
		return Float.floatToIntBits(aThis) == Float.floatToIntBits(aThat);
	}

	static public boolean areEqual(double aThis, double aThat) {
		return Double.doubleToLongBits(aThis) == Double.doubleToLongBits(aThat);
	}

	static public boolean areEqual(Object aThis, Object aThat) {
		return aThis == null ? aThat == null : aThis.equals(aThat);
	}
}
