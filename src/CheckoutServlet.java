import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

@WebServlet(name = "CheckoutServlet", urlPatterns = "/checkout")
public class CheckoutServlet extends HttpServlet {
    private DataSource dataSource;

    public void init(ServletConfig config){
        try{
            dataSource = (DataSource) new InitialContext().lookup("java:comp/env/jdbc/mysnack");
        } catch (NamingException e) {
            e.printStackTrace();
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        HttpSession session = request.getSession();

        String email = request.getParameter("email");
        String firstName = request.getParameter("firstname");
        String lastName = request.getParameter("lastname");
        double totalPrice = (double) session.getAttribute("totalPrice");
        String shippingMethod = request.getParameter("shipping-method");
        String street = request.getParameter("street");
        String city = request.getParameter("city");
        String state = request.getParameter("state");

        try{
            Connection dbcon = dataSource.getConnection();
            // Insert into users table
            String insertUsersQuery = "insert into users(user_email, user_firstname, user_lastname) \n" +
                    "select * from (select ?, ?, ?) as tmp \n" +
                    "where not exists (select * from users as u where u.user_email = ? and u.user_firstname = ? and u.user_lastname = ?);";

            PreparedStatement userStatement = dbcon.prepareStatement(insertUsersQuery);
            userStatement.setString(1, request.getParameter("email"));
            userStatement.setString(2, request.getParameter("firstname"));
            userStatement.setString(3, request.getParameter("lastname"));
            userStatement.setString(4, request.getParameter("email"));
            userStatement.setString(5, request.getParameter("firstname"));
            userStatement.setString(6, request.getParameter("lastname"));

            userStatement.executeUpdate();

            // Insert into order user table
            String insertOrderQuery = "insert into order_user(user_id, created_at, order_total_price, order_shipping_method, order_shipping_address)\n" +
                    "values ((select u.user_id from users as u where user_email = ? and u.user_firstname = ? and u.user_lastname = ?),\n" +
                    "NOW(), ?, ?, ?);";
            PreparedStatement userOrderStatement = dbcon.prepareStatement(insertOrderQuery);
            userOrderStatement.setString(1, email);
            userOrderStatement.setString(2, firstName);
            userOrderStatement.setString(3, lastName);
            userOrderStatement.setString(4, String.format("%.2f", totalPrice));
            userOrderStatement.setString(5, shippingMethod);

            String shippingAddress = street + ", " + city + ", " + state;
            userOrderStatement.setString(6, shippingAddress);
            userOrderStatement.executeUpdate();

            // Insert into order product detail table
            HashMap<String, ArrayList<String>> items = (HashMap<String, ArrayList<String>>) session.getAttribute("items");
            for(String productId : items.keySet()){
                String insertOrderDetailQuery = "insert into order_productdetail values((select max(order_id) from order_user), ?, ?);";
                PreparedStatement orderDetailStatement = dbcon.prepareStatement(insertOrderDetailQuery);
                orderDetailStatement.setString(1, productId);
                orderDetailStatement.setString(2, items.get(productId).get(2));

                orderDetailStatement.executeUpdate();
            }

            RequestDispatcher rd = request.getRequestDispatcher("/OrderDetailServlet");
            rd.forward(request, response);

            userStatement.close();
            userOrderStatement.close();
            dbcon.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
