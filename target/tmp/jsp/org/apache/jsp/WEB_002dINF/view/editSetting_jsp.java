package org.apache.jsp.WEB_002dINF.view;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;

public final class editSetting_jsp extends org.apache.jasper.runtime.HttpJspBase
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
      response.setContentType("text/html; charset=UTF-8");
      pageContext = _jspxFactory.getPageContext(this, request, response,
      			null, true, 8192, true);
      _jspx_page_context = pageContext;
      application = pageContext.getServletContext();
      config = pageContext.getServletConfig();
      session = pageContext.getSession();
      out = pageContext.getOut();
      _jspx_out = out;
      _jspx_resourceInjector = (org.glassfish.jsp.api.ResourceInjector) application.getAttribute("com.sun.appserv.jsp.resource.injector");

      out.write("\n");
      out.write(" \n");
      out.write("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">\n");
      out.write("<html>\n");
      out.write("<head>\n");
      out.write("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\n");
      out.write("<title>Insert title here</title>\n");
      out.write("</head>\n");
      out.write("<body>\n");
      out.write("<form action=\"panel?action=commitEditSetting\" method=\"post\">\n");
      out.write("<label>Title</label><input type=\"text\" name=\"title\" value=\"");
      out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.evaluateExpression("${setting.getTitle()}", java.lang.String.class, (PageContext)_jspx_page_context, null));
      out.write(" \"/>\n");
      out.write("<label>Description</label><input type=\"text\" name=\"description\" value=\"");
      out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.evaluateExpression("${setting.getDescription()}", java.lang.String.class, (PageContext)_jspx_page_context, null));
      out.write(" \"/>\n");
      out.write("<label>Domain</label><input type=\"text\" name=\"domain\" value=\"");
      out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.evaluateExpression("${setting.getDomain()}", java.lang.String.class, (PageContext)_jspx_page_context, null));
      out.write(" \"/>\n");
      out.write("<label>Article Per Page</label><input type=\"text\" name=\"articlePerPage\" value=\"");
      out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.evaluateExpression("${setting.getArticlePerPage()}", java.lang.String.class, (PageContext)_jspx_page_context, null));
      out.write(" \"/>\n");
      out.write("<label>DiqusId</label><input type=\"text\" name=\"diqusId\" value=\"");
      out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.evaluateExpression("${setting.getDiqusId()}", java.lang.String.class, (PageContext)_jspx_page_context, null));
      out.write(" \"/>\n");
      out.write("<label>Footer</label><input type=\"text\" name=\"footer\" value=\"");
      out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.evaluateExpression("${setting.getFooter()}", java.lang.String.class, (PageContext)_jspx_page_context, null));
      out.write(" \"/>\n");
      out.write("<input type=\"submit\" value=\"Finish\"/>\n");
      out.write("</form>\n");
      out.write("</body>\n");
      out.write("</html>\n");
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
