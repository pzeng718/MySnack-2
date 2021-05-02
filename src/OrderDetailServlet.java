import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@WebServlet(name = "OrderDetailServlet", urlPatterns = "/OrderDetailServlet")
public class OrderDetailServlet extends HttpServlet {
    private DataSource dataSource;

    public void init(ServletConfig config){
        try{
            dataSource = (DataSource) new InitialContext().lookup("java:comp/env/jdbc/mysnack");
        } catch (NamingException e) {
            e.printStackTrace();
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        out.println("<!DOCTYPE html>");
        out.println("<html lang=\"en\">");
        out.println("<head>");
        out.println("<meta charset=\"UTF-8\" />");
        out.println("<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\" />");
        out.println("<link href=\"https://cdn.jsdelivr.net/npm/bootstrap@5.0.0-beta3/dist/css/bootstrap.min.css\" rel=\"stylesheet\" integrity=\"sha384-eOJMYsd53ii+scO/bJGFsiCZc+5NDVN2yr8+0RDqr0Ql0h+rP48ckxlpbzKgwra6\" crossorigin=\"anonymous\">");
        out.println("</head>");
        out.println("<body>");
        out.println("<div class=\"container\" style=\"width: 70%\">");
        out.println("<h2 style=\"text-align: center\">Showing Order Details</h2>");
        try{
            Connection dbcon = dataSource.getConnection();

            String getOrderInfoQuery = "select ou.order_id as order_id, ou.created_at as time_created, ou.order_total_price as total_price, \n" +
                    "ou.order_shipping_method as shipping_method, ou.order_shipping_address as shipping_address, " +
                    "p.id as product_id, op.selected_amount as qty, p.price as unit_price \n" +
                    "from order_user as ou, order_productdetail as op, products as p \n" +
                    "where ou.order_id = (select max(order_id) from order_user) and ou.order_id = op.order_id and p.id = op.id;";
            PreparedStatement orderInfoStatement = dbcon.prepareStatement(getOrderInfoQuery);

            ResultSet orderInfoRS = orderInfoStatement.executeQuery();

            boolean infoDisplayed = false;
            while(orderInfoRS.next()){
                if(!infoDisplayed){
                    out.println("<p><strong>Order ID: </strong>" + orderInfoRS.getString("order_id") + "</p>");
                    out.println("<p><strong>Order placed time: </strong>" + orderInfoRS.getString("time_created") + "</p>");
                    out.println("<p><strong>Shipping method: </strong>" + orderInfoRS.getString("shipping_method") + "</p>");
                    out.println("<p><strong>Shipping address: </strong>" + orderInfoRS.getString("shipping_address") + "</p>");
                    out.println("<p><strong>Total price: </strong>" + orderInfoRS.getString("total_price") + "</p>");

                    out.println("<table class=\"table\">");
                    out.println("<thead><tr>");
                    out.println("<th scope=\"col\">Product ID</th>");
                    out.println("<th scope=\"col\">Qty</th>");
                    out.println("<th scope=\"col\">Unit Price</th></tr></thead><tbody>");
                    infoDisplayed = true;
                }
                out.println("<tr><td>" + orderInfoRS.getString("product_id") + "</td>");
                out.println("<td>" + orderInfoRS.getString("qty") + "</td>");
                out.println("<td>" + orderInfoRS.getString("unit_price") + "</td>");
                out.println("</tr>");
                System.out.println(orderInfoRS.getString("unit_price"));
            }
            out.println("</tbody></table>");
            out.println("<a href=\"index.html\" class=\"btn btn-success\" style=\"text-align:center\">Back to home</a></div>");
            out.println("</body></html>");
            out.close();

            orderInfoRS.close();
            orderInfoStatement.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
