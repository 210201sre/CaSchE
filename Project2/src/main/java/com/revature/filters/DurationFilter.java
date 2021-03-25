package com.revature.filters;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class DurationFilter extends OncePerRequestFilter{
	
	private static final Logger log = LoggerFactory.getLogger(DurationFilter.class);

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		long start = System.currentTimeMillis();
		MDC.put("Start", String.valueOf(start));
		try {
		
			// Continue on to the next filter
			filterChain.doFilter(request, response);
		} finally {
			
			long end = System.currentTimeMillis();
			
			MDC.put("Duration", String.format("%dms",  end - start));
			
			log.info("Request processed.");
			MDC.clear();
		}
		
		
	}

}
