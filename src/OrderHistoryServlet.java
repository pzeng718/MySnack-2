import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


@WebServlet(name = "OrderHistoryServlet", urlPatterns = "/order-history")
public class OrderHistoryServlet extends HttpServlet {
    private DataSource dataSource;

    public void init(ServletConfig config){
        try{
            dataSource = (DataSource) new InitialContext().lookup("java:comp/env/jdbc/mysnack");
        } catch (NamingException e) {
            e.printStackTrace();
        }
    }
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        HttpSession session = req.getSession();

        // TODO: check session if not empty then show order history

        PrintWriter out = resp.getWriter();

        int user_id = (int) session.getAttribute("user_id");
//        System.out.println("123 is: " + user_id);


        String getUserId = "SELECT u.user_id from users u where u.user_email = user_email and u.user_firstname = user_firstname and u.user_lastname = user_lastname";
//        System.out.println("UserId retrieved from table: " + getUserId);
//        System.out.println(session.getAttribute("user_id"));
//        String orderID = req.getParameter("order_id");
//        String userID = req.getParameter("user_id");
        try{
            Connection connect = dataSource.getConnection();
            String query = new StringBuilder()
                    .append("select p.name, p.price, op.selected_amount, o.created_at, o.order_total_price, o.order_shipping_address, o.order_shipping_method")
                    .append("\n from order_user o join order_productdetail op on o.order_id = op.order_id")
                    .append("\n join products p on op.id = p.id where o.order_id = op.order_id and op.id = p.id and o.user_id = ?")
                    .append("\n order by o.created_at desc")
                    .append("\n limit 5;").toString();
            PreparedStatement ps = connect.prepareStatement(query);
            ps.setInt(1, user_id);
            ResultSet result = ps.executeQuery();
            JsonArray jsonArr = new JsonArray();
            while(result.next()) {
                String productName = result.getString("name");
                String productUnitPrice = result.getString("price");
                String selectAmount = result.getString("selected_amount");
                String dateBought = result.getString("created_at");
                String orderTotalPrice = result.getString("order_total_price");
                String orderShippingAddress = result.getString("order_shipping_address");
                String orderShippingMethod = result.getString("order_shipping_method");


                JsonObject jsonProductObject = new JsonObject();
                jsonProductObject.addProperty("name", productName);
                jsonProductObject.addProperty("price", productUnitPrice);
                jsonProductObject.addProperty("selected_amount", selectAmount);
                jsonProductObject.addProperty("created_at", dateBought);
                jsonProductObject.addProperty("order_total_price", orderTotalPrice);
                jsonProductObject.addProperty("order_shipping_address", orderShippingAddress);
                jsonProductObject.addProperty("order_shipping_method", orderShippingMethod);

                jsonArr.add(jsonProductObject);
            }
            out.write(jsonArr.toString());
//            resp.setStatus(200);

            result.close();
            ps.close();
            connect.close();

        } catch (Exception e) {
             JsonObject jsonProductObject = new JsonObject();
             jsonProductObject.addProperty("errorMessage", e.getMessage());
             out.write(jsonProductObject.toString());
             System.out.println(e.getMessage());
             System.out.println(e);
             resp.setStatus(500);

         }

    }
}
