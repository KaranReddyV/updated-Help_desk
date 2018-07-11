package com.ojas.ticket.transfer;

public class Pagination {
	public static Long maximunSecondIndex(Long listTotal, Long maxIndex) {

		Long maxIndexTwo = maxIndex - (maxIndex - listTotal);
		return maxIndexTwo;

	}

	public static Long maximumIndex(Long pageno, Long pagesize) {
		Long maxIndex = ((pageno * pagesize));
		return maxIndex;

	}

	public static Long minimumIndex(Long pageno, Long pagesize) {
		Long minIndex = ((pageno - 1) * pagesize);
		return minIndex;

	}

	public static Long pageSize(Long pagesize, Long listTotal) {
		Long noOfPages = (listTotal / pagesize + ((listTotal % pagesize) > 0 ? 1 : 0));
		return noOfPages;
	}

}
