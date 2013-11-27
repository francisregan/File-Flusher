package com.pirate3d.piratefileflusher.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class FileTypeConverter {
	double valueInMB;

	public int fromMegabytestoGigabytes(int value){
		return 0;
	}
	
	public static double fromBytestoMegabytes(double value){
		double valInBytes =  value / 1048576;
		BigDecimal bd = new BigDecimal(valInBytes).setScale(2, RoundingMode.HALF_EVEN);
		return(bd.doubleValue());
	}
	
	public double fromGigabytestoMegabytes(int value){
		return value * 1024;
	}
}
