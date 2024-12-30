package com.uah.ismael.portal_formula1.paginator;

import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

public class PageUtil {

	public static <T> PageImpl<T> sortedPageImpl(Pageable pageable, List<T> list){
		int start = Math.min((int) pageable.getOffset(), list.size());
		int end = Math.min((start + pageable.getPageSize()), list.size());

		return new PageImpl<>(list.subList(start, end), pageable, list.size());
	}

}
