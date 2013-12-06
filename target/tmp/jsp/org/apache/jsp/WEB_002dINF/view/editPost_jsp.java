package org.apache.jsp.WEB_002dINF.view;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;

public final class editPost_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent {

  private static final JspFactory _jspxFactory = JspFactory.getDefaultFactory();

  private static java.util.List<String> _jspx_dependants;

  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_c_out_value_escapeXml_nobody;

  private org.glassfish.jsp.api.ResourceInjector _jspx_resourceInjector;

  public java.util.List<String> getDependants() {
    return _jspx_dependants;
  }

  public void _jspInit() {
    _jspx_tagPool_c_out_value_escapeXml_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
  }

  public void _jspDestroy() {
    _jspx_tagPool_c_out_value_escapeXml_nobody.release();
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

      out.write("<!DOCTYPE html>\n");
      out.write("\n");
      out.write(" \n");
      out.write("<html>\n");
      out.write("<head>\n");
      out.write("<script src=\"http://code.jquery.com/jquery-1.9.1.js\"></script>\n");
      out.write("<script src=\"http://tinymce.cachefly.net/4.0/tinymce.min.js\"></script><script type=\"text/javascript\" src=\"js/jquery.backstretch.min.js\"></script>\n");
      out.write("<script type=\"text/javascript\" src=\"https://www.dropbox.com/static/api/1/dropins.js\" id=\"dropboxjs\" data-app-key=\"bf1mlms18dgzf6g\"></script>\n");
      out.write("<link rel=\"stylesheet\" type=\"text/css\" href=\"css/demo_1.css\" />\n");
      out.write("<link rel=\"stylesheet\" type=\"text/css\" href=\"css/style_1.css\" />\n");
      out.write("<link rel=\"stylesheet\" type=\"text/css\" href=\"css/button.css\" />\n");
      out.write(" <link href='http://fonts.googleapis.com/css?family=Josefin+Slab:400,700' rel='stylesheet' type='text/css' />\n");
      out.write("<script>\n");
      out.write("        tinymce.init({selector:'textarea',\n");
      out.write("        height:600,\n");
      out.write("\t\tplugins: [\n");
      out.write("        \"advlist autolink lists link image charmap print preview anchor\",\n");
      out.write("        \"searchreplace visualblocks code fullscreen\",\n");
      out.write("        \"insertdatetime media table contextmenu paste\"\n");
      out.write("\t\t],\n");
      out.write("\t\ttoolbar: \"insertfile undo redo | styleselect | bold italic | alignleft aligncenter alignright alignjustify | bullist numlist outdent indent | link image media\"\n");
      out.write("\t\t});\n");
      out.write("</script>\n");
      out.write("</head>\n");
      out.write("<body>\n");
      out.write("<div class=\"container\" style=\"width:60%;margin:auto;\">\n");
      out.write("<h1>Edit Post</h1>\n");
      out.write("<form action=\"panel?action=commitEditPost\" method=\"post\">\n");
      out.write("<label for=\"title\" class=\"button danger\">Title:</label><input class=\"button big\" type=\"text\" name=\"title\" style=\"width:80%\" value=\"");
      out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.evaluateExpression("${entry.getTitle()}", java.lang.String.class, (PageContext)_jspx_page_context, null));
      out.write("\"><br><br>\n");
      out.write("<textarea name=\"content\">\n");
      if (_jspx_meth_c_out_0(_jspx_page_context))
        return;
      out.write("</textarea>\n");
      out.write("<input type=\"dropbox-chooser\" name=\"selected-file\"  id=\"db-chooser\"/>\n");
      out.write("<input type=\"hidden\" name=\"repositoryName\" value=\"");
      out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.evaluateExpression("${repository}", java.lang.String.class, (PageContext)_jspx_page_context, null));
      out.write("\"/>\n");
      out.write("<input type=\"hidden\" name=\"entryId\" value=\"");
      out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.evaluateExpression("${entry.getId()}", java.lang.String.class, (PageContext)_jspx_page_context, null));
      out.write("\"/>\n");
      out.write("<input type=\"submit\" class=\"button pill\" value=\"Submit\"/>\n");
      out.write("</form>\n");
      out.write("</div>\n");
      out.write("<script type=\"text/javascript\">\n");
      out.write("    // add an event listener to a Chooser button\n");
      out.write("    document.getElementById(\"db-chooser\").addEventListener(\"DbxChooserSuccess\",\n");
      out.write("        function(e) {\n");
      out.write("\t\t\tvar dlLink=e.files[0].link.replace(\"https://www\",\"https://dl\");\n");
      out.write("            alert(\"Here's the chosen file: \" + dlLink);\n");
      out.write("        }, false);\n");
      out.write("</script>\n");
      out.write("<script type=\"text/javascript\" src=\"js/jquery.backstretch.min.js\"></script>\n");
      out.write("<script type=\"text/javascript\">\n");
      out.write("            $(function() {\n");
      out.write("\t\t\t\t$.backstretch(\"images/bg1.jpg\");\n");
      out.write("            });\n");
      out.write("</script>\n");
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

  private boolean _jspx_meth_c_out_0(PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  c:out
    org.apache.taglibs.standard.tag.rt.core.OutTag _jspx_th_c_out_0 = (org.apache.taglibs.standard.tag.rt.core.OutTag) _jspx_tagPool_c_out_value_escapeXml_nobody.get(org.apache.taglibs.standard.tag.rt.core.OutTag.class);
    _jspx_th_c_out_0.setPageContext(_jspx_page_context);
    _jspx_th_c_out_0.setParent(null);
    _jspx_th_c_out_0.setValue((java.lang.Object) org.apache.jasper.runtime.PageContextImpl.evaluateExpression("${entry.getContent()}", java.lang.Object.class, (PageContext)_jspx_page_context, null));
    _jspx_th_c_out_0.setEscapeXml(true);
    int _jspx_eval_c_out_0 = _jspx_th_c_out_0.doStartTag();
    if (_jspx_th_c_out_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _jspx_tagPool_c_out_value_escapeXml_nobody.reuse(_jspx_th_c_out_0);
      return true;
    }
    _jspx_tagPool_c_out_value_escapeXml_nobody.reuse(_jspx_th_c_out_0);
    return false;
  }
}
