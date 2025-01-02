package com.uah.ismael.portal_formula1.paginator;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.ui.Model;

import java.util.List;

public class PageUtil {

	public static <T> PageImpl<T> sortedPageImpl(Pageable pageable, List<T> list){
		int start = Math.min((int) pageable.getOffset(), list.size());
		int end = Math.min((start + pageable.getPageSize()), list.size());

		return new PageImpl<>(list.subList(start, end), pageable, list.size());
	}

	public static <T> void addPaginationAttributes(Model model, Page<T> page, int currentPage, String sortField, String sortDir) {
		model.addAttribute("elements", page);
		model.addAttribute("currentPage", currentPage);
		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
	}

}
