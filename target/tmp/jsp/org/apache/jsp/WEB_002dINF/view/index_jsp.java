package org.apache.jsp.WEB_002dINF.view;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;

public final class index_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent {

  private static final JspFactory _jspxFactory = JspFactory.getDefaultFactory();

  private static java.util.List<String> _jspx_dependants;

  private org.glassfish.jsp.api.ResourceInjector _jspx_resourceInjector;

  public java.util.List<String> getDependants() {
    return _jspx_dependants;
  }

  public void _jspService(HttpServletRequest request, HttpServletResponse response)
        throws java.io.IOException, ServletException {

    PageContext pageContext = null;
    HttpSession session = null;
    ServletContext application = null;
    ServletConfig config = null;
    JspWriter out = null;
    Object page = this;
    JspWriter _jspx_out = null;
    PageContext _jspx_page_context = null;

    try {
      response.setContentType("text/html");
      pageContext = _jspxFactory.getPageContext(this, request, response,
      			null, true, 8192, true);
      _jspx_page_context = pageContext;
      application = pageContext.getServletContext();
      config = pageContext.getServletConfig();
      session = pageContext.getSession();
      out = pageContext.getOut();
      _jspx_out = out;
      _jspx_resourceInjector = (org.glassfish.jsp.api.ResourceInjector) application.getAttribute("com.sun.appserv.jsp.resource.injector");

      out.write("<!DOCTYPE html>\n");
      out.write("<html lang=\"en\">\n");
      out.write("    <head>\n");
      out.write("        <title>GitHubPager!</title>\n");
      out.write("        <meta charset=\"UTF-8\" />\n");
      out.write("        <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge,chrome=1\"> \n");
      out.write("        <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">  \n");
      out.write("        <meta name=\"keywords\" content=\"css3, transitions, menu, blur, navigation, typography, font, letters, text-shadow\" />\n");
      out.write("        <meta name=\"author\" content=\"Codrops\" />\n");
      out.write("        \n");
      out.write("        <link rel=\"stylesheet\" type=\"text/css\" href=\"/css/demo.css\" />\n");
      out.write("        <link rel=\"stylesheet\" type=\"text/css\" href=\"/css/style.css\" />\n");
      out.write("\t\t<!--[if IE]>\n");
      out.write("\t\t\t<link rel=\"stylesheet\" type=\"text/css\" href=\"css/styleIE.css\" />\n");
      out.write("\t\t<![endif]-->\n");
      out.write("        <link href='http://fonts.googleapis.com/css?family=Josefin+Slab' rel='stylesheet' type='text/css' />\n");
      out.write("    </head>\n");
      out.write("    <body style=\"background-image:  url(/images/pattern.png),url(/images/bg.jpg);\">\n");
      out.write("        <div class=\"container\">\n");
      out.write("            <h1>GitHubPager! <span>Easily blogging in GitHub</span></h1>\n");
      out.write("            <div class=\"content\">\n");
      out.write("                <ul class=\"bmenu\">\n");
      out.write("                    <li><a href=\"login\">Start blogging in GitHub</a></li>\n");
      out.write("                    <li><a href=\"about\">Instruction</a></li>\n");
      out.write("                    <li><a href=\"https://github.com/GitHubPager/GitHubPager\">Project Development</a></li>\n");
      out.write("                    <li><a href=\"https://github.com/GitHubPager/GitHubPager\">Theme Design</a></li>\n");
      out.write("                    <li><a href=\"https://github.com/elliott-wen\">Contact</a></li>\n");
      out.write("                </ul>\n");
      out.write("            </div>\n");
      out.write("        </div>\n");
      out.write("        <script type=\"text/javascript\" src=\"http://ajax.googleapis.com/ajax/libs/jquery/1.6.4/jquery.min.js\"></script>\n");
      out.write("    </body>\n");
      out.write("</html>");
    } catch (Throwable t) {
      if (!(t instanceof SkipPageException)){
        out = _jspx_out;
        if (out != null && out.getBufferSize() != 0)
          out.clearBuffer();
        if (_jspx_page_context != null) _jspx_page_context.handlePageException(t);
        else throw new ServletException(t);
      }
    } finally {
      _jspxFactory.releasePageContext(_jspx_page_context);
    }
  }
}
