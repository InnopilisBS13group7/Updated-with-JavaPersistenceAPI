/*
 * Generated by the Jasper component of Apache Tomcat
 * Version: jetty/9.2.11.v20150529
 * Generated at: 2018-04-04 00:34:05 UTC
 * Note: The last modified time of this file was set to
 *       the last modified time of the source file after
 *       generation to assist with modification tracking.
 */
package org.apache.jsp.WEB_002dINF.views;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;

public final class index_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent {

  private static final javax.servlet.jsp.JspFactory _jspxFactory =
          javax.servlet.jsp.JspFactory.getDefaultFactory();

  private static java.util.Map<java.lang.String,java.lang.Long> _jspx_dependants;

  static {
    _jspx_dependants = new java.util.HashMap<java.lang.String,java.lang.Long>(4);
    _jspx_dependants.put("file:/C:/Users/Пользователь/.m2/repository/org/springframework/spring-webmvc/4.2.2.RELEASE/spring-webmvc-4.2.2.RELEASE.jar", Long.valueOf(1522797808257L));
    _jspx_dependants.put("jar:file:/C:/Users/Пользователь/.m2/repository/org/apache/taglibs/taglibs-standard-impl/1.2.1/taglibs-standard-impl-1.2.1.jar!/META-INF/c.tld", Long.valueOf(1384354062000L));
    _jspx_dependants.put("file:/C:/Users/Пользователь/.m2/repository/org/apache/taglibs/taglibs-standard-impl/1.2.1/taglibs-standard-impl-1.2.1.jar", Long.valueOf(1517152036102L));
    _jspx_dependants.put("jar:file:/C:/Users/Пользователь/.m2/repository/org/springframework/spring-webmvc/4.2.2.RELEASE/spring-webmvc-4.2.2.RELEASE.jar!/META-INF/spring.tld", Long.valueOf(1444875264000L));
  }

  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fc_005fset_0026_005fvar_005fvalue_005fnobody;

  private javax.el.ExpressionFactory _el_expressionfactory;
  private org.apache.tomcat.InstanceManager _jsp_instancemanager;

  public java.util.Map<java.lang.String,java.lang.Long> getDependants() {
    return _jspx_dependants;
  }

  public void _jspInit() {
    _005fjspx_005ftagPool_005fc_005fset_0026_005fvar_005fvalue_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _el_expressionfactory = _jspxFactory.getJspApplicationContext(getServletConfig().getServletContext()).getExpressionFactory();
    _jsp_instancemanager = org.apache.jasper.runtime.InstanceManagerFactory.getInstanceManager(getServletConfig());
  }

  public void _jspDestroy() {
    _005fjspx_005ftagPool_005fc_005fset_0026_005fvar_005fvalue_005fnobody.release();
  }

