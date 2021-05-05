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
import java.sql.*;
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

            PreparedStatement userStatement = dbcon.prepareStatement(insertUsersQuery, Statement.RETURN_GENERATED_KEYS);
            userStatement.setString(1, request.getParameter("email"));
            userStatement.setString(2, request.getParameter("firstname"));
            userStatement.setString(3, request.getParameter("lastname"));
            userStatement.setString(4, request.getParameter("email"));
            userStatement.setString(5, request.getParameter("firstname"));
            userStatement.setString(6, request.getParameter("lastname"));

            userStatement.executeUpdate();

            String getUserIdQuery = "select user_id from users where user_email = ? and user_firstname = ? and user_lastname = ?";
            PreparedStatement userIdStatement = dbcon.prepareStatement(getUserIdQuery);
            userIdStatement.setString(1, email);
            userIdStatement.setString(2, firstName);
            userIdStatement.setString(3, lastName);

            ResultSet userIdResultSet = userIdStatement.executeQuery();

            int userId = 0;
            if(userIdResultSet.next()){
                userId = userIdResultSet.getInt("user_id");
                session.setAttribute("user_id", userId);
            }


            // Insert into order user table
            String insertOrderQuery = "insert into order_user(user_id, created_at, order_total_price, order_shipping_method, order_shipping_address)\n" +
                    "values (?, NOW(), ?, ?, ?);";
            PreparedStatement userOrderStatement = dbcon.prepareStatement(insertOrderQuery);
            userOrderStatement.setInt(1, userId);
            userOrderStatement.setString(2, String.format("%.2f", totalPrice));
            userOrderStatement.setString(3, shippingMethod);

            String shippingAddress = street + ", " + city + ", " + state;
            userOrderStatement.setString(4, shippingAddress);
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

            session.setAttribute("user_email", email);
            session.setAttribute("user_firstname", firstName);
            session.setAttribute("user_lastname", lastName);
            items.clear();

            session.setAttribute("items", items);

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
