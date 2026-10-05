package com.example.EnterpriseSystemsArchitecture.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component
public class XslResponseFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);
        filterChain.doFilter(request, responseWrapper);

        String contentType = responseWrapper.getContentType();
        String uri = request.getRequestURI();

        if (contentType != null && (contentType.contains("application/xml") || contentType.contains("text/xml"))) {
            String xslPath = null;
            if (uri.startsWith("/api/players")) {
                xslPath = "/xsl/players.xsl";
            } else if (uri.startsWith("/api/guilds")) {
                xslPath = "/xsl/guilds.xsl";
            }

            if (xslPath != null) {
                byte[] originalBody = responseWrapper.getContentAsByteArray();
                String xml = new String(originalBody, StandardCharsets.UTF_8);

                String pi = "<?xml-stylesheet type=\"text/xsl\" href=\"" + xslPath + "\"?>\n";

                if (xml.startsWith("<?xml")) {
                    int declEnd = xml.indexOf("?>");
                    if (declEnd != -1) {
                        xml = xml.substring(0, declEnd + 2) + "\n" + pi + xml.substring(declEnd + 2).trim();
                    } else {
                        xml = pi + xml;
                    }
                } else {
                    xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + pi + xml;
                }

                byte[] modifiedBody = xml.getBytes(StandardCharsets.UTF_8);
                response.setContentLength(modifiedBody.length);
                response.getOutputStream().write(modifiedBody);
                return;
            }
        }

        responseWrapper.copyBodyToResponse();
    }
}
