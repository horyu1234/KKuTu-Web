package me.horyu.kkutuweb.minify

import com.googlecode.htmlcompressor.compressor.HtmlCompressor
import javax.servlet.Filter
import javax.servlet.FilterChain
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.http.HttpServletResponse

class MinifyFilter : Filter {
    private var htmlCompressor = HtmlCompressor()

    override fun doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain) {
        val responseWrapper = CharResponseWrapper(response as HttpServletResponse)
        chain.doFilter(request, responseWrapper)

        val servletResponse = responseWrapper.toString()
        if (!response.isCommitted()) {
            response.getWriter().write(htmlCompressor.compress(servletResponse))
        }
    }

    override fun destroy() {}
}