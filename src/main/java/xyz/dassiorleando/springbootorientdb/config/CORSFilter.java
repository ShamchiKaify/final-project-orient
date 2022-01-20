package xyz.dassiorleando.springbootorientdb.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CORSFilter implements Filter {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    // This is to be replaced with a list of domains allowed to access the server
    //You can include more than one origin here
    public static final List<String> ALLOWED_ORIGINS = Arrays.asList(
            //  Localhost
            "http://localhost", "http://localhost:3000",

            //  now.sh
            "http://react-newspaper-archive.now.sh", "https://react-newspaper-archive.now.sh"
    );

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        // Let's make sure that we are working with HTTP (that is, against HttpServletRequest and HttpServletResponse objects)
        if (req instanceof HttpServletRequest && res instanceof HttpServletResponse) {
            HttpServletRequest request = (HttpServletRequest) req;
            HttpServletResponse response = (HttpServletResponse) res;

            // Access-Control-Allow-Origin
            String origin = request.getHeader("Origin");
            //  To restrict requests from the outside of the list above.
//            response.setHeader("Access-Control-Allow-Origin", ALLOWED_ORIGINS.contains(origin) ? origin : "*");
            //  Allow every request from everyone.
            response.setHeader("Access-Control-Allow-Origin", "*");
            response.setHeader("Access-Control-Allow-Headers","Access-Control-Allow-Origin, Content-Type");

            logger.error("----------");
            logger.info("Current Method: " + ((HttpServletRequest) req).getMethod());
            response.getHeaderNames().forEach(item -> {
                logger.info(item + " ---> " + response.getHeader(item));
            });
            logger.error("----------");
        }
        chain.doFilter(req, res);
    }

    @Override
    public void init(FilterConfig filterConfig) {
    }

    @Override
    public void destroy() {
    }

}