  public void _jspService(final javax.servlet.http.HttpServletRequest request, final javax.servlet.http.HttpServletResponse response)
        throws java.io.IOException, javax.servlet.ServletException {

    final javax.servlet.jsp.PageContext pageContext;
    javax.servlet.http.HttpSession session = null;
    final javax.servlet.ServletContext application;
    final javax.servlet.ServletConfig config;
    javax.servlet.jsp.JspWriter out = null;
    final java.lang.Object page = this;
    javax.servlet.jsp.JspWriter _jspx_out = null;
    javax.servlet.jsp.PageContext _jspx_page_context = null;


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

      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      if (_jspx_meth_c_005fset_005f0(_jspx_page_context))
        return;
      out.write("\r\n");
      out.write("\r\n");
      out.write("<!DOCTYPE html>\r\n");
      out.write("<html>\r\n");
      out.write("    <head>\r\n");
      out.write("        <meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\"/>\r\n");
      out.write("        ");
      out.write("\r\n");
      out.write("        ");
      out.write("\r\n");
      out.write("        ");
      out.write("\r\n");
      out.write("        ");
      out.write("\r\n");
      out.write("        ");
      out.write("\r\n");
      out.write("        <meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\"/>\r\n");
      out.write("        <script type=\"text/javascript\" src=\"");
      out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${contextPath}", java.lang.String.class, (javax.servlet.jsp.PageContext)_jspx_page_context, null));
      out.write("/resources/scripts/jquery.js\"></script>\r\n");
      out.write("        <script type=\"text/javascript\" src=\"");
      out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${contextPath}", java.lang.String.class, (javax.servlet.jsp.PageContext)_jspx_page_context, null));
      out.write("/resources/scripts/main.js\"></script>\r\n");
      out.write("        <script type=\"text/javascript\" src=\"");
      out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${contextPath}", java.lang.String.class, (javax.servlet.jsp.PageContext)_jspx_page_context, null));
      out.write("/resources/scripts/for_all.js\"></script>\r\n");
      out.write("        <link href=\"");
      out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${contextPath}", java.lang.String.class, (javax.servlet.jsp.PageContext)_jspx_page_context, null));
      out.write("/resources/style/main.css\" rel=\"stylesheet\">\r\n");
      out.write("        <style id=\"style20\"></style>\r\n");
      out.write("        <title>BookBooking</title>\r\n");
      out.write("    </head>\r\n");
      out.write("    <body>\r\n");
      out.write("        <div id=\"main\">\r\n");
      out.write("            <p id=\"topic\">BookBooking</p>\r\n");
      out.write("            <div id=\"main_button_box\">\r\n");
      out.write("                <div id=\"enter_error\">Forgot your password?</div>\r\n");
      out.write("                <div id=\"enter_error_back\"></div>\r\n");
      out.write("                <div id=\"enter_inputs_box\">\r\n");
      out.write("                    <input type=\"email\" class=\"enter_inputs\" id=\"enter_email\" placeholder=\"email\">\r\n");
      out.write("                    <input type=\"password\" class=\"enter_inputs\" id=\"enter_password\" placeholder=\"Password\">\r\n");
      out.write("                </div>\r\n");
      out.write("                <div class=\"main_buttons\" id=\"enter\">Sign in</div>\r\n");
      out.write("                <div id=\"reg_inputs_box\">\r\n");
      out.write("                    <div class=\"error\" id=\"reg_name_error\"></div>\r\n");
      out.write("                    <div class=\"error\" id=\"reg_surname_error\"></div>\r\n");
      out.write("                    <div class=\"error\" id=\"reg_email_error\"></div>\r\n");
      out.write("                    <div class=\"error\" id=\"reg_password_error\"></div>\r\n");
      out.write("                    <div class=\"error\" id=\"reg_password20_error\"></div>\r\n");
      out.write("                    <div class=\"error_more\" id=\"error_more_name\">\r\n");
      out.write("                        <div class=\"error_more_arrow\"></div>\r\n");
      out.write("                        The name must not be longer than 20 characters and must be written in English\r\n");
      out.write("                    </div>\r\n");
      out.write("                    <div class=\"error_more\" id=\"error_more_surname\">\r\n");
      out.write("                        <div class=\"error_more_arrow\"></div>\r\n");
      out.write("                        The surname must not be longer than 20 characters and must be written in English\r\n");
      out.write("                    </div>\r\n");
      out.write("                    <div class=\"error_more\" id=\"error_more_email\">\r\n");
      out.write("                        <div class=\"error_more_arrow\"></div>\r\n");
      out.write("                        email must end with \"@innopolis.ru\"\r\n");
      out.write("                    </div>\r\n");
      out.write("                    <div class=\"error_more\" id=\"error_more_password\">\r\n");
      out.write("                        <div class=\"error_more_arrow\"></div>\r\n");
      out.write("                        Password must be at least 8 letters, but no more than 21. It can include English letters, digits and special characters\r\n");
      out.write("                    </div>\r\n");
      out.write("                    <div class=\"error_more\" id=\"error_more_password20\">\r\n");
      out.write("                        <div class=\"error_more_arrow\"></div>\r\n");
      out.write("                        Both passwords must be the same\r\n");
      out.write("                    </div>\r\n");
      out.write("                    <input type=\"text\" class=\"reg_inputs\" id=\"reg_name\" placeholder=\"Name\">\r\n");
      out.write("                    <input type=\"text\" class=\"reg_inputs\" id=\"reg_surname\" placeholder=\"Surname\">\r\n");
      out.write("                    <input type=\"email\" class=\"reg_inputs\" id=\"reg_email\" placeholder=\"email\">\r\n");
      out.write("                    <input type=\"password\" class=\"reg_inputs\" id=\"reg_password\" placeholder=\"Password\">\r\n");
      out.write("                    <input type=\"password\" class=\"reg_inputs\" id=\"reg_password20\" placeholder=\"Repeat password\">\r\n");
      out.write("                </div>\r\n");
      out.write("                <div class=\"main_buttons\" id=\"reg\">Sign up</div>\r\n");
      out.write("                <div id=\"reg_error\">Ooops! This email already exists!</div>\r\n");
      out.write("            </div>\r\n");
      out.write("            <div id=\"more_box\">\r\n");
      out.write("                Learn more\r\n");
      out.write("                <img src=\"");
      out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${contextPath}", java.lang.String.class, (javax.servlet.jsp.PageContext)_jspx_page_context, null));
      out.write("/resources/img/arrow_down.png\" id=\"more_arrow\" />\r\n");
      out.write("            </div>\r\n");
      out.write("        </div>\r\n");
      out.write("        <div id=\"plus_box\"></div>\r\n");
      out.write("        <div id=\"more_box20\"></div>\r\n");
      out.write("        <div id=\"main_menu\">\r\n");
      out.write("            <div class=\"menu_blocks\" id=\"first_menu_block\"></div>\r\n");
      out.write("            <div class=\"menu_blocks\" id=\"second_menu_block\"></div>\r\n");
      out.write("            <div class=\"menu_blocks\" id=\"third_menu_block\"></div>\r\n");
      out.write("        </div>\r\n");
      out.write("        <div id=\"avatar\"></div>\r\n");
      out.write("        <div class=\"menu_points\" id=\"booking_system\">Booking</div>\r\n");
      out.write("        <div class=\"menu_points\" id=\"settings\">Settings</div>\r\n");
      out.write("        <div class=\"menu_points\" id=\"exit\">Exit</div>\r\n");
      out.write("    <!-- /container -->\r\n");
      out.write("    ");
      out.write("\r\n");
      out.write("    ");
      out.write("\r\n");
      out.write("    </body>\r\n");
      out.write("</html>");
    } catch (java.lang.Throwable t) {
      if (!(t instanceof javax.servlet.jsp.SkipPageException)){
        out = _jspx_out;
        if (out != null && out.getBufferSize() != 0)
          try {
            if (response.isCommitted()) {
              out.flush();
            } else {
              out.clearBuffer();
            }
          } catch (java.io.IOException e) {}
        if (_jspx_page_context != null) _jspx_page_context.handlePageException(t);
        else throw new ServletException(t);
      }
    } finally {
      _jspxFactory.releasePageContext(_jspx_page_context);
    }
  }

  private boolean _jspx_meth_c_005fset_005f0(javax.servlet.jsp.PageContext _jspx_page_context)
          throws java.lang.Throwable {
    javax.servlet.jsp.PageContext pageContext = _jspx_page_context;
    javax.servlet.jsp.JspWriter out = _jspx_page_context.getOut();
    //  c:set
    org.apache.taglibs.standard.tag.rt.core.SetTag _jspx_th_c_005fset_005f0 = (org.apache.taglibs.standard.tag.rt.core.SetTag) _005fjspx_005ftagPool_005fc_005fset_0026_005fvar_005fvalue_005fnobody.get(org.apache.taglibs.standard.tag.rt.core.SetTag.class);
    _jspx_th_c_005fset_005f0.setPageContext(_jspx_page_context);
    _jspx_th_c_005fset_005f0.setParent(null);
    // /WEB-INF/views/index.jsp(4,0) name = var type = java.lang.String reqTime = false required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_c_005fset_005f0.setVar("contextPath");
    // /WEB-INF/views/index.jsp(4,0) name = value type = javax.el.ValueExpression reqTime = true required = false fragment = false deferredValue = true expectedTypeName = java.lang.Object deferredMethod = false methodSignature = null
    _jspx_th_c_005fset_005f0.setValue(new org.apache.jasper.el.JspValueExpression("/WEB-INF/views/index.jsp(4,0) '${pageContext.request.contextPath}'",_el_expressionfactory.createValueExpression(_jspx_page_context.getELContext(),"${pageContext.request.contextPath}",java.lang.Object.class)).getValue(_jspx_page_context.getELContext()));
    int _jspx_eval_c_005fset_005f0 = _jspx_th_c_005fset_005f0.doStartTag();
    if (_jspx_th_c_005fset_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fc_005fset_0026_005fvar_005fvalue_005fnobody.reuse(_jspx_th_c_005fset_005f0);
      return true;
    }
    _005fjspx_005ftagPool_005fc_005fset_0026_005fvar_005fvalue_005fnobody.reuse(_jspx_th_c_005fset_005f0);
    return false;
  }
}
